#include <stdio.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include "helper.h"


/* The sort function to be called by each child process. Return a pointer to a
sorted list of records.*/
struct rec *sort(char *file, int offset, int size) {

  struct rec *sorted = malloc(sizeof(struct rec ) * size);

  FILE * f = fopen(file, "rb");
  if (f == NULL) {
    perror("fopen");
    exit(1);
  }

  if (fseek(f, offset, SEEK_SET) != 0) {
    perror("fseek");
    exit(1);
  }
  
  int i = 0;
  struct rec *curr = malloc(sizeof(struct rec));

  while (i < size) {
    int n = fread(curr, sizeof(struct rec), 1, f);
    if (n != 1) {
      perror("fread");
      exit(1);
    }
    sorted[i].freq = curr->freq;
    strcpy(sorted[i].word, curr->word);
    i++;
  }
  fclose(f);
  free(curr);
  qsort(sorted, size, sizeof(struct rec), compare_freq);

  return sorted;
}

int main(int argc, char ** argv) {
    int op = 0, n_process = 0, n_record = 0;
    char f_in[100];
    FILE *f_out = NULL;

    if (argc != 7) {
      fprintf(stderr, "Usage: psort -n <number of processes> -f <inputfile> -o <outputfile>\n");
      exit(1);
    }

    while ((op = getopt(argc, argv, "n:f:o:")) != -1) {
        switch (op) {
            case 'n': n_process = strtol(optarg, NULL, 10);
                break;
            case 'f':
                strcpy(f_in, optarg);
                n_record = get_file_size(optarg) / sizeof(struct rec);
                break;
            case 'o':
                f_out = fopen(optarg, "wb");
                if (f_out == NULL) {
                  perror("fopen");
                  exit(1);
                }
                break;
            default:
                fprintf(stderr, "Usage: psort -n <number of processes> -f <inputfile> -o <outputfile>\n");
                exit(1);

      }
  }

  if (n_process > n_record) {
    n_process = n_record;
  }

  int pipe_fd[n_process][2];
  struct rec *result = NULL;

  for (int i = 0; i < n_process; i++) {
      if (pipe(pipe_fd[i]) == -1) {
        perror("pipe");
      }
      int fork_status = fork();
      if (fork_status < 0) {
        perror("fork");
        exit(1);
      }
      /* Child process: perform sorting on a chunck of the file */
      else if (fork_status == 0) {
        int n_rec_written = n_record/n_process;
        int offset = 0;

        /* Close the read end of child pipe pipe_fd[i] */
        if (close(pipe_fd[i][0]) == -1) {
          perror("close reading end from child process");
          exit(1);
        }

        if (i < n_record%n_process) {
          n_rec_written++;
          offset = i * n_rec_written;
        }
        else {
          offset = n_record%n_process * (n_rec_written + 1)
          + (i - n_record%n_process) * n_rec_written;
        }

        result = sort(f_in, offset*sizeof(struct rec), n_rec_written);

        if (write(pipe_fd[i][1], result, sizeof(struct rec)*n_rec_written) == -1) {
          perror("write from child to pipe");
          exit(1);
        }

        if (close(pipe_fd[i][1]) == -1) {
            perror("close pipe after writing");
            exit(1);
        }
        exit(0);
      }

      /* Parent process: close the writing end */
      else {
        if (close(pipe_fd[i][1]) == -1) {
          perror("close writing end in the parent");
          exit(1);
        }
      }
  }

  /* Only parent gets here */
  int status;
  int child_pid;
  // if ((child_pid = wait(&status)) == -1) {
  //   perror("wait");
  //   exit(1);
  // }
  while ((child_pid = wait(&status)) > 0) {
    if (WIFEXITED(status)) {
      int exit_status = WEXITSTATUS(status);
      if (exit_status == 1) {
        fprintf(stderr, "Child terminated abnormally\n");
      }
    }
  }
  // else if (WIFEXITED(status)) {
  //   int exit_status = WEXITSTATUS(status);
  //   if (exit_status == 1) {
  //     fprintf(stderr, "Child terminated abnormally\n");
  //   }
  // }

  //Perform the merge
  int record_remaining = n_record;
  int min_index = 0;
  struct rec records[n_process];

  for (int i = 0; i < n_process; i++) {
    if (read(pipe_fd[i][0], &records[i], sizeof(struct rec)) == -1) {
      perror("read");
      exit(1);
    }
  }

  while (record_remaining > 0) {
    // find the smallest index
    for (int i = 0; i < n_process; i++) {
      if (records[i].freq < records[min_index].freq) {
        min_index = i;
      }
    }
    //
    if (fwrite(&records[min_index], sizeof(struct rec), 1, f_out) != 1) {
      perror("fwrite");
      exit(1);
    }
    //load a new record from that pipe
    int n;
    if ((n = read(pipe_fd[min_index][0], &records[min_index], sizeof(struct rec))) == -1) {
          perror("reading from pipes in parent");
          exit(1);
    }
    if (n == 0) { //the pipe reaches its end
      struct rec temp_max;
      temp_max.freq = 10000000;
      strcpy(temp_max.word, "Max_value");
      records[min_index] = temp_max;
    }
    record_remaining--;
  }

  fclose(f_out);
  free(result);

  return 0;
}
