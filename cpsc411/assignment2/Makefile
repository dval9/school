all: flex rdp

flex:
	flex parser.l
	cc -o lexer lex.yy.c
	rm lex.yy.c

rdp:	
	cc -o rdp rdp.c
clean:
	rm lexer rdp
