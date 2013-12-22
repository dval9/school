#include <sys/types.h> /*for open(2) and fstat64(2)*/
#include <sys/stat.h> /*for open(2) and fstat64(2)*/
#include <fcntl.h> /*for open(2)*/
#include <sys/mman.h> /*for mmap2(2)*/
#include <unistd.h> /*for close(2) and fstat64(2)*/
#include <stdio.h>
#include <stdlib.h>

int main()
{
  int inj_fd;
  void *start_addr;
  void *brk;
  struct stat st;
  void (*assm_func)() = NULL;
  inj_fd = open("jitasm.bin", O_RDWR);
  fstat(inj_fd, &st);
  brk = sbrk(st.st_size);
  start_addr = mmap(brk, st.st_size, PROT_READ | PROT_WRITE | PROT_EXEC, MAP_SHARED | MAP_FIXED, inj_fd, 0);
  close(inj_fd);
  assm_func = (void (*)(void)) start_addr;
  printf("***before code injection***\n");
  (*assm_func)();
  printf("***after code injection***\n");
  return 0;
}
