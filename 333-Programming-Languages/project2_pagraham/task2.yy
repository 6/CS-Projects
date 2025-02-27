%{
/*
 * CS 333 Task 2 - Peter Graham
 *
 * Reads in a text file and tells you the number of rows and characters and how
 * many of each vowel [a, e, i, o, u] are in the file.
 */

/*
 * Initialize row and character counts.
 * Note: rows will always be one more than the number of \n, so start count at 1
 */
int rows = 1;
int characters = 0;

// assign counts of each vowel to 0
int a = 0, e = 0, i = 0, o = 0, u = 0;
%}

VOWEL [aeiouAEIOU]

%%

{VOWEL} {
  char lower_vowel = tolower(yytext[0]);
  switch(lower_vowel) {
    case 'a': a++; break;
    case 'e': e++; break;
    case 'i': i++; break;
    case 'o': o++; break;
    case 'u': u++; break;
  }
  characters++;
}
\n rows++; characters++;
. characters++;

%%

int main(int argc, char** argv) {
  if(argc <= 1) {
    puts("Please specify the filename of the text file.");
    return(1);
  }

  yyin = fopen(argv[1], "r");
  yylex();

  printf("Rows: %d\nCharacters (including \\n): %d\n", rows, characters);
  puts("\nVowel counts:");
  printf("a:%d, e:%d, i:%d, o:%d, u:%d\n", a, e, i, o, u);

	int vowels = a+e+i+o+u;
	printf("\nTotal vowels: %d\n", vowels);
	printf("Average characters per row: %f\n", (float)characters/(float)rows);
	printf("Average vowels per row: %f\n", (float)vowels/(float)rows);
}
