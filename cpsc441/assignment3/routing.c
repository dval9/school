/**
 * Tom Crowfoot
 * 10037477
 * CPSC 441
 * Assignment 3
 * Routing algorithms and preformance
 */

#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>

#define MIN_DIST 1
#define MAX_DIST 999
#define MIN_TIME 1
#define MAX_TIME 999
#define MIN_COIN 1
#define MAX_COIN 99
#define MIN_TROLL 1
#define MAX_TROLL 9
#define MAX_AREA 26
#define INFTY 1000000

// each connection in the graph
struct Apath{
  int distance;
  int time;
  int coins;
  int trolls;
};
typedef struct Apath path;

path map[MAX_AREA][MAX_AREA]; // graph representation, each element is a connection between the indices
char *dwarf_home[MAX_AREA]; // list of homes for the dwarves
double ave_hops, ave_dist, ave_time, ave_gold, ave_trolls, num_dwarves; // for computing averages

// put impossible values into graph, pretend nothing is connected
// also put a temp name into dwarf_home so it doesnt segfault on strcmp
void init_graph()
{
  int i, j;
  path *temppath = malloc(sizeof(path));
  temppath->distance = INFTY;
  temppath->time = INFTY;
  temppath->coins = INFTY;
  temppath->trolls = INFTY;
  for(i=0;i<MAX_AREA;i++){
    for(j=0;j<MAX_AREA;j++){
      map[i][j] = *temppath;
    }
    dwarf_home[i] = strdup("Morgoth");
  }
}

// return the smallest distance
int smallest(int *distance, int *used)
{
  int i, index = -1, min = INFTY;
  for(i=0; i<MAX_AREA; i++){
    if(used[i] == 0 && distance[i] < min){
      if(index == -1)
	index = i;
      else if(distance[i] < distance[index])
	index = i;
    }
  }
  return index;
}

// calculate shortest path from source to bilbo, based on number of hops
void SHP(int source)
{
  int hops=0, dist=0, time=0, gold=0, trolls=0, pathptr=0, min;
  char *path=malloc(26);
  int i, j, k, l, distance[MAX_AREA], used[MAX_AREA], visited[MAX_AREA], parent[MAX_AREA];
  // dijkstras algorithm, builds a minimum tree in distance array
  // set everything to unconnected
  for(i=0; i<MAX_AREA; i++){
    distance[i] = INFTY;
    used[i] = 0;
    visited[i] = 0;
  }
  // distance to self is 0 to begin
  distance[source] = 0;
  for(j=0; j<MAX_AREA; j++){
    // will return with source for first iteration
    k = smallest(distance, used);
    used[k] = 1;
    // stop if we have bilbo
    if(strcmp(dwarf_home[k], "Bilbo") == 0)
      break;
    // update the distance to adjacent nodes if a shorter path is now available
    for(l=0; l<MAX_AREA; l++){
      if((used[l] == 0) && (map[k][l].distance != INFTY) && (distance[k] != INFTY) && (distance[k] + 1 < distance[l])){
	distance[l] = distance[k] + 1;
	// last updated parent will be the one we need to traverse the tree after
	parent[l] = k;
      }
    }
  }
  // use a char array as a stack to find path
  *path = source + 65;
  visited[source] = 1;
  pathptr++;
  // depth first search on the tree from dijkstras to find path to bilbos house
  while(1){
    min = INFTY;
    j = MAX_AREA;
    // find the next closest neighbour to search
    for(i=0; i<MAX_AREA; i++){
      if((visited[i] == 0) && (distance[i] < min) && (map[*(path+pathptr-1)-65][i].distance != INFTY) && (distance[i] > distance[*(path+pathptr-1)-65])){
	if(1 + hops <= distance[k] && (parent[i] == *(path+pathptr-1)-65)){
	  j = i;
	  min = distance[i];
	}
      }
    }
    // we didnt find another connecting node, so go back a step and find another path
    if(j == MAX_AREA){
      pathptr--;
      hops--;
      dist-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      *(path+pathptr) = 0;      
    }
    // found a path on the tree potentially to destination, update values
    else{
      visited[j] = 1;
      *(path+pathptr) = j+65;
      hops++;
      dist+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      pathptr++;
      // we just added the destination to the path, so we can stop
      if(strcmp(dwarf_home[*(path+pathptr-1)-65], "Bilbo") == 0){
	*(path+pathptr) = 0;
	break;
      }
    }
  }
  // update the averages
  ave_hops += hops;
  ave_dist += dist;
  ave_time += time;
  ave_gold += gold;
  ave_trolls += trolls;
  printf("%d\t%d\t%d\t%d\t%d\t%s\n", hops, dist, time, gold, trolls, path);
  free(path);
}

