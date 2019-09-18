package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * The Strategy to calculate the score of a mine sweeper game.
 */
class MineScoreStrategy implements ScoreStrategy, Serializable {
    @Override
    public int calculateScore(Object boardManager) {
        MineBoard board = ((MineBoardManager) boardManager).getBoard();
        int boardScore = 0;
        if (((MineBoardManager) boardManager).isWon()) {
            boardScore = board.getDimension() * board.getDimension();
        } else {
            for (MineTile t : board.getMinePosition()) {
                if (t.isFlagged()) {
                    boardScore++;
                }
            }
        }
        double time = ((MineBoardManager) boardManager).getTime();
        int timeScore = 10 * (int) (1 / time);
        return timeScore + boardScore;
    }
}
