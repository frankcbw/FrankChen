package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Board<T> implements Serializable, IObservable<Game2048Board> {
    /**
     * Number of row/col.
     */
    private int dimension;

    /**
     * Tiles that in the board.
     */
    private List<T> tiles = new ArrayList<>();

    /**
     * Whether the board have been changed.
     */
    private boolean changed = false;

    /**
     * List of observers.
     */
    private ArrayList<IObserver> observers = new ArrayList<>();

    /**
     * @return tiles in the board.
     */
    List<T> getTiles() {
        return tiles;
    }

    /**
     * Set tiles in the board.
     *
     * @param tiles tiles in the board
     */
    void setTiles(List<T> tiles) {
        this.tiles = tiles;
    }

    /**
     * @return dimension of the board.
     */
    int getDimension() {
        return dimension;
    }

    /**
     * Set dimension of the board.
     *
     * @param dimension new dimension of the board.
     */
    void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    T getTile(int row, int col) {
        return tiles.get(row * dimension + col);
    }

    /**
     * Set the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     */
    void setTile(int row, int col, T tile) {
        tiles.set(row * getDimension() + col, tile);
    }


    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        if (changed) {
            int i = observers.size();
            while (--i >= 0) {
                observers.get(i).update(this);
            }

        }
    }

    @Override
    public boolean hasChanged() {
        return changed;
    }

    @Override
    public void setChanged() {
        changed = true;
    }
}

