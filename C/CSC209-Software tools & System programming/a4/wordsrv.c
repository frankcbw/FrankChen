#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include <time.h>

#include "socket.h"
#include "gameplay.h"


#ifndef PORT
    #define PORT 55201
#endif
#define MAX_QUEUE 5


void add_player(struct client **top, int fd, struct in_addr addr);
int name_exist(struct client **top, char *name);
void activate_player(struct client **src, int fd, struct game_state *game);
void remove_player(struct client **top, int fd);

/* These are some of the function prototypes that we used in our solution
 * You are not required to write functions that match these prototypes, but
 * you may find the helpful when thinking about operations in your program.
 */
/* Send the message in outbuf to all clients */
void broadcast(struct game_state *game, char *outbuf);
void announce_turn(struct game_state *game);
void announce_winner(struct game_state *game, struct client *winner);
/* Move the has_next_turn pointer to the next active client */
void advance_turn(struct game_state *game);
void process_guess(struct game_state *game, char *guess);
int find_network_newline(const char *buf, int n);
int read_until_newline(char *msg, int fd);


/* The set of socket descriptors for select to monitor.
 * This is a global variable because we need to remove socket descriptors
 * from allset when a write to a socket fails.
 */
fd_set allset;


/* Add a client to the head of the linked list
 */
void add_player(struct client **top, int fd, struct in_addr addr) {
    struct client *p = malloc(sizeof(struct client));

    if (!p) {
        perror("malloc");
        exit(1);
    }

    printf("Adding client %s\n", inet_ntoa(addr));

    p->fd = fd;
    p->ipaddr = addr;
    p->name[0] = '\0';
    p->in_ptr = p->inbuf;
    p->inbuf[0] = '\0';
    p->next = *top;
    *top = p;
}


/* Determine if a name exists. Return 1 if exists and 0 otherwise. */
int name_exist(struct client **top, char *name) {
    for (struct client *i = *top; i != NULL; i = i->next) {
        if (strcmp(i->name, name) == 0) {
        return 1;
        }
    }
    return 0;
}


/* Activate the player of fd from the new player list src to the game.
 */
void activate_player(struct client **src, int fd, struct game_state *game) {
    struct client *p;
    struct client *temp;
    char msg[MAX_MSG];
    char *status = malloc(sizeof(char)*MAX_MSG);
    if (!status) {
        perror("malloc");
        exit(1);
    }

    for (p = *src; p->fd != fd && p->next->fd != fd; p = p->next);

    if (p->fd == fd) { // Want to activate p
        *src = p->next;
        p->next = game->head;
        if (!game->head) {
            game->has_next_turn = p;
        }
        game->head = p;
    }
    else { // Want to activate p->next
        temp = p->next;
        p->next = temp->next;
        temp->next = game->head;
        if (!game->head) {
            game->has_next_turn = temp;
        }
        game->head = temp;
    }
    sprintf(msg, "%s has just joined.\r\n", game->head->name);
    broadcast(game, msg);
    status_message(status, game);
    // show the status to the newly joined player
    if (write(game->head->fd, status, strlen(status)) == -1) {
        perror("write");
        exit(1);
    }

    free(status);
}


/* Removes client from the linked list and closes its socket.
 * Also removes socket descriptor from allset
 */
void remove_player(struct client **top, int fd) {
    struct client **p;

    for (p = top; *p && (*p)->fd != fd; p = &(*p)->next);
    // Now, p points to (1) top, or (2) a pointer to another client
    // This avoids a special case for removing the head of the list
    if (*p) {
        struct client *t = (*p)->next;
        printf("Removing client %d %s\n", fd, inet_ntoa((*p)->ipaddr));
        FD_CLR((*p)->fd, &allset);
        close((*p)->fd);
        free(*p);
        *p = t;
    } else {
        fprintf(stderr, "Trying to remove fd %d, but I don't know about it\n",
                 fd);
    }
}


/* Send the message in outbuf to all clients. */
void broadcast(struct game_state *game, char *outbuf) {
    for (struct client *p = game->head; p != NULL; p = p->next) {
        if (write(p->fd, outbuf, strlen(outbuf)) == -1) {
            perror("write");
            exit(1);
        }
    }
}


/* Announce whose turn currently is to other players and ask for a guess from
 * the current player.
 */
void announce_turn(struct game_state *game) {
    char msg[MAX_MSG];

    for (struct client *p = game->head; p != NULL; p = p->next) {
        if (p == game->has_next_turn) { // ask for a guess
            strncpy(msg, "Your guess?\r\n", MAX_MSG);
        }
        else { // tell other player whose turn is
            sprintf(msg, "It's %s's turn\r\n", game->has_next_turn->name);
        }
        if (write(p->fd, msg, strlen(msg)) == -1) {
            perror("write");
            exit(1);
        }
    }
}


