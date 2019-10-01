package fall2018.csc2017.slidingtiles;

import java.util.Collections;
import java.util.List;

/**
 * Manage a slidingBoard, including swapping slidingTiles, checking for a win, and managing taps.
 */
public class SlidingBoardManager extends BoardManager<SlidingBoard, SlidingTile> {
    /**
     * The linked list of slidingHistory moves of the slidingBoard.
     */
    private SlidingHistory slidingHistory = new SlidingHistory();

    /**
     * The index at which the element in slidingHistory represents the current location of blank tile.
     */
    private int currIndex = 0;

    /**
     * Add ordered tiles to the board.
     */
    void addTile() {
        int numTiles = getDimension() * getDimension();
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            getTiles().add(new SlidingTile(tileNum));
        }
    }

    @Override
    public void setTiles(List<SlidingTile> slidingTiles) {
        getBoard().setTiles(slidingTiles);
        super.setTiles(slidingTiles);
    }

    /**
     * Return how many inversions occur in the list of slidingTiles.
     * An inversion is when a tile precedes another tile with a lower number on it.
     *
     * @return the number of inversion
     */
    int findInversion() {
        int count = 0;
        for (int i = 0; i < getDimension() * getDimension() - 1; i++) {
            if (getTiles().get(i).getId() != 0) {
                for (int j = i + 1; j < getDimension() * getDimension(); j++) {
                    if (getTiles().get(j).getId() < getTiles().get(i).getId() && getTiles().get(j).getId() != 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Shuffle the list of slidingTiles until the state is solvable.
     */
    void shuffle() {
        Collections.shuffle(getTiles());
        while (!isSolvable()) {
            Collections.shuffle(getTiles());
        }
    }

    /**
     * Return if the shuffled state can be solved.
     *
     * @return if the state is solvable.
     */
    private boolean isSolvable() {
        int inversion = findInversion();
        if (getDimension() % 2 == 1) {
            return inversion % 2 == 0;
        } else {
            int i = 0;
            while (getTiles().get(i).getId() != 0) {
                i++;
            }
            int blankAtRowFromBottom = getDimension() - (i / getDimension());
            return inversion % 2 != blankAtRowFromBottom % 2;
        }
    }

    /**
     * Return the SlidingHistory.
     *
     * @return the SlidingHistory
     */
    SlidingHistory getSlidingHistory() {
        return slidingHistory;
    }

    void setSlidingHistory(SlidingHistory slidingHistory) {
        this.slidingHistory = slidingHistory;
    }

    /**
     * Return whether the slidingTiles are in row-major order.
     *
     * @return whether the slidingTiles are in row-major order
     */
    @Override
    boolean isWon() {
        for (int i = 0; i < getDimension() * getDimension() - 3; i++) {
            if (getTiles().get(i).getId() > getTiles().get(i + 1).getId()) {
                return false;
            }
        }
        return getTiles().get(getDimension() * getDimension() - 1).getId() == 0;
    }

    /**
     * Return whether any of the four surrounding slidingTiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / getDimension();
        int col = position % getDimension();
        int blankId = 0;
        // Are any of the 4 the blank tile?
        SlidingTile above = row == 0 ? null : getBoard().getTile(row - 1, col);
        SlidingTile below = row == getDimension() - 1 ? null : getBoard().getTile(row + 1, col);
        SlidingTile left = col == 0 ? null : getBoard().getTile(row, col - 1);
        SlidingTile right = col == getDimension() - 1 ? null : getBoard().getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the slidingBoard, swapping slidingTiles as appropriate.
     *
     * @param position the position
     */
    @Override
    void move(Object position) {
        int po = (int) position;
        int row = po / getDimension();
        int col = po % getDimension();
        int r_row;
        int r_col;
        if (isValidTap(po)) {
            int[] result = findBlankIndex();
            r_row = result[0];
            r_col = result[1];
            getBoard().swapTiles(row, col, r_row, r_col);
            result[0] = row;
            result[1] = col;
            if (currIndex == slidingHistory.getSize() - 1) {
                slidingHistory.add(new SlidingHistoryNode(result));
                currIndex++;
            } else {
                slidingHistory.remove(currIndex + 1);
                slidingHistory.add(new SlidingHistoryNode((result)));
                currIndex++;
            }
        }


    }

    /**
     * once we know that there is a blank space surrounding tile, which id is targetId
     * we use iterator to find that blank tile
     *
     * @return an array of coordination of blank tile
     */
    int[] findBlankIndex() {
        int[] result = new int[2];
        int position = 0;
        for (SlidingTile t : getTiles()) {
            if (t.getId() == 0) {
                result[0] = position / getDimension();
                result[1] = position % getDimension();
                break;
            }
            position++;
        }
        return result;

    }

    /**
     * @return always return false since this game will not end unless the player solve the puzzle.
     */
    @Override
    boolean isLost() {
        return false;
    }

    /**
     * Perform undo operation if i is -1 and perform redo operation if i is 1
     *
     * @param i is -1 or 1 to indicate performing undo or redo
     */
    void readHistory(int i) {
        int[] currPosition = new int[2];
        int[] postPosition = new int[2];
        //redo
        if (i == 1 && slidingHistory.get(currIndex).next != null) {
            currPosition = slidingHistory.get(currIndex).getData();
            postPosition = slidingHistory.get(currIndex + 1).getData();
            currIndex++;
        }
        // undo
        else if (i == -1 && slidingHistory.getSize() > 1) {
            currPosition = slidingHistory.get(currIndex).getData();
            postPosition = slidingHistory.get(currIndex - 1).getData();
            currIndex--;
        }
        getBoard().swapTiles(postPosition[0], postPosition[1], currPosition[0], currPosition[1]);
    }
}
