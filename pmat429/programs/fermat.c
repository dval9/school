#include <math.h>
#include <stdio.h>

int main(int argc, char **argv){
  int n,a,r,i;
  for(n=2;n<1000;n+=2){
    for(a=2;a<n;a++){
      r=1;
      for(i=1;i<n;i++){
	r=(r*a)%n;
      }
      if(r==1){
	printf("r:%d a:%d n:%d\n",r,a,n);
	return 0;
      }
    }
  }  
  return 0;
}
