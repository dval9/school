%{
  /** Tom Crowfoot
   * 10037477
   * CPSC 411 Assignment 3
   */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

  extern FILE *yyin;
  extern int yylineno;
  extern int charCount;
  extern char *yytext;

  typedef struct node{
    char *val;
    struct node *one, *two, *three, *four;
  } node;

  void pretty_print(node *root, int depth, int child);
  node *make_node0(char *val);
  node *make_node1(char *val, node *one);
  node *make_node2(char *val, node *one, node *two);
  node *make_node3(char *val, node *one, node *two, node *three);
  node *make_node4(char *val, node *one, node *two, node *three, node *four);
  void yyerror(char *s);

  %}

%union {struct node *node; char *str;}

%start prog

%token ADD SUB MUL DIV
%token AND OR NOT
%token EQUAL LT GT LE GE
%token ASSIGN
%token LPAR RPAR CLPAR CRPAR
%token COLON SEMICOLON COMMA
%token IF THEN WHILE DO READ ELSE BEGN END PRINT INT BOOL VAR FUN RETURN
 // tokens assumed to be strings for ease, can convert later
%token <str> ID NUM BVAL

 // everything is a node in the AST
%type <node> block declarations declaration var_declaration basic_var_declaration fun_declaration
%type <node> fun_block param_list parameters parameters1 identifier type program_body fun_body
%type <node> prog_stmts prog_stmt expr bint_term bint_factor compare_op int_expr addop int_term
%type <node> mulop int_factor argument_list arguments arguments1

%%

 // the return should be the root of the ast
prog : block {pretty_print($1,0,0);};

// declarations on the left, body on the right
block : declarations program_body {$$ = make_node2("block",$1,$2);};

// each declaration onto the left, continue to next on the right
declarations : declaration SEMICOLON declarations {$$ = make_node2("decl", $1, $3);}
| {$$ = NULL;};

declaration : var_declaration {$$ = $1;}
| fun_declaration {$$ = $1;};

var_declaration : VAR basic_var_declaration {$$ = $2;};

// left is the name, right is the type
basic_var_declaration : identifier COLON type {$$ = make_node2("var", $1, $3);};

// child one is the name, child two is the parameters, child three is the return type, child four is the body of the function
fun_declaration : FUN identifier param_list COLON type CLPAR fun_block CRPAR {$$ = make_node4("fun", $2, $3, $5, $7);};

// left is declarations, right is body, similar to block
fun_block : declarations fun_body {$$ = make_node2("funblock", $1, $2);};

param_list : LPAR parameters RPAR {$$ = $2;};

parameters : parameters1 {$$ = $1;}
| {$$ = NULL;};

// the parameters, left most is first parameter
parameters1 : parameters1 COMMA basic_var_declaration {$$ = make_node2("param", $1, $3);}
| basic_var_declaration {$$ = $1;};

// make a node with the id name
identifier : ID {$$ = make_node0($1);};

// make a node with the type
type : INT {$$ = make_node0("INT");}
| BOOL {$$ = make_node0("BOOL");};

// body of program
program_body : BEGN prog_stmts END {$$ = make_node1("progbody", $2);};

// body of function, similar to program_body except child two is the return
fun_body : BEGN prog_stmts RETURN expr SEMICOLON END {$$ = make_node2("funbody", $2, $4);};

// list the statements in the program, similar to declarations
prog_stmts : prog_stmt SEMICOLON prog_stmts {$$ = make_node2("stmt", $1, $3);}
| {$$ = NULL;};

// if child one is conditional, second is then, third is else
prog_stmt : IF expr THEN prog_stmt ELSE prog_stmt {$$ = make_node3("IF", $2, $4, $6);}
// while child one is conditional, second is body
| WHILE expr DO prog_stmt {$$ = make_node2("WHILE", $2, $4);}
// child is the id name
| READ ID {$$ = make_node1("READ", make_node0($2));}
// first child is name, second is value to assign
| ID ASSIGN expr {$$ = make_node2("ASSIGN", make_node0($1), $3);}
// child is which thing to print out
| PRINT expr {$$ = make_node1("PRINT", $2);}
// define a block for multipul statements
| CLPAR block CRPAR {$$ = $2;};

// basic expression tree
expr : expr OR bint_term {$$ = make_node2("OR", $1, $3);}
| bint_term {$$ = $1;};

