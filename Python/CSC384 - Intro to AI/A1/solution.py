# Look for #IMPLEMENT tags in this file. These tags indicate what has
# to be implemented to complete the LunarLockout  domain.

#   You may add only standard python imports---i.e., ones that are automatically
#   available on TEACH.CS
#   You may not remove any imports.
#   You may not import or otherwise source any of your own files

# import os for time functions
from search import *  # for search engines
from lunarlockout import LunarLockoutState, Direction, \
    lockout_goal_state  # for LunarLockout specific classes and problems


# LunarLockout HEURISTICS
def heur_trivial(state):
    '''trivial admissible LunarLockout heuristic'''
    '''INPUT: a LunarLockout state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    return 0


def heur_manhattan_distance(state):
    # OPTIONAL
    '''Manhattan distance LunarLockout heuristic'''
    '''INPUT: a lunar lockout state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    # Write a heuristic function that uses Manhattan distance to estimate distance between the current state and the goal.
    # Your function should return a sum of the Manhattan distances between each xanadu and the escape hatch.
    return 0


def heur_L_distance(state):
    # IMPLEMENT
    '''L distance LunarLockout heuristic'''
    '''INPUT: a lunar lockout state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    # Write a heuristic function that uses mahnattan distance to estimate distance between the current state and the goal.
    # Your function should return a sum of the L distances between each xanadu and the escape hatch.

    escape = state.width // 2
    total = 0

    for rover in state.xanadus:
      if rover[0] == escape or rover[1] == escape:
        total += 1
      else:
        total += 2

    return total

def is_solvable(state):
    """ Briefly Chech if the state is solvable.

        @param LunarLockoutState state: a state to be checked
        @rtype: bool
    """


    for rover in state.xanadus:

      result = [0, 0, 0, 0]

      for robot in state.robots:

        # Check if there is any robot at the same row or column with a xanadu
        if robot[0] == rover[0] or robot[1] == rover[1]:
          return True

        # Check if there is any robot at the top left of
        if robot[0] < rover[0] and robot[1] < rover[1]:
          result[0] = 1
        elif robot[0] < rover[0]:
          result[1] = 1
        elif robot[1] < rover[1]:
          result[2] = 1
        else:
          result[3] = 1

      if result.count(1) == 1:
        return False
      elif result in ([1, 0, 0, 1], [0, 1, 1, 0]):
        return False

    return True


def find_path(rover, escape):
    """
        @param tuple rover: a tuple represents a xanadu on the board
        @param int escape: an int represents the index of the escape hatch

        Returns a list of lists that represent two manhattan paths by which the rover can reach the escape hatch.
    """

    path = []
    path1 = []
    path2 = []

    if rover[0] < escape and rover[1] < escape:
        for i in range(rover[0], escape + 1):
            path1.append((i, rover[1]))
        for j in range(rover[1] + 1, escape + 1):
            path1.append((escape, j))
        for point in path1:
            path2.append(point[::-1])
    elif rover[1] < escape:
        for i in range(rover[0], escape - 1, -1):
            path1.append((i, rover[1]))
        for j in range(rover[1] + 1, escape + 1):
            path1.append((escape, j))
        for j in range(rover[1], escape + 1):
            path2.append((rover[0], j))
        for i in range(rover[0] - 1, escape - 1, -1):
            path2.append((i, escape))
    elif rover[0] < escape:
        for i in range(rover[0], escape + 1):
            path1.append((i, rover[1]))
        for j in range(rover[1] - 1, escape - 1, -1):
            path1.append((escape, j))
        for j in range(rover[1], escape - 1, -1):
            path2.append((rover[0], j))
        for i in range(rover[0] + 1, escape + 1):
            path2.append((escape, i))
    else:
        for i in range(rover[0], escape - 1, -1):
            path1.append((i, rover[1]))
        for j in range(rover[1] - 1, escape - 1, -1):
            path1.append((escape, j))
        for point in path1:
            path2.append(point[::-1])

    path.append(path1)
    path.append(path2)
    return path


def heur_alternate(state):
    # IMPLEMENT
    '''a better lunar lockout heuristic'''
    '''INPUT: a lunar lockout state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    # Your function should return a numeric value for the estimate of the distance to the goal.

    # Explanation: The alternate heuristic is improved based on the l-distance heuristic function. It first set a base
    # score using the heur_L_distance() function and loop through each robot for each rover to check if there is any
    # robot in the paths between the rover and the hatch. If the rover is at the same row or column with the hatch, it
    # simply counts any robot between the rover and the hatch, then add one to the base score if a robot is found as it
    # takes one extra move to remove the obstacle.
    # Otherwise, it uses a helper function to generate two paths (manhattan distance) for the rover to reach the hatch,
    # and count the number of robots on each path. It adds the greater number onto the base score (extra moves needed to
    # clean the path.

    score = heur_L_distance(state)
    escape = state.width // 2

    if not is_solvable(state):
      return float("inf")

    for rover in state.xanadus:
        if rover[0] == escape:
            for robot in state.robots:
                if rover[1] < robot[1] <= escape or rover[1] > robot[1] >= escape:
                    score += 1
        elif rover[1] == escape:
            for robot in state.robots:
                if rover[0] < robot[0] <= escape or rover[0] > robot[0] >= escape:
                    score += 1
        else:
            path = find_path(rover, escape)

            result1 = len([1 for robot in state.robots if robot in path[0]])
            result2 = len([1 for robot in state.robots if robot in path[1]])

            score += max(result1, result2)

    return score


