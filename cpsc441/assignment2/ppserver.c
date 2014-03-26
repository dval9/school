/**
 * Tom Crowfoot
 * 10037477
 * CPSC 441 Assignment 2
 * P2P file sharing
 * server file
 * based on the wordserver file by Carey Williamson
 */

#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>
#include <signal.h>
#include <pthread.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/uio.h>
#include <errno.h>

#define MAX_N 8
#define MAX_M 32
#define MAX_C 1024
#define BUFFER_SIZE 1024*2

int n, p, m, c, current_peer;
char filedata[MAX_M * MAX_C];
int scoreboard[MAX_N][MAX_M];
struct sockaddr_in clients[MAX_N];

void print_score(void) // prints what chunks each connected client has
{
  int i;
  int j;
  printf("i IP              Port  Map\n");
  printf("---------------------------\n");
  for(i=0; i<current_peer; i++){
    if(clients[i].sin_port != 0){ // port=0 means no client there
      printf("%d %s       %d ", i, inet_ntoa(clients[i].sin_addr), ntohs(clients[i].sin_port));
      for(j=0; j<m; j++)
	printf("%d ", scoreboard[i][j]);
      printf("\n");
    }
  }
  return;
}

int remove_client(struct sockaddr_in c) // remove a peer from our list
{
  int i;
  for(i=0; i<n; i++){
    if(!strcmp(inet_ntoa(clients[i].sin_addr), inet_ntoa(c.sin_addr)) && ntohs(clients[i].sin_port) == ntohs(c.sin_port)){
      clients[i].sin_port = 0; //set port to 0 to indicate no client there anymore
      return i;
    }      
  }
  return -1;
}
 
int add_client(struct sockaddr_in c)
{
  int i;
  //for(i=0; i<n; i++){ // this would allow max N connections at once, but any number over time
  for(i=current_peer; i<n; i++){// should only just do the first one, only ever allow N connections at once or overtime
    if(clients[i].sin_port == 0){
      clients[current_peer].sin_addr = c.sin_addr;
      clients[current_peer++].sin_port = c.sin_port;
      return i;
    }
  }
  return -1;
}

int find_client(struct sockaddr_in c) // return index of peer
{
  int i;
  for(i=0; i<n; i++)
    if(!strcmp(inet_ntoa(clients[i].sin_addr), inet_ntoa(c.sin_addr)) && ntohs(clients[i].sin_port)== ntohs(c.sin_port))
      return i;
  return -1;
}

