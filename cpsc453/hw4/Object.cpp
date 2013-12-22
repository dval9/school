#include "Object.h"


void Object::setMaterial(Material * mat){
  m = mat;   
}

Triangle::Triangle(Point v1,Point v2, Point v3, Point no){
  p1 = v1;
  p2 = v2;
  p3 = v3;
    
  n = no;
  isLight = false;
}

Sphere::Sphere(Point p, double r){
  center = p;
  radius = r;
    
  isLight = false;
}
#define EPS 1E-6

double det(Point x, Point y, Point z){
  return (x.x*y.y*z.z)+(y.x*z.y*x.z)+(z.x*x.y*y.z)-(z.x*y.y*x.z)-(y.x*x.y*z.z)-(x.x*z.y*y.z);
}

Point Triangle::getIntersection(Ray r){
  double a1, a2, detm;
  detm = det((p2-p1), (p3-p1), (r.v * -1));
  a1 = det((r.p-p1), (p3-p1), (r.v * -1))/detm;
  a2 = det((p2-p1), (r.p-p1), (r.v * -1))/detm;  
  if(a1 <= 0)
    return Point::Infinite();
  else if(a2 <= 0)
    return Point::Infinite();
  else if(a1 + a2 >= 1)
    return Point::Infinite();
  return (p1 + ((p2-p1)*a1) + ((p3-p1)*a2));
}

Point Triangle::getNormal(Point p){
  Point one = p1-p2;
  Point two = p1-p3;
  Point ret = one.cross(two);
  ret.normalize();
  return ret;
}

Point Sphere::getNormal(Point p){
  Point ret = (p-center);
    
  ret.normalize();
  return ret;
}

Point Sphere::getIntersection(Ray r){
  double one, two, b, c;
  b = ((r.v)*2)*((r.p) - center);
  c = ((r.p - center).length())*(((r.p) - center).length()) - (radius * radius);
  if(((b * b) - (4 * c)) >= 0){
    one = (-b + sqrt((b * b) - (4 * c)))/2;
    two = (-b - sqrt((b * b) - (4 * c)))/2;
    if((one > 0) && (two > 0)){
      one = min(one, two);
      return (r.p)+((r.v) * one);
    }
    else if(!(one > 0) && (two > 0))
      return (r.p)+((r.v) * two);
    else if((one > 0) && !(two > 0))
      return (r.p)+((r.v) * one);
  }	
  return Point::Infinite();  
}

























