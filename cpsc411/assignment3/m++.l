%{
/** Tom Crowfoot
* 10037477
* CPSC 411 Assignment 3
*/
#include "y.tab.h"
#include <string.h>
#include <stdlib.h>
int charCount = 0;
%}

%x COMMENT
%option stack
%option yylineno

%%

<COMMENT>"%".* charCount += yyleng; /* single line comment has higher priority than multi */
<COMMENT>"/*" {yy_push_state(COMMENT); charCount += yyleng;}/* nested multi-line comment */
<COMMENT>"*/" {yy_pop_state(); charCount += yyleng;} /* base of stack always contains init state */
<COMMENT>"\n" charCount = 1;
<COMMENT>. charCount += yyleng;

[ \t]+ charCount += yyleng;
"\n" charCount = 1;
"%".* charCount += yyleng;
"/*" {yy_push_state(COMMENT); charCount += yyleng;}
"+" {charCount += yyleng; return ADD;}
"-" {charCount += yyleng; return SUB;}
"*" {charCount += yyleng; return MUL;}
"/" {charCount += yyleng; return DIV;}
"&&" {charCount += yyleng; return AND;}
"||" {charCount += yyleng; return OR;}
not {charCount += yyleng; return NOT;}
"=" {charCount += yyleng; return EQUAL;}
"<" {charCount += yyleng; return LT;}
">" {charCount += yyleng; return GT;}
"=<" {charCount += yyleng; return LE;}
">=" {charCount += yyleng; return GE;}
":=" {charCount += yyleng; return ASSIGN;}
"(" {charCount += yyleng; return LPAR;}
")" {charCount += yyleng; return RPAR;}
"{" {charCount += yyleng; return CLPAR;}
"}" {charCount += yyleng; return CRPAR;}
":" {charCount += yyleng; return COLON;}
";" {charCount += yyleng; return SEMICOLON;}
"," {charCount += yyleng; return COMMA;}
if {charCount += yyleng; return IF;}
then {charCount += yyleng; return THEN;}
while {charCount += yyleng; return WHILE;}
do {charCount += yyleng; return DO;}
read {charCount += yyleng; return READ;}
else {charCount += yyleng; return ELSE;}
begin {charCount += yyleng; return BEGN;}
end {charCount += yyleng; return END;}
print {charCount += yyleng; return PRINT;}
int {charCount += yyleng; return INT;}
bool {charCount += yyleng; return BOOL;}
var {charCount += yyleng; return VAR;}
fun {charCount += yyleng; return FUN;}
return {charCount += yyleng; return RETURN;}
false {charCount += yyleng; yylval.str = strdup(yytext); return BVAL;}
true {charCount += yyleng; yylval.str = strdup(yytext); return BVAL;}
[a-zA-Z][_0-9a-zA-Z]* {charCount += yyleng; yylval.str = strdup(yytext); return ID;}
[0-9]+ {charCount += yyleng; yylval.str = strdup(yytext); return NUM;}
"*/" {charCount += yyleng; yylval.str = strdup(yytext); return yytext[0];} /* too many comments */
. {charCount += yyleng; yylval.str = strdup(yytext); return yytext[0];} /* char not a defined token */

%%

int yywrap(void)
{
return 1;
}
