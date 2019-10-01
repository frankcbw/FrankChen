package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BoardManager<T, K> implements Serializable {
    /**
     * Record time of the game.
     */
    private Double time = (double) 0;

    /**
     * List of tiles in the board.
     */
    private List<K> tiles = new ArrayList<>();

    /**
     * Dimension of the board.
     */
    private int dimension;

    /**
     * Game Board.
     */
    private T board;

    public BoardManager() {
    }

    /**
     * Construct a new board manager with dimension n.
     *
     * @param n dimension of the game board.
     */
    public BoardManager(int n) {
        this.dimension = n;
    }

    /**
     * @return tiles of the board.
     */
    public List<K> getTiles() {
        return tiles;
    }

    /**
     * Set tiles.
     *
     * @param tiles tiles of the board.
     */
    public void setTiles(List<K> tiles) {
        this.tiles = tiles;
    }

    /**
     * @return the time recorded
     */
    public Double getTime() {
        return time;
    }

    /**
     * Set the time.
     *
     * @param time the time to be set.
     */
    public void setTime(Double time) {
        this.time = time;
    }

    /**
     * @return dimension of the board.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Set dimension.
     *
     * @param dimension new dimension of the board.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    /**
     * @return game board
     */
    public T getBoard() {
        return board;
    }

    /**
     * Set the game board.
     *
     * @param board game board.
     */
    public void setBoard(T board) {
        this.board = board;
    }

    /**
     * @return Return whether the player wins according to the game rule.
     */
    abstract boolean isWon();

    /**
     * @return Return whether the player loses according to the game rule.
     */
    abstract boolean isLost();

    /**
     * Move the game board.
     *
     * @param o A slide direction or touch position.
     */
    abstract void move(Object o);

}
