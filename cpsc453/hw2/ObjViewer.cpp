// ObjViewer.cpp : Defines the entry point for the console application.
//
// #include "stdafx.h"

#include "TriangleMesh.h"

#include <GLTools.h>
#include <GLShaderManager.h>
#include <GLFrustum.h>
#include <GLBatch.h>
#include <GLMatrixStack.h>
#include <GLGeometryTransform.h>
#include <math.h>
#include <stdio.h>

// #include <glut/freeglut.h>            // Windows FreeGlut equivalent
#include <GL/glut.h>
void rotation(GLMatrixStack& matrx, GLfloat angle, GLfloat x, GLfloat y, GLfloat z);
GLShaderManager		g_shaderManager;			// Shader Manager
GLMatrixStack		g_modelViewMatrix;		// Modelview Matrix
GLMatrixStack		g_projMatrix;		// Projection Matrix
GLMatrixStack		g_orthoProjMatrix;
GLFrustum			g_viewFrustum;			// View Frustum
GLGeometryTransform	g_transformPipeline;		// Geometry Transform pipeline

GLTriangleBatch	g_triangleBatch; // A triangle batch will hold the mesh
// initial lengths of the arm components

GLfloat g_fScale = 300.0f;
GLfloat g_fXPosition = 0.f;
GLfloat g_fYPosition = 0.f;
GLfloat lightX = 100.f;
GLfloat lightY = 100.f;
GLfloat lightZ = 100.f;
GLfloat lightIa = .1f;
GLfloat lightIp = 1.f;
GLint ADSLightShaderF, ADSLightShaderG, ADSLightShaderP;
GLint locAmbientF, locAmbientG, locAmbientP;
GLint locDiffuseF, locDiffuseG, locDiffuseP;
GLint locSpecularF, locSpecularG, locSpecularP;
GLint locLightF, locLightG, locLightP;
GLint locMVPF, locMVPG, locMVPP;
GLint locMVF, locMVG, locMVP;
GLint locNMF, locNMG, locNMP;
GLint g_shaderSelect = 1;
GLint g_viewModeSelect = 1;
GLint vvh,vvw;