int main(int argc, char **argv)
{
  struct sockaddr_in si_server, si_client; // us, who we are talking to
  struct sockaddr *server, *client;
  int s, i, j, k;
  uint len=sizeof(si_server);
  int port;
  int readBytes;
  int flength;
  FILE *file;
  char tempbuf[MAX_M * MAX_C];// for reading file
  char buf[BUFFER_SIZE]; // for sendto and recvfrom
  char *buff; // for tokenizing buf
  char *filename;
  int quit = 0;
  int debug = 0;
  
  if(argc == 6){ // no debug
    n = atoi(argv[1]);
    if(n < 1 || n > 8)
      n = 1;
    p = atoi(argv[2]);
    if(p < 1 || p > n)
      p = n;
    c = atoi(argv[3]);
    if(c < 1 || c > MAX_C)
      c = 1;
    port = atoi(argv[4]);
    filename = argv[5];
  }
  else if(argc == 7){ // debug
    n = atoi(argv[1]);
    if(n < 1 || n > 8)
      n = 1;
    p = atoi(argv[2]);
    if(p < 1 || p > n)
      p = n;
    c = atoi(argv[3]);
    if(c < 1 || c > MAX_C)
      c = 1;
    port = atoi(argv[4]);
    filename = argv[5];
    if(strcasecmp(argv[6],"debug")==0)
      debug = 1;
    else{
      printf("usage: ./server <N> <P> <C> <Port> <Filename> [Debug]\n");
      exit(EXIT_FAILURE);
    }
  }
  else{
    printf("usage: ./server <N> <P> <C> <Port> <Filename> [Debug]\n");
    exit(EXIT_FAILURE);
  }
  current_peer = 0;// initially no peers connected so first will go to index 0
  if(debug)printf("Debug on\n");
  if((file = fopen(filename, "r")) == NULL){
    perror("fopen");
    exit(EXIT_FAILURE);
  }  
  fseek(file, 0, SEEK_END); // seek to end to get file length, then seek back to beginning
  flength = ftell(file);
  fseek(file, 0, SEEK_SET);
  while(fgets(tempbuf, flength, file) != NULL){ // read the whole file into tempbuf
    strncpy(filedata+strlen(filedata), tempbuf, strlen(tempbuf));
  }
  if(strncasecmp(filename, "dolittle.txt", 12)==0) // only 8 chunks for this file
    m = 8;
  else if(strncasecmp(filename, "32KB.txt", 12)==0) // 32 chunks for this one
    m = MAX_M;
  else{ // figure out how many chunks we will need based on c, if remainder, we need one more that won't be full
    m = (flength / c);
    if((flength % c) != 0)
      m++;
  }
  if((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1){
    perror("socket");
    exit(EXIT_FAILURE);
  }
  memset((char *) &si_server, 0, sizeof(si_server));
  si_server.sin_family = AF_INET;
  si_server.sin_port = htons(port);
  si_server.sin_addr.s_addr = htonl(INADDR_ANY);
  server = (struct sockaddr *) &si_server; // us
  client = (struct sockaddr *) &si_client; // who we talk to
  if(bind(s, server, sizeof(si_server))==-1){
    perror("bind");
    exit(EXIT_FAILURE);
  }
  printf("Server initialized with the following parameters: N(%d), P(%d), M(%d), C(%d), Filename(%s), IP(%s), Port(%u)\n", n, p, m, c, filename, inet_ntoa(si_server.sin_addr), ntohs(si_server.sin_port));
  while(!quit){ // forever
    memset(buf, 0, BUFFER_SIZE);
    if((readBytes=recvfrom(s, buf, BUFFER_SIZE, 0, client, &len)) == -1){ // we got a message
      perror("recvfrom");
      quit = 1;
    }
    buf[readBytes] = '\0'; // padding with end of string symbol
    if(strncmp(buf, "hi", 2) == 0){ // a client wants to join
      i = add_client(si_client);
      if(i == -1){// there was no more room so tell them to go away
	memset(buf, 0, BUFFER_SIZE);
	sprintf(buf, "no");
	printf("No more room for connections, refusing connection (%s:%u)\n", inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port));
	sendto(s, buf, strlen(buf), 0, client, len);
      }
      else{ // there was room, they are now index i, initialize them
	printf("Adding new peer %d (%s:%u) to scoreboard\n", i, inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port));
	for(j=0; j<m; j++)
	  scoreboard[i][j] = 0;
      	print_score();
	memset(buf, 0, BUFFER_SIZE);
	sprintf(buf, "yes %d %d %d %d %s %s %d %d", c, m, p, n, filename, inet_ntoa(si_client.sin_addr), i, ntohs(si_client.sin_port)); // send everything they will need
	sendto(s, buf, strlen(buf), 0, client, len);
      }
    }
    else if(strncmp(buf, "get", 3) == 0){ // a request for some data
      i = find_client(si_client); 
      if(i == -1)
	;
      else{
	buff = strtok(buf, " \n"); // get
	buff = strtok(NULL, " \n"); // number
	j = atoi(buff);
	for(k=0; k<n; k++) // check if any other peers has that peice
	  if(scoreboard[k][j] == 1)
	    break;
	if(k==n || p == 1){ // no one has that peice yet, send it ourselves
	  memset(buf, 0, BUFFER_SIZE);
	  sprintf(buf, "%d ", j);
	  strncat(buf, filedata+(j*c), c);
	  sendto(s, buf, strlen(buf), 0, client, len);
	  printf("There are no peers with piece %d for peer %d (%s:%u)\nSending piece from server\n", j, i, inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port));
	}
	else{ // get from user k
	  printf("There are other peers with piece %d for peer %d (%s:%u)\nPassing the buck to peer %d (%s:%u) for piece %d\n", j, i, inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port), k, inet_ntoa(clients[k].sin_addr), ntohs(clients[k].sin_port), j);
	  memset(buf, 0, BUFFER_SIZE);
	  sprintf(buf, "ask %d %s %d", j, inet_ntoa(clients[k].sin_addr), ntohs(clients[k].sin_port)); // send peer K's ip and port
	  sendto(s, buf, strlen(buf), 0, client, len);
	}
      }
    }
    else if(strncmp(buf, "ok", 2) == 0){ // client confirming they got a chunk of the file, note it down
      i = find_client(si_client); 
      if(i == -1)
	;
      else{
	buff = strtok(buf, " \n"); // ok
	buff = strtok(NULL, " \n"); // number
	j = atoi(buff);
	printf("Updating status for peer %d (%s:%u) for piece %d\n", i, inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port), j);
	scoreboard[i][j] = 1;
	print_score();
      }
    }
    else if(strncmp(buf, "bye", 3) == 0){ // client leaving, remove him so we don't tell anyone to ask for peices from them
      i = remove_client(si_client); 
      if(i == -1)
	;
      else{
	printf("Peer %d (%s:%u) is being removed\n", i, inet_ntoa(si_client.sin_addr), ntohs(si_client.sin_port));
	for(j=0; j<m; j++)
	  scoreboard[i][j] = 0;
      	print_score();
      }
    }
  }
  close(s);
  return 0;
}
