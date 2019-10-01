"""
Q2.
"""

import sys

input_lines = sys.stdin.readlines()
n_bottle = int(input_lines[0].strip())
a_radius = input_lines[1].strip().split(" ")
for i in range(n_bottle):
    a_radius[i] = int(a_radius[i])
a_radius.sort()
a_open = [1] * n_bottle


def fits(a, i, j, a_open_lst):
    """
    Fit the ith bottle into the jth. Return true if it works and false otherwise.
    """
    if a[i] < a[j] and a_open_lst[j]:
        a_open_lst[j] = 0
        a.pop(i)
        a_open_lst.pop(i)
        return True
    return False


curr = 0
curr_next = 1

while curr < len(a_radius) and curr_next < len(a_radius):
    if not fits(a_radius, curr, curr_next, a_open):
        curr_next += 1
        if curr_next > len(a_radius):
            curr += 1
            curr_next = curr + 1

print(len(a_radius))
