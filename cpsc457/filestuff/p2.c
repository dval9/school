/**
 *
 * Tom Crowfoot
 * 10037477
 * CPSC 457 
 * Problem 2
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char **argv){
  if(argc == 2 && *argv[1] == 'a'){
    int fd;
    char *buffer;
    int i;
    uint64_t clock_speed;
    uint64_t time, time2;
    unsigned low, high, low1, high1;
    clock_speed = 3377138;
    clock_speed *= 1000;    
    buffer = (char*)malloc(1024);
    if(buffer == NULL){
      printf("Error getting buffer\n");
      return -1;
    }
    for(i=0;i<1024;i++)
      buffer[i] = 0;
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high), "=r" (low));
    fd = open("/media/ext2hw3/1gbfile", O_RDWR | O_CREAT);
    if(fd == -1){
      printf("Error opening file\n");
      return -1;
    }	
    for(i=0;i<1024*1024;i++)
      write(fd, buffer, 1024);
    close(fd);
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high1), "=r" (low1));
    time = (((uint64_t)high<<32)|low);
    time2 = (((uint64_t)high1<<32)|low1);
    printf("Time in CPU ticks: %llu\n",(long long unsigned int)time2-time);
    printf("Time in seconds: %llu\n",(long long unsigned int)(time2-time)/clock_speed);
    free(buffer);
  }
  else if(argc == 2 && *argv[1] == 'b'){
    int fd;
    char *buffer;
    int i, r;
    uint64_t clock_speed;
    uint64_t time, time2;
    unsigned low, high, low1, high1;
    clock_speed = 3377138;
    clock_speed *= 1000;    
    buffer = (char*)malloc(1024);
    if(buffer == NULL){
      printf("Error getting buffer\n");
      return -1;
    }
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high), "=r" (low));
    fd = open("/media/ext2hw3/1gbfile", O_RDONLY);
    if(fd == -1){
      printf("Error opening file\n");
      return -1;
    }	
    for(i=0;i<1024*1024;i++){
      r = read(fd, buffer, 1024);
      if(r == -1){
	printf("Error reading file\n");
	return -1;
      }
    }
    close(fd);
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high1), "=r" (low1));
    time = (((uint64_t)high<<32)|low);
    time2 = (((uint64_t)high1<<32)|low1);
    printf("Time in CPU ticks: %llu\n",(long long unsigned int)time2-time);
    printf("Time in seconds: %llu\n",(long long unsigned int)(time2-time)/clock_speed);
    free(buffer);
  }
  else if(argc == 2 && *argv[1] == 'c'){
    int fd;
    char *buffer;
    int i;
    uint64_t clock_speed;
    uint64_t time, time2;
    unsigned low, high, low1, high1;
    clock_speed = 3377138;
    clock_speed *= 1000;    
    buffer = (char*)malloc(1024*4);
    if(buffer == NULL){
      printf("Error getting buffer\n");
      return -1;
    }
    for(i=0;i<1024*4;i++)
      buffer[i] = 0;
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high), "=r" (low));
    fd = open("/media/ext2hw3/4kbfile", O_RDWR|O_CREAT);
    if(fd == -1){
      printf("Error opening file\n");
      return -1;
    }	
    for(i=0;i<1000000;i++){
      write(fd, buffer, 1024*4);
      lseek(fd, 0, SEEK_SET);
    }
    close(fd);
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high1), "=r" (low1));
    time = (((uint64_t)high<<32)|low);
    time2 = (((uint64_t)high1<<32)|low1);
    printf("Time in CPU ticks: %llu\n",(long long unsigned int)time2-time);
    printf("Time in seconds: %llu\n",(long long unsigned int)(time2-time)/clock_speed);
    free(buffer);
  }
  else if(argc == 2 && *argv[1] == 'd'){
    int fd;
    char *buffer;
    int i, r;
    uint64_t clock_speed;
    uint64_t time, time2;
    unsigned low, high, low1, high1;
    clock_speed = 3377138;
    clock_speed *= 1000;
    buffer = (char*)malloc(1024*4);
    if(buffer == NULL){
      printf("Error getting buffer\n");
      return -1;
    }
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high), "=r" (low));	
    fd = open("/media/ext2hw3/4kbfile", O_RDONLY);
    if(fd == -1){
      printf("Error opening file\n");
      return -1;
    }
    for(i=0;i<1000000;i++){
      r = read(fd, buffer, 1024*4);
      if(r == -1){
	printf("Error reading file\n");
	return -1;
      }
      lseek(fd, 0, SEEK_SET);
    }
    close(fd);
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high1), "=r" (low1));
    time = (((uint64_t)high<<32)|low);
    time2 = (((uint64_t)high1<<32)|low1);
    printf("Time in CPU ticks: %llu\n",(long long unsigned int)time2-time);
    printf("Time in seconds: %llu\n",(long long unsigned int)(time2-time)/clock_speed);
    free(buffer);
  }
  else if(argc == 2 && *argv[1] == 'e'){
    int fd;
    char *buffer;
    int i, j;
    uint64_t clock_speed;
    uint64_t time, time2;
    unsigned low, high, low1, high1;
    char name[40];
    clock_speed = 3377138;
    clock_speed *= 1000;
    buffer = (char*)malloc(1024);
    if(buffer == NULL){
      printf("Error getting buffer\n");
      return -1;
    }
    for(i=0;i<1024;i++)
      buffer[i] = 0;
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high), "=r" (low));
    for(j = 0; j < 100; j++){
      sprintf(name, "%s%d/", "/media/ext2hw3/", j);
      mkdir(name, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
      for(i = 0; i < 10000; i++){
	sprintf(name, "%s%d/%d", "/media/ext2hw3/", j, i);
	fd = open(name, O_RDWR|O_CREAT);
	if(fd == -1){
	  printf("Error opening file\n");
	  return -1;
	}
	write(fd, buffer, 1024);
	close(fd);
      }
    }
    asm volatile ("cpuid\n\t"
		  "rdtsc \n\t"
		  "mov %%edx, %0\n\t"
		  "mov %%eax, %1\n\t": "=r" (high1), "=r" (low1));
    time = (((uint64_t)high<<32)|low);
    time2 = (((uint64_t)high1<<32)|low1);
    printf("Time in CPU ticks: %llu\n",(long long unsigned int)time2-time);
    printf("Time in seconds: %llu\n",(long long unsigned int)(time2-time)/clock_speed);
    free(buffer);
  }
  else
    printf("args: a|b|c|d|e\n");
  return 0;
}
