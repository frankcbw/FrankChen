#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void print_state(char *state, int size) {
  for (int i = 0; i < size; i++) {
    printf("%c", state[i]);
  }
  printf("\n");
}

void update_state(char *state, int size) {
  char new_state[size];
  new_state[0] = state[0];
  new_state[size-1] = state[size-1];
  for (int i = 1; i < size-1; i++) {
    if (state[i-1] == state[i+1]) {
      new_state[i] = '.';
    }
    else {
      new_state[i] = 'X';
    }
  }
  for (int j = 0; j < size; j++) {
    state[j] = new_state[j];
  }
}
