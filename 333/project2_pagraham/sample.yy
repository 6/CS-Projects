%{
/* 
 * Sample flex file
 */

double sum = 0;
%}

DIGIT [0-9]
INTEGER {DIGIT}+
PRICE {INTEGER}*\.{DIGIT}{DIGIT}

%%

exp1 |
exp2 printf("execute this macro for multiple Regexps");

{PRICE} {
  float price = atof(yytext);
  printf("Price: %s\n", price);
  sum += price;
}
{INTEGER} printf("Integer: %s\n", yytext);
.|\n

%%

int main(int argc, char** argv) {
  // check for input filename
  if(argc > 1)
    yyin = fopen(argv[1], "r");
  else
    yyin = stdin;

  // run lexical analysis
  yylex();

  printf("Total price: %f\n", sum);
}
