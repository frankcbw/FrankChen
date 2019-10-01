#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define MAXLINE 256
#define MAX_PASSWORD 10

#define SUCCESS "Password verified\n"
#define INVALID "Invalid password\n"
#define NO_USER "No such user\n"

int main(void) {
  char user_id[MAXLINE];
  char password[MAXLINE];
  int fd[2];

  if (pipe(fd) == -1) {
    perror("pipe");
  }

  if(fgets(user_id, MAXLINE, stdin) == NULL) {
      perror("fgets");
      exit(1);
  }
  if(fgets(password, MAXLINE, stdin) == NULL) {
      perror("fgets");
      exit(1);
  }

  int n = fork();
  if (n < 0) {
    perror("fork");
  }

  // child process
  else if (n == 0) {
    close(fd[1]);
    dup2(fd[0], STDIN_FILENO);
    execl("./validate", "validate", NULL);
  }

  // parent process
  else {
    close(fd[0]);
    if (write(fd[1], user_id, MAX_PASSWORD) == -1) {
      perror("write");
    }
    if (write(fd[1], password, MAX_PASSWORD) == -1) {
      perror("write");
    }
    int status;
    if (wait(&status) == -1) {
      perror("wait");
    }
    else {
      if (WIFEXITED(status)) {
        int exit_status = WEXITSTATUS(status);
        if (exit_status == 0) {
          printf(SUCCESS);
        }
        else if (exit_status == 2) {
          printf(INVALID);
        }
        else if (exit_status == 3) {
          printf(NO_USER);
        }
      }
    }
  }
  return 0;
}
