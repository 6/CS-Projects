/*
 * Peter Graham CS333 - Task 4
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * Main method that runs task 4.
 */
int main(int argc, char *argv[]) {
  return task4(); 
}

/*
 * Repeatedly allocates a small amount of memory in a loop that goes on for a
 * long time.
 */
int task4() {
  char* ptr;
  int num_bytes = 10;

  while(1) {
    ptr = malloc(num_bytes);
    memset(ptr, 'a', num_bytes);
    //free(ptr);
  }
  
  return(0);
}