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

  /* a node for ast */
  typedef struct node{
    char *val;
    struct node *one, *two, *three, *four;
  } node;

  /* the symbol table value struct */
  typedef struct symtabval{
    char *name;
    char *dec_type; /* var or fun */
    int offset; /* args start at -4, -5,... local start 1,2... */
    char *type; /* for var */
    char *fun_label; /* for fun */
    int args; /* for fun, just type */
    char *arg_types[10];//list arg types for fun
    char *ret_type; /* for fun */
  } symtabval;

  /* symbol table level */
  typedef struct symtab{
    symtabval *hashlist[211];
    int locals;
    int args;
    char *name;
    char *ret;
  } symtab;

  symtabval *lookup_fun(char *name, symtab *scope, int scopelevel);  
  int check_more_args(node *node, symtab *table, int scopelevel, int num);
  char *check_type(node *node1, node *node2, symtab *table, int scopelevel);
  void check_declared(node *node, symtab *table, int scopelevel);
  void insert_var(char *name, int offset, char *type, symtab *scope, int scopelevel);
  void insert_fun(char *name, char *label, char *ret, symtab *scope, int scopelevel);
  symtabval *lookup(char *name, symtab *scope, int scopelevel);
  int get_hash(char *key);
  void count_args(node *node, symtab *table, int scopelevel, int num);
  void pretty_print(node *root, int depth, int child);
  node *make_node0(char *val);
  node *make_node1(char *val, node *one);
  node *make_node2(char *val, node *one, node *two);
  node *make_node3(char *val, node *one, node *two, node *three);
  node *make_node4(char *val, node *one, node *two, node *three, node *four);
  void yyerror(char *s);
  void make_symtab(node *node, symtab *table, int scopelevel);
  int do_check_args(node *node, symtab *table, int scopelevel, int num);

  node *ast;

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
prog : block {ast = $1;};

// declarations on the left, body on the right
block : declarations program_body {$$ = make_node2("BLOCK",$1,$2);};

// each declaration onto the left, continue to next on the right
declarations : declaration SEMICOLON declarations {$$ = make_node2("DECL", $1, $3);}
| {$$ = NULL;};

declaration : var_declaration {$$ = $1;}
| fun_declaration {$$ = $1;};

var_declaration : VAR basic_var_declaration {$$ = $2;};

// left is the name, right is the type
basic_var_declaration : identifier COLON type {$$ = make_node2("VAR", $1, $3);};

// child one is the name, child two is the parameters, child three is the return type, child four is the body of the function
fun_declaration : FUN identifier param_list COLON type CLPAR fun_block CRPAR {$$ = make_node4("FUN", $2, $3, $5, $7);};

// left is declarations, right is body, similar to block
fun_block : declarations fun_body {$$ = make_node2("FUNBLOCK", $1, $2);};

param_list : LPAR parameters RPAR {$$ = $2;};

parameters : parameters1 {$$ = $1;}
| {$$ = NULL;};

// the parameters, left most is first parameter
parameters1 : parameters1 COMMA basic_var_declaration {$$ = make_node2("PARAM", $1, $3);}
| basic_var_declaration {$$ = $1;};

// make a node with the id name
identifier : ID {$$ = make_node0($1);};

// make a node with the type
type : INT {$$ = make_node0("INT");}
| BOOL {$$ = make_node0("BOOL");};

// body of program
program_body : BEGN prog_stmts END {$$ = make_node1("PROGBODY", $2);};

// body of function, similar to program_body except child two is the return
fun_body : BEGN prog_stmts RETURN expr SEMICOLON END {$$ = make_node2("FUNBODY", $2, $4);};

// list the statements in the program, similar to declarations
prog_stmts : prog_stmt SEMICOLON prog_stmts {$$ = make_node2("STMT", $1, $3);}
| {$$ = NULL;};

