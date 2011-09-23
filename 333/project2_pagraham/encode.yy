%{
/*
 * CS 333 Task 1 - Peter Graham
 *
 * Takes any character in a-z or A-Z and shifts it 13 spaces forward in the
 * alphabet, with wrap-around from z back to a.
 */

// a-z start and end values
int lower_start = 97;
int lower_end = 122;

// A-Z start and end values
int upper_start = 65;
int upper_end = 90;
%}

CHARACTER [a-zA-Z]

%%

{CHARACTER} {
  int value = (int) yytext[0];

  int start,end;
  if(value >= 97) {
    start = lower_start;
    end = lower_end;
  }
  else {
    start = upper_start;
    end = upper_end;
  }

  int new_value = value + 13;
  if(new_value > end) {
    new_value = start + (new_value - end) - 1;
  }

  char wrapped = new_value;
  printf("char %c\n", wrapped);
}
.|\n

%%

int main(int argc, char** argv) {
  yyin = stdin;

  // run lexical analysis
  yylex();
}
