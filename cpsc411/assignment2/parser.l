%{
int charCount = 1;
int success = 0;
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
if {printf("[IF,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;} /* all the valid tokens for M- */
then {printf("[THEN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
while {printf("[WHILE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
do {printf("[DO,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
input {printf("[INPUT,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
else {printf("[ELSE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
begin {printf("[BEGIN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
end {printf("[END,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
write {printf("[WRITE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
[a-zA-Z][0-9a-zA-Z]* {printf("[ID,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
[0-9]+ {printf("[NUM,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"+" {printf("[ADD,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
":=" {printf("[ASSIGN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"-" {printf("[SUB,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"*" {printf("[MUL,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"/" {printf("[DIV,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"(" {printf("[LPAR,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
")" {printf("[RPAR,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
";" {printf("[SEMICOLON,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"\n" charCount=1;
"*/" {printf("\nlex: extra \"%s\" found %d:%d\n", yytext, yylineno-1, charCount-1);charCount += yyleng;success = 1;} /* too many comments */
. {printf("\nlex: invalid character \"%s\" %d:%d\n", yytext, yylineno-1, charCount-1);charCount += yyleng;success = 1;} /* char not a defined token */

%%

int main(int argc, char **argv)
{
if(argc != 2){ /* open file, redirect from stdin */
printf("lex: no file name given\n");
exit(1);
}
yyin = fopen(argv[1], "r");
if(!yyin){
printf("lex: error opening file\n");
exit(1);
}
printf("[PROG,$,%d,%d] ", -1, 0); /* add end of file character for parising */
yylex();
printf("[EOFCHAR,$,%d,%d] ", yylineno-1, charCount-1); /* add end of file character for parising */
printf("\n"); /* make sure prompt starts on new line after program is done */
return success;
}

int yywrap(void)
{
return 1;
}
