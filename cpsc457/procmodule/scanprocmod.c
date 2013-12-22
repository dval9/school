#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/sched.h>
#include <linux/moduleparam.h>
#include <linux/proc_fs.h>

MODULE_LICENSE("GPL");

static struct task_struct *task = &init_task;
static int flag = 0;
struct mm_struct *mm;

int read_proc(char *buffer,char **start,off_t offset,int count,int *peof,void *dat)
{  
  int len = 0;
  *start = buffer;
  do{
    mm = task->mm;
    if(flag == 0)
      len += sprintf(buffer+len, "pid:pgd:start_stack:stack_canary\n");
    if(len+50 > count)
      return len;
    if(task == &init_task && flag == 1){
      *peof = 1;
      return len;
    }
    flag = 1;
    if(mm){
      down_read(&mm->mmap_sem);
      len += sprintf(buffer+len, "%d:%08x:%08x:%08x\n",
		     task->pid,
		     (unsigned int)&mm->pgd,
		     (unsigned int)&mm->start_stack,
		     (unsigned int)task->stack_canary);
      up_read(&mm->mmap_sem);
    }
  }while((task = next_task(task)) != &init_task);
  return len;
}

int init_module(void)
{ 
  create_proc_read_entry("scan_procs",0,NULL,read_proc,NULL);
  return 0;
}

void cleanup_module(void)
{
  remove_proc_entry("scan_procs",NULL);
}
