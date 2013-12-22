#include <stdlib.h>

int randomNumber(void){
   int n;
   n=rand();
   n%=7;
   return(n);
}
