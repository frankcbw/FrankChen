"""
Q6.
"""

import sys
from math import factorial
from math import gcd

input_lines = sys.stdin.readlines()

n_test = int(input_lines[0].strip())
curr = 1


def combination(a, b):
    """
    Return the number of combinations by selecting a out of b without replacement.
    """
    return factorial(b)/factorial(a)/factorial(b-a)


def mul_inv(a, m):
    """
    Return the modular multiplicative inverse of a under m.
    """
    b = m//a
    for x in range(b, m):
        if (a * x) % m == 1:
            return x
    return 1


while n_test > 0:
    input_data = list(map(lambda x: int(x), input_lines[curr].strip().split()))
    total_q, test_q, practice_q = input_data[0], input_data[1], input_data[2]
    total_test = combination(test_q, total_q)
    total_test_without_practice = combination(test_q, total_q - practice_q)
    prob_up = int(total_test - total_test_without_practice)
    prob_down = int(total_test)
    gcd_ = gcd(prob_up, prob_down)
    if gcd_ != 1:
        prob_up //= gcd_
        prob_down //= gcd_
    print(prob_up * mul_inv(prob_down, 1000000007))
    n_test -= 1
    curr += 1
