"""
Q5.
"""

import sys

input_lines = sys.stdin.readlines()

n_test = int(input_lines[0].strip())
curr = 1


def change_position(pos, n, pattern_lst):
    """
    Change positions in the n-monkey-list pos based on pattern_lst, and return the new position list after change.
    """
    pos_ = pos[::]
    for i in range(n):
        pos_[pattern_lst[i]] = pos[i]
    return pos_


while n_test > 0:
    n_monkey = int(input_lines[curr].strip())
    pattern = list(map(lambda x: int(x) - 1, input_lines[curr+1].strip().split()))
    pos_lst = list(range(n_monkey))
    if pattern == pos_lst:
        print(0)
    else:
        new_pos = change_position(pos_lst, n_monkey, pattern)
        count = 1
        while new_pos != pos_lst:
            new_pos = change_position(new_pos, n_monkey, pattern)
            count += 1
        print(count)
    n_test -= 1
    curr += 2
