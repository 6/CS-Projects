%{
/*
 * CS 333 Task 1 - Peter Graham
 *
 * Takes any character in a-z or A-Z and shifts it 13 spaces forward in the
 * alphabet, with wrap-around from z back to a.
 */
%}

CHARACTER [a-zA-Z]

%%

{CHARACTER} {
  printf("char %s\n", yytext);
}
.|\n

%%

int main(int argc, char** argv) {
  yyin = stdin;

  // run lexical analysis
  yylex();
}
