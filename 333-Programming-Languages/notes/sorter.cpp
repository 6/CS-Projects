#include <iostream>
#include <string>
using namespace std;

/**
 * Demonstrates defining a polymorphic sorting 
 * operation using templates for types that have
 * a < operator defined.
 */

template <class T>
void sort(T* a, int length)
{
	// sorts elements of a in place
	// at each iteration of the outer loop
	// the element at a[i] is properly ordered
	for (int i = 0; i < length; i++)
	{
		for (int j = i+1; j < length; j++)
		{
			if (a[j] < a[i])
			{
				T swap = a[j];
				a[j] = a[i];
				a[i] = swap;
			}
		}
	}
}

int main(int argc, char** argv)
{
	// convert command line arguments to 
	// an array of strings
	string array[argc];
	for (int i = 0; i < argc; i++)
	{
		array[i] = string(argv[i]);
		cout << array[i] << endl;
	}
	
	cout << "---" << endl;
	
	// creates a concrete sort routine for string
	sort<string>(array, argc);
	for (int i = 0; i < argc; i++)
	{
		cout << array[i] << endl;
	}
	
	// creates a concrete sort routine for char
	sort<char>(argv[argc-1], strlen(argv[argc-1]));
	cout << argv[argc-1] << endl;
}