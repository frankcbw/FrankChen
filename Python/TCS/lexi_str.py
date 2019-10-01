"""
Q1. Lexicographic String
"""
import sys
from collections import Counter

input_lines = sys.stdin.readlines()
n_test = int(input_lines[0].strip())

curr = 1
while n_test > 0:

    in_alpha = input_lines[curr].strip()
    in_str = input_lines[curr+1].strip()
    filtered_alpha = filter(lambda x: x in in_str, in_alpha)

    char_count = Counter(in_str)
    result = ""
    for c in filtered_alpha:
        result += c*char_count[c]
    print(result)
    curr += 2
    n_test -= 1
