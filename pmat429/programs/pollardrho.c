//Tom Crowfoot
//Pollard Rho algorithm
#include <stdio.h>
#include <math.h>

unsigned int alpha=49, beta=13, n=131, N=263;

//from wikipedia binary exp alg page
unsigned int gcd(unsigned int u, unsigned int v)
{
  int shift;
  
  /* GCD(0,v) == v; GCD(u,0) == u, GCD(0,0) == 0 */
  if (u == 0) return v;
  if (v == 0) return u;

  /* Let shift := lg K, where K is the greatest power of 2
     dividing both u and v. */
  for (shift = 0; ((u | v) & 1) == 0; ++shift) {
    u >>= 1;
    v >>= 1;
  }

  while ((u & 1) == 0)
    u >>= 1;

  /* From here on, u is always odd. */
  do {
    /* remove all factors of 2 in v -- they are not common */
    /*   note: v is not zero, so while will terminate */
    while ((v & 1) == 0)  /* Loop X */
      v >>= 1;

    /* Now u and v are both odd. Swap if necessary so u <= v,
       then set v = v - u (which is even). For bignums, the
       swapping is just pointer movement, and the subtraction
       can be done in-place. */
    if (u > v) {
      unsigned int t = v; v = u; u = t;}  // Swap u and v.
    v = v - u;                       // Here v >= u.
  } while (v != 0);

  /* restore common factors of 2 */
  return u << shift;
}

unsigned int f(unsigned int gamma)
{
  if((gamma % 3) == 0)
    return gamma*gamma % N;
  if((gamma % 3) == 1)
    return beta*gamma % N;
  if((gamma % 3) == 2)
    return alpha*gamma % N;
}

unsigned int g(unsigned int gamma, unsigned int m)
{
  if((gamma % 3) == 0)
    return (2*m) % n;
  if((gamma % 3) == 1)
    return (m+1) % n;
  if((gamma % 3) == 2)
    return m;
}

unsigned int h(unsigned int gamma, unsigned int m)
{
  if((gamma % 3) == 0)
    return (2*m) % n;
  if((gamma % 3) == 1)
    return m;
  if((gamma % 3) == 2)
    return (m+1) % n;
}

int main(int argc, char **argv)
{
  int i=0;
  int y, z, gamma, Y, Z, Gamma;
  int a,b;
  y=1;
  z=0;
  gamma=pow(beta, y);
  Y=y;
  Z=z;
  Gamma=gamma;
  printf("i\ty\tz\tg\ty2\tz2\tg2\n");
  printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\n", i,y,z,gamma,Y,Z,Gamma);
  for(i=1;;i++){
    y=g(gamma,y);
    z=h(gamma,z);
    gamma=f(gamma);
    Y=g(Gamma,Y);
    Z=h(Gamma,Z);
    Gamma=f(Gamma);
    Y=g(Gamma,Y);
    Z=h(Gamma,Z);
    Gamma=f(Gamma);
    printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\n", i,y,z,gamma,Y,Z,Gamma);
    if(gamma == Gamma && gcd(Z-z,n)==1)
      break;
  }
  printf("Solve: %d-%d=x(%d-%d) mod %d\n",y,Y,Z,z,n);
  a=y-Y;
  b=Z-z;
  a=a/b % n;
  printf("x=%d\n",a);
  return 0;
}
