// #include "StdAfx.h"
#include "TriangleMesh.h"

#include <fstream>
#include <iostream>
#include <math.h>
#include <stdlib.h>
#include <string.h>

CTriangleMesh::CTriangleMesh(void)
{
}

CTriangleMesh::~CTriangleMesh(void)
{
}

bool CTriangleMesh::LoadObj(const char* filename)
{
	using std::fstream;
	using std::ios;
	
	fstream file;
	file.open(filename, ios::in | ios::binary);
	if (!file.is_open()) {
		std::cout << "error open file " << filename << std::endl;
	}

	m_bbox.first.x = 
	m_bbox.first.y = 
	m_bbox.first.z = (float)10e10;

	m_bbox.second.x = 
	m_bbox.second.y = 
	m_bbox.second.z = (float)-10e10;


	while (!file.eof()) {
		char lineBuffer[1024];
		char seps[] = " ";
		char *token;
		//int i;
		Vector3f vertex;
		Vector3i face;

		file.getline(lineBuffer, sizeof(lineBuffer));
		switch (lineBuffer[0]) {
			case 'v':
				lineBuffer[0] = ' ';
				token = strtok(lineBuffer, seps);
				vertex.x = (float) atof(token);
				token = strtok(NULL, seps);
				vertex.y = (float) atof(token);
				token = strtok(NULL, seps);
				vertex.z = (float) atof(token);

				m_vertices.push_back(vertex);

				m_bbox.first.x = vertex.x < m_bbox.first.x ? vertex.x : m_bbox.first.x;
				m_bbox.first.y = vertex.y < m_bbox.first.y ? vertex.y : m_bbox.first.y;
				m_bbox.first.z = vertex.z < m_bbox.first.z ? vertex.z : m_bbox.first.z;

				m_bbox.second.x = vertex.x > m_bbox.second.x ? vertex.x : m_bbox.second.x;
				m_bbox.second.y = vertex.y > m_bbox.second.y ? vertex.y : m_bbox.second.y;
				m_bbox.second.z = vertex.z > m_bbox.second.z ? vertex.z : m_bbox.second.z;

				break;
			case 'f':
				lineBuffer[0] = ' ';
				token = strtok(lineBuffer, seps);
				face.x = atoi(token) - 1;
				token = strtok(NULL, seps);
				face.y = atoi(token) - 1;
				token = strtok(NULL, seps);
				face.z = atoi(token) - 1;

				m_faces.push_back(face);

				break;
		}
	}

	file.close();

	std::cout << "Obj file loaded with " << m_vertices.size() << "vertices and " << m_faces.size() << "faces" << std::endl;
	std::cout << "bounding-box: min(" << m_bbox.first.x << ", " << m_bbox.first.y << ", " << m_bbox.first.z << ") ";
	std::cout << "max(" << m_bbox.second.x << ", " << m_bbox.second.y << ", " << m_bbox.second.z << ")" << std::endl;
	return true;
}

void CTriangleMesh::ComputeNormals(void){
	Vector3f ab,ac,fn,vn;
	float length;
	for(int i=0; i<(int)m_vertices.size(); i++){
		vn.x = 0;
		vn.y = 0;
		vn.z = 0;
		m_vnormals.push_back(vn);
	}
	for(int i=0; i<(int)m_faces.size(); i++){
		ab.x = m_vertices[m_faces[i].x].x - m_vertices[m_faces[i].y].x;
		ab.y = m_vertices[m_faces[i].x].y - m_vertices[m_faces[i].y].y;
		ab.z = m_vertices[m_faces[i].x].z - m_vertices[m_faces[i].y].z;
		ac.x = m_vertices[m_faces[i].x].x - m_vertices[m_faces[i].z].x;
		ac.y = m_vertices[m_faces[i].x].y - m_vertices[m_faces[i].z].y;
		ac.z = m_vertices[m_faces[i].x].z - m_vertices[m_faces[i].z].z;
		fn.x = ab.y*ac.z-ab.z*ac.y;
		fn.y = ab.z*ac.x-ab.x*ac.z;
		fn.z = ab.x*ac.y-ab.y*ac.x;
		length = sqrt(fn.x*fn.x+fn.y*fn.y+fn.z*fn.z);
		fn.x /= length;
		fn.y /= length;
		fn.z /= length;
		m_fnormals.push_back(fn);
		m_vnormals[m_faces[i].x].x += fn.x;
		m_vnormals[m_faces[i].x].y += fn.y;
		m_vnormals[m_faces[i].x].z += fn.z;
		m_vnormals[m_faces[i].y].x += fn.x;
		m_vnormals[m_faces[i].y].y += fn.y;
		m_vnormals[m_faces[i].y].z += fn.z;
		m_vnormals[m_faces[i].z].x += fn.x;
		m_vnormals[m_faces[i].z].y += fn.y;
		m_vnormals[m_faces[i].z].z += fn.z;
	}
	for(int i=0; i<(int)m_vertices.size(); i++){
		vn.x = m_vnormals[i].x;
		vn.y = m_vnormals[i].y;
		vn.z = m_vnormals[i].z;
		length = sqrt(vn.x*vn.x+vn.y*vn.y+vn.z*vn.z);
		vn.x /= length;
		vn.y /= length;
		vn.z /= length;
		m_vnormals[i] = vn;
	}
}

