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
#include <pthread.h>
#include <openssl/md5.h>
#include <openssl/crypto.h>

uint64_t search_space = 62523502209ULL;/* search space is 63^6 */
uint64_t counter = 0ULL; 
time_t start;
time_t end;
pthread_mutex_t *mutex_buffer;

/* functions required by openssl to handle multithreading with pthreads class 
   code taken from 'the definitive guide to linux network programming, pages 250ish*/
static unsigned long thread_id_function(void) {
  return ((unsigned long) pthread_self());
}

static void locking_function(int mode, int id, const char *file, int line) {
  if(mode & CRYPTO_LOCK)
    pthread_mutex_lock(&mutex_buffer[id]);
  else
    pthread_mutex_unlock(&mutex_buffer[id]);
}

/* When SIGUSR2 is received, output progress of search */
void sig_handler(int signum)
{
  printf("checked %llu or %llu%%\n", (long long unsigned int)counter, (long long unsigned int)(counter*100/search_space));
}

void *fun1();
void *fun2();
void *fun3();
void *fun4();

int main(int argc, char **argv)
{
  int i;
  pthread_t thread1, thread2, thread3, thread4;
  int  iret1, iret2, iret3, iret4;
  start = time(NULL);
  signal(SIGUSR2, sig_handler);
  /* setup for openssl to handle multithreading 
     code taken from 'the definitive guide to linux network programming, pages 250ish*/
  mutex_buffer = (pthread_mutex_t *) malloc(CRYPTO_num_locks() *  sizeof(pthread_mutex_t));
  for(i=0; i<CRYPTO_num_locks(); i++)
    pthread_mutex_init(&mutex_buffer[i],NULL);
  CRYPTO_set_id_callback(thread_id_function);
  CRYPTO_set_locking_callback(locking_function);
  /* each thread searches quarter the space, split on the first character of the message */
  iret1 = pthread_create( &thread1, NULL, fun1, NULL);
  iret2 = pthread_create( &thread2, NULL, fun2, NULL);
  iret3 = pthread_create( &thread3, NULL, fun3, NULL);
  iret4 = pthread_create( &thread4, NULL, fun4, NULL);
  pthread_join( thread1, NULL);
  pthread_join( thread2, NULL); 
  pthread_join( thread3, NULL); 
  pthread_join( thread4, NULL); 
  return 0;
}
void *fun1(){
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
  /* only search four i values as four threads */
  for(i = 62; i >= 0; i-=4){
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
void *fun2(){
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
  /* only search four i values as four threads */
  for(i = 61; i >= 0; i-=4){
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
void *fun3(){
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
  /* only search four i values as four threads */
  for(i = 60; i >= 0; i-=4){
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
void *fun4(){
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
  /* only search four i values as four threads */
  for(i = 59; i >= 0; i-=4){
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
