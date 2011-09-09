/*
 * CS333 Project 1 - C Programming
 * Peter Graham
 */
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
  return task1();
}

int task1() {
  char c = 80;
  short s = 22;
  int i = 0x1;
  long l = 123456789101112;
  float f = .123e3;
  double d = 1234.5678;
  printf("%c %d %d %ld %f %f\n\n", c, s, i, l, f, d);
  
  print_var_info("char", sizeof(c), c);
  print_var_info("short", sizeof(s), s);
  print_var_info("int", sizeof(i), i);
  print_var_info("long", sizeof(l), l);
  print_var_info("float", sizeof(f), f);
  print_var_info("double", sizeof(d), d);

  return(0);
}

int print_var_info(char *type_str, long num_bytes, char* var) {
  printf("%s (bytes: %ld)\n", type_str, num_bytes);
  
  char* ptr = (char*) &var;
  int i;
  for(i=0; i < num_bytes; i++){
    printf("%02x ", ptr[i]);
  }
  puts("\n-------");
  return(0);
}