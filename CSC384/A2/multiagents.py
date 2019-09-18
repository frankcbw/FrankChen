# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


import random

import util
from game import Agent, Directions # noqa
from util import manhattanDistance  # noqa


class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """

    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices)  # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        PacPos = successorGameState.getPacmanPosition()
        Food = successorGameState.getFood().asList()
        Capsule = successorGameState.getCapsules()
        GhostStates = successorGameState.getGhostStates()
        GhostPos = [g.getPosition() for g in GhostStates if g.scaredTimer <= 0 and g.getPosition() != PacPos]
        ScaredGhost = [g.getPosition() for g in GhostStates if g.scaredTimer > 0]

        "*** YOUR CODE HERE ***"

        if successorGameState.isWin():
            return float("inf")
        elif successorGameState.isLose():
            return -float("inf")

        min_f_distance = min(manhattanDistance(PacPos, x) for x in Food)
        min_g_distance = 1 / min(manhattanDistance(PacPos, x) for x in GhostPos) if GhostPos else 0
        min_scared_distance = min(manhattanDistance(PacPos, x) for x in ScaredGhost) if ScaredGhost else 0
        min_capsule_distance = min(manhattanDistance(PacPos, x) for x in Capsule) if Capsule else 0
        capsule_left = len(Capsule)
        food_left = len(Food)

        score = - 100 * capsule_left \
                - 50 * food_left \
                - 10 * min_capsule_distance \
                - 2.5 * min_f_distance \
                - 5 * min_g_distance \
                - 2 * min_scared_distance

        return score


def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()


class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn="scoreEvaluationFunction", depth="2"):
        self.index = 0  # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)


class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game
        """
        "*** YOUR CODE HERE ***"

        # Helper for ghosts (Min)
        def min_level_score(game_state, depth, agent_index):
            """
            Search game_state at a min level (the ghost agent at agent_index)

            :param game_state: current GameState
            :type game_state: GameState
            :param depth: the depth bound
            :type depth: int
            :param agent_index: index of the ghost agent
            :type agent_index: int
            :return: the min score chosen based on game_state
            :rtype: int
            """
            # Terminal states
            if game_state.isWin() or game_state.isLose():
                return self.evaluationFunction(game_state)

            min_score = float("inf")

            legal_actions = game_state.getLegalActions(agent_index)

            for action in legal_actions:
                successor = game_state.generateSuccessor(agent_index, action)

                # Last Ghost, choose min score from the max level
                if agent_index == game_state.getNumAgents() - 1:
                    min_score = min(min_score, max_level_score(successor, depth))

                # Move to next ghost, choose min score from the next min level
                else:
                    min_score = min(min_score, min_level_score(successor, depth, agent_index + 1))

            return min_score

        # Helper for PacMan (Max)
        def max_level_score(game_state, pre_depth):
            """
            Search the game_state at the max level (PacMan at index 0) at the depth pre_depth + 1

            :param game_state: current GameState
            :type game_state: GameState
            :param pre_depth: depth for the previous min_level_score call
            :type pre_depth: int
            :return: the max score chosen based on game_state
            :rtype: int
            """
            # Update the depth
            curr_depth = pre_depth + 1

            # Terminal states or depth bound is reached
            if game_state.isWin() or game_state.isLose() or curr_depth == self.depth:
                return self.evaluationFunction(game_state)

            max_score = -float("inf")

            legal_actions = game_state.getLegalActions()

            for action in legal_actions:
                successor = game_state.generateSuccessor(0, action)

                # Move to min levels, choose the max score from the next min level
                max_score = max(max_score, min_level_score(successor, curr_depth, 1))

            return max_score

        goal_score = -float("inf")

        curr_legal_actions = gameState.getLegalActions(0)
        to_return = ""

        for action in curr_legal_actions:
            successor = gameState.generateSuccessor(0, action)
            curr_score = min_level_score(successor, 0, 1)
            # Pick the max score, record the corresponding action
            if curr_score > goal_score:
                goal_score = curr_score
                to_return = action

        return to_return

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
          Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"

        # Helper for ghosts (Min)
        def min_level_score(game_state, depth, agent_index, alpha, beta):
            """
            Search game_state at a min level (the ghost agent at agent_index) using alpha-beta pruning.

            :param game_state: current GameState
            :type game_state: GameState
            :param depth: the depth bound
            :type depth: int
            :param agent_index: index of the ghost agent
            :type agent_index: int
            :param alpha: the highest score explored so far
            :type alpha: int
            :param beta: the lowest score explored so far
            :type beta: int
            :return: the min score chosen based on game_state
            :rtype: int
            """
            # Terminal states
            if game_state.isWin() or game_state.isLose():
                return self.evaluationFunction(game_state)

            min_score = float("inf")
            sub_beta = beta

            legal_actions = game_state.getLegalActions(agent_index)

            for action in legal_actions:
                successor = game_state.generateSuccessor(agent_index, action)

                # Last Ghost, choose min score from the max level
                if agent_index == game_state.getNumAgents() - 1:
                    min_score = min(min_score, max_level_score(successor, depth, alpha, sub_beta))

                # Move to next ghost, choose min score from the next min level
                else:
                    min_score = min(min_score, min_level_score(successor, depth, agent_index + 1, alpha, sub_beta))

                # Stop as PacMan won't choose it anyway
                if min_score <= alpha:
                    break
                sub_beta = min(sub_beta, min_score)

            return min_score

        # Helper for PacMan (Max)
        def max_level_score(game_state, pre_depth, alpha, beta):
            """
            Search the game_state at the max level (PacMan at index 0) at the depth pre_depth + 1  using alpha-beta
            pruning.

            :param game_state: current GameState
            :type game_state: GameState
            :param pre_depth: depth for the previous min_level_score call
            :type pre_depth: int
            :param alpha: the highest score explored so far
            :type alpha: int
            :param beta: the lowest score explored so far
            :type beta: int
            :return: the max score chosen based on game_state
            :rtype: int
            """
            # Update the depth
            curr_depth = pre_depth + 1

            # Terminal states or depth bound is reached
            if game_state.isWin() or game_state.isLose() or curr_depth == self.depth:
                return self.evaluationFunction(game_state)

            max_score = -float("inf")
            sub_alpha = alpha

            legal_actions = game_state.getLegalActions()

            for action in legal_actions:
                successor = game_state.generateSuccessor(0, action)

                # Move to min levels, choose the max score from the next min level
                max_score = max(max_score, min_level_score(successor, curr_depth, 1, sub_alpha, beta))

                # Stop as ghosts won't choose it anyways
                if max_score >= beta:
                    break
                sub_alpha = max(alpha, max_score)

            return max_score

        alpha = -float("inf")
        beta = float("inf")

        curr_legal_actions = gameState.getLegalActions(0)
        to_return = ""

        for action in curr_legal_actions:
            successor = gameState.generateSuccessor(0, action)
            curr_score = min_level_score(successor, 0, 1, alpha, beta)

            # Pick the max score, record the corresponding action, update the alpha value
            if curr_score > alpha:
                alpha = curr_score
                to_return = action

        return to_return


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        "*** YOUR CODE HERE ***"

        # Helper for ghosts (Chance)
        def chance_level_score(game_state, depth, agent_index):
            """
            Search game_state at a chance level (the ghost agent at agent_index), return the average score

            :param game_state: current GameState
            :type game_state: GameState
            :param depth: the depth bound
            :type depth: int
            :param agent_index: index of the ghost agent
            :type agent_index: int
            :return: the min score chosen based on game_state
            :rtype: int
            """
            # Terminal states
            if game_state.isWin() or game_state.isLose():
                return self.evaluationFunction(game_state)

            legal_actions = game_state.getLegalActions(agent_index)

            total_score = 0
            total_state = len(legal_actions)

            for action in legal_actions:
                successor = game_state.generateSuccessor(agent_index, action)

                # Last Ghost, choose min score from the max level
                if agent_index == game_state.getNumAgents() - 1:
                    score = max_level_score(successor, depth)

                # Move to next ghost, choose min score from the next min level
                else:
                    score = chance_level_score(successor, depth, agent_index + 1)

                total_score += score

            return float(total_score)/float(total_state)

        # Helper for PacMan (Max)
        def max_level_score(game_state, pre_depth):
            """
            Search the game_state at the max level (PacMan at index 0) at the depth pre_depth + 1

            :param game_state: current GameState
            :type game_state: GameState
            :param pre_depth: depth for the previous min_level_score call
            :type pre_depth: int
            :return: the max score chosen based on game_state
            :rtype: int
            """
            # Update the depth
            curr_depth = pre_depth + 1

            # Terminal states or depth bound is reached
            if game_state.isWin() or game_state.isLose() or curr_depth == self.depth:
                return self.evaluationFunction(game_state)

            max_score = -float("inf")

            legal_actions = game_state.getLegalActions()

            for action in legal_actions:
                successor = game_state.generateSuccessor(0, action)

                # Move to min levels, choose the max score from the next min level
                max_score = max(max_score, chance_level_score(successor, curr_depth, 1))

            return max_score

        goal_score = -float("inf")

        curr_legal_actions = gameState.getLegalActions(0)
        to_return = ""

        for action in curr_legal_actions:
            successor = gameState.generateSuccessor(0, action)
            curr_score = chance_level_score(successor, 0, 1)
            # Pick the max score, record the corresponding action
            if curr_score > goal_score:
                goal_score = curr_score
                to_return = action

        return to_return


