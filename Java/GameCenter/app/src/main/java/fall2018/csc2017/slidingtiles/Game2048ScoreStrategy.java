package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * Game score strategy for 2048 game
 */
public class Game2048ScoreStrategy implements ScoreStrategy, Serializable {
    Game2048ScoreStrategy() {
    }

    @Override
    public int calculateScore(Object o) {
        Game2048BoardManager boardManager = (Game2048BoardManager) o;
        //        for (Game2048Tile tile: ((Game2048BoardManager)boardManager).getSlidingBoard()) {
//            score += tile.getValue();
//        }
        return boardManager.getScore();
    }
}
