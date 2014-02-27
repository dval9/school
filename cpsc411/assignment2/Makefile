all: 
	@flex parser.l
	@cc -o lexer lex.yy.c
	@rm lex.yy.c
	@cc -o rdp rdp.c
clean:
	@rm lexer rdp