// if child one is conditional, second is then, third is else
prog_stmt : IF expr THEN prog_stmt ELSE prog_stmt {$$ = make_node3("IF", $2, make_node1("STMT", $4), make_node1("STMT", $6));}
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
arguments1 : arguments1 COMMA expr {$$ = make_node2("ARGS", $1, $3);}
| expr {$$ = $1;};

%%

int main(int argc, char **argv)
{
  symtab table[20]; /* hopefully never more than this levels of scope */
  int scopelevel;
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
  scopelevel = 0;// symtab startes empty
  table[scopelevel].name = strdup("main_prog");
  pretty_print(ast, 0,0);// print the ast
  make_symtab(ast, table, scopelevel);// do symantic check and make IR
  pretty_print(ast, 0,0);// print the IR
  return 0;
}

void yyerror(char *s)
{
  printf("error: %s at %d:%d on token %s\n", s, yylineno, charCount, yytext);
  exit(EXIT_FAILURE);
}

/* go through the ast looking for declarations */
/* also makes the IR at same time*/
void make_symtab(node *node, symtab *table, int scopelevel)
{
  char *labeloffset;
  int i;
  symtabval *temp;
  /* add to symtab */
  if(strcmp(node->val, "DECL") == 0){
    if(strcmp(node->one->val, "VAR") == 0){ /* is var, easy */
      table[scopelevel].locals++;
      if(lookup(node->one->one->val, table, scopelevel) == NULL){
	insert_var(node->one->one->val, table[scopelevel].locals, node->one->two->val, table, scopelevel);
      }
      else{
	printf("var %s already declared in function %s\n", node->one->one->val, table[scopelevel].name);
	exit(1);
      }
    }
    else if(strcmp(node->one->val, "FUN") == 0){ /* is fun, go to next scope when adding this */
      if(lookup(node->one->one->val, table, scopelevel) == NULL){
	labeloffset = malloc(100);
	/* generate a unique name for function offset */
	sprintf(labeloffset, "FOFF_%s_%d", node->one->one->val, scopelevel);
	table[scopelevel+1].args = 0; /* set these to 0 again, incase they werent from a previous fun */
	table[scopelevel+1].locals = 0;
	table[scopelevel+1].name = strdup(node->one->one->val);
	table[scopelevel+1].ret = strdup(node->one->three->val);
	insert_fun(node->one->one->val, labeloffset, node->one->three->val, table, scopelevel);
	for(i=0; i<211; i++) /* also remove anything that might be hashed from previous scope */
	  table[scopelevel+1].hashlist[i] = NULL;
	if(node->one->two != NULL)
	  count_args(node->one->two, table, scopelevel+1, 0);
	/* args were already inserted in next scope level by count_args */	
	node->one->one->val = strdup(labeloffset);
	free(labeloffset);
	if(node->one->four != NULL)
	  make_symtab(node->one->four, table, scopelevel+1); /* next scope */
      }
      else{
	printf("fun %s already declared in function %s\n", node->one->one->val, table[scopelevel].name);
	exit(1);
      }
    }
    /* try next decl */
    if(node->two != NULL)
      make_symtab(node->two, table, scopelevel);
  }
  else if(strcmp(node->val, "STMT") == 0){// node is a stmt, check semantics on the statement 	  
    if(strcmp(node->one->val, "READ") == 0){// check read makes sense
      temp = lookup(node->one->one->val, table, scopelevel);
      if(temp == NULL){
	printf("%s undeclared in function %s\n", node->one->one->val, table[scopelevel].name);
	exit(1);
      }
      labeloffset = malloc(100);// change arg name to offset for IR
      sprintf(labeloffset, "OFF_%d_%d", temp->offset, scopelevel);
      node->one->one->val = strdup(labeloffset);
      free(labeloffset);
    }
    else if(strcmp(node->one->val, "IF") == 0){
      check_type(node->one->one->one, node->one->one->two, table, scopelevel);
      if(node->one->two != NULL)
	make_symtab(node->one->two, table, scopelevel);
      if(node->one->three != NULL)
	make_symtab(node->one->three, table, scopelevel);
    }
    else if(strcmp(node->one->val, "WHILE") == 0){ 
      check_type(node->one->one->one, node->one->one->two, table, scopelevel);
      if(node->one->two != NULL)
	make_symtab(node->one->two, table, scopelevel);
    }
    else if(strcmp(node->one->val, "ASSIGN") == 0){// check that the assign makes sense and change for IR
      temp = lookup(node->one->one->val, table, scopelevel);
      if(temp == NULL || strcmp(temp->dec_type, "FUN")==0){
	printf("%s undeclared in function %s\n", node->one->one->val, table[scopelevel].name);
	exit(1);
      }
      check_type(node->one->one, node->one->two, table, scopelevel);// make sure that left and right of assign same type
      labeloffset = malloc(100);
      sprintf(labeloffset, "OFF_%d_%d", temp->offset, scopelevel);
      node->one->one->val = strdup(labeloffset);
      free(labeloffset);
    }
    else if(strcmp(node->one->val, "PRINT") == 0){// check if the print is good, change for IR
      if(strcmp(node->one->one->val, "ADD") == 0 || 
	 strcmp(node->one->one->val, "SUB") == 0 ||
	 strcmp(node->one->one->val, "MUL") == 0 ||
	 strcmp(node->one->one->val, "DIV") == 0){
	check_type(node->one->one->one, node->one->one->two, table, scopelevel);
      }
      else if(strcmp(node->one->one->val, "false") == 0 || strcmp(node->one->one->val, "false") == 0){
      }
      else{
	temp = lookup(node->one->one->val, table, scopelevel);
	if(temp == NULL){
	  printf("%s undeclared in function %s\n", node->one->one->val, table[scopelevel].name);
	  exit(1);
	}
	if(strcmp(temp->dec_type, "FUN") == 0){// print function handled different
	  labeloffset = malloc(100);
	  sprintf(labeloffset, "%s", temp->fun_label);
	  node->one->one->val = strdup(labeloffset);
	  free(labeloffset);
	  do_check_args(node->one->one, table, scopelevel, 0);// check the args of the function
	}
	else{// just a var
	  labeloffset = malloc(100);
	  sprintf(labeloffset, "OFF_%d_%d", temp->offset, scopelevel);
	  node->one->one->val = strdup(labeloffset);
	  free(labeloffset);
	}
      }
    }
    if(node->two != NULL)
      make_symtab(node->two, table, scopelevel);
  }
  else if(strcmp(node->val, "FUNBODY") == 0){ // check return type is correct
    if(node->one != NULL){
      make_symtab(node->one, table, scopelevel);
    }
    if(strcmp(node->two->val, "NEG") == 0){
      if(isalpha(node->two->one->val[0])){// return value is a declaration
	temp = lookup(node->two->one->val, table, scopelevel);
	if(temp == NULL){
	  printf("return value %s undeclared in function %s\n", node->two->one->val, table[scopelevel].name);
	  exit(1);
	}
	if(strcmp(temp->ret_type, table[scopelevel].ret)){
	  printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	  exit(1);
	}
      }
      else if(strcmp(node->two->one->val, "false") == 0 || strcmp(node->two->one->val, "true") == 0){ // cant have negative bool
	printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	exit(1);
      }
      else{
	if(strcmp("INT", table[scopelevel].ret)){// its an int
	  printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	  exit(1);
	}
      }
    }
    else if(strcmp(node->two->val, "LT") == 0 || // could be either both ints or both bools
	    strcmp(node->two->val, "GT") == 0 ||
	    strcmp(node->two->val, "EQUAL") == 0 ||
	    strcmp(node->two->val, "LE") == 0 ||
	    strcmp(node->two->val, "GE") == 0 ||  
	    strcmp(node->two->val, "AND") == 0 ||
	    strcmp(node->two->val, "OR") == 0){
      if(check_type(node->two->one, node->two->two, table, scopelevel) == NULL){//NULL means wrong types
	printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	exit(1);
      }
    }
    else if(strcmp(node->two->val, "ADD") == 0 || // both should be ints, not bools
	    strcmp(node->two->val, "SUB") == 0 ||
	    strcmp(node->two->val, "MUL") == 0 ||
	    strcmp(node->two->val, "DIV") == 0){// this should be an INT type returned from check_type
      if(strcmp(check_type(node->two->one, node->two->two, table, scopelevel), table[scopelevel].ret)){// its an bool
	printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	exit(1);
      }
    }
    else if(strcmp(node->two->val, "false") == 0 || strcmp(node->two->val, "true") == 0){
      if(strcmp("BOOL", table[scopelevel].ret)){// its an bool
	printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	exit(1);
      }
    }
    else if(isalpha(node->two->val[0])){// return value is a declaration
      temp = lookup(node->two->val, table, scopelevel);
      if(temp == NULL){
	printf("return value %s undeclared in function %s\n", node->two->val, table[scopelevel].name);
	exit(1);
      }
      if(strcmp(temp->dec_type, "FUN")==0){
	if(strcmp(temp->ret_type, table[scopelevel].ret)){
	  printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	  exit(1);
	}
	labeloffset = malloc(100);
	sprintf(labeloffset, "%s", temp->fun_label);
	node->two->val = strdup(labeloffset);
	free(labeloffset);
	do_check_args(node->two, table, scopelevel, 0);// check the args of the function
      }
      else{
	if(strcmp(temp->type, table[scopelevel].ret)){
	  printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	  exit(1);
	}
	labeloffset = malloc(100);
	sprintf(labeloffset, "OFF_%d_%d", temp->offset, scopelevel);
	node->two->val = strdup(labeloffset);
	free(labeloffset);	
      }
    }
    else{
      if(strcmp("INT", table[scopelevel].ret)){// its an int
	printf("incorrect return type in function %s, expected %s\n", table[scopelevel].name, table[scopelevel].ret);
	exit(1);
      }
    } 
  }
  else{
    /* that wasnt a thing, look for more */
    /* try each branch */
    if(node->one != NULL){
      check_declared(node->one, table, scopelevel);
      make_symtab(node->one, table, scopelevel);
    }
    if(node->two != NULL){
      check_declared(node->two, table, scopelevel);
      make_symtab(node->two, table, scopelevel);
    }
    if(node->three != NULL){
      check_declared(node->three, table, scopelevel);
      make_symtab(node->three, table, scopelevel);
    }
    if(node->four != NULL){
      check_declared(node->four, table, scopelevel);
      make_symtab(node->four, table, scopelevel);    
    }
  }
}

