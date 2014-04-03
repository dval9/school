/** Tom Crowfoot
 * 10037477
 * CPSC 441 Assignment 4
 */

#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <time.h>

#define S 0.25 /* fraction of day singing */
#define SLEN 10 /* average song 10 min */
#define QLEN 30 /* average quiet 30 min */
#define DLEN 60*24 /* minutes in a day */
#define WLEN DLEN*7 /* minutes in a week */
#define QUIET 0 /* no birds singing */
#define MELODIOUS 1 /* one bird singing */
#define SQUAWKY 2 /* more than one bird singing */
#define MAXINT 2147483647 /* 2^31-1 */

typedef struct bird{
  int state;
  double start, end;
} bird;

/* return a random float on [0..1] */
double get_rand()
{
  return (1.0 * random()) / (1.0 * MAXINT);
}

/* return the exponential distribution for mean i */
double exp_dist(double i)
{
  return (i * -1.0) * log(get_rand());
}

int main(int argc, char **argv)
{
  int i, state, num_bird, num_sing, event, event_bird, perfect_song, total_song, is_perfect;
  double quiet, melody, squak, curr_time, next_time, total;
  srand(time(NULL));
  if(argc != 2){
    printf("usage: ./budgie <num_birds>\n");
    exit(EXIT_FAILURE);
  }
  num_bird = atoi(argv[1]);
  // can't have less than 0 birds
  if(num_bird < 0){
    printf("invalid number of budgies\n");
    exit(EXIT_FAILURE);
  }
  // an array of the birds, to keep track who is singing and who is not
  bird *birds[num_bird];
  // initialize each bird to not be singing
  for(i=0; i<num_bird; i++){
    birds[i] = malloc(sizeof(bird));
    birds[i]->state = 0;
    birds[i]->start = 0.0;
    birds[i]->end = 0.0;
  }
  state = QUIET;
  quiet = 0.0;
  melody = 0.0;
  squak = 0.0;
  total = WLEN;
  curr_time = 0.0;
  num_sing = 0;
  perfect_song = 0;
  total_song = 0;
  event = -1;
  do{
    // initial value of impossible
    next_time = WLEN;
    for(i=0; i<num_bird; i++){
      // if that bird isnt singing, generate the start and end time for it
      if(birds[i]->state == 0){
	birds[i]->state = 1;
	birds[i]->start = curr_time + exp_dist(30); // 30 minutes of quiet
	birds[i]->end = birds[i]->start + exp_dist(10); // 10 minutes of singing
	// find the next event to happen, either a bird start singing or end singing
      }if(birds[i]->start < next_time && birds[i]->start > curr_time){
	next_time = birds[i]->start;
	event = 1;
	event_bird = i;
      }if(birds[i]->end < next_time && birds[i]->end > curr_time){
	next_time = birds[i]->end;
	event = 0;
	event_bird = i;
      }
    }
    // no one singing
    if(state == QUIET){
      quiet += next_time - curr_time;
      if(event == 0){
	;// this is bad as no birds were singing to stop
      }
      // a bird started singing, move to melody
      if(event == 1){
	num_sing++;
	state = MELODIOUS;
	total_song++;
	is_perfect = 1; // potentially a perfect song
      }
      // only one bird singing
    }else if(state == MELODIOUS){
      melody += next_time - curr_time;
      // bird stopped singing, move to quiet
      if(event == 0){
	num_sing--;
	birds[event_bird]->state = 0;
	state = QUIET;
	if(is_perfect)
	  perfect_song++;
      }
      // another bird started singing, go to squak
      if(event == 1){
	num_sing++;
	state = SQUAWKY;
	total_song++;
	is_perfect = 0; // no longer a perfect song
      }
      // 2+ birds singing
    }else{
      squak += next_time - curr_time;
      // a bird stopped, if there is only one singing go to melody, else stay in state
      if(event == 0){
	num_sing--;
	birds[event_bird]->state = 0;
	if(num_sing == 1)
	  state = MELODIOUS;
      }
      // more birds singing
      if(event == 1){
	num_sing++;
	state = SQUAWKY;
	total_song++;
      }
    }
    // update the current time to the time of the event we just handled
    curr_time = next_time;
  }while(curr_time < total);
  printf("Results for a time of %8.6f seconds with %d bird(s)\n", total, num_bird);
  printf("State:\t\tSeconds\t\t%%\n");
  printf("Quiet time:\t%8.6f\t%8.6f\n", quiet, quiet/total);
  printf("Melodious time:\t%8.6f\t%8.6f\n", melody, melody/total);
  printf("Squawky time:\t%8.6f\t%8.6f\n", squak, squak/total);
  printf("Number of perfect songs: %d, Number of total songs: %d, Percentage: %8.6f\n", perfect_song, total_song, (double)perfect_song/total_song);
  return EXIT_SUCCESS;
}