// same as SHP, except uses distance instead of hops
void SDP(int source)
{
  int hops=0, dist=0, time=0, gold=0, trolls=0, pathptr=0, min;
  char *path=malloc(26);
  int i, j, k, l, distance[MAX_AREA], used[MAX_AREA], visited[MAX_AREA], parent[MAX_AREA];
  for(i=0; i<MAX_AREA; i++){
    distance[i] = INFTY;
    used[i] = 0;
    visited[i] = 0;
  }
  distance[source] = 0;
  for(j=0; j<MAX_AREA; j++){
    k = smallest(distance, used);
    used[k] = 1;
    if(strcmp(dwarf_home[k], "Bilbo") == 0)
      break;
    for(l=0; l<MAX_AREA; l++){
      // dist instead of hop
      if((used[l] == 0) && (map[k][l].distance != INFTY) && (distance[k] != INFTY) && (distance[k] + map[k][l].distance < distance[l])){
	distance[l] = distance[k] + map[k][l].distance;
  	parent[l] = k;
      }
    }
  }
  *path = source + 65;
  visited[source] = 1;
  pathptr++;
  while(1){
    min = INFTY;
    j = MAX_AREA;
    for(i=0; i<MAX_AREA; i++){
      if((visited[i] == 0) && (distance[i] < min) && (map[*(path+pathptr-1)-65][i].distance != INFTY) && (distance[i] > distance[*(path+pathptr-1)-65])){
	// distance instead of hop
	if(map[*(path+pathptr-1)-65][i].distance + dist <= distance[k] && (parent[i] == *(path+pathptr-1)-65)){
	  j = i;
	  min = distance[i];
	}
      }
    }
    if(j == MAX_AREA){
      pathptr--;
      hops--;
      dist-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      *(path+pathptr) = 0;      
    }
    else{
      visited[j] = 1;
      *(path+pathptr) = j+65;
      hops++;
      dist+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      pathptr++;
      if(strcmp(dwarf_home[*(path+pathptr-1)-65], "Bilbo") == 0){
	*(path+pathptr) = 0;
	break;
      }
    }
  }
  // update the averages
  ave_hops += hops;
  ave_dist += dist;
  ave_time += time;
  ave_gold += gold;
  ave_trolls += trolls;
  printf("%d\t%d\t%d\t%d\t%d\t%s\n", hops, dist, time, gold, trolls, path);
  free(path);
}

// same as SHP, except uses time instead of hops
void STP(int source)
{
  int hops=0, dist=0, time=0, gold=0, trolls=0, pathptr=0, min;
  char *path=malloc(26);
  int i, j, k, l, distance[MAX_AREA], used[MAX_AREA], visited[MAX_AREA], parent[MAX_AREA];
  for(i=0; i<MAX_AREA; i++){
    distance[i] = INFTY;
    used[i] = 0;
    visited[i] = 0;
  }
  distance[source] = 0;
  for(j=0; j<MAX_AREA; j++){
    k = smallest(distance, used);
    used[k] = 1;
    if(strcmp(dwarf_home[k], "Bilbo") == 0)
      break;
    for(l=0; l<MAX_AREA; l++){
      // time instead of hop
      if((used[l] == 0) && (map[k][l].distance != INFTY) && (distance[k] != INFTY) && (distance[k] + map[k][l].time < distance[l])){
	distance[l] = distance[k] + map[k][l].time;
	parent[l] = k;
      }
    }
  }
  *path = source + 65;
  visited[source] = 1;
  pathptr++;
  while(1){
    min = INFTY;
    j = MAX_AREA;
    for(i=0; i<MAX_AREA; i++){
      if((visited[i] == 0) && (distance[i] < min) && (map[*(path+pathptr-1)-65][i].distance != INFTY) && (distance[i] > distance[*(path+pathptr-1)-65])){
	// time instead of hop
	if(map[*(path+pathptr-1)-65][i].time + time <= distance[k] && (parent[i] == *(path+pathptr-1)-65)){
	  j = i;
	  min = distance[i];
	}
      }
    }
    if(j == MAX_AREA){
      pathptr--;
      hops--;
      dist-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      *(path+pathptr) = 0;      
    }
    else{
      visited[j] = 1;
      *(path+pathptr) = j+65;
      hops++;
      dist+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      pathptr++;
      if(strcmp(dwarf_home[*(path+pathptr-1)-65], "Bilbo") == 0){
	*(path+pathptr) = 0;
	break;
      }
    }
  }
  // update the averages
  ave_hops += hops;
  ave_dist += dist;
  ave_time += time;
  ave_gold += gold;
  ave_trolls += trolls;
  printf("%d\t%d\t%d\t%d\t%d\t%s\n", hops, dist, time, gold, trolls, path);
  free(path);
}