CTriangleMesh g_triMesh;
//////////////////////////////////////////////////////////////////
// This function does any needed initialization on the rendering
// context. 
void SetupRC(char* file)
{
	// Initialze Shader Manager

	ADSLightShaderF = g_shaderManager.LoadShaderPairWithAttributes("./shaders/ADSFlat.vp", "./shaders/ADSFlat.fp", 2, GLT_ATTRIBUTE_VERTEX, "vVertex", GLT_ATTRIBUTE_NORMAL, "vNormal");
	ADSLightShaderG = g_shaderManager.LoadShaderPairWithAttributes("./shaders/ADSGouraud.vp", "./shaders/ADSGouraud.fp", 2, GLT_ATTRIBUTE_VERTEX, "vVertex", GLT_ATTRIBUTE_NORMAL, "vNormal");
	ADSLightShaderP = g_shaderManager.LoadShaderPairWithAttributes("./shaders/ADSPhong.vp", "./shaders/ADSPhong.fp", 2, GLT_ATTRIBUTE_VERTEX, "vVertex", GLT_ATTRIBUTE_NORMAL, "vNormal");

	locAmbientF	= glGetUniformLocation(ADSLightShaderF, "ambientColor");
	locDiffuseF = glGetUniformLocation(ADSLightShaderF, "diffuseColor");
	locSpecularF = glGetUniformLocation(ADSLightShaderF, "specularColor");
	locLightF = glGetUniformLocation(ADSLightShaderF, "vLightPosition");
	locMVPF	= glGetUniformLocation(ADSLightShaderF, "mvpMatrix");
	locMVF = glGetUniformLocation(ADSLightShaderF, "mvMatrix");
	locNMF = glGetUniformLocation(ADSLightShaderF, "normalMatrix");

	locAmbientG	= glGetUniformLocation(ADSLightShaderP, "ambientColor");
	locDiffuseG	= glGetUniformLocation(ADSLightShaderP, "diffuseColor");
	locSpecularG = glGetUniformLocation(ADSLightShaderP, "specularColor");
	locLightG = glGetUniformLocation(ADSLightShaderP, "vLightPosition");
	locMVPG = glGetUniformLocation(ADSLightShaderP, "mvpMatrix");
	locMVG = glGetUniformLocation(ADSLightShaderP, "mvMatrix");
	locNMG = glGetUniformLocation(ADSLightShaderP, "normalMatrix");

	locAmbientP	= glGetUniformLocation(ADSLightShaderP, "ambientColor");
	locDiffuseP	= glGetUniformLocation(ADSLightShaderP, "diffuseColor");
	locSpecularP = glGetUniformLocation(ADSLightShaderP, "specularColor");
	locLightP = glGetUniformLocation(ADSLightShaderP, "vLightPosition");
	locMVPP	= glGetUniformLocation(ADSLightShaderP, "mvpMatrix");
	locMVP = glGetUniformLocation(ADSLightShaderP, "mvMatrix");
	locNMP = glGetUniformLocation(ADSLightShaderP, "normalMatrix");

	glEnable(GL_DEPTH_TEST);
	//glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	// antialiasing stuff
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	glEnable(GL_BLEND);
	glEnable(GL_LINE_SMOOTH);
	glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	glLineWidth((GLfloat)2.0f);

	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	g_triMesh.LoadObj(file);
	g_triMesh.ComputeNormals();

	g_triangleBatch.BeginMesh(g_triMesh.m_faces.size() * 3); // #vertices = #triangles * 3
	for( int i = 0; i < (int)g_triMesh.m_faces.size(); i++ ) { //iterate over all the rows in the
		//index list
		M3DVector3f triangleVerts[3];
		M3DVector3f triangleNormals[3];

		M3DVector2f triangleTexCoords[3] = 
		{{0.0f, 0.0f},{0.0f, 0.0f},{0.0f, 0.0f}}; // no texturing for now 

		triangleNormals[0][0] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].x].x;
		triangleNormals[0][1] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].x].y;
		triangleNormals[0][2] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].x].z;

		triangleNormals[1][0] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].y].x;
		triangleNormals[1][1] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].y].y;
		triangleNormals[1][2] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].y].z;

		triangleNormals[2][0] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].z].x;
		triangleNormals[2][1] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].z].y;
		triangleNormals[2][2] = g_triMesh.m_vnormals[g_triMesh.m_faces[i].z].z;

		triangleVerts[0][0] = g_triMesh.m_vertices[g_triMesh.m_faces[i].x].x;
		triangleVerts[0][1] = g_triMesh.m_vertices[g_triMesh.m_faces[i].x].y;
		triangleVerts[0][2] = g_triMesh.m_vertices[g_triMesh.m_faces[i].x].z;

		triangleVerts[1][0] = g_triMesh.m_vertices[g_triMesh.m_faces[i].y].x;
		triangleVerts[1][1] = g_triMesh.m_vertices[g_triMesh.m_faces[i].y].y;
		triangleVerts[1][2] = g_triMesh.m_vertices[g_triMesh.m_faces[i].y].z;

		triangleVerts[2][0] = g_triMesh.m_vertices[g_triMesh.m_faces[i].z].x;
		triangleVerts[2][1] = g_triMesh.m_vertices[g_triMesh.m_faces[i].z].y;
		triangleVerts[2][2] = g_triMesh.m_vertices[g_triMesh.m_faces[i].z].z;

		g_triangleBatch.AddTriangle( triangleVerts, triangleNormals, triangleTexCoords );
	}
	//g_triangleBatch.CopyColorData4f(tetColours);
	g_triangleBatch.End();

}

///////////////////////////////////////////////////
// Screen changes size or is initialized
void ChangeSize(int nWidth, int nHeight)
{
	vvh = nHeight;
	vvw = nWidth;
}

