%{
/*
 * Sample flex file.
 */
 
double sum = 0;
%}

DIGIT	[0-9]
INTEGER	{DIGIT}+
PRICE	{INTEGER}*\.{DIGIT}{DIGIT}


%%

{PRICE}			{
	double price = atof(yytext);
	printf("Price: %f\n", price);
	sum += price;
}
{INTEGER}		// printf("Integer: %s\n", yytext);
.|\n

%%

int main(int argc, char** argv) 
{
	// check for input file on command line.
	if( argc > 1 ) 
		yyin = fopen( argv[1], "r" ); 
	else 
		yyin = stdin; 
	
	// run lexical analysis
	yylex();
	
	printf("Total price: %f\n", sum);
} 