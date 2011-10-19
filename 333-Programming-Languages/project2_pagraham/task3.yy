%{
/*
 * CS 333 Task 3 - Peter Graham
 *
 * Strips an HTML file of all tags, comments, and extra whitespace. It leaves a
 * blank line wherever there used to be a <p> tag, and it leaves spaces between
 * words intact.
 */
// keep track of if we're inside an HTML comment or not
int inside_comment = 0;
%}

WHITESPACE [ \t\n\r]

COMMENT_START <!--
COMMENT_END -->
TAG <[^>]+>

/* 
 * This will catch edge-cases such as:
 * <p />
 * <P class="a" id="b">
 */
P_TAG <[pP]({WHITESPACE}[^>]*)?\/?>

WHITESPACE_TO_STRIP {WHITESPACE}+

HTML_ENTITY &[a-zA-Z]+;

%%

{COMMENT_START} inside_comment = 1;
{COMMENT_END} inside_comment = 0;

{P_TAG} if(inside_comment == 0) puts("\n");

{TAG} // print nothing
{WHITESPACE_TO_STRIP} printf(" "); // replace multiple whitespace with one space

{HTML_ENTITY} {
  if(inside_comment == 0) {
    if(strcmp(yytext, "&lt;") == 0)
      printf("<");
    else if(strcmp(yytext, "&gt;") == 0)
      printf(">");
    else if(strcmp(yytext, "&copy;") == 0)
      printf("Copyright");
    else if(strcmp(yytext, "&ndash;") == 0)
      printf("-");
    else if(strcmp(yytext, "&mdash;") == 0)
      printf("--");
    else if(strcmp(yytext, "&amp;") == 0)
      printf("&");
    else
      printf("%s", yytext);
  }
}

%%

int main(int argc, char** argv) {
  if(argc <= 1) {
    puts("Please specify the filename of the HTML file.");
    return(1);
  }

  yyin = fopen(argv[1], "r");
  yylex();
}
