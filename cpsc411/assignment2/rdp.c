/**
 * Tom Crowfoot
 * 10037477
 * CPSC 411 Assignment 2
 * Recursive descent parser for M-
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define GET_TOKEN token = strtok_r(NULL, " \n", &tokptr1)
#define GET_TYPE token_type = strtok_r(token, "[,]", &tokptr2)
#define GET_EXACT token_exact = strtok_r(NULL, "[,]", &tokptr2)
#define GET_LINE line = strtok_r(NULL, "[,]", &tokptr2)
#define GET_POS pos = strtok_r(NULL, "[,]", &tokptr2)
#define GET_ALL GET_TOKEN;GET_TYPE;GET_EXACT;GET_LINE;GET_POS

#define IF "IF"
#define THEN "THEN"
#define ELSE "ELSE"
#define WHILE "WHILE"
#define DO "DO"
#define INPUT "INPUT"
#define ID "ID"
#define ASSIGN "ASSIGN"
#define WRITE "WRITE"
#define BEGIN "BEGIN"
#define END "END"
#define SEMICOLON "SEMICOLON"
#define ADD "ADD"
#define SUB "SUB"
#define MUL "MUL"
#define DIV "DIV"
#define LPAR "LPAR"
#define RPAR "RPAR"
#define NUM "NUM"
#define EOFCHAR "EOFCHAR"

struct node{
  char *value;
  struct node *left;
  struct node *right;
};

void stmt();
void stmtlist();
void term();
void termP();
void expr();
void exprP();
void factor();

char *input, *token, *token_type, *token_exact, *line, *pos, *tokptr1, *tokptr2;

/**
 * stmt -> IF expr THEN stmt ELSE stmt
 *        | WHILE expr DO stmt
 *        | INPUT ID
 *        | ID ASSIGN expr
 *        | WRITE expr
 *        | BEGIN stmtlist END. 
 *
 * Nullable = no.
 * First = {IF, WHILE, INPUT, ID, WRITE, BEGIN}.
 * Follow = {ELSE, SEMICOLON}.
 * Endable = yes.
 */
