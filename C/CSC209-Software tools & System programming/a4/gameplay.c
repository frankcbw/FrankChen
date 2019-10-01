#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include "gameplay.h"

/* Return a status message that shows the current state of the game.
 * Assumes that the caller has allocated MAX_MSG bytes for msg.
 */
char *status_message(char *msg, struct game_state *game) {
    sprintf(msg, "***************\r\n"
           "Word to guess: %s\r\nGuesses remaining: %d\r\n"
           "Letters guessed: \r\n", game->guess, game->guesses_left);
    for(int i = 0; i < 26; i++){
        if(game->letters_guessed[i]) {
            int len = strlen(msg);
            msg[len] = (char)('a' + i);
            msg[len + 1] = ' ';
            msg[len + 2] = '\0';
        }
    }
    strncat(msg, "\r\n***************\r\n", MAX_MSG);
    return msg;
}


/* Initialize the gameboard:
 *    - initialize dictionary
 *    - select a random word to guess from the dictionary file
 *    - set guess to all dashes ('-')
 *    - initialize the other fields
 * We can't initialize head and has_next_turn because these will have
 * different values when we use init_game to create a new game after one
 * has already been played
 */
void init_game(struct game_state *game, char *dict_name) {
    char buf[MAX_WORD];
    if(game->dict.fp != NULL) {
        rewind(game->dict.fp);
    } else {
        game->dict.fp = fopen(dict_name, "r");
        if(game->dict.fp == NULL) {
            perror("Opening dictionary");
            exit(1);
        }
    }

    int index = random() % game->dict.size;
    printf("Looking for word at index %d\n", index);
    for(int i = 0; i <= index; i++) {
        if(!fgets(buf, MAX_WORD, game->dict.fp)){
            fprintf(stderr,"File ended before we found the entry index %d",index);
            exit(1);
        }
    }

    // Found word
    if(buf[strlen(buf) - 1] == '\n') {  // from a unix file
        buf[strlen(buf) - 1] = '\0';
    } else {
        fprintf(stderr, "The dictionary file does not appear to have Unix line endings\n");
    }
    strncpy(game->word, buf, MAX_WORD);
    game->word[MAX_WORD-1] = '\0';
    for(int j = 0; j < strlen(game->word); j++) {
        game->guess[j] = '-';
    }
    game->guess[strlen(game->word)] = '\0';

    for(int i = 0; i < NUM_LETTERS; i++) {
        game->letters_guessed[i] = 0;
    }
    game->guesses_left = MAX_GUESSES;

}


/* Return the number of lines in the file
 */
int get_file_length(char *filename) {
    char buf[MAX_MSG];
    int count = 0;
    FILE *fp;
    if((fp = fopen(filename, "r")) == NULL) {
        perror("open");
        exit(1);
    }

    while(fgets(buf, MAX_MSG, fp) != NULL) {
        count++;
    }

    fclose(fp);
    return count;
}


/* Check if a guess is correct. If correct, update game status and return 1.
 * Else return 0.
 */
int check_guess(struct game_state *game, char *guess) {
    int is_correct = 0;

    game->letters_guessed[guess[0] -'a'] = 1;
    for (int i = 0; i < strlen(game->word); i++) {
        if (guess[0] == game->word[i]) {
          game->guess[i] = guess[0];
          is_correct = 1;
        }
    }
    if (!is_correct) {
        game->guesses_left--;
    }
    return is_correct;
}


/* Check if a game is over. Return -1 if the game is still on; return 1 if game
 * over and a player has won; return 0 if game over and nobody wins.
 */
int check_gameover(struct game_state *game) {
    if (game->guesses_left > 0 && strcmp(game->word, game->guess) != 0) {
        return -1;
    }
    else if (!strcmp(game->word, game->guess)) {
      return 1;
    }
    return 0;
}
