/*
 * Demonstrates pointer and array equivalence in C.
 */

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char** argv)
{
	int* pint = malloc(6 * sizeof(int));
	
	pint[0] = 0;
	
	*(pint + 1) = 1;
	
	int off = 2;
	pint[off] = 2;
	
	off++;
	off[pint] = 3;
	
	off++;
	*(off + pint) = 4;
	
	pint += 5;
	*pint = 5;
	pint -= 5;
	
	int i;
	for (i = 0; i < 6; i++)
	{
		printf("%d ", pint[i]);
	}
	printf("\n");
	free( pint );
}