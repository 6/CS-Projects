/*
 * Peter Graham CS333 - program to generate a "bus error"
 *
 * Based off of Wikipedia's "Segmentation fault example" on the following page:
 * http://en.wikipedia.org/wiki/Segmentation_fault
 */
main(){
  char *s="s";
  *s=0;
}