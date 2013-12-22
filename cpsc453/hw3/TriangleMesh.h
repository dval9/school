#pragma once

#include <vector>
#include <utility>

struct Vector3f{
	float x;
	float y;
	float z;
};

struct Vector3i{
	int x;
	int y;
	int z;
};

class CTriangleMesh
{
public:
	std::vector<Vector3f> m_vertices;
	std::vector<Vector3f> m_textures;
	std::vector<Vector3f> m_tvertices;
	std::vector<Vector3i> m_faces;
	std::vector<Vector3f> m_fnormals;
	std::vector<Vector3f> m_vnormals;

	std::pair<Vector3f, Vector3f> m_bbox;

	CTriangleMesh(void);
	~CTriangleMesh(void);

	bool LoadObj(const char* filename);
	void ComputeNormals(void);
};
