%{
int charCount = 1;
%}
%x COMMENT
%option stack
%option yylineno
%%
<COMMENT>"%".* charCount += yyleng; /* single line comment has higher priority than multi */
<COMMENT>"/*" {yy_push_state(COMMENT);charCount += yyleng;} /* nested multi-line comment */
<COMMENT>"*/" {yy_pop_state();charCount += yyleng;} /* base of stack always contains init state */
<COMMENT>"\n" charCount=1;
<COMMENT>. charCount += yyleng;
[ \t]+ charCount += yyleng;
"%".* charCount += yyleng;
"/*" {yy_push_state(COMMENT);charCount += yyleng;}
if {printf("IF ");charCount += yyleng;} /* all the valid tokens for M- */
then {printf("THEN ");charCount += yyleng;}
while {printf("WHILE ");charCount += yyleng;}
do {printf("DO ");charCount += yyleng;}
input {printf("INPUT ");charCount += yyleng;}
else {printf("ELSE ");charCount += yyleng;}
begin {printf("BEGIN ");charCount += yyleng;}
end {printf("END ");charCount += yyleng;}
write {printf("WRITE ");charCount += yyleng;}
[a-zA-Z][0-9a-zA-Z]* {printf("ID ");charCount += yyleng;}
[0-9]+ {printf("NUM ");charCount += yyleng;}
"+" {printf("ADD ");charCount += yyleng;}
":=" {printf("ASSIGN ");charCount += yyleng;}
"-" {printf("SUB ");charCount += yyleng;}
"*" {printf("MUL ");charCount += yyleng;}
"/" {printf("DIV ");charCount += yyleng;}
"(" {printf("LPAR ");charCount += yyleng;}
")" {printf("RPAR ");charCount += yyleng;}
";" {printf("SEMICOLON ");charCount += yyleng;}
"\n" charCount=1;
"*/" {printf("\n***extra \"%s\" found %d:%d***\n", yytext, yylineno, charCount);charCount += yyleng;} /* too many comments */
. {printf("\n***invalid character \"%s\" %d:%d***\n", yytext, yylineno, charCount);charCount += yyleng;} /* char not a defined token */
%%

int main(int argc, char **argv)
{
/* open file, redirect from stdin */
if(argc != 2){
printf("no file name given\n");
exit(1);
}
yyin = fopen(argv[1], "r");
if(!yyin){
printf("error opening file\n");
exit(2);
}
yylex();
printf("\n"); /* make sure prompt starts on new line after program is done */
return 0;
}

int yywrap(void)
{
return 1;
}
