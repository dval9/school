#include "RayTracer.h"
#include <stdio.h>

RayTracer::RayTracer(Scene * s, int maxd, int sm){
  scene = s;
  maxdepth = maxd;
  samples = sm;
}

// This function determines the closest object that a ray intersects.
// The getIntersection() method is polymorphic. Please provide implementations
// for all the objects in your scene (see Object.h and Object.cpp).
Object * RayTracer::intersect(Ray r){
  scene->startIteration();
  Object * current = NULL;
  Object * minObject = NULL;
  Point inter;
  //Initialize min to infinite point;
  Point min = Point::Infinite();
  while((current = scene->getNextObject()) != NULL){
    inter = current->getIntersection(r);
    if((inter - r.p).length() > 1E-6){
      if((inter-r.p).length()  < (min-r.p).length()){
        min = inter;
        minObject = current;
      }
    }
  }
  return minObject;
}

// Trace a ray recursively
Color RayTracer::trace(Ray r, int depth){
  Color rad=Color(0.0,0.0,0.0,0.0);
  Object* nearest = intersect(r);
  if(nearest){
    if(depth < maxdepth){
      rad = Phong(nearest->getNormal(nearest->getIntersection(r)), nearest->getIntersection(r), r, nearest->getMaterial(), nearest, depth);
    }
  }
  return rad;
}

// Local Phong illumination at a point.
Color RayTracer::Phong(Point normal,Point p, Ray r, Material * m, Object * o, int depth){
  Color ret = (m->getAmbient(p));
  for(unsigned int i=0; i < (scene->lights).size(); i++){
    Point srd = p - (scene->lights[i]);
    srd.normalize();
    Ray sr = Ray(scene->lights[i], srd);
    Object* ob = intersect(sr);
    if(ob == o){
      ret = ret + m->getDiffuse(p) * (r.v * normal) * m->kd;
      ret = ret + m->getSpecular(p);
    }
  }
  if(m->type == REFLECTIVE){
    Ray ra = r.reflect(normal, p);
    ret = ret + trace(ra, depth + 1);
  }
  ret.clamp(1.0);
  return ret;
}


// This function generates point on the image plane and starts a trace 
// through them.
// Grid supersampling is also implemented.
Color RayTracer::calculate(int x, int y){
  Color c = Color(0.0,0.0,0.0,0.0);
  for(int i = 1; i <= samples; i++){
    for(int j = 1; j <= samples; j++){
      double one, two;
      // We subtract 0.5 so that the (u,v)-origin is at the
      //center of the image plane.
      one = double(x)-0.5+double(i)/double(samples);
      two = double(y)-0.5+double(j)/double(samples);
      Ray r = scene->makeRay(one,two);
      c = c+trace(r,0);
    }
  }
  c = c*(1.0/double(samples*samples));
  return c;
}