/* check args of the function and make IR */
int do_check_args(node *node, symtab *table, int scopelevel, int num)
{
  char label[100];
  symtabval *temp;
  int count = 0;
  if(strcmp(node->one->val, "ARGS") == 0){
    check_more_args(node->one->one, table, scopelevel, num+1);
    check_more_args(node->one->two, table, scopelevel, num+2);
    count +=2;
  }
  else if(strcmp(node->one->val, "ADD") == 0 ||// need to figure out subtypes
	  strcmp(node->one->val, "SUB") == 0 ||
	  strcmp(node->one->val, "MUL") == 0 ||
	  strcmp(node->one->val, "DIV") == 0 ||
	  strcmp(node->one->val, "LT") == 0 ||
	  strcmp(node->one->val, "GT") == 0 ||
	  strcmp(node->one->val, "EQUAL") == 0 ||
	  strcmp(node->one->val, "LE") == 0 ||
	  strcmp(node->one->val, "GE") == 0 ||  
	  strcmp(node->one->val, "AND") == 0 ||
	  strcmp(node->one->val, "OR") == 0){
    check_type(node->one->one, node->one->two, table, scopelevel);
  }
  else{
    temp = lookup(node->one->val, table, scopelevel);
    if(temp == NULL){
      printf("%s undeclared in function %s\n", node->one->val, table[scopelevel].name);
      exit(1);
    }
    sprintf(label, "OFF_%d_%d", temp->offset, scopelevel);
    node->one->val = strdup(label);
    count++;
  }
  return count;
}