/* Announce the winner using broadcast(). */
void announce_winner(struct game_state *game, struct client *winner) {
    char *win_msg = malloc(sizeof(char) * MAX_MSG);
    if (!win_msg) {
        perror("malloc");
        exit(1);
    }
    sprintf(win_msg, "The word was %s.\r\nGame Over! %s is the winner!\r\n",
    game->word, winner->name);
    broadcast(game, win_msg);
    free(win_msg);
}


/* Pass the turn to the next active player. */
void advance_turn(struct game_state *game) {
    struct client *p = game->has_next_turn;
    game->has_next_turn = (p->next == NULL) ? game->head : p->next;
}


/* Check if a valid guess is correct and update the game status. While game is
 * not over, the turn stays at the same player if correct. The turn is passed
 * onto the next player otherwise.
 */
void process_guess(struct game_state *game, char *guess) {
    char status[MAX_MSG];
    int is_correct = check_guess(game, guess); // check if the guess is correct
    status_message(status, game);
    broadcast(game, status);
    if (check_gameover(game) == -1) {
        if (!is_correct) { // guess is wrong and pass the turn
            advance_turn(game);
        }
        announce_turn(game); // keep the turn
    }
}


/*
 * Search the first n characters of buf for a network newline (\r\n).
 * Return one plus the index of the '\n' of the first network newline,
 * or -1 if no network newline is found.
 */
int find_network_newline(const char *buf, int n) {
    int i = 0;
    while (i+1 < n) {
        if (buf[i] == '\r' && buf[i+1] == '\n') {
            return i+2;
        }
        i++;
    }
    return -1;
}


/* Read onto msg until a newline is reached from fd. Return 1 if fd is closed.
 * O otherwsie.
 */
int read_until_newline(char *msg, int fd) {
    int inbuf = 0;
    int room = sizeof(msg);
    char *after = msg;

    int num_read;
    int found_newline = 0; //flag for breaking out of nested loop

    while((num_read = read(fd, after, room)) >= 0) {
        if (num_read == 0) { //fd is disconnected
          return 1;
        }
        inbuf += num_read;
        room -= num_read;
        after = msg + inbuf;

        int i_newline;
        while((i_newline = find_network_newline(msg, inbuf)) > 0) {
            // newline found
            msg[i_newline-2] = '\0';
            found_newline = 1;
            break;
        }
        if (found_newline) {break;}
    }
    return 0;
}


