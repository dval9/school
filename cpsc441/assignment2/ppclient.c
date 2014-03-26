/**
 * Tom Crowfoot
 * 10037477
 * CPSC 441 Assignment 2
 * P2P file sharing
 * client file
 * based on wordclient by Carey Williamson
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <errno.h>
#include <time.h>
#include <math.h>
#include <fcntl.h>

#define BUFFER_SIZE 1024*2

int main(int argc, char **argv)
{
  struct sockaddr_in si_server, si_server2, si_server3; // server, recvfrom, connect to other client
  struct sockaddr *server, *server2, *server3;
  int s, i, j, l;
  uint len = sizeof(si_server2);
  char buf[BUFFER_SIZE]; // used for sendto and recvfrom
  char peer_ip[100];
  int peer_port;
  char *buff; // for tokenizing buf
  char *server_ip;
  FILE *file;
  char *filename;
  char myfilename[100];
  int chunks[32]; // always max of 32, could need less though
  int gets = 0, next; // how many chunks we have, if we keep recving
  char filedata[32*1024]; // maximum file size m*c
  int readBytes;
  int c;//chunk size
  int m;//chunks
  int p, n;//peers, connections
  int myport;
  char *my_ip;
  int myindex;
  int port;
  int timeout;
  int debug = 0;
  struct timeval time_val;
  fd_set readfds;
  
  if(argc == 2){ // no debug
    buff = strtok(argv[1], ":");
    server_ip = buff;
    buff = strtok(NULL, "");
    port = atoi(buff);
  }
  else if(argc == 3){ // debug
    buff = strtok(argv[1], ":");
    server_ip = buff;
    buff = strtok(NULL, ":");
    port = atoi(buff);
    if(strcasecmp(argv[2],"debug") == 0)
      debug = 1;
    else{
      printf("usage: ./peer <ServerIP:Port> [Debug]\n");
      exit(EXIT_FAILURE);
    }
  }
  else{
    printf("usage: ./peer <ServerIP:Port> [Debug]\n");
    exit(EXIT_FAILURE);
  }
  if(debug)printf("Debug on\n");
  srand(time(NULL)); // for getting random chunks
  if((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1){
    perror("socket");
    exit(EXIT_FAILURE);
  }
  memset((char *) &si_server, 0, sizeof(si_server));
  si_server.sin_family = AF_INET;
  si_server.sin_port = htons(port);
  server = (struct sockaddr *) &si_server; // the server
  server2 = (struct sockaddr *) &si_server2; // used to see who we recv from
  if(inet_pton(AF_INET, server_ip, &si_server.sin_addr) == 0){
    perror("inet_pton");
    exit(EXIT_FAILURE);
  }
  memset(buf, 0, BUFFER_SIZE);
  strncpy(buf, "hi", 2); // attempt to get a slot on the server
  if(sendto(s, buf, strlen(buf), 0, server, sizeof(si_server)) == -1){
    perror("sendto");
    exit(EXIT_FAILURE);
  }
  printf("Connecting to server (%s:%u)\n", inet_ntoa(si_server.sin_addr), ntohs(si_server.sin_port));
  memset(buf, 0, BUFFER_SIZE);
  if((readBytes=recvfrom(s, buf, BUFFER_SIZE, 0, server2, &len))==-1){
    perror("recvfrom");
    exit(EXIT_FAILURE);
  }
  if(strncmp(buf, "no", 2) == 0){ // there was no room
    printf("Connection to server refused, exiting\n");
    exit(EXIT_SUCCESS);
  }
  else if(strncmp(buf, "yes", 3) == 0){ // we got a spot
    buff = strtok(buf, " \n");
    buff = strtok(NULL, " \n");
    c = atoi(buff);
    buff = strtok(NULL, " \n");
    m = atoi(buff);
    buff = strtok(NULL, " \n");
    p = atoi(buff);
    buff = strtok(NULL, " \n");
    n = atoi(buff);
    buff = strtok(NULL, " \n");
    filename = strdup(buff);
    buff = strtok(NULL, " \n");
    my_ip = strdup(buff);
    buff = strtok(NULL, " \n");
    myindex = atoi(buff);
    buff = strtok(NULL, " \n");
    myport = atoi(buff);
  }
  time_val.tv_sec = 3; //timeout of 3 seconds before re-requesting
  time_val.tv_usec = 0;
  FD_ZERO(&readfds);
  FD_SET(s, &readfds);
  timeout = 0;
  for(i=0;i<32;i++) // make sure we don't think we have any parts
    chunks[i] = 0;
  sprintf(myfilename, "P%d_%s", myindex, filename);
  printf("Peer %d initialized with the following parameters: N(%d), P(%d), M(%d), C(%d), Filename(%s), IP(%s), PORT(%u)\n", myindex, n, p, m, c, myfilename, my_ip, myport);
  do{
    if(timeout == 0){ // haven't timed out, lets request a random chunk
      do{
	i = rand() / (RAND_MAX / (m + 1));
      }while(i > m-1 || chunks[i] == 1);
    }
    memset(buf, 0, BUFFER_SIZE);
    sprintf(buf, "get %d", i); // ask server for piece i
    sleep(1); // make it slower to see what is happening
    if(sendto(s, buf, strlen(buf), 0, server, sizeof(si_server)) == -1){
      perror("sendto");
      exit(EXIT_FAILURE);
    }
    printf("Requesting piece %d from server\n", i);
    next = 0;
    while(!next){
      select(s+1, &readfds, NULL, NULL, &time_val); // wait up to 3 seconds for messages
      if(FD_ISSET(s, &readfds)){ // we got something, read it and reset the select() stuff
	memset(buf, 0, BUFFER_SIZE);
	if((readBytes=recvfrom(s, buf, BUFFER_SIZE, 0, server2, &len))==-1){
	  perror("recvfrom");
	  exit(EXIT_FAILURE);
	}
	time_val.tv_sec = 3; //timeout of 3 seconds before re-requesting
	time_val.tv_usec = 0;
	FD_ZERO(&readfds);
	FD_SET(s, &readfds);
	timeout = 0;
      }
      else{ // no responces, reset select() stuff and re-send request to server for same chunk
	printf("Timing out after 3 seconds of no responce, re-requesting a chunk from server\n");
	time_val.tv_sec = 3; //timeout of 3 seconds before re-requesting
	time_val.tv_usec = 0;
	FD_ZERO(&readfds);
	FD_SET(s, &readfds);
	timeout = 1;
	break;
      }
      if(strncmp(buf, "get", 3) == 0){ // a request from another client, send what they want back to who asked
	buff = strtok(buf, " \n");
	buff = strtok(NULL, " \n");
	j = atoi(buff);
	memset(buf, 0, BUFFER_SIZE);
	sprintf(buf, "%d ", j);
	strncat(buf, filedata+(j*c), c);
	sendto(s, buf, strlen(buf), 0, server2, sizeof(si_server2));
	printf("Sending piece %d to peer (%s:%u)\n", j, inet_ntoa(si_server2.sin_addr), ntohs(si_server2.sin_port));
      }
      else if(strncmp(buf, "ask", 3) == 0){ // server telling us to ask another client
	buff = strtok(buf, " \n");
	buff = strtok(NULL, " \n"); // peice number
	l = atoi(buff);
	buff = strtok(NULL, " \n"); // ip of peer
	memset(peer_ip, 0, 100);
	strcpy(peer_ip, buff);
	buff = strtok(NULL, " \n"); // port of peer
	peer_port = atoi(buff);
	si_server3.sin_family = AF_INET;
	si_server3.sin_port = htons(peer_port);
	server3 = (struct sockaddr *) &si_server3; // the client we are to ask
	if(inet_pton(AF_INET, peer_ip, &si_server3.sin_addr) == 0){
	  perror("inet_pton");
	  exit(EXIT_FAILURE);
	} // request the piece the the client
	printf("Getting piece %d from peer (%s:%u)\n", l, inet_ntoa(si_server3.sin_addr), ntohs(si_server3.sin_port));
	memset(buf, 0, BUFFER_SIZE);
	sprintf(buf, "get %d", l);
	if(sendto(s, buf, strlen(buf), 0, server3, sizeof(si_server3)) == -1){
	  perror("sendto");
	  exit(EXIT_FAILURE);
	}
      }
      else{ // we have a chunk of file
	buff = strtok(buf, " ");
	l = atoi(buff);
	if(chunks[l] == 1){ // we already have that chunk, don't use it 
	  break;
	}
	else{ // we don't have this one yet
	  buff = strtok(NULL, "");
	  if(strcmp(inet_ntoa(si_server2.sin_addr),inet_ntoa(si_server.sin_addr)) == 0 && ntohs(si_server2.sin_port) == ntohs(si_server.sin_port))
	    printf("Getting piece %d from server\n", i);
	  strncpy(filedata+(l*c), buff, c); // copy it into our file buffer in correct place
	  memset(buf, 0, BUFFER_SIZE);
	  sprintf(buf, "ok %d", l); // send ack to server saying we have this now
	  if(sendto(s, buf, strlen(buf), 0, server, sizeof(si_server)) == -1){
	    perror("sendto");
	    exit(EXIT_FAILURE);
	  }
	  printf("Successfully got piece %d. Updating server.\n", l);
	  chunks[l]=1;
	  gets++;
	  next = 1; // stop recving, send another request to server
	}
      }
    }
  }while(gets!=m);
  printf("File %s is complete. Exiting.\n", myfilename); // we have whole file now
  memset(buf, 0, BUFFER_SIZE);
  strncpy(buf, "bye", 3); // send goodbye message to server
  if(sendto(s, buf, strlen(buf), 0, server, sizeof(si_server)) == -1){
    perror("sendto");
    exit(EXIT_FAILURE);
  }
  close(s);
  filedata[c*m] = 0;
  file = fopen(myfilename, "w"); // open file and write it
  fprintf(file, "%s", filedata);
  fclose(file);
  return 0;
}
