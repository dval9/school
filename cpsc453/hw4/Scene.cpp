#include "Scene.h"

void Scene::startIteration(){
    indexO = 0;
}
// N is width/height
// the field of view is specified in degrees.
Scene::Scene(Point * vo, Point * no, double f, int No){
    v = vo;
    n = no;
    fov = f*acos(-1)/180.0;

    // scale n to account for implicit image plane.
    *n = (*n)*(1/(2*tan(fov/2.0)));
    Point uo = v->cross(*n);
    uo.normalize();
    u = new Point(uo.x,uo.y,uo.z);
    C = Point(0,0,0);
    N = No;

    indexO = 0;
    indexL = 0;
}

Object * Scene::getNextObject(){
  if((unsigned int)indexO == (unsigned int)objects.size()) return NULL;
    return objects[indexO++];
}

Point Scene::getNextLight(){
    indexL++;
    return lights[indexL%lights.size()];    
}

void Scene::addObject(Object * o){
    objects.push_back(o);   
}

void Scene::addLight(Point l){
    lights.push_back(l);
}

// Sets the origin of the camera
void Scene::setCamera(Point * c){
    camera = c;
}

// A ray through a point on the image plane.
// The location of the image plane in the n-direction is implicitly taken to be the point where
// the width and height of the image plane are both 1. Please see the constructor above.
Ray Scene::makeRay(double xo, double yo){
    // calculate point on image plane
    double normx = (xo/N)-0.5;
    double normy = (yo/N)-0.5;
    
    Point po = (*u)*normx + (*v)*normy + (* camera) + (*n);
    
    Ray r = Ray(po, po - (*camera));
    return r;
}

// A test scene based on description handed out with the assignment.
// Please modify material properties as necessary to test your ray tracer.

Scene * Scene::initTestScene(int N){

    // make new scene with up vector, direction vector and fov
    Scene * ret = new Scene(new Point(0,1.0,0),new Point(0,0,1.0),55.0,N);  
    // Add in sphere
	
	Material * test = new Material();
    test->type = DIFFUSE;
    test->kd = 0.03;
    test->ambient = Color(0.0,0.0,0.0,1.0);
    test->diffuse = Color(0.0,0.6,0.6,1.0);
    test->specular = Color(0.2,0.2,0.2,1.0);
    
    Material * test1 = new Material();
    test1->kd = 0.03;
    test1->type = DIFFUSE;
    test1->ambient = Color(0.3,0.3,0.3,1.0);
    test1->diffuse = Color(0.8,0.8,0.8,1.0);
    test1->specular = Color(0.2,0.2,0.2,1.0);
    Material * test2 = new Material();
    test2->kd = 0.25;
    test2->type = DIFFUSE;
    test2->ambient = Color(0.4,0.25,0.25,1.0);
    test2->diffuse = Color(0.5,0.1,0.1,1.0);
    test2->specular = Color(0.2,0.2,0.2,1.0);	

    Material* sphere = new Material();
    sphere->type = REFLECTIVE;
    // give sphere some reflectivity;
    sphere->kr = 0.85;
    sphere->ambient = Color(0.00,0.00,0.00,1.0);
    sphere->diffuse = Color(0.0,0.0,0.0,1.0);
    sphere->specular = Color(0.0,0.0,0.0,1.0);
	
    Object * s1 = new Sphere(Point(400.0,130.0,320.0),120.0);
	s1->setMaterial(sphere);
	ret->addObject(s1);
	
    // Make points for square
    Point p1 = Point(100,165,65);
    Point p2 = Point(52,165, 225);
    Point p3 = Point(210, 165, 272);
    Point p4 = Point(260, 165, 114);
	
    Point p5 = Point(260, 0, 114);
    Point p6 = Point(260, 165, 114);
    Point p7 = Point(210, 165, 272);
    Point p9 = Point(210, 0, 272);
	
	Point p10 = Point(100, 0, 65);
	Point p11 = Point(100, 165, 65);
	Point p12 = Point(260, 165, 114);
	Point p13 = Point(260, 0, 114);
	
	Point p14 = Point(52, 0, 225);
	Point p15 = Point(52, 165, 225);
	Point p16 = Point(100, 165, 65);
	Point p17 = Point(100, 0, 65);
	
	Point p18 = Point(210, 0, 272);
	Point p19 = Point(210, 165, 272);
	Point p20 = Point(52, 165, 225);
	Point p21 = Point(52, 0, 225);
	
	Point n = Point(1,1,1);
    // make normals for triangle
    Object* f1 = new Triangle(p14,p15,p16,n);
	Object* f2 = new Triangle(p16,p17,p14,n);
    Object* l1 = new Triangle(p18,p19,p20,n);
	Object* l2 = new Triangle(p20,p21,p18,n);
	Object* r1 = new Triangle(p10,p11,p12,n);
	Object* r2 = new Triangle(p12,p13,p10,n);
	Object* b1 = new Triangle(p5,p6,p7,n);
	Object* b2 = new Triangle(p7,p9,p5,n);
	Object* t1 = new Triangle(p1,p2,p3,n);
	Object* t2 = new Triangle(p3,p4,p1,n);

	
    t1->setMaterial(test1);
    t2->setMaterial(test1);
	ret->addObject(t1);
    ret->addObject(t2);
	
	l1->setMaterial(test1);
    l2->setMaterial(test1);
	ret->addObject(l1);
    ret->addObject(l2);
	
	r1->setMaterial(test1);
    r2->setMaterial(test1);
	ret->addObject(r1);
    ret->addObject(r2);
	
	b1->setMaterial(test1);
    b2->setMaterial(test1);
	ret->addObject(b1);
    ret->addObject(b2);
	
	f1->setMaterial(test1);
    f2->setMaterial(test1);
	ret->addObject(f1);
    ret->addObject(f2);
	
    // Add light sources
    ret->addLight(Point(185.0,2000.0,169.0));
    ret->addLight(Point(400.0,2000.0,320.0));

    // set Camera location
    ret->setCamera(new Point(278,273,-500));

    // walls
    // Make points for square
    Point pp1 = Point(550,  0, 0);
    Point pp2 = Point(0, 0, 0);
    Point pp3 = Point(0, 0, 560);
    Point pp4 = Point(550, 0, 560);
	
    Point pp5 = Point(560, 550, 0);
    Point pp6 = Point(560, 550, 560);
    Point pp7 = Point(0, 550, 560);
    Point pp9 = Point(0, 550, 0);
	
	Point pp10 = Point(550, 0, 560);
	Point pp11 = Point(0, 0, 560);
	Point pp12 = Point(0, 550, 560);
	Point pp13 = Point(560, 550, 560);
	
	Point pp14 = Point(0, 0, 560);
	Point pp15 = Point(0, 0, 0);
	Point pp16 = Point(0, 550, 0);
	Point pp17 = Point(0, 550, 560);
	
	Point pp18 = Point(550, 0, 0);
	Point pp19 = Point(550, 0, 560);
	Point pp20 = Point(560, 550, 560);
	Point pp21 = Point(560, 550, 0);
	
	
    // make normals for triangle
    Object* bo1 = new Triangle(pp1,pp2,pp3,n);
	Object* bo2 = new Triangle(pp3,pp4,pp1,n);
	Object* ba1 = new Triangle(pp10,pp11,pp12,n);
	Object* ba2 = new Triangle(pp12,pp13,pp10,n);
	Object* le1 = new Triangle(pp14,pp15,pp16,n);
	Object* le2 = new Triangle(pp16,pp17,pp14,n);
	Object* ri1 = new Triangle(pp18,pp19,pp20,n);
	Object* ri2 = new Triangle(pp20,pp21,pp18,n);
	
    bo1->setMaterial(test);
    bo2->setMaterial(test);
	ret->addObject(bo1);
    ret->addObject(bo2);	
	
	le1->setMaterial(test2);
    le2->setMaterial(test2);
	ret->addObject(le1);
    ret->addObject(le2);
	
	ri1->setMaterial(test1);
    ri2->setMaterial(test1);
	ret->addObject(ri1);
    ret->addObject(ri2);
	
	ba1->setMaterial(test2);
    ba2->setMaterial(test2);
	ret->addObject(ba1);
    ret->addObject(ba2);
    
    return ret;
}

