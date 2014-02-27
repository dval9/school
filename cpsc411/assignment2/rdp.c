/**
 * Tom Crowfoot
 * 10037477
 * CPSC 411 Assignment 2
 * Recursive descent parser for M-
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>

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

struct ASTnode{
  char *value;
  struct ASTnode *one;
  struct ASTnode *two;
  struct ASTnode *three;
  struct ASTnode *four;
};
typedef struct ASTnode node;

node *stmt();
node *stmtlist();
node *term();
node *termP(node *more);
node *expr();
node *exprP(node *more);
node *factor();
void print_tree(node *ast);
void eval(node *ast);

char *input, *token, *token_type, *token_exact, *line, *pos, *tokptr1, *tokptr2;
FILE *outstream;
int label;

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
node *stmt()
{
  if(!strncmp(IF,token_type,2)){ // cond,then,else
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    if(!strncmp(THEN,token_type,4)){
      GET_ALL;
      ast->two = stmt();
      if(!strncmp(ELSE,token_type,4)){
	GET_ALL;
	ast->three = stmt();
	return ast;
      }else{printf("error: expected 'ELSE' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'THEN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WHILE,token_type,5)){ // cond,body
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    if(!strncmp(DO,token_type,2)){
      GET_ALL;
      ast->two = stmt();
      return ast;
    }else{printf("error: expected 'DO' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(INPUT,token_type,5)){ // read into id
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    if(!strncmp(ID,token_type,2)){
      node *ast1 = malloc(sizeof(node));
      ast1->value = strdup(token_exact);
      ast->one = ast1;
      GET_ALL;
      return ast;
    }else{printf("error: expected 'ID' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){ // id := expr
    node *ast1 = malloc(sizeof(node));
    ast1->value = strdup(token_exact);
    GET_ALL;
    if(!strncmp(ASSIGN,token_type,6)){
      node *ast = malloc(sizeof(node));
      ast->value = strdup(token_type);
      ast->one = ast1;
      GET_ALL;
      ast->two = expr();
      return ast;
    }else{printf("error: expected 'ASSIGN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WRITE,token_type,5)){ // print id
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    return ast;
  }else if(!strncmp(BEGIN,token_type,5)){ // begin body end
    GET_ALL;
    node *ast = malloc(sizeof(node));
    ast = stmtlist();
    if(!strncmp(END,token_type,3)){
      GET_ALL;
      return ast;
    }else{printf("error: expected 'END' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'stmt' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *stmtlist()
{
  if(!strncmp(IF,token_type,2)){ // cond, then, else, next statement
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    if(!strncmp(THEN,token_type,2)){
      GET_ALL;
      ast->two = stmt();
      if(!strncmp(ELSE,token_type,4)){
	GET_ALL;
	ast->three = stmt();
	if(!strncmp(SEMICOLON,token_type,9)){
	  GET_ALL;
	  ast->four = stmtlist();
	  return ast;
	}else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
      }else{printf("error: expected 'ELSE' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'THEN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WHILE,token_type,5)){ // cond, body, end, next statement
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    if(!strncmp(DO,token_type,2)){
      GET_ALL;
      ast->two = stmt();
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	ast->three = stmtlist();
	return ast;
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'DO' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(INPUT,token_type,5)){ // read into id, next statement
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    if(!strncmp(ID,token_type,2)){
      node *ast1 = malloc(sizeof(node));
      ast1->value = strdup(token_exact);
      ast->one = ast1;
      GET_ALL;
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	ast->two = stmtlist();
	return ast;
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'ID' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){ // id := expr, next statement
    node *ast1 = malloc(sizeof(node));
    ast1->value = strdup(token_exact);
    GET_ALL;
    if(!strncmp(ASSIGN,token_type,6)){
      node *ast = malloc(sizeof(node));
      ast->value = strdup(token_type);
      ast->one = ast1;
      GET_ALL;
      ast->two = expr();
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	ast->three = stmtlist();
	return ast;
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'ASSIGN' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(WRITE,token_type,5)){ // write id, next statement
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_type);
    GET_ALL;
    ast->one = expr();
    if(!strncmp(SEMICOLON,token_type,9)){
      GET_ALL;
      ast->two = stmtlist();
      return ast;
    }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(BEGIN,token_type,5)){ // nested begin, end
    GET_ALL;
    node *ast1 = malloc(sizeof(node));
    ast1 = stmtlist();
    if(!strncmp(END,token_type,3)){
      GET_ALL;
      if(!strncmp(SEMICOLON,token_type,9)){
	GET_ALL;
	node *ast = malloc(sizeof(node));
	ast->one = ast1;
	ast->two = stmtlist();
	return ast;
      }else{printf("error: expected 'SEMICOLON' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
    }else{printf("error: expected 'END' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(END,token_type,3)){
    return NULL;
  }else{printf("error: invalid expression for 'stmtlist' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *expr()
{
  if(!strncmp(LPAR,token_type,4)){ // ignore (), pass on expr
    GET_ALL;
    node *ast = malloc(sizeof(node));
    ast = expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
      node *ast1 = malloc(sizeof(node));
      ast1 = termP(ast);
      node *ast2 = malloc(sizeof(node));
      if(ast1 != NULL)
	ast2 = exprP(ast1);
      else
	ast2 = exprP(ast);
      if(ast2 != NULL)
	return ast2;
      else{
	if(ast1 != NULL)
	  return ast1;
	else
	  return ast;
      }
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){ // get an id, pass on expr
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    node *ast2 = malloc(sizeof(node));
    if(ast1 != NULL)
      ast2 = exprP(ast1);
    else
      ast2 = exprP(ast);
    if(ast2 != NULL)
      return ast2;
    else{
      if(ast1 != NULL)
	return ast1;
      else
	return ast;
    }
  }else if(!strncmp(NUM,token_type,3)){ // get an num, pass on expr
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    node *ast2 = malloc(sizeof(node));
    if(ast1 != NULL)
      ast2 = exprP(ast1);
    else
      ast2 = exprP(ast);
    if(ast2 != NULL)
      return ast2;
    else{
      if(ast1 != NULL)
	return ast1;
      else
	return ast;
    }
  }else if(!strncmp(SUB,token_type,3)){ // get a negative num, pass on expr
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      node *ast = malloc(sizeof(node));
      char *temp = malloc(strlen(token_exact)+2);
      strcpy(temp, "-");
      strcat(temp, token_exact);
      ast->value = strdup(temp);
      free(temp);
      GET_ALL;
      node *ast1 = malloc(sizeof(node));
      ast1 = termP(ast);
      node *ast2 = malloc(sizeof(node));
      if(ast1 != NULL)
	ast2 = exprP(ast1);
      else
	ast2 = exprP(ast);
      if(ast2 != NULL)
	return ast2;
      else{
	if(ast1 != NULL)
	  return ast1;
	else
	  return ast;
      }
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'expr' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *exprP(node *more)
{
  if(!strncmp(ADD,token_type,3)){ // add, keep going
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    ast->one = more;
    ast->two = term();
    node *ast1 = malloc(sizeof(node));
    ast1 = exprP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if(!strncmp(SUB,token_type,3)){ // sub, keep going
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    ast->one = more;
    ast->two = term();
    node *ast1 = malloc(sizeof(node));
    ast1 = exprP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if((!strncmp(THEN,token_type,4)) || (!strncmp(DO,token_type,2)) || (!strncmp(SEMICOLON,token_type,9)) || (!strncmp(RPAR,token_type,4)) || (!strncmp(ELSE,token_type,4))){
    return NULL; // expr done
  }else{printf("error: invalid expression for 'exprP' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *term()
{
  if(!strncmp(LPAR,token_type,4)){ // see expr
    GET_ALL;
    node *ast = malloc(sizeof(node));
    ast = expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
      node *ast1 = malloc(sizeof(node));
      ast1 = termP(ast);
      if(ast1 != NULL)
	return ast1;
      else
	return ast;
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if(!strncmp(NUM,token_type,3)){
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if(!strncmp(SUB,token_type,3)){
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      node *ast = malloc(sizeof(node));
      char *temp = malloc(strlen(token_exact)+2);
      strcpy(temp, "-");
      strcat(temp, token_exact);
      ast->value = strdup(temp);
      free(temp);
      GET_ALL;
      node *ast1 = malloc(sizeof(node));
      ast1 = termP(ast);
      if(ast1 != NULL)
	return ast1;
      else
	return ast;
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'term' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *termP(node *more)
{
  if(!strncmp(MUL,token_type,3)){ // see exprP
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    ast->one = more;
    ast->two = factor();
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if(!strncmp(DIV,token_type,3)){
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    ast->one = more;
    ast->two = factor();
    node *ast1 = malloc(sizeof(node));
    ast1 = termP(ast);
    if(ast1 != NULL)
      return ast1;
    else
      return ast;
  }else if((!strncmp(ADD,token_type,3)) || (!strncmp(SUB,token_type,3)) || (!strncmp(THEN,token_type,4)) || (!strncmp(DO,token_type,2)) || (!strncmp(SEMICOLON,token_type,9)) || (!strncmp(RPAR,token_type,4)) || (!strncmp(ELSE,token_type,4))){
    return NULL;
  }else{printf("error: invalid expression for 'termP' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
node *factor()
{
  if(!strncmp(LPAR,token_type,4)){ // just get another expr, ignore ()
    GET_ALL;
    node *ast = malloc(sizeof(node));
    ast = expr();
    if(!strncmp(RPAR,token_type,4)){
      GET_ALL;
      return ast;
    }else{printf("error: expected 'RPAR' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else if(!strncmp(ID,token_type,2)){ // just return id
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    return ast;
  }else if(!strncmp(NUM,token_type,3)){ // just return num
    node *ast = malloc(sizeof(node));
    ast->value = strdup(token_exact);
    GET_ALL;
    return ast;
  }else if(!strncmp(SUB,token_type,3)){ // just return negative num
    GET_ALL;
    if(!strncmp(NUM,token_type,3)){
      node *ast = malloc(sizeof(node));
      char *temp = malloc(strlen(token_exact)+2);
      strcpy(temp, "-");
      strcat(temp, token_exact);
      ast->value = strdup(temp);
      free(temp);
      GET_ALL;
      return ast;
    }else{printf("error: expected 'NUM' token but found [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  }else{printf("error: invalid expression for 'factor' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
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
  FILE *infile;
  label = 1;
  if(argc == 3){ //output file specified
    infile = fopen(argv[1],"r");
    if(infile == NULL){
      perror("fopen");
      exit(EXIT_FAILURE);
    }
    outstream = fopen(argv[2],"w+");
    if(outstream == NULL){
      perror("fopen");
      exit(EXIT_FAILURE);
    }
  }
  else if(argc == 2){ //no outout file name given
    infile = fopen(argv[1],"r");
    if(infile == NULL){
      perror("fopen");
      exit(EXIT_FAILURE);
    }
    outstream = fopen("output_for_sm","w+");
    if(outstream == NULL){
      perror("fopen");
      exit(EXIT_FAILURE);
    }
  }
  else{
    printf("usage: rdp <infile> <outfile>\n");
    exit(EXIT_FAILURE);
  }
  input = malloc(10000000*sizeof(char));
  fgets(input, 10000000, infile); // read the tokens from lexer, hopefully not longer than this
  token = strtok_r(input, " \n", &tokptr1);
  GET_ALL; // remove the starting token
  node *ast = malloc(sizeof(node));
  ast = stmt();
  if(!strncmp(EOFCHAR,token_type,7)){ // parser was successful, so no errors, generate code
    print_tree(ast);
  }else{printf("error: invalid expression for 'main' token = [%s,%s,%s,%s]\n", token_type, token_exact, line, pos);exit(EXIT_FAILURE);}
  free(input);
  fclose(infile);
  fclose(outstream);
  exit(EXIT_SUCCESS);
}

void eval(node *ast)
{
  long val;
  if(ast->one == NULL && ast->two == NULL){ // either a register name, or value so handle it
    if(!isdigit(ast->value[0]))
      fprintf(outstream, "rPUSH %s\n", ast->value);
    else{
      val = strtol(ast->value, NULL, 10);
      fprintf(outstream, "cPUSH %d\n", val);
    }
  }
  if(ast->one != NULL)
    eval(ast->one);
  if(ast->two != NULL)
    eval(ast->two);
  if(*ast->value == '+')
    fprintf(outstream, "OP2 +\n");
  if(*ast->value == '-')
    fprintf(outstream, "OP2 -\n");
  if(*ast->value == '*')
    fprintf(outstream, "OP2 *\n");
  if(*ast->value == '/')
    fprintf(outstream, "OP2 /\n");
}

void print_tree(node *ast)
{
  if(!strcmp(ast->value, "IF")){ // first child is conditional, second is then, third is else
    eval(ast->one);
    fprintf(outstream, "cJUMP L%d\n", label);
    print_tree(ast->two);
    fprintf(outstream, "JUMP L%d\n", label+1);
    fprintf(outstream, "L%d:\n", label++);
    print_tree(ast->three);
    fprintf(outstream, "L%d:\n", label++);
    if(ast->four != NULL)
      print_tree(ast->four);
  }
  if(!strcmp(ast->value, "WHILE")){ // first child is conditional, second is body
    fprintf(outstream, "L%d:\n", label++);
    eval(ast->one);
    fprintf(outstream, "cJUMP L%d\n", label);
    print_tree(ast->two);
    fprintf(outstream, "JUMP L%d\n", label-1);
    fprintf(outstream, "L%d:\n", label++);
    if(ast->three != NULL)
      print_tree(ast->three);
  }
  if(!strcmp(ast->value, "ASSIGN")){ // first child is register, second is the value
    eval(ast->two);
    fprintf(outstream, "LOAD %s\n", ast->one->value);
    if(ast->three != NULL)
      print_tree(ast->three);
  }
  if(!strcmp(ast->value, "WRITE")){ // first child is register to write
    fprintf(outstream, "rPUSH %s\n", ast->one->value);
    fprintf(outstream, "PRINT\n");
    if(ast->two != NULL)
      print_tree(ast->two);
  }
  if(!strcmp(ast->value, "INPUT")){ // first child is register to save input
    fprintf(outstream, "READ %s\n", ast->one->value);
    if(ast->two != NULL)
      print_tree(ast->two);
  }
  return;
}