def fval_function(sN, weight):
    # IMPLEMENT
    """
    Provide a custom formula for f-value computation for Anytime Weighted A star.
    Returns the fval of the state contained in the sNode.

    @param sNode sN: A search node (containing a LunarLockoutState)
    @param float weight: Weight given by Anytime Weighted A star
    @rtype: float
    """

    # Many searches will explore nodes (or states) that are ordered by their f-value.
    # For UCS, the fvalue is the same as the gval of the state. For best-first search, the fvalue is the hval of the state.
    # You can use this function to create an alternate f-value for states; this must be a function of the state and the weight.
    # The function must return a numeric f-value.
    # The value will determine your state's position on the Frontier list during a 'custom' search.
    # You must initialize your search engine object as a 'custom' search engine if you supply a custom fval function.
    return sN.gval + weight * sN.hval


def anytime_weighted_astar(initial_state, heur_fn, weight=4., timebound=2):
    # IMPLEMENT
    '''Provides an implementation of anytime weighted a-star, as described in the HW1 handout'''
    '''INPUT: a lunar lockout state that represents the start state and a timebound (number of seconds)'''
    '''OUTPUT: A goal state (if a goal is found), else False'''
    '''implementation of weighted astar algorithm'''

    search_engine = SearchEngine("custom", "full")
    search_engine.init_search(initial_state, lockout_goal_state, heur_fn,
                              fval_function=(lambda sN: fval_function(sN, weight)))

    start_time = os.times()[0]
    goal = search_engine.search(timebound)


    if goal:
        costbound = goal.gval + heur_fn(goal)
        best = goal
        remaining_time = timebound - (os.times()[0] - start_time)

        while remaining_time > 0 and weight > 1:
            weight /= 1.5
            search_engine.init_search(initial_state, lockout_goal_state, heur_fn,
                                      fval_function=(lambda sN: fval_function(sN, weight)))
            start_time = os.times()[0]
            new_goal = search_engine.search(remaining_time, costbound = (float("inf"), float("inf"), costbound))
            remaining_time -= (os.times()[0] - start_time)

            if new_goal:
                costbound = new_goal.gval + heur_fn(new_goal)
                best = new_goal

        return best

    return False




def anytime_gbfs(initial_state, heur_fn, timebound=2):
    # OPTIONAL
    '''Provides an implementation of anytime greedy best-first search.  This iteratively uses greedy best first search,'''
    '''At each iteration, however, a cost bound is enforced.  At each iteration the cost of the current "best" solution'''
    '''is used to set the cost bound for the next iteration.  Only paths within the cost bound are considered at each iteration.'''
    '''INPUT: a lunar lockout state that represents the start state and a timebound (number of seconds)'''
    '''OUTPUT: A goal state (if a goal is found), else False'''
    return 0


