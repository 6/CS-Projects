#include <stdio.h>

// variable binding
int main(int argc, char** argv) {
  int x, y;
  int* z;

  // l-val of x, r-val of x
  x = x + 1;

  //  lval of  y, r-val of x
  y = x + 1;

  // l-val of z, lval of x (used as r-val)
  z = &x;

  // lval of x, rval of z accesing r val of x
  x = *z + 1;

  // rval of z (ref to lval of x), rval of y
  *z = y;

  // lval of z, lval of y (used as an rval)
  z = &y + 4;

  // lval x, rval of z
  x = (int) z;
}