void stmt()
{
  if(!strncmp(IF,token_type,2)){
    GET_ALL;
    expr();
    if(!strncmp(THEN,token_type,4)){
      GET_ALL;
      stmt();
      if(!strncmp(ELSE,token_type,4)){
	GET_ALL;
	stmt();
      }else{printf("error: expected 'ELSE' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'THEN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WHILE,token_type,5)){
    GET_ALL;
    expr();
    if(!strncmp(DO,token_type,2)){
      GET_ALL;
      stmt();
    }else{printf("error: expected 'DO' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(INPUT,token_type,5)){
    GET_ALL;
    if(!strncmp(ID,token_type,2)){
      GET_ALL;
    }else{printf("error: expected 'ID' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    GET_ALL;
    if(!strncmp(ASSIGN,token_type,6)){
      GET_ALL;
      expr();
    }else{printf("error: expected 'ASSIGN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WRITE,token_type,5)){
    GET_ALL;
    expr();
  }else if(!strncmp(BEGIN,token_type,5)){
    GET_ALL;
    stmtlist();
    if(!strncmp(END,token_type,3)){
      GET_ALL;
    }else{printf("error: expected 'END' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'stmt' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * stmtlist -> IF expr THEN stmt ELSE stmt SEMICOLON stmtlist
 *           | WHILE expr DO stmt SEMICOLON stmtlist
 *           | INPUT ID SEMICOLON stmtlist
 *           | ID ASSIGN expr SEMICOLON stmtlist
 *           | WRITE expr SEMICOLON stmtlist
 *           | BEGIN stmtlist END SEMICOLON stmtlist
 *           | . 
 *
 * Nullable = yes.
 * First = {IF, WHILE, INPUT, ID, WRITE, BEGIN}.
 * Follow = {END}.
 * Endable = no.
 */
void stmtlist()
{
  if(!strncmp(IF,token_type,2)){
    GET_ALL;
    expr();
    if(!strncmp(THEN,token_type,2)){
      GET_ALL;
      stmt();
      if(!strncmp(ELSE,token_type,4)){
	GET_ALL;
	stmt();
	if(!strncmp(SEMICOLON,token_type,9)){
	  GET_ALL;
	  stmtlist();
	}else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
      }else{printf("error: expected 'ELSE' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'THEN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WHILE,token_type,5)){
    GET_ALL;
    expr();
    if(!strncmp(DO,token_type,2)){
      GET_ALL;
      stmt();
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	stmtlist();
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'DO' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(INPUT,token_type,5)){
    GET_ALL;
    if(!strncmp(ID,token_type,2)){
      GET_ALL;
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	stmtlist();
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'ID' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    GET_ALL;
    if(!strncmp(ASSIGN,token_type,6)){
      GET_ALL;
      expr();
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	stmtlist();
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'ASSIGN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WRITE,token_type,5)){
    GET_ALL;
    expr();
    if(!strncmp(SEMICOLON,token_type,9)){
      GET_ALL;
      stmtlist();
    }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(BEGIN,token_type,5)){
    GET_ALL;
    stmtlist();
    if(!strncmp(END,token_type,3)){
      GET_ALL;
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	stmtlist();
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'END' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(END,token_type,3)){
    ;
  }else{printf("error: invalid expression for 'stmtlist' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * expr -> LPAR expr RPAR termP exprP
 *       | ID termP exprP
 *       | NUM termP exprP
 *       | SUB NUM termP exprP. 
 *
 * Nullable = no.
 * First = {LPAR, ID, NUM, SUB}.
 * Follow = {THEN, DO, SEMICOLON, RPAR, ELSE}.
 * Endable = yes.
 */
void expr()
{
  if(!strncmp(LPAR,token_type,4)){
    GET_ALL;
    expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
      termP();
      exprP();
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    GET_ALL;
    termP();
    exprP();
  }else if(!strncmp(NUM,token_type,3)){
    GET_ALL;
    termP();
    exprP();
  }else if(!strncmp(SUB,token_type,3)){
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      GET_ALL;
      termP();
      exprP();
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'expr' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * exprP -> ADD term exprP
 *        | SUB term exprP
 *        | . 
 *
 * Nullable = yes.
 * First = {ADD, SUB}.
 * Follow = {THEN, DO, SEMICOLON, RPAR, ELSE}.
 * Endable = yes.
 */
void exprP()
{
  if(!strncmp(ADD,token_type,3)){
    GET_ALL;
    term();
    exprP();
  }else if(!strncmp(SUB,token_type,3)){
    GET_ALL;
    term();
    exprP();
  }else if((!strncmp(THEN,token_type,4)) || (!strncmp(DO,token_type,2)) || (!strncmp(SEMICOLON,token_type,9)) || (!strncmp(RPAR,token_type,4)) || (!strncmp(ELSE,token_type,4))){
    ;
  }else{printf("error: invalid expression for 'exprP' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * term -> LPAR expr RPAR termP
 *       | ID termP
 *       | NUM termP
 *       | SUB NUM termP.  
 *
 * Nullable = no.
 * First = {LPAR, ID, NUM, SUB}.
 * Follow = {ADD, SUB, THEN, DO, SEMICOLON, RPAR, ELSE}.
 * Endable = yes.
 */
void term()
{
  if(!strncmp(LPAR,token_type,4)){
    GET_ALL;
    expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
      termP();
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    GET_ALL;
    termP();
  }else if(!strncmp(NUM,token_type,3)){
    GET_ALL;
    termP();
  }else if(!strncmp(SUB,token_type,3)){
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      GET_ALL;
      termP();
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'term' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * termP -> MUL factor termP
 *        | DIV factor termP
 *        | . 
 *
 * Nullable = yes.
 * First = {MUL, DIV}.
 * Follow = {ADD, SUB, THEN, DO, SEMICOLON, RPAR, ELSE}.
 * Endable = yes.
 */
void termP()
{
  if(!strncmp(MUL,token_type,3)){
    GET_ALL;
    factor();
    termP();
  }else if(!strncmp(DIV,token_type,3)){
    GET_ALL;
    factor();
    termP();
  }else if((!strncmp(ADD,token_type,3)) || (!strncmp(SUB,token_type,3)) || (!strncmp(THEN,token_type,4)) || (!strncmp(DO,token_type,2)) || (!strncmp(SEMICOLON,token_type,9)) || (!strncmp(RPAR,token_type,4)) || (!strncmp(ELSE,token_type,4))){
    ;
  }else{printf("error: invalid expression for 'termP' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 *  factor -> LPAR expr RPAR
 *         | ID
 *         | NUM
 *         | SUB NUM. 
 *
 * Nullable = no.
 * First = {LPAR, ID, NUM, SUB}.
 * Follow = {MUL, DIV, ADD, SUB, THEN, DO, SEMICOLON, RPAR, ELSE}.
 * Endable = yes.
 */
void factor()
{
  if(!strncmp(LPAR,token_type,4)){
    GET_ALL;
    expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    GET_ALL;
  }else if(!strncmp(NUM,token_type,3)){
    GET_ALL;
  }else if(!strncmp(SUB,token_type,3)){
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      GET_ALL;
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'factor' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  return;
}

/**
 * main -> stmt. 
 *
 * Nullable = no.
 * First = {IF, WHILE, INPUT, ID, WRITE, BEGIN}.
 * Follow = {}.
 * Endable = yes.
 */
int main(int argc, char** argv)
{
  input = malloc(10000000*sizeof(char));
  fgets(input, 10000000, stdin);
  token = strtok_r(input, " \n", &tokptr1);
  GET_ALL;
  stmt();
  if(!strncmp(EOFCHAR,token_type,7)){
    printf("looks good!\n");
  }else{printf("error: invalid expression for 'main' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  free(input);
  exit(EXIT_SUCCESS);
}
