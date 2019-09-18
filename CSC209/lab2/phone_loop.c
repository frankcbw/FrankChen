#include <stdio.h>
int main() {
  char phone[11];
  int num;
  int to_return = 0;

  scanf("%s", phone);

  while (scanf("%d", &num) != EOF) {

  	if (num == -1) {
    	printf("%s\n", phone);
  	}
  	else if (num >= 0 && num <= 9) {
    	printf("%c\n", phone[num]);
  	}
  	else {
    	printf("ERROR\n");
    	to_return = 1;
  	}
  }
return to_return;
}
