/*
 * Demonstrates memory access in C.
 */

#include <stdio.h>
#include <stdlib.h>

typedef unsigned char uchar;

void showMemory(uchar* variable, int bytes, const char* message);

/**
 * Shows the contents of a block of memory. Starting at the 
 * variable address and running through bytes, displays the
 * contents of each byte in memory in hexdecimal format.
 */
void showMemory(uchar* variable, int bytes, const char* message)
{
	printf("%8s (%4d bytes at %08x): ", message, bytes, variable);
	int i;
	for (i = 0; i <bytes; i++)
	{
		if (i % 16 == 0)
			printf("\n%04d:", i);
		printf(" %02x", variable[i]);
	}
	printf("\n\n");
}

/**
 * Shows how basic data types are stored in memory of a C program.
 */
int main(int argc, char** argv)
{
	char varChar = 'c';
	short varShort = 0x1042;
	int varInt = 123456789;
	long varLong = 0xc3b2a190;
	float varFloat = 3.14159;
	double varDouble = 1.0 / 3.0;
	char varString[16] = "ABCDEFGHIJKLMNO";
	char varString2[16];
	varString2 = varString;
	char* pchar = varString;

	showMemory((uchar*) &varChar, sizeof(char), "char");
	showMemory((uchar*) &varShort, sizeof(short), "short");
	showMemory((uchar*) &varInt, sizeof(int), "int");
	showMemory((uchar*) &varLong, sizeof(long), "long");
	showMemory((uchar*) &varFloat, sizeof(varFloat), "float");
	showMemory((uchar*) &varDouble, sizeof(varDouble), "double");
	showMemory((uchar*) &varString, sizeof(varString), "string");

	showMemory((uchar*) &pchar, 64, "memory");	
	return(0);
}
