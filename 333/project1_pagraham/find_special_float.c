/*
 * Peter Graham CS333 - program for finding float to which you can add 1 and get
 * back the same number.
 */
#include <stdio.h>
#include <stdlib.h>

/*
 * Main method that runs the method to find the special float.
 */
int main(int argc, char *argv[]) {
  return find_special_float(); 
}

// Finds the float to which you can add one and get back same number
int find_special_float() {
  float f = 0;
  float previous_f;
  while(1) {
    previous_f = f;
    f++;
    if(f == previous_f) {
      printf("Adding 1 to %f gives back %f\n", previous_f, f);
      break;
    }
  }
  return(0);
}