/* helper for do_check_args */
int check_more_args(node *node, symtab *table, int scopelevel, int num)
{
  char label[100];
  symtabval *temp;
  int count = 0;
  if(strcmp(node->val, "ARGS") == 0){
    check_more_args(node->one, table, scopelevel, num+1);
    check_more_args(node->two, table, scopelevel, num+2);
    count +=2;
  }
  else if(strcmp(node->val, "ADD") == 0 ||// need to figure out subtypes
	  strcmp(node->val, "SUB") == 0 ||
	  strcmp(node->val, "MUL") == 0 ||
	  strcmp(node->val, "DIV") == 0 ||
	  strcmp(node->val, "LT") == 0 ||
	  strcmp(node->val, "GT") == 0 ||
	  strcmp(node->val, "EQUAL") == 0 ||
	  strcmp(node->val, "LE") == 0 ||
	  strcmp(node->val, "GE") == 0 ||  
	  strcmp(node->val, "AND") == 0 ||
	  strcmp(node->val, "OR") == 0){
    check_type(node->one, node->two, table, scopelevel);
  }
  else{
    temp = lookup(node->val, table, scopelevel);
    if(temp == NULL){
      printf("%s undeclared in function %s\n", node->val, table[scopelevel].name);
      exit(1);
    }
    sprintf(label, "OFF_%d_%d", temp->offset, scopelevel);
    node->val = strdup(label);
    count ++;
  }
  return count;
}

