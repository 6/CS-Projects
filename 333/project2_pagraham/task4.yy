%{
/*
 * CS 333 Task 4 - Peter Graham
 *
 * A simple lexical parser for Clite. The output of your program is a sequence
 * of strings (one per line) specifying the tokens found.
 */
int inside_comment = 0;
%}

INLINE_COMMENT \/\/.*$
/* Flex doesn't support negative lookahead, so do this the slow way */
MULTILINE_COMMENT_START \/\*
MULTILINE_COMMENT_END \*\/

KEYWORD (if|else|while|for|bool|char|int|float)

OPERATOR  [-+*/%]
COMPARISON (==|<|>|<=|>=)
ASSIGNMENT =

BRACKET_OR_PAREN [{}()]
SEMICOLON ;

INTEGER [0-9]+
FLOAT {INTEGER}\.{INTEGER}

/* Valid identifier according to Tucker Noonan textbook p 61 */
IDENTIFIER [[:alpha:]][[:alnum:]]+

%%

{INLINE_COMMENT} if(inside_comment == 0) printf("Inline-comment-%s\n", yytext);
{MULTILINE_COMMENT_START} {
  inside_comment = 1;
  puts("Open-multiline-comment");
}
{MULTILINE_COMMENT_END} {
  inside_comment = 0;
  puts("Close-multiline-comment");
}

{BRACKET_OR_PAREN} {
  if(inside_comment == 0) {
    if(strcmp(yytext, "{") == 0)
      puts("Open-bracket");
    else if(strcmp(yytext, "}") == 0)
      puts("Close-bracket");
    else if(strcmp(yytext, "(") == 0)
      puts("Open-paren");
    else
      puts("Close-paren");
  }
}

{SEMICOLON} if(inside_comment == 0) puts("Semicolon");

{OPERATOR} if(inside_comment == 0) printf("Operator-%s\n", yytext);
{COMPARISON} if(inside_comment == 0) puts("Comparison");
{ASSIGNMENT} if(inside_comment == 0) puts("Assignment");

{KEYWORD} if(inside_comment == 0) printf("Keyword-%s\n", yytext);

{FLOAT} if(inside_comment == 0) printf("Float-%s\n", yytext);
{INTEGER} if(inside_comment == 0) printf("Integer-%s\n", yytext);

{IDENTIFIER} if(inside_comment == 0) printf("Identifier-%s\n", yytext);
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
