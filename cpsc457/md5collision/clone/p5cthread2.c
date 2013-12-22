/**
 *
 * Tom Crowfoot
 * 10037477
 * CPSC 457 
 * Problem 5
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <time.h>
#include <openssl/md5.h>
#include <openssl/crypto.h>
#include <sched.h>
#include <sys/types.h>
#include <sys/wait.h>

#define _GNU_SOURCE

uint64_t search_space = 62523502209ULL;/* search space is 63^6 */
uint64_t counter = 0ULL; 
time_t start;
time_t end;

/* When SIGUSR2 is received, output progress of search */
void sig_handler(int signum)
{
  printf("checked %llu or %llu%%\n", (long long unsigned int)counter, (long long unsigned int)(counter*100/search_space));
}

int fun1();
int fun2();

int main(void)
{
  char *stack1, *stack2, *stacktop1, *stacktop2;
  start = time(NULL);
  signal(SIGUSR2, sig_handler);
  /* need to give each child a stack space in memory */
  stack1 = malloc(1024*1024);
  stack2 = malloc(1024*1024);
  /* stack grows down */
  stacktop1 = stack1+(1024*1024);
  stacktop2 = stack2+(1024*1024);
  /* each thread searches half the space, split on the first character of the message */
  pid_t pid1 = clone(fun1, stacktop1, SIGCHLD, NULL);
  pid_t pid2 = clone(fun2, stacktop2, SIGCHLD, NULL);
  /* wait for any child to return a signal */
  waitpid(-1, NULL, 0);
  /* cull remaining children */
  kill(pid1, 2);
  kill(pid2, 2);
  return 0;
}
int fun1(){
  int i, j, k, l, m, n;
  MD5_CTX c;
  char *lang = malloc(26+26+10+1);/* [A..Za..z0..9_] */
  char *message = malloc(11);
  char *buffer = malloc(16);
  char *cipher = malloc(16);
  /* initialize the cipher given */   
  cipher[0] = 0x57;
  cipher[1] = 0x50;
  cipher[2] = 0x1a;
  cipher[3] = 0xc7;
  cipher[4] = 0xb9;
  cipher[5] = 0xd5;
  cipher[6] = 0x44;
  cipher[7] = 0x0a;
  cipher[8] = 0xde;
  cipher[9] = 0xe8;
  cipher[10] = 0xb3;
  cipher[11] = 0xdd;
  cipher[12] = 0x97;
  cipher[13] = 0x09;
  cipher[14] = 0x72;
  cipher[15] = 0xcb;
  /* initialize message with known values */
  message[3] = 0x20;
  message[4] = 0x69;
  message[6] = 0x20;
  message[8] = 0x6f;
  message[10] = 0x6c;
  /* populate language of possible characters*/
  for(i = 0; i < 26; i++)
    lang[i] = i + 0x41;
  for(i = 0; i < 26; i++)
    lang[26+i] = i + 0x61;
  for(i = 0; i < 10; i++)
    lang[52+i] = i + 0x30;
  lang[62] = 0x20;
/* start checking for collisions */
  /* only search every other i value as two threads */
  for(i = 61; i >= 0; i-=2){
    for(j = 62; j >= 0; j--){
      for(k = 62; k >= 0; k--){
	for(l = 62; l >= 0; l--){
	  for(m = 62; m >= 0; m--){
	    for(n = 62; n >= 0; n--){
	      message[0] = lang[i];
	      message[1] = lang[j];
	      message[2] = lang[k];
	      message[5] = lang[l];
	      message[7] = lang[m];
	      message[9] = lang[n];
	      counter++;
	      /* compute the digest */
	      MD5_Init(&c);
	      MD5_Update(&c, message, 11);
	      MD5_Final((unsigned char *)buffer, &c);
	      /* if the digest matches the initialized cipher, print the message that caused the collision, and time and quit */
	      if(strcmp(cipher,buffer)==0){		
		printf("match found! message was: %s\n", message);
		end = time(NULL);
		printf("time: %lu\n",(long)end-start);
		exit(0);
	      }
	    }
	  }
	}
      }
    }
  }
  end = time(NULL);
  printf("time: %lu\n",(long)end-start);
  return 0;
}
int fun2(){
  int i, j, k, l, m, n;
  MD5_CTX c;
  char *lang = malloc(26+26+10+1);/* [A..Za..z0..9_] */
  char *message = malloc(11);
  char *buffer = malloc(16);
  char *cipher = malloc(16); 
/* initialize the cipher given */   
  cipher[0] = 0x57;
  cipher[1] = 0x50;
  cipher[2] = 0x1a;
  cipher[3] = 0xc7;
  cipher[4] = 0xb9;
  cipher[5] = 0xd5;
  cipher[6] = 0x44;
  cipher[7] = 0x0a;
  cipher[8] = 0xde;
  cipher[9] = 0xe8;
  cipher[10] = 0xb3;
  cipher[11] = 0xdd;
  cipher[12] = 0x97;
  cipher[13] = 0x09;
  cipher[14] = 0x72;
  cipher[15] = 0xcb;
  /* initialize message with known values */
  message[3] = 0x20;
  message[4] = 0x69;
  message[6] = 0x20;
  message[8] = 0x6f;
  message[10] = 0x6c;
  /* populate language of possible characters*/
  for(i = 0; i < 26; i++)
    lang[i] = i + 0x41;
  for(i = 0; i < 26; i++)
    lang[26+i] = i + 0x61;
  for(i = 0; i < 10; i++)
    lang[52+i] = i + 0x30;
  lang[62] = 0x20;
  /* start checking for collisions */
  /* only search every other i value as two threads */
  for(i = 62; i >= 0; i-=2){
    for(j = 62; j >= 0; j--){
      for(k = 62; k >= 0; k--){
	for(l = 62; l >= 0; l--){
	  for(m = 62; m >= 0; m--){
	    for(n = 62; n >= 0; n--){
	      message[0] = lang[i];
	      message[1] = lang[j];
	      message[2] = lang[k];
	      message[5] = lang[l];
	      message[7] = lang[m];
	      message[9] = lang[n];
	      /* compute the digest */
	      counter++;
	      MD5_Init(&c);
	      MD5_Update(&c, message, 11);
	      MD5_Final((unsigned char *)buffer, &c);
	      /* if the digest matches the initialized cipher, print the message that caused the collision, and time and quit */
	      if(strcmp(cipher,buffer)==0){		
		printf("match found! message was: %s\n", message);
		end = time(NULL);
		printf("time: %lu\n",(long)end-start);
		exit(0);
	      }
	    }
	  }
	}
      }
    }
  }
  end = time(NULL);
  printf("time: %lu\n",(long)end-start);
  return 0;
}