Scene * Scene::initTestSceneCustom(int N){

    // make new scene with up vector, direction vector and fov
    Scene * ret = new Scene(new Point(0,1.0,0),new Point(0,0,1.0),55.0,N);  
    // Add in sphere
	
	Material * test = new Material();
    test->type = DIFFUSE;
    test->kd = 0.03;
    test->ambient = Color(0.0,0.0,0.0,1.0);
    test->diffuse = Color(0.0,0.6,0.6,1.0);
    test->specular = Color(0.2,0.2,0.2,1.0);
    
    Material * test1 = new Material();
    test1->kd = 0.03;
    test1->type = DIFFUSE;
    test1->ambient = Color(0.3,0.3,0.3,1.0);
    test1->diffuse = Color(0.8,0.8,0.8,1.0);
    test1->specular = Color(0.2,0.2,0.2,1.0);
    Material * test2 = new Material();
    test2->kd = 0.25;
    test2->type = DIFFUSE;
    test2->ambient = Color(0.4,0.25,0.25,1.0);
    test2->diffuse = Color(0.5,0.1,0.1,1.0);
    test2->specular = Color(0.2,0.2,0.2,1.0);

    Material* sphere = new Material();
    sphere->type = REFLECTIVE;
    // give sphere some reflectivity;
    sphere->kr = 0.85;
    sphere->ambient = Color(0.00,0.0,0.00,1.0);
    sphere->diffuse = Color(0.0,0.0,0.0,1.0);
    sphere->specular = Color(0.0,0.0,0.0,1.0);
	
    Object * s1 = new Sphere(Point(400.0,130.0,320.0),120.0);
	s1->setMaterial(sphere);
	ret->addObject(s1);
	Object * s2 = new Sphere(Point(640.0,130.0,320.0),120.0);
	s2->setMaterial(sphere);
	ret->addObject(s2);
	Object * s3 = new Sphere(Point(140.0,130.0,320.0),120.0);
	s3->setMaterial(sphere);
	ret->addObject(s3);
	
    // Add light sources
    ret->addLight(Point(185.0,2000.0,169.0));
    ret->addLight(Point(400.0,2000.0,320.0));
	ret->addLight(Point(300.0,2000.0,200.0));

    // set Camera location
    ret->setCamera(new Point(278*2,273,-500));

    // walls
    // Make points for square
    Point pp1 = Point(1100,  0, 0);
    Point pp2 = Point(0, 0, 0);
    Point pp3 = Point(0, 0, 1120);
    Point pp4 = Point(1100, 0, 1120);
	Point n = Point(1, 1, 1);
	
    // make normals for triangle
    Object* bo1 = new Triangle(pp1,pp2,pp3,n);
	Object* bo2 = new Triangle(pp3,pp4,pp1,n);
	
    bo1->setMaterial(test);
    bo2->setMaterial(test);
	ret->addObject(bo1);
    ret->addObject(bo2);
    
    return ret;
}