// same as SHP, except uses trolls instead of hops
void FTP(int source)
{
  int hops=0, dist=0, time=0, gold=0, trolls=0, pathptr=0, min;
  char *path=malloc(26);
  int i, j, k, l, distance[MAX_AREA], used[MAX_AREA], visited[MAX_AREA], parent[MAX_AREA];
  for(i=0; i<MAX_AREA; i++){
    distance[i] = INFTY;
    used[i] = 0;
    visited[i] = 0;
  }
  distance[source] = 0;
  parent[source]=source;
  for(j=0; j<MAX_AREA; j++){
    k = smallest(distance, used);
    used[k] = 1;
    if(strcmp(dwarf_home[k], "Bilbo") == 0)
      break;
    for(l=0; l<MAX_AREA; l++){
      // trolls instead of hop
      if((used[l] == 0) && (map[k][l].distance != INFTY) && (distance[k] != INFTY) && (distance[k] + map[k][l].trolls < distance[l])){
	distance[l] = distance[k] + map[k][l].trolls;
	parent[l] = k;
      }
    }
  }
  *path = source + 65;
  visited[source] = 1;
  pathptr++;
  while(1){
    min = INFTY;
    j = MAX_AREA;
    for(i=0; i<MAX_AREA; i++){
      if((visited[i] == 0) && (distance[i] < min) && (map[*(path+pathptr-1)-65][i].distance != INFTY) && (distance[i] > distance[*(path+pathptr-1)-65])){
	// trolls instead of hop	
	if(map[*(path+pathptr-1)-65][i].trolls + trolls <= distance[k] && (parent[i] == *(path+pathptr-1)-65)){
	  j = i;
	  min = distance[i];
	}
      }
    }
    if(j == MAX_AREA){
      pathptr--;
      hops--;
      dist-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls-=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      *(path+pathptr) = 0;
    }
    else{
      visited[j] = 1;
      *(path+pathptr) = j+65;
      hops++;
      dist+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].distance;
      time+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].time;
      gold+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].coins;
      trolls+=map[*(path+pathptr-1)-65][*(path+pathptr)-65].trolls;
      pathptr++;
      if(strcmp(dwarf_home[*(path+pathptr-1)-65], "Bilbo") == 0){
	*(path+pathptr) = 0;
	break;
      }
    }
  }
  // update the averages
  ave_hops += hops;
  ave_dist += dist;
  ave_time += time;
  ave_gold += gold;
  ave_trolls += trolls;
  printf("%d\t%d\t%d\t%d\t%d\t%s\n", hops, dist, time, gold, trolls, path);
  free(path);
}

