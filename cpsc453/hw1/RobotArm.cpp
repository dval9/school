// RobotArm.cpp
// Skeleton code by Usman Alim
// CPSC 453 - Fall 2013 

// Based on the GLTools library that accompanies 
// The OpenGL SuperBible

#include <GLTools.h>
#include <GLShaderManager.h>
#include <GLFrustum.h>
#include <GLBatch.h>
#include <GLMatrixStack.h>
#include <GLGeometryTransform.h>

#include <math.h>
#include <stdio.h>

#ifdef __APPLE__
#include <glut/glut.h>
#else
#define FREEGLUT_STATIC
#include <GL/glut.h>
#endif

GLShaderManager		shaderManager;			// Shader Manager
GLMatrixStack		modelViewMatrix;		// Modelview Matrix
GLMatrixStack		projectionMatrix;		// Projection Matrix
GLFrustum			viewFrustum;			// View Frustum
GLGeometryTransform	transformPipeline;		// Geometry Transform pipeline

// batch geometry for floor
GLBatch	floorBatch;

// batch geometry for the robot arm
GLTriangleBatch stem;
GLTriangleBatch forearm;
GLTriangleBatch hand;

// initial lengths of the arm components

GLfloat stemLength = 4.0f;
GLfloat forearmLength = 3.0f;
GLfloat handLength = 2.0f;
GLfloat stemAngle = 0.0f;
GLfloat forearmAngle = 0.0f;
GLfloat handAngle = 0.0f;
GLfloat scale = 1.0f;
        
//////////////////////////////////////////////////////////////////
// This function does any needed initialization on the rendering
// context. 
void SetupRC()
{
	// Initialze Shader Manager
	shaderManager.InitializeStockShaders();
	
	glEnable(GL_DEPTH_TEST);
	//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

  // antialiasing stuff
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  glEnable(GL_BLEND);
  glEnable(GL_LINE_SMOOTH);
  glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
  glLineWidth((GLfloat)2.0f);
  
  
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	
	// Make spheres to represent the arm components.
  // The triangulation of a sphere is wrapped around the z axis of the sphere
	gltMakeSphere(stem, 0.5f*stemLength, 30, 15);
  gltMakeSphere(forearm, 0.5f*forearmLength, 30, 15);
  gltMakeSphere(hand, 0.5f*handLength, 30, 15);
  
  floorBatch.Begin(GL_LINES, 84);
    for(GLfloat x = -20.0f; x <= 20.0f; x+= 2.0f) {
      floorBatch.Vertex3f(x, -5.0, 20.0f);
      floorBatch.Vertex3f(x, -5.0f, -20.0f);
      
      floorBatch.Vertex3f(20.0f, -5.0f, x);
      floorBatch.Vertex3f(-20.0f, -5.0f, x);
    }
    floorBatch.End();    
}

