/*
 * Peter Graham CS333
 */
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
  //return task1();
  return task2();
}

/* 
 * A program that declares a variable of each of the basic types (char, short, 
 * int, long, float, double) and assigns each one a value. It explores how large
 * each of these basic types is and how the data is stored in memory.
 */
int task1() {
  char c = 80; // ordinal value
  short s = 012; // octal
  int i = 0x1; // hex
  long l = 123456789101112;
  float f = .123e3;
  double d = 1234.5678;
  printf("%c %d %d %ld %f %f\n\n", c, s, i, l, f, d);
  
  print_memory_contents("char", sizeof(c), &c);
  print_memory_contents("short", sizeof(s), &s);
  print_memory_contents("int", sizeof(i), &i);
  print_memory_contents("long", sizeof(l), &l);
  print_memory_contents("float", sizeof(f), &f);
  print_memory_contents("double", sizeof(d), &d);

  return(0);
}

/*
 * A program that looks at how memory is laid out in a variable.
 */
int task2() {
  typedef struct {
      int i;
      short s1;
      short s2;
      char c;
  } Cool;
  
  Cool c = {0x1,012,013,'P'};
  print_memory_contents("cool", sizeof(c), &c);
  
  return(0);
}

int print_memory_contents(char *type_str, long num_bytes, char* ptr) {
  printf("%s (bytes: %ld)\n", type_str, num_bytes);

  // loop through each element in the char pointer, byte-by-byte
  int i;
  for(i=0; i < num_bytes; i++){
    printf("%02x ", ptr[i]);
  }
  
  puts("\n-------");
  return(0);
}