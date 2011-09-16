/*
 * Peter Graham CS333 - several different runtime errors
 */
#include <stdio.h>
#include <stdlib.h>

/*
 * Main method that runs a method to generate a runtime error.
 */
int main(int argc, char *argv[]) {
  int program;
  puts("Enter an integer 0-3 to choose a program generate a runtime error:");
  scanf("%d", &program);
  
  printf("Running program number %d\n", program);

  switch(program) {
    case 0: runtime_error0();
    case 1: runtime_error1();
    case 2: runtime_error2();
    case 3: runtime_error3();
  }

  return(0);
}

// Generate a "segmentation fault"
runtime_error0() {
  runtime_error0();
}

// Generate a "segmentation fault"
runtime_error1() {
  puts((char*)1);
}

// Generate a "segmentation fault"
runtime_error2() {
  int a[1];a[2]=0;
}

// Generate a "floating point exception"
runtime_error3() {
  int z=0;return 1/z;
}