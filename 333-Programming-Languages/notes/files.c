/*
 * File I/O in C.
 */

#include <stdio.h>

int main(int argc, char** argv)
{
	FILE* fp = fopen("sample.txt", "r");
	char c;
	
	// fscanf returns num bytes it reads	
	while (fscanf(fp, "%c", &c) != EOF)
	{
		// basically printf
		fprintf(stdout, "%c", c);
	}
	
	// definitely want to close if writing.
	fclose(fp);
}
