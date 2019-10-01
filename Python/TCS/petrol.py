"""
Q3
"""

import sys

input_values = sys.stdin.read().strip().split(" ")
petrol_level = list(map(lambda x: int(x), input_values))
petrol_level.sort(reverse=True)
n_cars = len(petrol_level)
dp = [[0] * (sum(petrol_level)//2+1)] * (n_cars + 1)
