package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Game2048Board extends Board<Game2048Tile> implements Iterable<Game2048Tile> {
    private int score = 0;

    /**
     * Whether the board have been changed.
     */
    private boolean isChanged = false;

    /**
     * Set an empty board which every tile with value 0.
     */
    void setUpTiles() {
        for (int row = 0; row != getDimension(); row++) {
            for (int col = 0; col != getDimension(); col++) {
                this.setTile(row, col, new Game2048Tile());
            }
        }
    }

    // precondition: list.size() == 16
    void setUpTiles(List<Integer> list) {
        int i = 0;
        for (Game2048Tile tile : this) {
            tile.setValue(list.get(i));
            i++;
        }
    }

    boolean isChanged() {
        return isChanged;
    }

    /**
     * @return score
     */
    int getScore() {
        return score;
    }

    /**
     * Set score
     *
     * @param score score.
     */
    void setScore(int score) {
        this.score = score;
    }

    /**
     * Randomly pick an empty tile with 0 and change its value to 2 or 4.
     *
     * @return the random tile with changed value.
     */
    Game2048Tile addTile() {
        ArrayList<Game2048Tile> empty = findEmpty();
        Game2048Tile randomTile = empty.get((int) (Math.random() * empty.size()));
        randomTile.random();
        isChanged = false;
        return randomTile;
        //return null;
    }

    void cheat() {
        ArrayList<Game2048Tile> empty = findEmpty();
        for (Game2048Tile tile : empty) {
            tile.setValue(1024);
        }

        isChanged = true;
        setChanged();
        notifyObservers();
    }

    /**
     * Find all empty tiles.
     *
     * @return Array List of empty tiles.
     */
    ArrayList<Game2048Tile> findEmpty() {
        ArrayList<Game2048Tile> result = new ArrayList<>();
        for (Game2048Tile tile : getTiles()) {
            if (tile.isEmpty()) {
                result.add(tile);
            }
        }
        return result;
    }

    /**
     * @param row row number
     * @return Array of tiles in that row.
     */
    Game2048Tile[] getRow(int row) {
        Game2048Tile[] result = new Game2048Tile[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            result[i] = getTiles().get(row * getDimension() + i);
        }
        return result;
    }

    /**
     * @param col col number
     * @return Array of tiles in that column.
     */
    Game2048Tile[] getColumn(int col) {
        Game2048Tile[] result = new Game2048Tile[getDimension()];

        for (int i = 0; i < getDimension(); i++) {
            result[i] = getTiles().get(i * getDimension() + col);
        }
        return result;
    }

    /**
     * Merge an array of tiles if exist two adjacent tiles with a same value.
     * For example, [2 , 2 , 4 , 4] merge to left -> [4, 8, 0, 0]
     *
     * @param tileArray array of tiles before merge.
     * @param direction direction of merge can only be LEFT or RIGHT.
     */
    void mergeList(Game2048Tile[] tileArray, String direction) {
        int dimension = getDimension();
        ArrayList<Integer> temp = new ArrayList<>(dimension);
        int i;
        switch (direction) {
            case "LEFT":
                i = 0;
                while (i <= dimension - 1) {
                    if (i == dimension - 1) {
                        temp.add(tileArray[i].getValue());
                        i++;
                    } else if (tileArray[i].getValue() == 0) {
                        i++;
                    } else {
                        int j = i + 1;
                        while (j < dimension - 1 && tileArray[j].getValue() == 0) {
                            j++;
                        }
                        Integer valueI = tileArray[i].getValue();
                        Integer valueJ = tileArray[j].getValue();
                        if (valueI.equals(valueJ)) {
                            temp.add(valueI * 2);
                            score = score + valueI * 2;
                            i = j + 1;
                        } else {
                            temp.add(valueI);
                            i = j;
                        }
                    }
                }

                while (temp.size() != dimension) {
                    temp.add(0);
                }

                for (i = 0; i < dimension; i++) {
                    if (tileArray[i].getValue() != temp.get(i)) {
                        isChanged = true;
                        tileArray[i].setValue(temp.get(i));
                    }
                }
                break;

            case "RIGHT":
                i = dimension - 1;
                while (i >= 0) {
                    if (i == 0) {
                        temp.add(tileArray[i].getValue());
                        i--;
                    } else if (tileArray[i].getValue() == 0) {
                        i--;
                    } else {
                        int j = i - 1;
                        while (j > 0 && tileArray[j].getValue() == 0) {
                            j--;
                        }
                        Integer valueI = tileArray[i].getValue();
                        Integer valueJ = tileArray[j].getValue();
                        if (valueI.equals(valueJ)) {
                            temp.add(valueI * 2);
                            score = score + valueI * 2;
                            i = j - 1;
                        } else {
                            temp.add(valueI);
                            i = j;
                        }
                    }
                }

                while (temp.size() != dimension) {
                    temp.add(0);
                }

                for (i = dimension - 1; i >= 0; i--) {
                    if (tileArray[i].getValue() != temp.get(temp.size() - 1 - i)) {
                        isChanged = true;
                        tileArray[i].setValue(temp.get(temp.size() - 1 - i));
                    }
                }
                break;

            default:
                throw new NoSuchElementException("No Such Direction");

        }
    }

    /**
     * Merge the whole 2048 game board.
     *
     * @param direction direction of merge can only be LEFT or RIGHT or UP or DOWN.
     */
    void merge(String direction) {
        int dimension = getDimension();
        switch (direction) {
            case "LEFT":
                for (int row = 0; row <= dimension - 1; row++) {
                    mergeList(getRow(row), "LEFT");
                }
                break;
            case "UP":
                for (int col = 0; col <= dimension - 1; col++) {
                    mergeList(getColumn(col), "LEFT");
                }
                break;
            case "RIGHT":
                for (int row = 0; row <= dimension - 1; row++) {
                    mergeList(getRow(row), "RIGHT");
                }
                break;
            case "DOWN":
                for (int col = 0; col <= dimension - 1; col++) {
                    mergeList(getColumn(col), "RIGHT");
                }
                break;
            default:
                throw new NoSuchElementException("No Such Direction");
        }


        setChanged();
        notifyObservers();
    }

    @NonNull
    @Override
    public Iterator<Game2048Tile> iterator() {
        return new Tile2048Iterator(getTiles());
    }


    /**
     * Internal nested iterator iterates through 2-D array Game2048Tiles
     */
    private class Tile2048Iterator implements Iterator<Game2048Tile> {
        int currentPosition = -1;
        List<Game2048Tile> tiles;
        // eg. (row, col) = (3, 2), then it is indeed at row 3, column 2 (start from 0 ......> 3)
        // we have 3 complete rows, and to make complete: + 2
        // current position = 3*NUM_COLS + 2

        /**
         * a new Tile2048Iterator takes game 2048 tiles and process it
         *
         * @param tiles Game2048Tile from board
         */
        Tile2048Iterator(List<Game2048Tile> tiles) {
            this.tiles = tiles;
        }

        /**
         * Return true if array has next tile
         * false otherwise
         *
         * @return if there is more tile
         */
        @Override
        public boolean hasNext() {

            return currentPosition < getDimension() * getDimension() - 1;
        }

        /**
         * Return the next tile to be processed
         *
         * @return the next tile object
         */
        @Override
        public Game2048Tile next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                currentPosition++;
                return this.tiles.get(currentPosition);
            }
        }
    }
}