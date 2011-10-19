/*
 * Peter Graham CS333 - Task 3
 */
#include <stdio.h>
#include <stdlib.h>

/*
 * Main method that runs task 3.
 */
int main(int argc, char *argv[]) {
  return task3(); 
}

/*
 * See how much memory we can access by giving a char* the address of itself and
 * printing out the memory byte-by-byte from index zero onwards with no stopping
 * condition.
 */
int task3() {
  short s = 10;
  char P = 'P';
  char** ptr;
  ptr = (char**) &ptr;
  
  printf("ptr is located at address %p and has the value %p\n", &ptr, *ptr);
  
  int i;
  for(i=0; ; i++) {
    printf("%d: %p\n", i, ptr[i]);
  }
  return(0);
}