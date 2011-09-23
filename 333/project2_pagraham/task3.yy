%{
/*
 * CS 333 Task 3 - Peter Graham
 *
 * Strips an HTML file of all tags, comments, and extra whitespace. It leaves a
 * blank line wherever there used to be a <p> tag, and it leaves spaces between
 * words intact.
 */
%}

WHITESPACE [ \t\n\r]

TAG_OR_COMMENT <[^>]+>

WHITESPACE_TO_STRIP {TAG_OR_COMMENT}{WHITESPACE}+|{WHITESPACE}+{TAG_OR_COMMENT}

/* 
 * This will catch edge-cases such as:
 * <p />
 * <p class="a" id="b">
 */
P_TAG <[pP]({WHITESPACE}[^>]*)?\/?>

HTML_ENTITY &[a-zA-Z]+;

%%

{P_TAG} {
  puts("");
}

{TAG_OR_COMMENT} // print nothing
{WHITESPACE_TO_STRIP} // print nothing

{HTML_ENTITY} {
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

%%

int main(int argc, char** argv) {
  if(argc <= 1) {
    puts("Please specify the filename of the HTML file.");
    return(1);
  }

  yyin = fopen(argv[1], "r");
  yylex();
}
