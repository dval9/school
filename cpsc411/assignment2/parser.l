%{
int charCount = 1;
int success = 0;
FILE *outfile;
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
if {fprintf(outfile, "[IF,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;} /* all the valid tokens for M- */
then {fprintf(outfile, "[THEN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
while {fprintf(outfile, "[WHILE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
do {fprintf(outfile, "[DO,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
input {fprintf(outfile, "[INPUT,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
else {fprintf(outfile, "[ELSE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
begin {fprintf(outfile, "[BEGIN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
end {fprintf(outfile, "[END,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
write {fprintf(outfile, "[WRITE,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
[a-zA-Z][0-9a-zA-Z]* {fprintf(outfile, "[ID,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
[0-9]+[a-zA-Z]+ {fprintf(outfile, "\nlex: invalid \"%s\" found %d:%d\n", yytext, yylineno-1, charCount-1);charCount += yyleng;}
[0-9]+ {fprintf(outfile, "[NUM,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"+" {fprintf(outfile, "[ADD,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
":=" {fprintf(outfile, "[ASSIGN,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"-" {fprintf(outfile, "[SUB,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"*" {fprintf(outfile, "[MUL,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"/" {fprintf(outfile, "[DIV,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
"(" {fprintf(outfile, "[LPAR,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
")" {fprintf(outfile, "[RPAR,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
";" {fprintf(outfile, "[SEMICOLON,%s,%d,%d] ", yytext, yylineno-1, charCount-1);charCount += yyleng;}
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
outfile = fopen("temp_file_for_parsing", "w+");
fprintf(outfile, "[PROG,$,%d,%d] ", -1, 0); /* add end of file character for parising */
yylex();
fprintf(outfile, "[EOFCHAR,$,%d,%d] ", yylineno-1, charCount-1); /* add end of file character for parising */
fclose(outfile);
//printf("\n"); /* make sure prompt starts on new line after program is done */
return success;
}

int yywrap(void)
{
return 1;
}
