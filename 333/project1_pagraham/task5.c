/*
 * Peter Graham CS333 - Task 5
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * Main method that accepts input for and runs task 5.
 */
int main(int argc, char *argv[]) {
  puts("Enter a string (up to 100 chars) to try hacking strcpy (hint: try 'abcdef'):");
  char input_string[100];
  scanf("%s", input_string);
  return task5(input_string); 
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
  char local_string[3];  
  strcpy(local_string, param_str);
  
  printf ("Local string: %s, Decision var: %d\n", local_string, decision);
  
  if(decision == 0) {
    puts("Safe");
  }
  else {
    puts("Hacked");
  }
  
  return(0);
}
