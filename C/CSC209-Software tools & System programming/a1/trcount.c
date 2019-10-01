#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// Constants that determine that address ranges of different memory regions

#define GLOBALS_START 0x400000
#define GLOBALS_END   0x700000
#define HEAP_START   0x4000000
#define HEAP_END     0x8000000
#define STACK_START 0xfff000000

int main(int argc, char **argv) {
    
    FILE *fp = NULL;

    if(argc == 1) {
        fp = stdin;

    } else if(argc == 2) {
        fp = fopen(argv[1], "r");
        if(fp == NULL){
            perror("fopen");
            exit(1);
        }
    } else {
        fprintf(stderr, "Usage: %s [tracefile]\n", argv[0]);
        exit(1);
    }

    /* Complete the implementation */


    /* Use these print statements to print the ouput. It is important that 
     * the output match precisely for testing purposes.
     * Fill in the relevant variables in each print statement.
     * The print statements are commented out so that the program compiles.  
     * Uncomment them as you get each piece working.
     */
    /*
    printf("Reference Counts by Type:\n");
    printf("    Instructions: %d\n", );
    printf("    Modifications: %d\n", );
    printf("    Loads: %d\n", );
    printf("    Stores: %d\n", );
    printf("Data Reference Counts by Location:\n");
    printf("    Globals: %d\n", );
    printf("    Heap: %d\n", );
    printf("    Stack: %d\n", );
    */
	long unsigned address;
	char instruction;
	int i_count = 0;
	int m_count = 0;
	int l_count = 0;
	int s_count = 0;	
	int global_count = 0;
	int heap_count = 0;
	int stack_count = 0;
	
	while (fscanf(fp, "%c,%lx\n", &instruction, &address) != EOF) {
		if(instruction == 'I') {
			i_count += 1;
		}
		else if (instruction == 'M') {
			m_count += 1;		
		}
		else if (instruction == 'L') {
			l_count += 1;		
		}
		else {
			s_count += 1;
		}
		if (instruction != 'I' && address >= GLOBALS_START && address <= GLOBALS_END) {
			global_count += 1;		
		}
		else if (instruction != 'I' && address >= HEAP_START && address <= HEAP_END) {
			heap_count += 1;		
		}
		else if (instruction != 'I' && address >= STACK_START) {
			stack_count +=1;				
		}
	}
	fclose(fp);
	printf("Reference Counts by Type:\n");
    	printf("    Instructions: %d\n", i_count);
    	printf("    Modifications: %d\n", m_count);
    	printf("    Loads: %d\n", l_count);
    	printf("    Stores: %d\n", s_count);
    	printf("Data Reference Counts by Location:\n");
    	printf("    Globals: %d\n", global_count);
    	printf("    Heap: %d\n", heap_count);
    	printf("    Stack: %d\n", stack_count);
	

    return 0;
}
