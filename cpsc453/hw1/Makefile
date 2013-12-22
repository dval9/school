MAIN = RobotArm
SHAREDPATH = ../GLTools/src/
SHAREDINCPATH = ../GLTools/include/
LIBDIRS = -L/usr/X11R6/lib -L/usr/X11R6/lib64 -L/usr/local/lib
INCDIRS = -I/usr/include -I/usr/local/include -I/usr/include/GL -I$(SHAREDINCPATH)  -I$(SHAREDINCPATH)GL

CC = g++
CFLAGS = $(COMPILERFLAGS) -g $(INCDIRS)
LIBS = -lX11 -lglut -lGL -lGLU -lm

all:
	$(CC) $(CFLAGS) -o $(MAIN) $(LIBDIRS) $(MAIN).cpp $(SHAREDPATH)glew.c $(SHAREDPATH)GLTools.cpp $(SHAREDPATH)GLBatch.cpp $(SHAREDPATH)GLTriangleBatch.cpp $(SHAREDPATH)GLShaderManager.cpp $(SHAREDPATH)math3d.cpp $(LIBS)
clean:
	rm -f *.o
