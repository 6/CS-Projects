#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/timeb.h>
typedef struct timeb timeb;

double timediff(timeb* t0, timeb* t1)
{
    time_t msec0 = t0->time * 1000 + t0->millitm;
    time_t msec1 = t1->time * 1000 + t1->millitm;
    return (msec1 - msec0) / 1000.0;
}

double time_allocate(int n_bytes) {
  printf("Timing allocation of %d bytes...\n", n_bytes);
  // Time 2000 memory allocations to estimate average time per allocation call
  double total_seconds = 0;
  int total_allocations = 2000;
  int i;
  struct timeb time_start, time_end;
  char *buffer;
  for(i=0; i<total_allocations; i++) {
    ftime(&time_start);
    buffer = malloc(n_bytes);
    // Write something into the allocated mem (otherwise, compiler can optimize)
    memset(buffer, 123, n_bytes);
    ftime(&time_end);
    
    double diff = timediff(&time_start, &time_end);
    //printf("Diff: %f seconds\n", diff);
    total_seconds += diff;
  }
  return total_seconds / total_allocations;
}

int main(){
  // Experiment with three different memory sizes (small, medium, and large)
  printf("Avg: %f\n", time_allocate(1000000));
  printf("Avg: %f\n", time_allocate(10000000));
  printf("Avg: %f\n", time_allocate(100000000));
}