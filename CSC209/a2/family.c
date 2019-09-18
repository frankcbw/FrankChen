#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "family.h"

/* Number of word pointers allocated for a new family.
   This is also the number of word pointers added to a family
   using realloc, when the family is full.
*/
static int family_increment = 0;


/* Set family_increment to size, and initialize random number generator.
   The random number generator is used to select a random word from a family.
   This function should be called exactly once, on startup.
*/
void init_family(int size) {
    family_increment = size;
    srand(time(0));
}

/* Given a pointer to the head of a linked list of Family nodes,
   print each family's signature and words.

   Do not modify this function. It will be used for marking.
*/
void print_families(Family* fam_list) {
    int i;
    Family *fam = fam_list;

    while (fam) {
        printf("***Family signature: %s Num words: %d\n",
               fam->signature, fam->num_words);
        for(i = 0; i < fam->num_words; i++) {
            printf("     %s\n", fam->word_ptrs[i]);
        }
        printf("\n");
        fam = fam->next;
    }
}


/* Return a pointer to a new family whose signature is
   a copy of str. Initialize word_ptrs to point to
   family_increment+1 pointers, numwords to 0,
   maxwords to family_increment, and next to NULL.
*/
Family *new_family(char *str) {
    Family *fam_ptr = malloc(sizeof(Family));
    if(fam_ptr == NULL) {
      perror("malloc");
      exit(1);
    }
    else {
      char **word_ptrs = malloc(sizeof(char*) * (family_increment + 1));
      if(word_ptrs == NULL) {
        perror("malloc");
        exit(1);
      }
      for (int i = 0; i < family_increment; i++) {
        word_ptrs[i] = malloc(sizeof(char) * (strlen(str)+1));
      }
      word_ptrs[family_increment] = NULL;
      Family fam = {str, word_ptrs, 0, family_increment, NULL};
      *fam_ptr = fam;
    }
    return fam_ptr;
}


/* Add word to the next free slot fam->word_ptrs.
   If fam->word_ptrs is full, first use realloc to allocate family_increment
   more pointers and then add the new pointer.
*/
void add_word_to_family(Family *fam, char *word) {
    if (fam->max_words == fam->num_words) {
      fam->word_ptrs = realloc(fam->word_ptrs, sizeof(char*) *
      (fam->max_words + family_increment + 1));
      if (fam->word_ptrs == NULL) {
        perror("realloc");
        exit(1);
      }
      else{
        fam->max_words += family_increment;
        fam->word_ptrs[fam->max_words] = NULL;
      }
    }
    strcpy(fam->word_ptrs[fam->num_words], word);
    fam->num_words += 1;
    return;
}


/* Return a pointer to the family whose signature is sig;
   if there is no such family, return NULL.
   fam_list is a pointer to the head of a list of Family nodes.
*/
Family *find_family(Family *fam_list, char *sig) {
    Family *curr = fam_list;
    while (curr != NULL && strcmp(curr->signature, sig) != 0) {
      curr = curr->next;
    }
    return curr;
}


/* Return a pointer to the family in the list with the most words;
   if the list is empty, return NULL. If multiple families have the most words,
   return a pointer to any of them.
   fam_list is a pointer to the head of a list of Family nodes.
*/
Family *find_biggest_family(Family *fam_list) {
    Family *curr = fam_list;
    Family *max = fam_list;
    while (curr != NULL) {
      if (curr->num_words > max->num_words) {
        max = curr;
      }
      curr = curr->next;
    }
    return max;
}


/* Deallocate all memory rooted in the List pointed to by fam_list. */
void deallocate_families(Family *fam_list) {
    Family *curr = fam_list;
    while (curr != NULL) {
      Family *next = curr->next;
      for (int i = 0; i < curr->max_words; i++) {
        free(curr->word_ptrs[i]);
      }
      free(curr->word_ptrs);
      free(curr->signature);
      free(curr);
      curr = next;
    }
    return;
}


/* Generate and return a linked list of all families using words pointed to
   by word_list, using letter to partition the words.

   Implementation tips: To decide the family in which each word belongs, you
   will need to generate the signature of each word. Create only the families
   that have at least one word from the current word_list.
*/
Family *generate_families(char **word_list, char letter) {
    Family *curr = NULL;
    int word_length = strlen(word_list[0]);
    int i = 0;
    while (word_list[i] != NULL) {
      char sig[word_length+1];
      for (int j = 0; j < word_length; j++) {
        if (word_list[i][j] == letter) {
          sig[j] = letter;
        }
        else {
          sig[j] = '-';
        }
      }
      sig[word_length] = '\0';

      Family *new = find_family(curr, sig);
      if (new == NULL) {
          new = new_family(sig);
          new->next = curr;
          add_word_to_family(new, word_list[i]);
          curr = new;
      }
      else {
          add_word_to_family(new, word_list[i]);
      }
      i++;
    }
    return curr;
}


/* Return the signature of the family pointed to by fam. */
char *get_family_signature(Family *fam) {
    return fam->signature;
}


/* Return a pointer to word pointers, each of which
   points to a word in fam. These pointers should not be the same
   as those used by fam->word_ptrs (i.e. they should be independently malloc'd),
   because fam->word_ptrs can move during a realloc.
   As with fam->word_ptrs, the final pointer should be NULL.
*/
char **get_new_word_list(Family *fam) {
    char **new_word_list = malloc(sizeof(char*) * (fam->max_words + 1));
    for (int i = 0; i < fam->max_words; i++) {
      strncpy(new_word_list[i], fam->word_ptrs[i], strlen(fam->signature));
    }
    new_word_list[fam->max_words] = NULL;
    return new_word_list;
}


/* Return a pointer to a random word from fam.
   Use rand (man 3 rand) to generate random integers.
*/
char *get_random_word_from_family(Family *fam) {
    int n = rand() % fam->max_words;
    return fam->word_ptrs[n];
}
