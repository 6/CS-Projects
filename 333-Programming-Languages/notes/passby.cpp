#include <stdio.h>

/*
 * Pass by value.
 */
void incrementV1(int value)
{
	value++;
}

/*
 * Pass by value. Value is a pointer.
 */
void incrementV2(int* value)
{
	(*value)++;
}

/*
 * Pass by reference.
 */
void incrementV3(int& value)
{
	value++;
}

int main(int argc, char** argv)
{
	int value = 1;
	printf("initial value: %d\n", value);
	
	incrementV1(value);
	printf("incrementV1:   %d\n", value);

	incrementV2(&value);
	printf("incrementV2:   %d\n", value);

	incrementV3(value);
	printf("incrementV3:   %d\n", value);
}