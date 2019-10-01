package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineBoard extends Board<MineTile> {
    /**
     * Numer of mines.
     */
    private int mineNum;

    /**
     * Number of mines left.
     */
    private int mineLeft;

    private List<MineTile> minePosition = new ArrayList<>();

    /**
     * @return number of mines.
     */
    int getMineNum() {
        return mineNum;
    }

    void setMineNum(int mineNum) {
        this.mineNum = mineNum;
    }

    /**
     * @return number of mines left.
     */
    int getMineLeft() {
        return mineLeft;
    }

    public void setMineLeft(int mineLeft) {
        this.mineLeft = mineLeft;
    }

    List<MineTile> getMinePosition() {
        return minePosition;
    }

    void setMinePosition(List<MineTile> minePosition) {
        this.minePosition = minePosition;
    }

    /**
     * @param position position at list of tiles
     * @return tile that at the position
     */
    MineTile getTile(int position) {
        int row = position / getDimension();
        int col = position % getDimension();
        return super.getTile(row, col);
    }

    void setMines(int position) {
        int mineNum = getMineNum();
        List<MineTile> startNine = getSurround(position);
        startNine.add(getTile(position));
        Random r = new Random();
        List<Integer> randomNums = new ArrayList<>();
        int i = 0;
        while (i < mineNum) {
            Integer num = r.nextInt(getDimension() * getDimension());

            if (!randomNums.contains(num) && !startNine.contains(getTile(num))) {
                randomNums.add(num);
                getTile(num).setMine();
                minePosition.add(getTile(num));
                i++;
            }
        }
    }

    /**
     * Show the tile with the position.
     *
     * @param position tile position.
     */
    void reveal(int position) {
        MineTile currTile = getTile(position);
        if (currTile.getNumber() == 0) {
            currTile.reveal();
            for (Tile tileT : getSurround(position)) {
                MineTile tile = (MineTile) tileT;
                if (tile.isObscured() && !tile.isFlagged()) {
                    tile.reveal();
                    if (tile.getNumber() == 0) {
                        reveal(tile.getPosition());
                    }
                }
            }
        } else {
            currTile.reveal();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Flag the tile with the position.
     *
     * @param position tile position
     */
    void flag(int position) {
        getTile(position).flag();
        if (getTile(position).isFlagged()) {
            mineLeft--;
        } else {
            mineLeft++;
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Get the 8 surrounding tiles of the tile with the position.
     *
     * @param position tile position
     * @return List of surrounding tiles.
     */
    List<MineTile> getSurround(int position) {
        int dimension = getDimension();
        int row = position / dimension;
        int col = position % dimension;
        List<MineTile> surround = new ArrayList<>();

        if (row != 0 && col != 0) {
            surround.add(getTiles().get(position - dimension - 1));
        }
        if (row != 0 && col != dimension - 1) {
            surround.add(getTiles().get(position - dimension + 1));
        }
        if (row != 0) {
            surround.add(getTiles().get(position - dimension));
        }
        if (row != dimension - 1 && col != 0) {
            surround.add(getTiles().get(position + dimension - 1));
        }
        if (row != dimension - 1 && col != dimension - 1) {
            surround.add(getTiles().get(position + dimension + 1));
        }
        if (row != dimension - 1) {
            surround.add(getTiles().get(position + dimension));
        }
        if (col != 0) {
            surround.add(getTiles().get(position - 1));
        }
        if (col != dimension - 1) {
            surround.add(getTiles().get(position + 1));
        }
        return surround;
    }

    /**
     * Show all tiles.
     */
    void showMines() {
        for (MineTile tile : minePosition) {
            tile.showMine();
        }

        setChanged();
        notifyObservers();
    }
}