// basic expression tree
bint_term : bint_term AND bint_factor {$$ = make_node2("AND", $1, $3);}
| bint_factor {$$ = $1;};

// basic expression tree, NOT has only one child, the expr to negate
bint_factor : NOT bint_factor {$$ = make_node1("NOT", $2);}
// free the node created to pass up the string
| int_expr compare_op int_expr {$$ = make_node2($2->val, $1, $3); free($2);}
| int_expr {$$ = $1;};

// pass back up a node to get the string
compare_op : EQUAL {$$ = make_node0("EQUAL");}
| LT {$$ = make_node0("LT");}
| GT {$$ = make_node0("GT");}
| LE {$$ = make_node0("LE");}
| GE {$$ = make_node0("GE");};

// basic expression tree, free the node passed up like in bint_factor
int_expr : int_expr addop int_term {$$ = make_node2($2->val, $1, $3); free($2);}
| int_term {$$ = $1;};

// pass back up a node to get the string
addop : ADD {$$ = make_node0("ADD");}
| SUB {$$ = make_node0("SUB");};

// basic expression tree, free the node passed up like in bint_factor
int_term : int_term mulop int_factor {$$ = make_node2($2->val, $1, $3); free($2);}
| int_factor {$$ = $1;};

// pass back up a node to get the string
mulop : MUL {$$ = make_node0("MUL");}
| DIV {$$ = make_node0("DIV");};

int_factor : LPAR expr RPAR {$$ = $2;}
// list the arguments to a function, one is the function name, two is more args
| ID argument_list {$$ = make_node1($1, $2);}
| NUM {$$ = make_node0($1);}
| BVAL {$$ = make_node0($1);}
// unary minus
| SUB int_factor {$$ = make_node1("NEG", $2);};

argument_list : LPAR arguments RPAR {$$ = $2;}
| {$$ = NULL;};

arguments : arguments1 {$$ = $1;}
| {$$ = NULL;};

// list the arguments to the function, like parameters were
arguments1 : arguments1 COMMA expr {$$ = make_node2("args", $1, $3);}
| expr {$$ = $1;};

%%

int main(int argc, char **argv)
{
  if(argc != 2){
    printf("error: invalid args\nusage: ccm filename");
    exit(1);
  }
  // set the lex file to read 
  yyin = fopen(argv[1], "r");
  if(!yyin){
    printf("error: opening file\nusage: ccm filename");
    exit(1);
  }
  // parse the file!
  yyparse();
  return 0;
}
void yyerror(char *s)
{
  printf("error: %s at %d:%d on token %s\n", s, yylineno, charCount, yytext);
}
// print the tree inorder, i.e. do parent, then one, then two, then three, then four
// the number refers to which child number it is, and the name is the value of the node
void pretty_print(node *node, int depth, int child)
{
  int i;
  for(i=0;i<depth;i++)
    printf("    ");
  printf("%d:%s\n", child, node->val);
  if(node->one != NULL)
    pretty_print(node->one, depth+1, 1);
  if(node->two != NULL)
    pretty_print(node->two, depth+1, 2);
  if(node->three != NULL)
    pretty_print(node->three, depth+1, 3);
  if(node->four != NULL)
    pretty_print(node->four, depth+1, 4);
}
// functions to make nodes, depending on how many children they will have
node *make_node0(char *val)
{
  node *ast = malloc(sizeof(node));
  ast->val = strdup(val);
  return ast;
}
node *make_node1(char *val, node *one)
{
  node *ast = malloc(sizeof(node));
  ast->val = val;
  ast->one = one;
  return ast;
}
node *make_node2(char *val, node *one, node *two)
{
  node *ast = malloc(sizeof(node));
  ast->val = val;
  ast->one = one;
  ast->two = two;
  ast->three = NULL;
  return ast;
}
node *make_node3(char *val, node *one, node *two, node *three)
{
  node *ast = malloc(sizeof(node));
  ast->val = val;
  ast->one = one;
  ast->two = two;
  ast->three = three;
  return ast;
}
node *make_node4(char *val, node *one, node *two, node *three, node *four)
{
  node *ast = malloc(sizeof(node));
  ast->val = val;
  ast->one = one;
  ast->two = two;
  ast->three = three;
  ast->four = four;
  return ast;
}
