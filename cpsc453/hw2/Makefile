MAIN = ObjViewer
MAIN2 = TriangleMesh
SHAREDPATH = ../GLTools/src/
SHAREDINCPATH = ../GLTools/include/
LIBDIRS = -L/usr/X11R6/lib -L/usr/X11R6/lib64 -L/usr/local/lib -L./MyGLTools/lib
INCDIRS = -I./MyGLTools/include -I./MyGLTools/include/GL -I/usr/include -I/usr/include/c++/4.4.4 -I/usr/include/c++/4.4.4/backward -I/usr/include/c++/4.4.4/x86_64-redhat-linux -I/usr/lib/gcc/x86_64-redhat-linux/4.4.4/include -I/usr/local/include

CC = g++
CFLAGS = $(COMPILERFLAGS) -g $(INCDIRS)
LIBS = -lX11 -lglut -lGL -lGLU -lMyGLTools

all:
	g++ -c TriangleMesh.cpp
	$(CC) $(CFLAGS) -o $(MAIN) $(LIBDIRS) $(MAIN).cpp TriangleMesh.o $(SHAREDPATH)glew.c $(SHAREDPATH)GLTools.cpp $(SHAREDPATH)GLBatch.cpp $(SHAREDPATH)GLTriangleBatch.cpp $(SHAREDPATH)GLShaderManager.cpp $(SHAREDPATH)math3d.cpp $(LIBS)
clean:
	rm -f *.o
