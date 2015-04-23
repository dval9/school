#include <stdio.h>
#include <math.h>
#include <stdlib.h>

unsigned long long a=2,b=5,n=26123;

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

unsigned long long modpow(unsigned long long a, unsigned long long m){
  int i;
  unsigned long long r=a;;
  for(i=0;i<m-1;i++){
    r=(r*a) % n;
  }
  return r;
}


int main(int argc, char **argv){
  unsigned long long q,i=0,kq,d,qkq;
  int *primes = malloc(sizeof(unsigned long long)*20);
  primes[0] = (unsigned long long)2;
  primes[1] = (unsigned long long)3;
  primes[2] = (unsigned long long)5;
  primes[3] = (unsigned long long)7;
  primes[4] = (unsigned long long)11;
  primes[5] = (unsigned long long)13;
  primes[6] = (unsigned long long)17;
  primes[7] = (unsigned long long)19;
  primes[8] = (unsigned long long)23;
  primes[9] = (unsigned long long)29;
  primes[10] = (unsigned long long)31;
  primes[11] = (unsigned long long)37;
  primes[12] = (unsigned long long)41;
  primes[13] = (unsigned long long)43;
  primes[14] = (unsigned long long)47;
  primes[15] = (unsigned long long)53;
  primes[16] = (unsigned long long)59;
  primes[17] = (unsigned long long)61;
  primes[18] = (unsigned long long)67;
  primes[19] = (unsigned long long)71;
  printf("q\tkq\tq^kq\ta mod n\n");
  for(;;){
    q=primes[i];
    if(q > b)
      break;
    kq = (unsigned long long)floor(log(n)/log(q));
    qkq=pow(q,kq);
    a=(int)modpow(a,qkq);
    printf("%d\t%d\t%d\t%d\n",q,kq,qkq,a);
    i++;
  }
  d=gcd(a-1,n);
  
  printf("d=%d so n=%d*%d\n",d,d,n/d);
  return 0;
}