/* check both types of nodes are same */
char *check_type(node *node1, node *node2, symtab *table, int scopelevel)
{
  char *type1, *type2;
  char label1[100], label2[100];
  int rename1 = 0, rename2 = 0;
  symtabval *temp1, *temp2;
  if(strcmp(node1->val, "ADD") == 0 ||// need to figure out subtypes
     strcmp(node1->val, "SUB") == 0 ||
     strcmp(node1->val, "MUL") == 0 ||
     strcmp(node1->val, "DIV") == 0 ||
     strcmp(node1->val, "LT") == 0 ||
     strcmp(node1->val, "GT") == 0 ||
     strcmp(node1->val, "EQUAL") == 0 ||
     strcmp(node1->val, "LE") == 0 ||
     strcmp(node1->val, "GE") == 0 ||  
     strcmp(node1->val, "AND") == 0 ||
     strcmp(node1->val, "OR") == 0){
    type1 = check_type(node1->one, node1->two, table, scopelevel);
  }
  else if(strcmp(node1->val, "false") == 0 || strcmp(node1->val, "true") == 0){
    type1 = strdup("BOOL");// its a boolean
  }
  else if(isdigit(node1->val[0])){
    type1 = strdup("INT"); // int
  }
  else{// a variable, look it up
    temp1 = lookup(node1->val, table, scopelevel);
    if(temp1 == NULL){
      printf("undeclared variable %s in function %s\n", node1->val, table[scopelevel].name);
      exit(EXIT_FAILURE);
    }
    if(strcmp(temp1->dec_type, "FUN") == 0){// function, get label instead of offset
      type1 = strdup(temp1->ret_type);
      sprintf(label1, "%s", temp1->fun_label);
      do_check_args(node1, table, scopelevel, 0);// check the args of the function
    }
    else{// get offset
      type1 = strdup(temp1->type);
      sprintf(label1, "OFF_%d_%d", temp1->offset, scopelevel);
    }
    rename1 = 1;
  }
  if(strcmp(node2->val, "ADD") == 0 ||// for the second node, same as first
     strcmp(node2->val, "SUB") == 0 ||
     strcmp(node2->val, "MUL") == 0 ||
     strcmp(node2->val, "DIV") == 0 ||
     strcmp(node2->val, "LT") == 0 ||
     strcmp(node2->val, "GT") == 0 ||
     strcmp(node2->val, "EQUAL") == 0 ||
     strcmp(node2->val, "LE") == 0 ||
     strcmp(node2->val, "GE") == 0 ||  
     strcmp(node2->val, "AND") == 0 ||
     strcmp(node2->val, "OR") == 0){
    type2 = check_type(node2->one, node2->two, table, scopelevel);
  }     
  else if(strcmp(node2->val, "false") == 0 || strcmp(node2->val, "true") == 0){
    type2 = strdup("BOOL");
  }
  else if(isdigit(node2->val[0])){
    type2 = strdup("INT");
  }
  else{
    temp2 = lookup(node2->val, table, scopelevel);
    if(temp2 == NULL){
      printf("undeclared variable %s in function %s\n", node2->val, table[scopelevel].name);
      exit(EXIT_FAILURE);
    }
    if(strcmp(temp2->dec_type, "FUN") == 0){
      type2 = strdup(temp2->ret_type);
      sprintf(label2, "%s", temp2->fun_label);
      do_check_args(node2, table, scopelevel, 0);// check the args of the function
    }
    else{
      type2 = strdup(temp2->type);
      sprintf(label2, "OFF_%d_%d", temp2->offset, scopelevel);
    }
    rename2 = 1;
  }// if either are null, bad and shouldnt happen
  if(type1 == NULL){
    printf("%s type not found in function %s\n", node1->val, table[scopelevel].name);
    exit(1);
  }
  if(type2 == NULL){
    printf("%s type not found in function %s\n", node2->val, table[scopelevel].name);
    exit(1);
  }// different types
  if(strcmp(type1, type2)){
    printf("%s and %s are different type in function %s\n", node1->val, node2->val, table[scopelevel].name);
    exit(1);
  }
  if(rename1)// only change if we need
    node1->val = strdup(label1);
  if(rename2)
    node2->val = strdup(label2);
  return type1;
}