void KeyboardFunc(unsigned char key, int x, int y)
{
	switch (key){
	case 'a':
		rotation(g_modelViewMatrix,-3.f,0.f,1.f,0.f);
		break;
	case 'd':
		rotation(g_modelViewMatrix,3.f,0.f,1.f,0.f);
		break;
	case 's':
		rotation(g_modelViewMatrix,-3.f,1.f,0.f,0.f);
		break;
	case 'w':
		rotation(g_modelViewMatrix,3.f,1.f,0.f,0.f);
		break;
	case 'q':
		rotation(g_modelViewMatrix,-3.f,0.f,0.f,1.f);
		break;
	case 'e':
		rotation(g_modelViewMatrix,3.f,0.f,0.f,1.f);
		break;
	case 'r':
		g_fScale += 5.f;
		break;
	case 'f':
		g_fScale -= 5.f;
		break;
	case '1':
		g_shaderSelect =1;
		break;
	case '2':
		g_shaderSelect =2;
		break;
	case '3':
		g_shaderSelect =3;
		break;
	case 'i':
		lightY += 5.f;
		break;
	case 'k':
		lightY -= 5.f;
		break;
	case 'j':
		lightX -= 5.f;
		break;
	case 'l':
		lightX += 5.f;
		break;
	case 'u':
		lightZ -= 5.f;
		break;
	case 'o':
		lightZ += 5.f;
		break;
	case 'y':
		if(lightIa < 1.f)
			lightIa += .1;
		break;
	case 'h':
		if(lightIa > 0.f)
		lightIa -= .1;
		break;
	case 't':
		if(lightIp < 1.f)
			lightIp += .1;
		break;
	case 'g':
		if(lightIp > 0.f)
			lightIp -= .1;
		break;
	case 'z':
		g_viewModeSelect = 1;
		break;
	case 'x':
		g_viewModeSelect = 2;
		break;
	}
	glutPostRedisplay();
}

void rotation(GLMatrixStack& matrx, GLfloat angle, GLfloat x, GLfloat y, GLfloat z){
	M3DMatrix44f matrix;
	m3dRotationMatrix44(matrix, angle/180.f*3.1415926535,x,y,z);
	matrx.MultMatrix(matrix);
}