def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION:  If a state is winning, then return infinity; if losing, negative infinity is returned.
                    Otherwise, the state is evaluated based on a few features: the closest distances to food, capsules,
                    active ghosts and scared ghosts, total food left, and total capsule left. I gave each feature a
                    coefficient and put them into a linear equation. By testing different sets of coefficients, the
                    following result is the best so far.
    """

    PacPos = currentGameState.getPacmanPosition()
    Food = currentGameState.getFood().asList()
    Capsule = currentGameState.getCapsules()
    GhostStates = currentGameState.getGhostStates()
    GhostPos = [g.getPosition() for g in GhostStates if g.scaredTimer == 0 and g.getPosition() != PacPos]
    ScaredGhost = [g.getPosition() for g in GhostStates if g.scaredTimer > 0]

    if currentGameState.isWin():
        return float("inf")
    elif currentGameState.isLose():
        return -float("inf")

    min_f_distance = min(manhattanDistance(PacPos, x) for x in Food)
    min_g_distance = 1/min(manhattanDistance(PacPos, x) for x in GhostPos) if GhostPos else 0
    min_scared_distance = min(manhattanDistance(PacPos, x) for x in ScaredGhost) if ScaredGhost else 0
    min_capsule_distance = min(manhattanDistance(PacPos, x) for x in Capsule) if Capsule else 0
    capsule_left = len(Capsule)
    food_left = len(Food)

    score = - 100 * capsule_left \
            - 50 * food_left \
            - 4 * min_capsule_distance \
            - 2 * min_f_distance \
            - 5 * min_g_distance \
            - 3 * min_scared_distance

    return score


# Abbreviation
better = betterEvaluationFunction
