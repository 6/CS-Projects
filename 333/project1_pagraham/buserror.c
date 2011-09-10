/*
 * Peter Graham CS333
 * Program to generate a "bus error" in 25 characters total. This works since
 * strings in C are read-only.
 */
main(){char *s="a";*s=0;}