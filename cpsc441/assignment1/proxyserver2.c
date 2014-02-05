/**
 * Tom Crowfoot
 * 10037477
 * CPSC 441
 * Assignment 1 Web Proxy
 * Only allows even sized files, blocks odd sized files.
 */

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <signal.h>
#include <string.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <errno.h>

int listen_socket, active_socket, web_socket;

/* attempt to clean up sockets */
void sig_handler(int signum)
{
  printf("%d:shutting down server\n",getpid());
  close(listen_socket);
  close(active_socket);
  close(web_socket);
  exit(EXIT_SUCCESS);
}

int main(int argc, char **argv)
{
  int port = 31337; // port to run on, 31337 is default
  int child = 0; // keep track of children to kill
  int debug = 0;
  struct sockaddr_in server_addr, client_addr;
  socklen_t client_addr_len = sizeof(client_addr);
  char *server_ip = (char*)malloc(100*sizeof(char));
  if(argc == 3){ /* optional possible argument to set port to something other than default, and debug mode, but either not needed */
    if(strncmp(argv[1], "debug", 5) == 0){
      debug = 1;
      port = atoi(argv[2]);
      if(port < 1024 || port > 65535){
	printf("bad port as argument, running on default");
	port = 31337;
      }
    }
    else if(strncmp(argv[2], "debug", 5) == 0){
      debug = 1;
      port = atoi(argv[1]);
      if(port < 1024 || port > 65535){
	printf("bad port as argument, running on default");
	port = 31337;
      }
    }
  }
  if(argc == 2){
    if(strncmp(argv[1], "debug", 5) == 0)
      debug = 1;
    else{
      port = atoi(argv[1]);
      if(port < 1024 || port > 65535){
	printf("bad port as argument, running on default");
	port = 31337;
      }
    }
  }
  if(debug)
    printf("proxy running with debug enabled\n");
  signal(SIGINT, sig_handler); // regester signal handler for ^C, should use default for any other signal
  memset(&server_addr, 0, sizeof(server_addr)); /* setup server socket and start listening */
  server_addr.sin_family = AF_INET;
  server_addr.sin_port = htons(port);
  server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  listen_socket = socket(AF_INET, SOCK_STREAM, 0);
  if(listen_socket == -1){
    perror("socket");
    exit(EXIT_FAILURE);
  }
  if(bind(listen_socket, (struct sockaddr*) &server_addr, sizeof(server_addr)) == -1){
    perror("bind");
    exit(EXIT_FAILURE);
  }
  if(listen(listen_socket, 10) == -1){
    perror("listen");
    exit(EXIT_FAILURE);
  }
  inet_ntop(AF_INET, &server_addr.sin_addr.s_addr, server_ip, sizeof(server_addr));
  printf("***%d:proxy running on %s:%d***\n", getpid(), server_ip, port);
  /* server starts listening forever, fork off child when it gets connection */
  for(;;){
    active_socket = accept(listen_socket, (struct sockaddr*) &client_addr, &client_addr_len);
    if(active_socket == -1){
      perror("accept");
      continue;
    }
    /* server is parent, so gets non 0 return from fork, child gets 0 */
    if(fork() == 0){
      char receive_data[10000]; // data read from socket
      char *client_ip = (char*)malloc(100*sizeof(char)); // ip address of web browser
      int read_len = 0; // length read from socket
      char webname[1000]; // webserver we want to talk to
      char *redirect_text = "HTTP/1.1 302 Found\r\nLocation: http://pages.cpsc.ucalgary.ca/~carey/CPSC441/errorpage.html\r\nConnection: close\r\n\r\n"; // redirection string for odd length html
      char *redirect_img = "HTTP/1.1 302 Found\r\nLocation: http://pages.cpsc.ucalgary.ca/~carey/CPSC441/trollface.jpg\r\nConnection: close\r\n\r\n"; // redirection string for odd length images
      struct addrinfo hints, *res; // for resolving web ip information
      char *clength = (char*)malloc(10); // length of file
      int stop_read = 0; // stop listening to browser
      int stop_read2 = 0; // stop reading from web
      int i; // for loops
      int s2 = 0; // find host name
      int mlength = 0; // find end of hostname, also flag for checking header info
      int b1 = 0; // for helping extract file length
      int b2 = 0; // see b1
      int clength1 = 0; // file length to check if even or odd
      int is_image = 0; // tell if image
      int is_text = 0; // is html
      int handle_it = 1; // handle multipul reads for odd length files different than even length
      child = 1; // kill me when done
      close(listen_socket); // dont need anymore
      inet_ntop(AF_INET, &client_addr.sin_addr.s_addr, client_ip, sizeof(client_addr));
      printf("***%d:connection from %s***\n", getpid(), client_ip);
      while(!stop_read){ /* for http/1.1 */
	memset(receive_data, 0, sizeof(receive_data)); /* reset everything to initial values if we get more than one request from browser */
	memset(clength, 0, 10);
	memset(webname, 0, sizeof(webname));
	memset(receive_data, 0, sizeof(receive_data));
	read_len = 0;
	stop_read = 0;
	stop_read2 = 0;
	s2 = 0;
	mlength = 0;
	b1 = 0;
	b2 = 0;
	clength1 = 0;
	is_image = 0;
	is_text = 0;
	handle_it = 1;
	if(debug)printf("%d:get request from browser\n", getpid());
	if((read_len = recv(active_socket, &receive_data, sizeof(receive_data), 0)) == 0){ /* read request from browser */
	  if(debug)printf("%d:done getting messages from browser\n", getpid());
	  stop_read = 1;
	  continue;
	}
	if(debug)printf("%d:browser request\n%s\n", getpid(), receive_data);
	for(i = 0; i < read_len; i++){
	  if(strncmp(receive_data + i, "Host: ", 6) == 0){
	    s2 = i + 6;
	    for(;; i++){
	      if(strncmp(receive_data + i, "\n", 1) == 0)
		break;
	    }
	    break;
	  }
	}
	if((strncmp(webname, receive_data+s2, i-s2-1)) != 0){
	  close(web_socket);
	  strncpy(webname, receive_data+s2, i-s2-1); // copy the host name into a string
	  printf("%d:connecting to %s\n", getpid(), webname);
	  memset(&hints, 0, sizeof(hints)); // go find us the ip address and stuff of the website
	  hints.ai_family = AF_INET;
	  hints.ai_socktype = SOCK_STREAM;
	  getaddrinfo(webname, "80", &hints, &res);
	  web_socket = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
	  if(web_socket == -1){
	    perror("socket");
	    exit(EXIT_FAILURE);
	  }
	  if(connect(web_socket, res->ai_addr, res->ai_addrlen) == -1){
	    perror("connect");
	    exit(EXIT_FAILURE);
	  }
	}
	if(debug)printf("%d:sending message to server\n", getpid());
	send(web_socket, receive_data, read_len, 0);
	if(debug)printf("%d:getting message from web\n", getpid());
	stop_read2 = 0;
	while(!stop_read2){ // we need to read until done, and handle what we get correctly
	  memset(receive_data, 0, sizeof(receive_data));
	  if((read_len = recv(web_socket, &receive_data, sizeof(receive_data), 0))  == 0){ // recv is 0 when connection is terminated, -1 for error
	    if(debug)printf("%d:done getting message from web\n", getpid());
	    stop_read2 = 1;
	    continue;
	  }
	  if(debug)printf("%d:responce from web\n%s\n", getpid(), receive_data);
	  if(mlength >= 0){ // only bother doing this the first time for the header
	    for(i = 0; i < read_len; i++){
	      if(mlength == -1)
		break;
	      if(strncmp(receive_data + i, "Content-Length:", 15) == 0){ // get content length of reply from web
		mlength = -1;
		b1 = i + 16;
		for(i = b1; i < read_len; i++){
		  if(receive_data[i] == '\n'){
		    b2 = i-1;
		    break;
		  }
		}
	      }
	    }
	    for(i = 0; i < read_len; i++){ // check content type
	      if(strncmp(receive_data + i, "Content-Type: image", 19) == 0){
		is_image = 1;
		mlength = -2;
		break;
	      }
	      else if(strncmp(receive_data + i, "Content-Type: text/html", 23) == 0){
		is_text = 1;
		mlength = -2;
		break;
	      }
	    }
	  }
	  memcpy(clength, receive_data + b1, b2-b1); // copy file length to convert to int
	  *(clength + (b2-b1)) = 0; // dunno if i need null terminated string, but sure 
	  clength1 = atoi(clength); // now have int
	  if(handle_it) // only do this once
	    if(debug)printf("%d:message length %d; is image = %d; is text = %d\n", getpid(), clength1, is_image, is_text);
	  if(clength1 % 2 == 0 || handle_it == 0){ // message is even length, do this, or if multiple times through here just short circuit because clength might have changed to junk
	    if(send(active_socket, receive_data, read_len, 0) == -1)
	      perror("send");
	    if(handle_it) // we only want one of these messages for each even file
	      if(debug)printf("%d:request was even length, let it pass\n", getpid());
	    handle_it = 0; // we know its even length, just short circuit
	  }
	  else if(clength1 % 2 == 1 && handle_it){ // file is odd length, cant have that
	    if(is_image){ // image, send it the replacement
	      if(send(active_socket, redirect_img, strlen(redirect_img), 0) == -1)
		perror("send");
	      if(debug)printf("%d:request was odd size image, redirecting\n", getpid());
	      stop_read = 1;
	      stop_read2 = 1; // we arent allowing oddness, so no point to keep reading that request, go cleanup
	    }
	    else if(is_text){ // not image, tell it to go elsewhere
	      if(send(active_socket, redirect_text, strlen(redirect_text), 0) == -1)
		perror("send");
	      if(debug)printf("%d:request was odd size html, redirecting\n", getpid());
	      stop_read = 1;
	      stop_read2 = 1; // we arent allowing oddness, so no point to keep reading that request, go cleanup
	    }
	    else if(!is_text && !is_image){ // must be css or javascript or something, so let it through, only block html
	      if(send(active_socket, receive_data, read_len, 0) == -1)
		perror("send");
	      if(debug)printf("%d:request was an odd file, but neither image or html so letting through\n", getpid());
	    }
	  }
	}
      }// done reading, start cleanup
      printf("%d:done with web, closing connection\n", getpid());
      if(close(web_socket) == -1)
	perror("close"); 
    }
    /* server should keep going, only exit if child process gets here */
    if(close(active_socket) == -1)
      perror("close");
    if(child)
      exit(EXIT_SUCCESS);
  }
  exit(EXIT_SUCCESS);
}
  