// Called to draw scene
void RenderScene(void)
{
	if(g_viewModeSelect == 1){
		glViewport(0, 0, vvw, vvh);
		float fNear = 1.0f;
		float fFar = 1000.0f;

		g_viewFrustum.SetPerspective(60.0f, ((float)vvw)/(vvh), fNear, fFar);
		g_projMatrix.LoadMatrix(g_viewFrustum.GetProjectionMatrix());
		g_projMatrix.Translate( 0.0f, 0.0f, - 500.f);
		// Set the transformation pipeline to use the two matrix stacks
		g_transformPipeline.SetMatrixStacks(g_modelViewMatrix, g_projMatrix);
	}
	if(g_viewModeSelect == 2){
		g_viewFrustum.SetOrthographic(-346.f, 346.f, -346.f*(float)vvh/vvw, 346.f*(float)vvh/vvw, -500, 500);
		g_orthoProjMatrix.LoadMatrix(g_viewFrustum.GetProjectionMatrix());
		// Set the transformation pipeline to use the two matrix stacks
		g_transformPipeline.SetMatrixStacks(g_modelViewMatrix, g_orthoProjMatrix);
	}

	Vector3f modelCentre;
	modelCentre.x = (g_triMesh.m_bbox.second.x + g_triMesh.m_bbox.first.x) / 2.f;
	modelCentre.y = (g_triMesh.m_bbox.second.y + g_triMesh.m_bbox.first.y) / 2.f;
	modelCentre.z = (g_triMesh.m_bbox.second.z + g_triMesh.m_bbox.first.z) / 2.f;

	Vector3f modelDimension;
	modelDimension.x = g_triMesh.m_bbox.second.x - g_triMesh.m_bbox.first.x;
	modelDimension.y = g_triMesh.m_bbox.second.y - g_triMesh.m_bbox.first.y;
	modelDimension.z = g_triMesh.m_bbox.second.z - g_triMesh.m_bbox.first.z;
	float modelDiag = sqrt(modelDimension.x * modelDimension.x + modelDimension.y * modelDimension.y
		+ modelDimension.z * modelDimension.z);

    // Clear the color and depth buffers
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    
    // Save the current modelview matrix (the identity matrix)
    g_modelViewMatrix.PushMatrix();
		
    g_modelViewMatrix.Translate(g_fXPosition, g_fYPosition, 0.f);
    g_modelViewMatrix.Scale(g_fScale/modelDiag, g_fScale/modelDiag, g_fScale/modelDiag);
	g_modelViewMatrix.Translate(-modelCentre.x, -modelCentre.y, - modelCentre.z);

	GLfloat vEyeLight[] = { lightX, lightY, lightZ };
	GLfloat vAmbientColor[] = { lightIa, lightIa, lightIa, 1.f };
	GLfloat vDiffuseColor[] = { lightIp, lightIp, lightIp, lightIp};
	GLfloat vSpecularColor[] = { lightIp, lightIp, lightIp, lightIp };
	if(g_shaderSelect==1){
		glUseProgram(ADSLightShaderF);
		glUniform4fv(locAmbientF, 1, vAmbientColor);
		glUniform4fv(locDiffuseF, 1, vDiffuseColor);
		glUniform4fv(locSpecularF, 1, vSpecularColor);
		glUniform3fv(locLightF, 1, vEyeLight);
		glUniformMatrix4fv(locMVPF, 1, GL_FALSE, g_transformPipeline.GetModelViewProjectionMatrix());
		glUniformMatrix4fv(locMVF, 1, GL_FALSE, g_transformPipeline.GetModelViewMatrix());
		glUniformMatrix3fv(locNMF, 1, GL_FALSE, g_transformPipeline.GetNormalMatrix());
	}
	if(g_shaderSelect==2){
		glUseProgram(ADSLightShaderG);
		glUniform4fv(locAmbientG, 1, vAmbientColor);
		glUniform4fv(locDiffuseG, 1, vDiffuseColor);
		glUniform4fv(locSpecularG, 1, vSpecularColor);
		glUniform3fv(locLightG, 1, vEyeLight);
		glUniformMatrix4fv(locMVPG, 1, GL_FALSE, g_transformPipeline.GetModelViewProjectionMatrix());
		glUniformMatrix4fv(locMVG, 1, GL_FALSE, g_transformPipeline.GetModelViewMatrix());
		glUniformMatrix3fv(locNMG, 1, GL_FALSE, g_transformPipeline.GetNormalMatrix());
	}
	if(g_shaderSelect==3){
		glUseProgram(ADSLightShaderP);
		glUniform4fv(locAmbientP, 1, vAmbientColor);
		glUniform4fv(locDiffuseP, 1, vDiffuseColor);
		glUniform4fv(locSpecularP, 1, vSpecularColor);
		glUniform3fv(locLightP, 1, vEyeLight);
		glUniformMatrix4fv(locMVPP, 1, GL_FALSE, g_transformPipeline.GetModelViewProjectionMatrix());
		glUniformMatrix4fv(locMVP, 1, GL_FALSE, g_transformPipeline.GetModelViewMatrix());
		glUniformMatrix3fv(locNMP, 1, GL_FALSE, g_transformPipeline.GetNormalMatrix());
	}

	g_triangleBatch.Draw();

    g_modelViewMatrix.PopMatrix();
    
    // Do the buffer Swap
    glutSwapBuffers();
    //glutPostRedisplay();
}

int main(int argc, char* argv[])
{
	gltSetWorkingDirectory(argv[0]);
		
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH);
    glutInitWindowSize(800,600);
  
    glutCreateWindow("HW2");
 
    glutReshapeFunc(ChangeSize);
    glutDisplayFunc(RenderScene);
    glutKeyboardFunc(KeyboardFunc);

    
    GLenum err = glewInit();
    if (GLEW_OK != err) {
        fprintf(stderr, "GLEW Error: %s\n", glewGetErrorString(err));
        return 1;
        }
        

    SetupRC(argv[1]);
    glutMainLoop();    
    return 0;
}
