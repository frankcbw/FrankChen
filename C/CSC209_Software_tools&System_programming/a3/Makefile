FLAGS = -Wall -g -std=gnu99
DEPENDENCIES = helper.h

all: mkwords psort sb

mkwords: helper.o helper.h
	gcc ${FLAGS} -o $@ $^ mkwords.c -lm

psort: helper.o psort.o
	gcc ${FLAGS} -o $@ $^

sb: helper.o sb.o
	gcc ${FLAGS} -o $@ $^

%.o: %.c ${DEPENDENCIES}
	gcc ${FLAGS} -c $<

clean:
	rm -f *.o  mkwords psort sb
