#include <stdio.h>

/**
 * Prompts for a digit until 0 is entered.
 */
int main(int argc, char** argv)
{
	int val = -1;
	
	while (val != 0)
	{
		printf("Enter a number 0..9: ");
		int count = scanf( "%d", &val );
		
		if (count == 0)
		{
			scanf("%*s");
			fprintf(stderr, "Error: value is not a number\n");
			continue;
		}
		
		if (val < 0 || val > 9)
		{
			fprintf(stderr, 
				"Error: number %d is out of range (0..9)\n", val);
			continue;
		}
		
		printf("\taccepted: %d\n", val);
	}
	
	printf("Terminating\n");
}