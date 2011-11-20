#include <stdio.h>
#include <sys/timeb.h>
typedef struct timeb timeb;

double timediff(timeb* t0, timeb* t1)
{
    time_t msec0 = t0->time * 1000 + t0->millitm;
    time_t msec1 = t1->time * 1000 + t1->millitm;
    return (msec1 - msec0) / 1000.0;
}

int main(){
  struct timeb time_start, time_end;
  char ignore_this[1];
  
  printf("Starting timer...\n");
  ftime(&time_start);
  
  printf("Press <Enter> to stop timing");
  gets(ignore_this);
  ftime(&time_end);
  
  printf("Diff: %f seconds\n", timediff(&time_start, &time_end));
  
  return 0;
}
