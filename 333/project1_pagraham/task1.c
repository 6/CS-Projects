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
  int i = 0x012345;
  long l = 123456789101112;
  float f = .123e3;
  double d = 1234.5678;
  //printf("%c %d %d %ld %f %f\n", type_char, type_short, type_int, type_long, type_float, type_double);
  
  print_type_info("char", c, sizeof(c));
  print_type_info("short", s, sizeof(s));
  print_type_info("int", i, sizeof(i));
  print_type_info("long", l, sizeof(l));
  print_type_info("float", f, sizeof(f));
  print_type_info("double", d, sizeof(d));
  
  return(0);
}

int print_type_info(char *desc, char* type, int num_bytes) {
  printf("\n-------\n%s %d byte(s)\n", desc, num_bytes);
  
  char* ptr = (char*) &type;
  int i;
  for(i=0; i < num_bytes; i++){
    printf("%02x ", ptr[i]);
  }
  
  return(0);
}