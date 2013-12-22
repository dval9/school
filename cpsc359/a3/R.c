#include <stdlib.h>
#include <time.h>

int old=7;

int randomNumber(void){
	int i=0;
	int n;
	while(i==0){
		n=rand();
		n%=7;
		if(n!=old)
			i=1;
	}
	old=n;
   return(n);
}
