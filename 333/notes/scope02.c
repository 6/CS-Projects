/**
 * Examples of scoping in C.
 *
 * Scoping rules apply to all identifiers.
 */

int printf(const char* string, ...); // #include <stdio.h>

void func1(int func2);
void func2();

int main(int argc, char** argv)
{	
	printf("main(): func1 => %d\n", func1);
	printf("main(): func2 => %d\n", func2);
	printf("main(): func3 => %d\n", func3);

	func1(0);
	func2();
}

void func1(int func2)
{
	int func1 = 10;
	printf("func1(): func1 => %d\n", func1);
	printf("func1(): func2 => %d\n", func2);
// 	printf("func1(): func3 => %d\n", func3);	
}

void func2()
{
	printf("func2(): func1 => %d\n", func1);
	printf("func2(): func2 => %d\n", func2);

	void func3();
	printf("func2(): func3 => %d\n", func3);
	func3();
}

void func3()
{
	void (*fptr)(int) = func1;
	int func1 = 30;
	printf("func3(): func1 => %d\n", func1);
	printf("func3(): func2 => %d\n", func2);
	printf("func3(): func3 => %d\n", func3);
	printf("func3(): fptr  => %d\n", fptr);
	fptr(40);
}
