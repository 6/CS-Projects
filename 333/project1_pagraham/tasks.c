/*
 * Peter Graham CS333
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]) {
  //return task1();
  return task2();
  //return task3();
  //return task4();
  //return task5("what123");
}

/* 
 * Declares a variable of each of the basic types (char, short, int, long,
 * float, double) and assigns each one a value. It explores how large each of
 * these basic types is and how the data is stored in memory.
 */
int task1() {
  char c = 80; // ordinal value
  short s = 012; // octal
  int i = 0x1; // hex
  long l = 123456789101112;
  float f = .123e3;
  double d = 1234.5678;
  printf("\n%c %d %d %ld %f %f\n\n", c, s, i, l, f, d);
  
  print_memory_contents("char", sizeof(c), &c);
  print_memory_contents("short", sizeof(s), &s);
  print_memory_contents("int", sizeof(i), &i);
  print_memory_contents("long", sizeof(l), &l);
  print_memory_contents("float", sizeof(f), &f);
  print_memory_contents("double", sizeof(d), &d);
  
  print_endianess();

  return(0);
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
 * See how much memory we can access by giving a char* the address of itself and
 * printing out the memory byte-by-byte from index zero onwards with no stopping
 * condition.
 */
int task3() {
  short s;
  char P;
  char* ptr;
  
  s = 10;
  P = 'P';
  ptr = (char*) &ptr;
  
  int i;
  for(i=0; ; i++) {
    printf("%02x\n", ptr[i]);
  }
  
  return(0);
}

/*
 * Repeatedly allocates a small amount of memory in a loop that goes on for a
 * long time.
 */
int task4() {
  char* ptr;
  while(1) {
    ptr = malloc(10000);
    free(ptr);
  }
  
  return(0);
}

/*
 * Demonstrates that there is a security issue in strcpy() by overwriting a
 * decision variable within the function. Prints out "safe" if the decision
 * variable has the value 0 and "hacked" if the decision variable is non-zero.
 *
 * Note: compile with the flag -D_FORTIFY_SOURCE=0 for this to work on newer
 * versions of GCC (otherwise it will runtime abort). Source:
 * http://thexploit.com/secdev/turning-off-buffer-overflow-protections-in-gcc/
 */
int task5(char* param_str) {
  int decision = 0;
  char local_string[4];  
  strcpy(local_string, param_str);
  
  printf ("%s, %d\n", local_string, decision);
  
  if(decision == 0) {
    puts("Safe");
  }
  else {
    puts("Hacked");
  }
  
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

/*
 * Print if machine endianess using example from Wikipedia:
 * http://en.wikipedia.org/wiki/Endianness
 */
int print_endianess() {
  int i = 0x0A0B0C0D;
  char* i_ptr = (char*) &i;
  
  if(i_ptr[0] == 0x0A) {
    puts("Big endian, since MSB value is at the lowest memory address");
  }
  else if(i_ptr[0] == 0x0D) {
    puts("Little endian, since LSB value is at the lowest memory address");
  }
  else {
    puts("Some other endian (middle-endian?), since neither the MSB nor LSB values are at the lowest memory address");
  }
  return(0);
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

// Generate a "segmentation fault" in 4 characters
r() {
  r();
}

// Generate a "segmentation fault" in 15 characters
runtime_error1() {
  puts((char*)1);
}

// Generate a "segmentation fault" in 16 characters
runtime_error2() {
  int a[1];a[2]=0;
}

// Generate a "floating point exception" in 12 characters
runtime_error3() {
  int z=0;1/z;
}