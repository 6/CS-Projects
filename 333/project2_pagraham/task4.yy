%{
/*
 * CS 333 Task 4 - Peter Graham
 *
 * A simple lexical parser for Clite. The output of your program is a sequence
 * of strings (one per line) specifying the tokens found.
 */
%}

KEYWORD (if|else|while|for|bool|char|int|float)

OPERATOR  [-+*/%]
COMPARISON (==|<|>|<=|>=)
ASSIGNMENT =

BRACKET_OR_PAREN [{}()]
SEMICOLON ;

INTEGER [0-9]+
FLOAT {INTEGER}\.{INTEGER}

/* Valid identifier according to Tucker Noonan textbook p 61 */
IDENTIFIER [a-zA-Z][a-zA-Z0-9]+

%%

{BRACKET_OR_PAREN} {
  if(strcmp(yytext, "{") == 0)
    puts("Open-bracket");
  else if(strcmp(yytext, "}") == 0)
    puts("Close-bracket");
  else if(strcmp(yytext, "(") == 0)
    puts("Open-paren");
  else
    puts("Close-paren");
}

{SEMICOLON} puts("Semicolon");

{OPERATOR} printf("Operator-%s\n", yytext);
{COMPARISON} puts("Comparison");
{ASSIGNMENT} puts("Assignment");

{KEYWORD} printf("Keyword-%s\n", yytext);

{FLOAT} printf("Float-%s\n", yytext);
{INTEGER} printf("Integer-%s\n", yytext);

{IDENTIFIER} printf("Identifier-%s\n", yytext);
.|\n

%%

int main(int argc, char** argv) {
  if(argc <= 1) {
    puts("Please specify the filename of the Clite file.");
    return(1);
  }

  yyin = fopen(argv[1], "r");
  yylex();
}
