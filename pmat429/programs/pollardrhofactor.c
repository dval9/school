#include <stdio.h>
#include <math.h>

const unsigned long long n = 1365509;

//from wikipedia binary exp alg page
unsigned long long gcd(unsigned long long u, unsigned long long v)
{
  unsigned long long shift;

  if (u == 0) return v;
  if (v == 0) return u;

  for (shift = 0; ((u | v) & 1) == 0; ++shift) {
    u >>= 1;
    v >>= 1;
  }

  while ((u & 1) == 0)
    u >>= 1;

  do {
    while ((v & 1) == 0)  
      v >>= 1;

    if (u > v) {
      unsigned long long t = v; v = u; u = t;}  // Swap u and v.
    v = v - u;                       // Here v >= u.
  } while (v != 0);

  return u << shift;
}

unsigned long long f(unsigned long long x){
  return (x*x+1) % n;
}

int main(int argc, char **argv){
  unsigned long long x=2,x2=x, d, i;
  printf("i\txi\tx2i\td\n");
  for(i=1;;i++){
    x=f(x);
    x2=f(x2);
    x2=f(x2);
    d=n-x+x2;
    d=gcd(d,n);
    printf("%d\t%d\t%d\t%d\n",i,x,x2,d);
    if(d>1 && d<n)
      break;
    if(d==n)
      break;
  }
  printf("%d=%d*%d\n",n,d,n/d);
  return 0;
}
