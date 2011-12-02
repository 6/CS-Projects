#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/timeb.h>
typedef struct timeb timeb;

/*
Does the time required for memory management change with the number of allocation and free operations the program executes? Test this by writing a program that makes lots of malloc/free calls and timing a few hundred or a few thousand such calls. See if there is a difference for allocating the same sized block all the time as opposed to allocating memory of different sizes.
*/

double timediff(timeb* t0, timeb* t1)
{
    time_t msec0 = t0->time * 1000 + t0->millitm;
    time_t msec1 = t1->time * 1000 + t1->millitm;
    return (msec1 - msec0) / 1000.0;
}

double malloc_free(long int n_bytes) {
  printf("Timing allocation of %ld bytes...\n", n_bytes);
  // Time 2000 memory allocations to estimate average time per allocation call
  double total_seconds = 0;
  int total_allocations = 10;
  int i;
  struct timeb time_start, time_end;
  char *buffer;
  for(i=0; i<total_allocations; i++) {
    ftime(&time_start);
    buffer = malloc(n_bytes);
    // Write something into the allocated mem (otherwise, compiler can optimize)
    memset(buffer, 123, n_bytes);
    free(buffer);
    ftime(&time_end);
    
    double diff = timediff(&time_start, &time_end);
    total_seconds += diff;
  }
  return total_seconds / total_allocations;
}

int main(int argc, char * argv[]) {
  // Experiment with three different memory sizes (small, medium, and large)
  printf("Avg: %f\n", malloc_free(100000000));
  printf("Avg: %f\n", malloc_free(500000000));
  printf("Avg: %f\n", malloc_free(1000000000));
  return 1;
}
