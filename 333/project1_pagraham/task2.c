/*
 * Peter Graham CS333 - Task 2
 */
#include <stdio.h>
#include <stdlib.h>

/*
 * Main method that runs task 2.
 */
int main(int argc, char *argv[]) {
  return task2(); 
}

/*
 * Looks at how memory is laid out in a structure.
 */
int task2() {
  typedef struct {
      char c1;
      char c2;
      int i;
      short s;
  } Cool;
  
  Cool c = {'a', 'b', 1, 1};
  print_memory_contents("task 2 struct", sizeof(c), &c);
  
  return(0);
}

/*
 * Prints out the contents of a given char* pointer.
 *
 * @param *type_str: a string describing the variable type
 * @param num_bytes: number of bytes in the variable
 * @param ptr: char* pointer for the variable
 */
int print_memory_contents(char *type_str, long num_bytes, char* ptr) {
  printf("%s (bytes: %ld)\n", type_str, num_bytes);

  // loop through each element in the char pointer, byte-by-byte
  int i;
  for(i=0; i < num_bytes; i++){
    printf("%02x ", ptr[i]); // print out byte with leading 0 if applicable
  }
  
  puts("\n-------");
  return(0);
}