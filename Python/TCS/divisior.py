"""
Q4
"""

import sys
import math

input_lines = sys.stdin.readlines()
n_test = int(input_lines[0].strip())

curr = 1
while n_test > 0:
    result = []
    num = int(input_lines[curr].strip())
    for i in range(1, int(math.sqrt(num)+1)):
        if num % i == 0:
            print(i, end=" ")
            if num / i != i:
                result = [str(int(num/i))] + result
    print(" ".join(result))
    n_test -= 1
    curr += 1