PROBLEMS = (
  #5x5 boards: all are solveable
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((0, 1),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((0, 2),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((0, 3),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((1, 1),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((1, 2),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((1, 3),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((1, 4),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((2, 0),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((2, 1),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (0, 2),(0,4),(2,0),(4,0)),((4, 4),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((4, 0),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((4, 1),)),
  LunarLockoutState("START", 0, None, 5, ((0, 0), (1, 0),(2,2),(4,2),(0,4),(4,4)),((4, 3),)),
  #7x7 BOARDS: all are solveable
  LunarLockoutState("START", 0, None, 7, ((4, 2), (1, 3), (6,3), (5,4)), ((6, 2),)),
  LunarLockoutState("START", 0, None, 7, ((2, 1), (4, 2), (2,6)), ((4, 6),)),
  LunarLockoutState("START", 0, None, 7, ((2, 1), (3, 1), (4, 1), (2,6), (4,6)), ((2, 0),(3, 0),(4, 0))),
  LunarLockoutState("START", 0, None, 7, ((1, 2), (0 ,2), (2 ,3), (4, 4), (2, 5)), ((2, 4),(3, 1),(4, 0))),
  LunarLockoutState("START", 0, None, 7, ((3, 2), (0 ,2), (3 ,3), (4, 4), (2, 5)), ((1, 2),(3, 0),(4, 0))),
  LunarLockoutState("START", 0, None, 7, ((3, 1), (0 ,2), (3 ,3), (4, 4), (2, 5)), ((1, 2),(3, 0),(4, 0))),
  LunarLockoutState("START", 0, None, 7, ((2, 1), (0 ,2), (1 ,2), (6, 4), (2, 5)), ((2, 0),(3, 0),(4, 0))),
  )

if __name__ == "__main__":



    #TEST CODE
    solved = 0; unsolved = []; counter = 0; percent = 0; timebound = 2; #2 second time limit for each problem
    print("*************************************")
    print("Running A-star")

    for i in range(len(PROBLEMS)): #note that there are 40 problems in the set that has been provided.  We just run through 10 here for illustration.

      print("*************************************")
      print("PROBLEM {}".format(i))

      s0 = PROBLEMS[i] #Problems will get harder as i gets bigger

      print("*******RUNNING A STAR*******")
      se = SearchEngine('astar', 'full')
      se.init_search(s0, lockout_goal_state, heur_alternate)
      final = se.search(timebound)

      if final:
        final.print_path()
        solved += 1
      else:
        unsolved.append(i)
      counter += 1

    if counter > 0:
      percent = (solved/counter)*100

    print("*************************************")
    print("{} of {} problems ({} %) solved in less than {} seconds.".format(solved, counter, percent, timebound))
    print("Problems that remain unsolved in the set are Problems: {}".format(unsolved))
    print("*************************************")

    solved = 0; unsolved = []; counter = 0; percent = 0;
    print("Running Anytime Weighted A-star")

    for i in range(len(PROBLEMS)):
      print("*************************************")
      print("PROBLEM {}".format(i))

      s0 = PROBLEMS[i]
      weight = 4
      final = anytime_weighted_astar(s0, heur_alternate, weight, timebound)

      if final:
        final.print_path()
        solved += 1
      else:
        unsolved.append(i)
      counter += 1

    if counter > 0:
      percent = (solved/counter)*100

    print("*************************************")
    print("{} of {} problems ({} %) solved in less than {} seconds.".format(solved, counter, percent, timebound))
    print("Problems that remain unsolved in the set are Problems: {}".format(unsolved))
    print("*************************************")

    solved = 0; unsolved = []; counter = 0; percent = 0;
    print("Running Anytime GBFS")

    for i in range(len(PROBLEMS)):
      print("*************************************")
      print("PROBLEM {}".format(i))

      s0 = PROBLEMS[i]
      final = anytime_gbfs(s0, heur_alternate, timebound)

      if final:
        final.print_path()
        solved += 1
      else:
        unsolved.append(i)
      counter += 1

    if counter > 0:
      percent = (solved/counter)*100

    print("*************************************")
    print("{} of {} problems ({} %) solved in less than {} seconds.".format(solved, counter, percent, timebound))
    print("Problems that remain unsolved in the set are Problems: {}".format(unsolved))
    print("*************************************")





