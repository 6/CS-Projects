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
  char type_char = 80;
  short type_short = 22;
  int type_int = 0x012345;
  long type_long = 123456789101112;
  float type_float = .123e3;
  double type_double = 1234.5678;
  //printf("%c %d %d %ld %f %f\n", type_char, type_short, type_int, type_long, type_float, type_double);
  
  print_type_info("char", type_char, sizeof(type_char));
  print_type_info("short", type_short, sizeof(type_short));
  print_type_info("int", type_int, sizeof(type_int));
  print_type_info("long", type_long, sizeof(type_long));
  print_type_info("float", type_float, sizeof(type_float));
  print_type_info("double", type_double, sizeof(type_double));
  
  return(0);
}

int print_type_info(char *desc, char* chr, int num_bytes) {
  printf("\n-------\n%s %d byte(s)\n", desc, num_bytes);
  
  char* ptr = (char*) &chr;
  int i;
  for(i=0; i < num_bytes; i++){
    printf("%02x ", ptr[i]);
  }
  
  return(0);
}