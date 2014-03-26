all: clean y.tab.c lex.yy.c
	cc -o ccm y.tab.c lex.yy.c
y.tab.c: 
	bison -y -d m++.y
lex.yy.c: y.tab.h
	flex m++.l
y.tab.h: 
	bison -y -d m++.y
clean:
	rm -rf ccm y.tab.h y.tab.c lex.yy.c