/* check that the vars are declared */
void check_declared(node *node, symtab *table, int scopelevel)
{
  int i;
  /* check that its not a keyword */
  if(!(strcmp(node->val, "BLOCK") == 0 || 
       strcmp(node->val, "DECL") == 0 || 
       strcmp(node->val, "VAR") == 0 || 
       strcmp(node->val, "FUN") == 0 || 
       strcmp(node->val, "FUNBLOCK") == 0 || 
       strcmp(node->val, "PARAM") == 0 || 
       strcmp(node->val, "INT") == 0 || 
       strcmp(node->val, "BOOL") == 0 || 
       strcmp(node->val, "PROGBODY") == 0 || 
       strcmp(node->val, "FUNBODY") == 0 || 
       strcmp(node->val, "STMT") == 0 || 
       strcmp(node->val, "IF") == 0 || 
       strcmp(node->val, "WHILE") == 0 ||
       strcmp(node->val, "READ") == 0 || 
       strcmp(node->val, "ASSIGN") == 0 || 
       strcmp(node->val, "PRINT") == 0 || 
       strcmp(node->val, "OR") == 0 || 
       strcmp(node->val, "AND") == 0 || 
       strcmp(node->val, "NOT") == 0 || 
       strcmp(node->val, "EQUAL") == 0 || 
       strcmp(node->val, "LT") == 0 || 
       strcmp(node->val, "GT") == 0 || 
       strcmp(node->val, "LE") == 0 || 
       strcmp(node->val, "GE") == 0 || 
       strcmp(node->val, "ADD") == 0 || 
       strcmp(node->val, "SUB") == 0 || 
       strcmp(node->val, "MUL") == 0 || 
       strcmp(node->val, "DIV") == 0 || 
       strcmp(node->val, "NEG") == 0 || 
       strcmp(node->val, "ARGS") == 0 ||
       strcmp(node->val, "false") == 0 || 
       strcmp(node->val, "true") == 0)){
    if(isalpha(node->val[0])){ /* if the first char is a letter, its a variable */
      if(lookup(node->val, table, i) != NULL){
	printf("undeclared variable %s in function %s\n", node->val, table[scopelevel].name);
	exit(EXIT_FAILURE);
      }
    }
  }
}