int main(int argc, char **argv) {
    int clientfd, maxfd, nready;
    struct client *p;
    struct sockaddr_in q;
    fd_set rset;

    if(argc != 2){
        fprintf(stderr,"Usage: %s <dictionary filename>\n", argv[0]);
        exit(1);
    }

    // Create and initialize the game state
    struct game_state game;

    srandom((unsigned int)time(NULL));
    // Set up the file pointer outside of init_game because we want to
    // just rewind the file when we need to pick a new word
    game.dict.fp = NULL;
    game.dict.size = get_file_length(argv[1]);

    init_game(&game, argv[1]);

    // head and has_next_turn also don't change when a subsequent game is
    // started so we initialize them here.
    game.head = NULL;
    game.has_next_turn = NULL;

    /* A list of client who have not yet entered their name.  This list is
     * kept separate from the list of active players in the game, because
     * until the new playrs have entered a name, they should not have a turn
     * or receive broadcast messages.  In other words, they can't play until
     * they have a name.
     */
    struct client *new_players = NULL;

    struct sockaddr_in *server = init_server_addr(PORT);
    int listenfd = set_up_server_socket(server, MAX_QUEUE);

    // initialize allset and add listenfd to the
    // set of file descriptors passed into select
    FD_ZERO(&allset);
    FD_SET(listenfd, &allset);
    // maxfd identifies how far into the set to search
    maxfd = listenfd;

    while (1) {
        // make a copy of the set before we pass it into select
        rset = allset;
        nready = select(maxfd + 1, &rset, NULL, NULL, NULL);
        if (nready == -1) {
            perror("select");
            continue;
        }

        if (FD_ISSET(listenfd, &rset)){
            printf("A new client is connecting\n");
            clientfd = accept_connection(listenfd);

            FD_SET(clientfd, &allset);
            if (clientfd > maxfd) {
                maxfd = clientfd;
            }
            printf("Connection from %s\n", inet_ntoa(q.sin_addr));
            add_player(&new_players, clientfd, q.sin_addr);
            char *greeting = WELCOME_MSG;
            if(write(clientfd, greeting, strlen(greeting)) == -1) {
                fprintf(stderr, "Write to client %s failed\n", inet_ntoa(q.sin_addr));
                remove_player(&new_players, p->fd);
            };
        }

        /* Check which other socket descriptors have something ready to read.
         * The reason we iterate over the rset descriptors at the top level and
         * search through the two lists of clients each time is that it is
         * possible that a client will be removed in the middle of one of the
         * operations. This is also why we call break after handling the input.
         * If a client has been removed the loop variables may not longer be
         * valid.
         */
        int cur_fd;
        for(cur_fd = 0; cur_fd <= maxfd; cur_fd++) {
            if(FD_ISSET(cur_fd, &rset)) {
                // Check if this socket descriptor is an active player

                for(p = game.head; p != NULL; p = p->next) {
                    if(cur_fd == p->fd) {
                        //TODO - handle input from an active client
                        char *guess = malloc(sizeof(char) * MAX_MSG);
                        if (!guess) {
                            perror("malloc");
                            exit(1);
                        }
                        int if_closed = read_until_newline(guess, p->fd);
                        if (if_closed) { //p is disconnected
                            fprintf(stderr, "%s is disconnected\n", p->name);
                            char *quit_msg = malloc(sizeof(char) * MAX_MSG);
                            if (!quit_msg) {
                                perror("malloc");
                                exit(1);
                            }
                            char *status_msg = malloc(sizeof(char) * MAX_MSG);
                            if (!status_msg) {
                                perror("malloc");
                                exit(1);
                            }
                            sprintf(quit_msg, "%s has left\r\n", p->name);
                            if (p == game.has_next_turn) { //process the turn
                                advance_turn(&game);
                                status_message(status_msg, &game);
                                remove_player(&game.head, p->fd);
                                broadcast(&game, quit_msg);
                                broadcast(&game, status_msg);
                            } else {
                                remove_player(&game.head, p->fd);
                                broadcast(&game, quit_msg);
                            }
                            announce_turn(&game);
                            free(quit_msg);
                            free(status_msg);
                            break;
                        }

                        if (p == game.has_next_turn) { // p's turn
                            if (strlen(guess) == 1 && strcmp(guess, "a") >= 0
                                && strcmp(guess, "z") <= 0) { //valid guess
                                char *guess_msg = malloc(sizeof(char) * MAX_MSG);
                                if (!guess_msg) {
                                    perror("malloc");
                                    exit(1);
                                }
                                sprintf(guess_msg, "%s guesses: %s\r\n", p->name, guess);
                                broadcast(&game, guess_msg);
                                printf("%s has guessed: %s\n", p->name, guess);
                                process_guess(&game, guess);
                                free(guess_msg);

                                int is_over;
                                if ((is_over = check_gameover(&game)) != -1) {
                                    // game over
                                    if (is_over == 1) { //some player is the winner
                                        announce_winner(&game, p);
                                    } else { // nobody wins
                                        char *game_over = GAMEOVER_MSG;
                                        broadcast(&game, game_over);
                                        advance_turn(&game);
                                    }
                                    char *start_over = STARTOVER_MSG;
                                    broadcast(&game, start_over);
                                    printf("Game Over\n");
                                    // start a new game
                                    init_game(&game, argv[1]); // restart a new game

                                    char *game_status = malloc(sizeof(char) * MAX_MSG);
                                    if (!game_status) {
                                        perror("malloc");
                                        exit(1);
                                    }
                                    status_message(game_status, &game);
                                    broadcast(&game, game_status);
                                    announce_turn(&game);
                                    printf("New Game started\n");
                                }
                            }
                            else { //invalid guess
                                char *warn = "It's not a valid guess. Try again.\r\n";
                                if(write(p->fd, warn, strlen(warn)) == -1) {
                                    fprintf(stderr, "Write to client %s failed\n", inet_ntoa(q.sin_addr));
                                    remove_player(&game.head, p->fd);
                                }
                            }
                            free(guess);
                        } else { // not p's turn
                            char *warn = "It's not your turn.\r\n";
                            if(write(p->fd, warn, strlen(warn)) == -1) {
                                fprintf(stderr, "Write to client %s failed\n", inet_ntoa(q.sin_addr));
                                remove_player(&game.head, p->fd);
                            }
                        }
                        break;
                    }
                }

                // Check if any new players are entering their names
                for(p = new_players; p != NULL; p = p->next) {
                    if(cur_fd == p->fd) {
                        // TODO - handle input from an new client who has
                        // not entered an acceptable name.
                        char *name = malloc(sizeof(char) * MAX_NAME);
                        if (!name) {
                            perror("malloc");
                            exit(1);
                        }
                        int if_closed = read_until_newline(name, p->fd);
                        if (if_closed) { // p disconnected
                            fprintf(stderr, "Client %s is disconnected\n", inet_ntoa(q.sin_addr));
                            remove_player(&new_players, p->fd);
                        }

                        if (name_exist(&game.head, name)) { //name already exists
                            char *msg = NAME_EXIST_MSG;
                            if(write(p->fd, msg, strlen(msg)) == -1) {
                                fprintf(stderr, "Write to client %s failed\n", inet_ntoa(q.sin_addr));
                                remove_player(&new_players, p->fd);
                            }
                        } else { //name registered succussfully
                          strncpy(p->name, name, MAX_NAME);
                          activate_player(&new_players, p->fd, &game);
                          printf("%s has joined the game.\n", p->name);
                          announce_turn(&game);
                        }

                        free(name);
                        break;
                    }
                }
            }
        }
    }
    return 0;
}
