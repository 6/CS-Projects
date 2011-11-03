/*
CS333 Task 4 - Sarah Harmon and Peter Graham

Try to write a program whose semantics changes depending upon the 
particular machine you use.

To run: in terminal
	1. cd to directory
	2. gcc -o myexec main.c
	3. ./myexec
	
*/

#include <stdio.h>

int main (int argc, char *argv[])
{
	/* In this example, we attempt to capitalize the letters of the alphabet. */
	
	int ii = 0;
	char* alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	printf("%s\n", alphabet);
	
	for (ii=0; ii<25; ii++)
	{
		//this generates a bus error on my machine
		alphabet[ii] = toupper(alphabet[ii]);	
	}
	
	printf("%s\n", alphabet);
	
	/* In this example, we attempt to divide by zero */
	printf("Let's try another example.");	//not printed	
	int x = 0;
	
	//this generates a floating point exception on my machine
	// --> nothing gets printed out (above or below)
	int zero = (x*(x-1)) / x;					
	printf("The result is %d\n", zero);			
	
	printf("Did anything happen?");			//not printed
	
	return 0;
}

