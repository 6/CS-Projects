/**
 * Examples of scoping in C.
 *
 * A binding is an association between an identifier and an 
 * entity (e.g. a variable or function). The scope of a 
 * binding is the collection of program statements that can 
 * access that particular binding.
 */

#include <stdio.h>
void function2();

int a = 0;
int b = 1;
int c = 2;

void function1(int a)
{
	int b = 10;
	printf("function1():\n");
	printf("\ta => %d\n\tb => %d\n\tc => %d\n",
		a, b, c);
	function2();
}

void function2()
{
	static int a = 20;
	a++;
	printf("function2():\n");
	printf("\ta => %d\n\tb => %d\n\tc => %d\n", 
		a, b, c);
}

int main(int argc, char** argv)
{
	short a = 30;
	b = 31;
	int d = 33;
	
	printf("main() start:\n");
	printf("\ta => %d\n\tb => %d\n\tc => %d\n\td => %d\n",
		a, b, c, d);
	
	function1(34);
	function2();
	
	{ // block1
		long a = 40;
		b = 41;
		int c = 42;
		
		printf("main() block1:\n");
		printf("\ta => %d\n\tb => %d\n\tc => %d\n\td => %d\n", 
			a, b, c, d);
		
		for (c = 0; c < 1; c++)
		{// block2
			int a = 50;
			int d = 53;
			
			printf("main() block2:\n");
			printf("\ta => %d\n\tb => %d\n\tc => %d\n\td => %d\n",
				a, b, c, d);
			
			function1(54);
			function2();
		}
	}
	
	printf("main() end:\n");
	printf("\ta => %d\n\tb => %d\n\tc => %d\n\td => %d\n",
		a, b, c, d);
}