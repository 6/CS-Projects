#include <stdio.h>
#include <stdlib.h>

// structs are user-defined data types
struct sMap
{
	int key;
	float value;
};

// typedefs enable renaming types
// (compare "Map" vs. "struct sMap")
typedef struct sMap Map;

/*
 * Sets a key/value pair in a map.
 */
void set(Map* map, int key, float value)
{
	(*map).key = key;
	map->value = value;
}

/*
 * Prints all the Map values in an array.
 */
void print(Map* map, int count)
{
	printf("Map:\n");
	int i;
	for (i = 0; i < count; i++)
	{
		printf("\t%d : %8.5f\n", map[i].key, (map+i)->value);
	}
}

int main(int argc, char** argv)
{
	// create a simple map
	int count = 5;
	Map example[count];
	int i;
	for (i = 0; i < count; i++)
	{
		set((example + i), i, i / 3.0);
	}
	print(example, count);
}