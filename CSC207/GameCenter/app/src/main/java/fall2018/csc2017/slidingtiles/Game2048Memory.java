package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The class extending Memory stores information of Game2048BoardManager and makes copy of it.
 */
class Game2048Memory extends Memory<Game2048BoardManager> {

    /**
     * The time of playing of the Game2048BoardManager cloned
     */
    private double time2048;

    /**
     * The score of the Game2048BoardManager cloned
     */
    private int score;

    /**
     * The value of each tile in the Game2048BoardManager cloned
     */
    private List<Integer> tileValueList = new ArrayList<>();

    @Override
    public void makeCopy(Game2048BoardManager manager) {
        this.time2048 = manager.getTime();
        score = manager.getScore();
        for (Game2048Tile t : manager.getBoard()) {
            this.tileValueList.add(t.getValue());
        }
    }

    @Override
    public Game2048BoardManager copy() {
        Factory f = new Factory();
        return f.Load2048Manager(time2048, score, tileValueList);
    }

    /**
     * Return time2048.
     *
     * @return time2048
     */
    double getTime2048() {
        return time2048;
    }

    /**
     * Return score.
     *
     * @return score
     */
    int getScore() {
        return score;
    }

    /**
     * Return tileValueList.
     *
     * @return tileValueList
     */
    List<Integer> getTileValueList() {
        return tileValueList;
    }
}
