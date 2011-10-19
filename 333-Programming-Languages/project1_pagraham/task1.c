/*
 * Peter Graham CS333 - Task 1
 */
#include <stdio.h>
#include <stdlib.h>

/*
 * Main method that runs task 1.
 */
int main(int argc, char *argv[]) {
  return task1(); 
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