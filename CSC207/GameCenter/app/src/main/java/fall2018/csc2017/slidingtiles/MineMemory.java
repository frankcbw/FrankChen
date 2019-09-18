package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The class extending Memory stores information of MineBoardManager and makes copy of it.
 */
class MineMemory extends Memory<MineBoardManager> {

    /**
     * The dimension of the MineBoardManager cloned.
     */
    private int dimension;

    /**
     * The number of mines of the MineBoardManager cloned.
     */
    private int mine;

    /**
     * The time of playing of the MineBoardManager cloned.
     */
    private double timeMine;

    /**
     * If the MineBoardManager cloned is firstly created.
     */
    private boolean isFirst;

    /**
     * The number of mines left of the MineBoardManager cloned.
     */
    private int mineLeft;

    /**
     * The isObscured, number, isMine, isFlagged of each tile in the MineBoardManager cloned.
     */
    private List<Boolean> isObscuredOfTiles = new ArrayList<>();
    private List<Integer> numberOfTiles = new ArrayList<>();
    private List<Boolean> isMineOfTiles = new ArrayList<>();
    private List<Boolean> isFlaggedOfTiles = new ArrayList<>();

    @Override
    void makeCopy(MineBoardManager manager) {
        dimension = manager.getBoard().getDimension();
        mine = manager.getBoard().getMineNum();
        mineLeft = manager.getBoard().getMineLeft();
        timeMine = manager.getTime();
        isFirst = manager.isFirst();
        timeMine = manager.getTime();
        for (Tile tile : manager.getBoard().getTiles()) {
            MineTile t = (MineTile) tile;
            isObscuredOfTiles.add(t.isObscured());
            numberOfTiles.add(t.getNumber());
            isMineOfTiles.add(t.isMine());
            isFlaggedOfTiles.add(t.isFlagged());
        }
    }

    @Override
    MineBoardManager copy() {
        List<MineTile> tiles = new ArrayList<>();
        for (int i = 0; i < dimension * dimension; i++) {
            tiles.add(new MineTile(isObscuredOfTiles.get(i), numberOfTiles.get(i), isMineOfTiles.get(i), isFlaggedOfTiles.get(i)));
        }
        Factory f = new Factory();
        return f.loadMineManager(dimension, mine, mineLeft, timeMine, tiles);
    }

    /**
     * Return isFirst.
     *
     * @return isFirst
     */
    boolean isFirst() {
        return isFirst;
    }

    /**
     * Return dimension.
     *
     * @return dimension
     */
    int getDimension() {
        return dimension;
    }

    /**
     * Return mine.
     *
     * @return mine
     */
    int getMine() {
        return mine;
    }

    /**
     * Return timeMine.
     *
     * @return timeMine
     */
    double getTimeMine() {
        return timeMine;
    }

    /**
     * Return mineLeft.
     *
     * @return mineLeft
     */
    int getMineLeft() {
        return mineLeft;
    }

    /**
     * Return isObscuredOfTiles.
     *
     * @return isObscuredOfTiles
     */
    List<Boolean> getIsObscuredOfTiles() {
        return isObscuredOfTiles;
    }

    /**
     * Return numberOfTiles.
     *
     * @return numberOfTiles
     */
    List<Integer> getNumberOfTiles() {
        return numberOfTiles;
    }

    /**
     * Return isMineOfTiles.
     *
     * @return isMineOfTiles
     */
    List<Boolean> getIsMineOfTiles() {
        return isMineOfTiles;
    }

    /**
     * Return isFlaggedOfTiles.
     *
     * @return isFlaggedOfTiles
     */
    List<Boolean> getIsFlaggedOfTiles() {
        return isFlaggedOfTiles;
    }
}
