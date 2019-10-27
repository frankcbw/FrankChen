#Look for #IMPLEMENT tags in this file. These tags indicate what has
#to be implemented.

import random
'''
This file will contain different variable ordering heuristics to be used within
bt_search.

var_ordering == a function with the following template
    var_ordering(csp)
        ==> returns Variable 

    csp is a CSP object---the heuristic can use this to get access to the
    variables and constraints of the problem. The assigned variables can be
    accessed via methods, the values assigned can also be accessed.

    var_ordering returns the next Variable to be assigned, as per the definition
    of the heuristic it implements.

val_ordering == a function with the following template
    val_ordering(csp,var)
        ==> returns [Value, Value, Value...]
    
    csp is a CSP object, var is a Variable object; the heuristic can use csp to access the constraints of the problem, and use var to access var's potential values. 

    val_ordering returns a list of all var's potential values, ordered from best value choice to worst value choice according to the heuristic.

'''

def ord_mrv(csp):
    vars_unassigned = csp.get_all_unasgn_vars()
    vars_unassigned.sort(key=lambda x:len(x.cur_domain()))
    return vars_unassigned[0]

def val_lcv(csp,var):
    vals = var.cur_domain()[:]
    val_n_pruned = {x: 0 for x in vals}
    for val in var.cur_domain():
        var.assign(val)
        for con in csp.get_cons_with_var(var):
            if con.get_n_unasgn() == 1:
                to_check = [x.get_assigned_value() for x in con.get_scope()]
                unassign = to_check.index(None)
                for possible_val in con.get_scope()[unassign].cur_domain():
                    to_check[unassign] = possible_val
                    if not con.check(to_check):
                        val_n_pruned[val] += 1
        var.unassign()
    vals.sort(key=lambda x: val_n_pruned[x])
    return vals