///////////////////////////////////////////////////
// Screen changes size or is initialized
void ChangeSize(int nWidth, int nHeight)
{
	glViewport(0, 0, nWidth, nHeight);
  
  float near = 1.0f;
  float far = near + 56.0f;
  
  //Create the projection matrix, and load it on the projection
  //matrix stack
  
  viewFrustum.SetPerspective(60.0f, ((float)nWidth)/(nHeight), near, far);
  projectionMatrix.LoadMatrix(viewFrustum.GetProjectionMatrix());
  projectionMatrix.Translate( 0.0f, 0.0f, -near - 0.5f*(far-near));
  // Set the transformation pipeline to use the two matrix stacks 
	transformPipeline.SetMatrixStacks(modelViewMatrix, projectionMatrix);
}

        
// Called to draw scene
void RenderScene(void)
	{
    // Color values
    static GLfloat vFloorColor[] = { 0.5f, 0.6f, 0.5f, 1.0f};
    static GLfloat vStemColor[] = {1.0f, 0.0f, 0.0f, 1.0f};
    static GLfloat vForearmColor[] = {0.0f, 1.0f, 0.0f, 1.0f};
    static GLfloat vHandColor[] = {0.0f, 0.0f, 1.0f, 1.0f};
    
    static GLfloat lightPos[] = {0.0f, 0.0f, 200.0f};

    // Clear the color and depth buffers
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    // Save the current modelview matrix (the identity matrix)
    modelViewMatrix.PushMatrix();	
		
    // Draw the ground
    shaderManager.UseStockShader(GLT_SHADER_FLAT,
                                 transformPipeline.GetModelViewProjectionMatrix(),
                                 vFloorColor);	
    floorBatch.Draw();
    
    // Draw the components as ellipses.
    // First we get the spheres in the desired position and
    // orientation, then scale them in the x and y (in the reference
    // frame of the sphere) directions just before drawing them.
    
	//ellipses are stretched along the z and y axis to give long appearance on x axis
	//stem needs to rotate around y axis
	//forearm is moved to end of stem, rotated to desired, then moved so the end of forearm touches end of stem
	//hand is moved in similar way in relation to forearm
	//forearm and hand move up and down, or rotate along z axis
    modelViewMatrix.Rotate(stemAngle, 0.0f, 1.0f, 0.0f);//rotate stem here
    modelViewMatrix.Translate(2.0f*scale, 0.0f, 0.0f);
    modelViewMatrix.PushMatrix();
    modelViewMatrix.Scale(1.0*scale, 0.2f*scale, 0.2f*scale);//scale stem here
    shaderManager.UseStockShader(GLT_SHADER_POINT_LIGHT_DIFF, transformPipeline.GetModelViewMatrix(),
                                 transformPipeline.GetProjectionMatrix(),
                                 lightPos, vStemColor);
    stem.Draw();
    modelViewMatrix.PopMatrix();

    modelViewMatrix.Translate(2.0f*scale, 0.0f, 0.0f);
    modelViewMatrix.Rotate(forearmAngle, 0.0f, 0.0f, 1.0f);//rotate forearm here
    modelViewMatrix.Translate(1.5f*scale, 0.0f, 0.0f);
    modelViewMatrix.PushMatrix();
    modelViewMatrix.Scale(1.0f*scale, 0.2f*scale, 0.2f*scale);//scale forearm here
    shaderManager.UseStockShader(GLT_SHADER_POINT_LIGHT_DIFF, transformPipeline.GetModelViewMatrix(),
                                 transformPipeline.GetProjectionMatrix(),
                                 lightPos, vForearmColor);
    forearm.Draw();
    modelViewMatrix.PopMatrix();
    
    modelViewMatrix.Translate(1.5f*scale, 0.0f, 0.0f);
    modelViewMatrix.Rotate(handAngle, 0.0f, 0.0f, 1.0f);//rotate hand here
    modelViewMatrix.Translate(1.0f*scale, 0.0f, 0.0f);
    modelViewMatrix.Scale(1.0f*scale,0.2f*scale,0.2f*scale);//scale hand here
    shaderManager.UseStockShader(GLT_SHADER_POINT_LIGHT_DIFF, transformPipeline.GetModelViewMatrix(),
                                 transformPipeline.GetProjectionMatrix(),
                                 lightPos, vHandColor);
    hand.Draw();

    // Restore the original modleview matrix (the idenity matrix)
    modelViewMatrix.PopMatrix();
    
    // Do the buffer Swap
    glutSwapBuffers();
    
    // Tell GLUT to do it again
    glutPostRedisplay();
}

void Keyboard(unsigned char key, int x, int y){
	if(key == 'a')
		handAngle -= 6;
	if(key == 's')
		handAngle += 6;
}

void KeyboardArrows(int key, int x, int y){
	if(key == GLUT_KEY_DOWN)
		forearmAngle -= 6;
	if(key == GLUT_KEY_UP)
		forearmAngle += 6;
	if(key == GLUT_KEY_LEFT)
		stemAngle -= 6;
	if(key == GLUT_KEY_RIGHT)
		stemAngle += 6;
}

void Mouse(int button, int state, int x, int y){
	if(button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		if(scale > 0.05f)
			scale -= 0.05f;
	}
	else if(button == GLUT_LEFT_BUTTON && state == GLUT_DOWN){
		if(scale < 5.0f)
			scale += 0.05f;
	}
}

int main(int argc,
		 char* argv[])
{
	gltSetWorkingDirectory(argv[0]);		
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(800,600);  

    glutCreateWindow("RobotArm for HW1");

    glutReshapeFunc(ChangeSize);
    glutDisplayFunc(RenderScene);
    glutKeyboardFunc(Keyboard);
	glutSpecialFunc(KeyboardArrows);
    glutMouseFunc(Mouse);

    GLenum err = glewInit();
    if (GLEW_OK != err) {
        fprintf(stderr, "GLEW Error: %s\n", glewGetErrorString(err));
        return 1;
        }
        

    SetupRC();
    glutMainLoop();    
    return 0;
}
