/**
 *
 * Tom Crowfoot
 * 10037477
 * CPSC 457 
 * Problem 5
 * Single threaded program to find a collision with a MD5 hash.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <inttypes.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>
#include <openssl/md5.h>

uint64_t search_space = 62523502209ULL; /* search space is 63^6 */
uint64_t counter = 0ULL; 
unsigned char *message;

/* When SIGUSR2 is received, output progress of search */
void sig_handler(int signum)
{
  printf("checked %llu or %llu%%\n", (long long unsigned int)counter, (long long unsigned int)(counter*100/search_space));
}

int main(int argc, char **argv)
{
  signal(SIGUSR2, sig_handler);
  time_t start = time(NULL);
  time_t end;  
  int i, j, k, l, m, n;
  MD5_CTX c;
  char *lang = malloc(26+26+10+1); /* [A..Za..z0..9_] */
  message = malloc(11);
  unsigned char *buffer = malloc(16);
  unsigned char *cipher = malloc(16);  
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
  for(i = 62; i >= 0; i--){
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
	      if(strcmp((const char *)cipher,(const char *)buffer)==0){		
		printf("match found! message was: %s\n", message);
		end = time(NULL);
		printf("time: %lu\n",(long)end-start);
		exit (0);
	      }
	    }
	  }
	}
      }
    }
  }
  /* hopefully won't get here, means that no collision was found */
  end = time(NULL);
  printf("time: %lu\n",(long)end-start);
  return 0;
}