int main(int argc, char **argv)
{
  int i, j, debug = 0;
  FILE *file;
  char line[30];
  char *dest1, *dest2, *dist, *time, *coin, *troll, *dwarf;
  if(argc < 3){
    printf("usage: ./route <map> <address book> [debug]\n");
    exit(EXIT_FAILURE);
  }
  if(argc == 4)
    if(strcmp(argv[3], "debug")==0)
       debug = 1;
  if(debug)
    printf("Debug mode on\n");
  // nothing is connected to begin
  init_graph();
  if((file = fopen(argv[1], "r")) == NULL){
    perror("file");
    exit(EXIT_FAILURE);
  }
  // read the maps file, adding in paths
  while(fgets(line, 30, file) != NULL){
    path *newpath = malloc(sizeof(path));
    dest1 = strtok(line, " \n\0");
    dest2 = strtok(NULL, " \n\0");
    dist = strtok(NULL, " \n\0");
    time = strtok(NULL, " \n\0");
    coin = strtok(NULL, " \n\0");
    troll = strtok(NULL, " \n\0");
    newpath->distance = atoi(dist);
    newpath->time = atoi(time);
    newpath->coins = atoi(coin);
    newpath->trolls = atoi(troll);
    map[*dest1-'A'][*dest2-'A'] = *newpath;
    map[*dest2-'A'][*dest1-'A'] = *newpath;
  }
  fclose(file);
  if((file = fopen(argv[2], "r")) == NULL){
    perror("file");
    exit(EXIT_FAILURE);
  }
  // read in the homes file, adding dwarves
  num_dwarves = 0;
  while(fgets(line, 30, file) != NULL){
    dwarf = strtok(line, " \n\0");
    dest1 = strtok(NULL, " \n\0");
    dwarf_home[*dest1-'A'] = strdup(dwarf);
    num_dwarves++;
  }
  // don't count bilbo
  num_dwarves--;
  fclose(file);
  // print graph topology for debug
  if(debug){
    printf("  A B C D E F G H I J K L M N O P Q R S T U V W X Y Z\n");
    for(i=0;i<MAX_AREA;i++){
      printf("%c ", i+65);
      for(j=0;j<MAX_AREA;j++){
	if(map[i][j].distance == INFTY)
	  printf("  ");
	else
	  printf("1 ");
      }
      printf("\n");
    }
  }
  // iterate through array of dwarves, print shortest path based on hops
  ave_hops = ave_dist = ave_time = ave_gold = ave_trolls = 0;
  printf("Shortest Hops Path\n");
  printf("Dwarf\tHome\tHops\tDist\tTime\tGold\tTrolls\tPath\n");
  printf("--------------------------------------------------------------------------------\n");
  for(i=0;i<MAX_AREA;i++){
    // dont bother with bilbo, morgoth = null
    if(strcmp(dwarf_home[i], "Morgoth") && strcmp(dwarf_home[i], "Bilbo")){
      printf("%s\t%c\t", dwarf_home[i], i+65);
      SHP(i);
    }
  }
  printf("--------------------------------------------------------------------------------\n");
  printf("AVERAGE:\t%g\t%g\t%g\t%g\t%g\n\n", ave_hops/num_dwarves, ave_dist/num_dwarves, ave_time/num_dwarves, ave_gold/num_dwarves, ave_trolls/num_dwarves);
  // iterate through array of dwarves, print shortest path based on distance
  ave_hops = ave_dist = ave_time = ave_gold = ave_trolls = 0;
  printf("Shortest Distance Path\n");
  printf("Dwarf\tHome\tHops\tDist\tTime\tGold\tTrolls\tPath\n");
  printf("--------------------------------------------------------------------------------\n");
  for(i=0;i<MAX_AREA;i++){
    // dont bother with bilbo, morgoth = null
    if(strcmp(dwarf_home[i], "Morgoth") && strcmp(dwarf_home[i], "Bilbo")){
      printf("%s\t%c\t", dwarf_home[i], i+65);
      SDP(i);
    }
  }
  printf("--------------------------------------------------------------------------------\n");
  printf("AVERAGE:\t%g\t%g\t%g\t%g\t%g\n\n", ave_hops/num_dwarves, ave_dist/num_dwarves, ave_time/num_dwarves, ave_gold/num_dwarves, ave_trolls/num_dwarves);
  // iterate through array of dwarves, print shortest path based on time
  ave_hops = ave_dist = ave_time = ave_gold = ave_trolls = 0;
  printf("Shortest Time Path\n");
  printf("Dwarf\tHome\tHops\tDist\tTime\tGold\tTrolls\tPath\n");
  printf("--------------------------------------------------------------------------------\n");
  for(i=0;i<MAX_AREA;i++){
    // dont bother with bilbo, morgoth = null
    if(strcmp(dwarf_home[i], "Morgoth") && strcmp(dwarf_home[i], "Bilbo")){
      printf("%s\t%c\t", dwarf_home[i], i+65);
      STP(i);
    }
  }
  printf("--------------------------------------------------------------------------------\n");
  printf("AVERAGE:\t%g\t%g\t%g\t%g\t%g\n\n", ave_hops/num_dwarves, ave_dist/num_dwarves, ave_time/num_dwarves, ave_gold/num_dwarves, ave_trolls/num_dwarves);
  // iterate through array of dwarves, print shortest path based on trolls
  ave_hops = ave_dist = ave_time = ave_gold = ave_trolls = 0;
  printf("Fewest Trolls Path\n");
  printf("Dwarf\tHome\tHops\tDist\tTime\tGold\tTrolls\tPath\n");
  printf("--------------------------------------------------------------------------------\n");
  for(i=0;i<MAX_AREA;i++){
    // dont bother with bilbo, morgoth = null
    if(strcmp(dwarf_home[i], "Morgoth") && strcmp(dwarf_home[i], "Bilbo")){
      printf("%s\t%c\t", dwarf_home[i], i+65);
      FTP(i);
    }
  }
  printf("--------------------------------------------------------------------------------\n");
  printf("AVERAGE:\t%g\t%g\t%g\t%g\t%g\n\n", ave_hops/num_dwarves, ave_dist/num_dwarves, ave_time/num_dwarves, ave_gold/num_dwarves, ave_trolls/num_dwarves);
  return 0;
}
