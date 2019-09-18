#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/* Reads a trace file produced by valgrind and an address marker file produced
 * by the program being traced. Outputs only the memory reference lines in
 * between the two markers
 */

int main(int argc, char **argv) {
    
    if(argc != 3) {
         fprintf(stderr, "Usage: %s tracefile markerfile\n", argv[0]);
         exit(1);
    }

    // Addresses should be stored in unsigned long variables
    // unsigned long start_marker, end_marker;



    /* For printing output, use this exact formatting string where the
     * first conversion is for the type of memory reference, and the second
     * is the address
     */
    // printf("%c,%#lx\n", VARIABLES TO PRINT GO HERE);
	FILE *fp;
	long unsigned start_marker;
	long unsigned end_marker;	
	fp = fopen(argv[2], "r");
	fscanf(fp, "%lx %lx", &start_marker, &end_marker);
	fclose(fp);
	
	fp = fopen(argv[1], "r");
	long address;
	char instruction;
	int size;
	
	while (address != start_marker) {
		fscanf(fp, "%c %lx,%d\n", &instruction, &address, &size);	
	}
	
	while (address != end_marker) {
		fscanf(fp, "%c %lx,%d\n", &instruction, &address, &size);
		if (address != end_marker) {
			printf("%c,%#lx\n", instruction, address);
		}
	}
	fclose(fp);

    return 0;
}
