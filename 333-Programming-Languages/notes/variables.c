#include <stdio.h>
#include <stdlib.h>

/*
 * A simple C program that demonstrates the basic use of
 variables, pointers, and dynamic memory allocation.
 */
int main(int argc, char** argv)
{
	// the printf function is declared in stdio.h
	// printf provides formatted text output
	printf("Hello, World!\n");
	
	// C is statically typed--types are specified at
	// variable declaration
	int a = 4;
	int b = 6;
	printf("%d + %d = %d\n", a, b, (a+b));
		
	// a pointer is a reference to a memory location
	// the data stored at that memory location can be 
	// explicitly manipulated
	int* ptrA = &a;
	*ptrA = 36;
	printf("%d + %d = %d\n", a, b, (a+b));
	
	// malloc is used to dynamically allocate blocks
	// of memory; be sure to free memory later
	int count = 10;
	char* buffer = malloc(sizeof(char) * count);
	int i;
	for (i = 0; i < count; i++)
	{
		*(buffer + i) = 'a' + i;
		// buffer[i] = ... would also work
	}
	
	// C strings are 0-terminated char arrays
	printf("buffer: %s\n", buffer);
	free(buffer);
}