/* count the args, and put them in the symbol table when declaring*/
void count_args(node *node, symtab *table, int scopelevel, int num)
{
  symtabval *temp;
  symtabval *fun;
  fun = lookup(table[scopelevel].name, table, scopelevel-1);
  /* add the argument to the scope, increase arg counter */
  if(strcmp(node->val, "VAR") == 0){      
    temp = lookup_fun(node->one->val, table, scopelevel);
    if(temp == NULL){
      insert_var(node->one->val, -4 - table[scopelevel].args, node->two->val, table, scopelevel); /* first arg will be -4, second -5, ... */
      table[scopelevel].args++;
      fun->arg_types[num] = strdup(node->two->val);
    }
    else{
      printf("arg %s already declared in function %s\n", node->one->val, table[scopelevel].name);
      exit(1);
    }   
  }
  /* get more args */
  else if(strcmp(node->val, "PARAM") == 0){
    if(node->one != NULL)
      count_args(node->one, table, scopelevel, num+1);
    if(node->two != NULL)
      count_args(node->two, table, scopelevel, num+2);
  }
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

/* hashing function for symbol table, adds the values of each character of name */
/* if more than 211 decls per scope, this will break! */
int get_hash(char *key)
{
  int val = 0, i = 0;
  while(key[i] != 0){
    val = ((val << 4) + key[i]) % 211;
    i++;
  }
  return val;
}

/* insert a record into symboltable */
void insert_var(char *name, int offset, char *type, symtab *scope, int scopelevel)
{
  int hashval;
  symtabval *new;
  hashval = get_hash(name);
  new = malloc(sizeof(symtabval));
  new->name = strdup(name);
  new->offset = offset;
  new->type = strdup(type);
  new->dec_type = strdup("VAR");
  while(scope[scopelevel].hashlist[hashval] != NULL)
    hashval++;
  scope[scopelevel].hashlist[hashval] = new;
}

/* insert a function, args are handled by count_args */
void insert_fun(char *name, char *label, char *ret, symtab *scope, int scopelevel)
{
  int hashval;
  symtabval *new;
  hashval = get_hash(name);
  new = malloc(sizeof(symtabval));
  new->name = strdup(name);
  new->fun_label = strdup(label);
  new->dec_type = strdup("FUN");
  new->ret_type = strdup(ret);
  while(scope[scopelevel].hashlist[hashval] != NULL)
    hashval++;
  scope[scopelevel].hashlist[hashval] = new;
}

/* lookup for a declaration, return null if not found, return the entry if found */
symtabval *lookup(char *name, symtab *scope, int scopelevel)
{
  int hashval, i;
  for(i = scopelevel; i >=0; i--){
    hashval = get_hash(name);
    while((scope[i].hashlist[hashval] != NULL) && (strcmp(scope[i].hashlist[hashval]->name, name) != 0))
      hashval++;
    if((scope[i].hashlist[hashval] != NULL) && (strcmp(scope[i].hashlist[hashval]->name, name) == 0))
      break;
  }
  if(i<0)
    return NULL;
  return scope[i].hashlist[hashval];
}

/* lookup for a declaration at current scope only, return null if not found, return the entry if found */
symtabval *lookup_fun(char *name, symtab *scope, int scopelevel)
{
  int hashval, i;
  hashval = get_hash(name);
  while((scope[scopelevel].hashlist[hashval] != NULL) && (strcmp(scope[scopelevel].hashlist[hashval]->name, name) != 0))
    hashval++;
  return scope[scopelevel].hashlist[hashval];
}
