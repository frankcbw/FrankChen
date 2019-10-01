#Look for #IMPLEMENT tags in this file.
'''
All models need to return a CSP object, and a list of lists of Variable objects
representing the board. The returned list of lists is used to access the
solution.

For example, after these three lines of code

    csp, var_array = kenken_csp_model(board)
    solver = BT(csp)
    solver.bt_search(prop_FC, var_ord)

var_array[0][0].get_assigned_value() should be the correct value in the top left
cell of the KenKen puzzle.

The grid-only models do not need to encode the cage constraints.

1. binary_ne_grid (worth 10/100 marks)
    - A model of a KenKen grid (without cage constraints) built using only
      binary not-equal constraints for both the row and column constraints.

2. nary_ad_grid (worth 10/100 marks)
    - A model of a KenKen grid (without cage constraints) built using only n-ary
      all-different constraints for both the row and column constraints.

3. kenken_csp_model (worth 20/100 marks)
    - A model built using your choice of (1) binary binary not-equal, or (2)
      n-ary all-different constraints for the grid.
    - Together with KenKen cage constraints.

'''
from cspbase import *
from propagators import *
from heuristics import *
import itertools
import numpy

def set_up_csp_vars(kenken_grid, name):
    size = kenken_grid[0][0]
    vars = []
    var_array = [[] for _ in range(size)]

    for i in range(size):
        for j in range(size):
            name = "{}{}".format(i + 1, j + 1)
            domain = list(range(1, size + 1))
            var = Variable(name, domain)
            vars.append(var)
            var_array[i].append(var)

    csp = CSP(name, vars)

    return csp, var_array

def binary_ne_grid(kenken_grid):
    csp, var_array = set_up_csp_vars(kenken_grid, 'binary_ne')
    size = len(var_array)

    cons_combinations = list(itertools.combinations(range(size), 2))
    vals_permutations = list(itertools.permutations(range(1, size+1), 2))
    count = 1

    for i in range(size):
        row = var_array[i]
        col = [var_array[j][i] for j in range(size)]
        for x, y in cons_combinations:
            con_row = Constraint('c{}'.format(count), [row[x], row[y]])
            count += 1
            con_col = Constraint('c{}'.format(count), [col[x], col[y]])
            count += 1

            con_row.add_satisfying_tuples(vals_permutations)
            con_col.add_satisfying_tuples(vals_permutations)

            csp.add_constraint(con_row)
            csp.add_constraint(con_col)

    return csp, var_array

def nary_ad_grid(kenken_grid):
    csp, var_array = set_up_csp_vars(kenken_grid, 'nary_ad')
    size = len(var_array)

    count = 1
    vals_permutations = list(itertools.permutations(range(1, size+1), size))

    for i in range(size):
        row = var_array[i]
        col = [var_array[j][i] for j in range(size)]

        con_row = Constraint('c{}'.format(count), row)
        count += 1
        con_col = Constraint('c{}'.format(count), col)
        count += 1

        con_row.add_satisfying_tuples(vals_permutations)
        con_col.add_satisfying_tuples(vals_permutations)

        csp.add_constraint(con_row)
        csp.add_constraint(con_col)

    return csp, var_array

def add(vals):
    return sum(vals)

def subtract(vals):
    return vals[0] - sum(vals[1:])

def multiply(vals):
    return numpy.prod(vals)

def divide(vals):
    return vals[0] / numpy.prod(vals[1:])

def parse_con(con_info, var_array, name):
    """
    The function takes a list of required infos for a constraint and return a Constraint object
    Corresponding operations: {0: +, 1: -, 2: /, 3: *}

    """

    if len(con_info) == 2:
        pos = str(con_info[0])
        row, col = int(pos[:len(pos)//2]) - 1, int(pos[len(pos)//2:]) - 1
        var = var_array[row][col]
        con = Constraint(name, [var])
        con.add_satisfying_tuples([(con_info[1],)])
        return con
    else:
        op_dict = {0: add, 1: subtract, 2: divide, 3: multiply}
        operation = con_info.pop()
        goal_val = con_info.pop()
        scope = []

        for pos in con_info:
            pos = str(pos)
            row, col = int(pos[:len(pos)//2]) - 1, int(pos[len(pos)//2:]) - 1
            scope.append(var_array[row][col])

        con = Constraint(name, scope)
        possible_combinations = itertools.product(*[x.cur_domain() for x in scope])

        sat_tuples = []
        if operation in (0, 3):
            sat_tuples = [tuple(x) for x in possible_combinations if op_dict[operation](x) == goal_val]

        else:
            for x in possible_combinations:
                for p in itertools.permutations(x, len(x)):
                    if op_dict[operation](p) == goal_val:
                        sat_tuples.append(tuple(x))
                        break

        con.add_satisfying_tuples(sat_tuples)
        return con


def kenken_csp_model(kenken_grid):
    csp, var_array = binary_ne_grid(kenken_grid)

    count = len(csp.get_all_cons())+1
    for con_info in kenken_grid[1:]:
        con = parse_con(con_info, var_array, 'c{}'.format(count))
        count += 1
        csp.add_constraint(con)

    return csp, var_array


if __name__ == '__main__':
    csp, val_array = binary_ne_grid([[7]])

    solver = BT(csp)

    solver.bt_search(prop_FC, ord_mrv, val_lcv)
