package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Game2048Test {

    /**
     * New factory to build the board.
     */
    private Factory factory = new Factory();

    /**
     * Use builder to build a new board to test.
     */
    private Game2048Board board = new BuilderBoard().build2048Board();

    /**
     * Build a board manager that controls our board.
     */
    private Game2048BoardManager manager = (Game2048BoardManager) factory.createNewManager(board);

    /**
     * A helper function that makes the board full and not movable. Each tile has no adjacent tiles
     * with same value.
     */
    private void setBoardCannotMove(int largestTile) {
        board.setUpTiles();
        int n = 2;
        List<Game2048Tile> tiles = board.getTiles();
        for (Game2048Tile tile : tiles) {
            tile.setValue(n);
            if (n < largestTile) {
                n = n * 2;
            } else {
                n = 2;
            }
        }
    }

    /**
     * A helper function that makes the board full and mergeble. All tiles have value of 2.
     */
    private void setBoardCanMove() {
        board.setUpTiles();
        List<Game2048Tile> tiles = board.getTiles();
        for (Game2048Tile tile : tiles) {
            tile.setValue(2);
        }
        board.setScore(0);
    }

    /**
     * A helper function that converts a size 4 array of Game2048Tile(a row or a column of the board)
     * into an size 4 array of int (represents tile values).
     */
    private int[] tileToInt(Game2048Tile[] line) {
        int[] intLine = new int[4];
        for (int i = 0; i < 4; i++) {
            intLine[i] = line[i].getValue();
        }
        return intLine;
    }

    /**
     * A helper function to convert an size 4 array of int (represents tile values)
     * into a size 4 array of Game2048Tile(a row or a column of the board).
     */
    private Game2048Tile[] intToTile(int[] intLine) {
        Game2048Tile tile1 = new Game2048Tile();
        tile1.setValue(intLine[0]);
        Game2048Tile tile2 = new Game2048Tile();
        tile2.setValue(intLine[1]);
        Game2048Tile tile3 = new Game2048Tile();
        tile3.setValue(intLine[2]);
        Game2048Tile tile4 = new Game2048Tile();
        tile4.setValue(intLine[3]);
        return new Game2048Tile[]{
                tile1, tile2, tile3, tile4
        };
    }

    /**
     * A helper function to reset game score and call mergeList.
     */
    private void setUpMergeList(Game2048Tile[] testCase, String direction) {
        board.setScore(0);
        board.mergeList(testCase, direction);
    }

    /**
     * Test whether isLost returns true when the board neither can move nor contains tile2048.
     */
    @Test
    public void testLose() {
        setBoardCannotMove(1024);
        assertTrue(manager.isLost());
    }

    /**
     * Test whether isLost returns false when the board is movable and contains tile2048.
     */
    @Test
    public void testNotLoseHas2048CanMove() {
        board.setUpTiles();
        board.cheat();
        assertTrue(manager.canMove());
        assertFalse(manager.isLost());
    }

    /**
     * Test whether isLost returns false when the board is not movable but it contains tile2048.
     */
    @Test
    public void testNotLoseHas2048CantMove() {
        setBoardCannotMove(2048);
        assertFalse(manager.isLost());
    }

    /**
     * Test whether isLost returns false when the board is full but movable.
     */
    @Test
    public void testNotLoseFullBoard() {
        setBoardCanMove();
        assertFalse(manager.isLost());
    }

    /**
     * Test whether isWon returns true when the board is not movable but contains tile2048.
     */
    @Test
    public void testWinContains2048CantMove() {
        setBoardCannotMove(2048);
        assertTrue(manager.isWon());
    }

    /**
     * Test whether isWon returns false when for a new board in the game.
     */
    @Test
    public void testNotWinNewBoard() {
        board.setUpTiles();
        board.addTile();
        assertTrue(manager.canMove());
        assertFalse(manager.isWon());
    }

    /**
     * Test whether isWon returns full when the board is movable but not containing tile 2048.
     */
    @Test
    public void testNotWinCanMove() {
        board.setUpTiles();
        Game2048Tile tile = board.getTile(0, 0);
        tile.setValue(2);
        assertTrue(manager.canMove());
        assertFalse(manager.isWon());
    }

    /**
     * Test moving a line with no merging involved.
     */
    @Test
    public void simpleSlideRight() {
        int[] expected = {0, 0, 2, 4};
        int[] before = {2, 4, 0, 0};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(0, board.getScore());
    }

    /**
     * Test merging a line with no moving involved.
     */
    @Test
    public void simpleMergeRight() {
        int[] expected = {0, 0, 0, 4};
        int[] before = {0, 0, 2, 2};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(4, board.getScore());
    }

    /**
     * Test moving and merging a line.
     */
    @Test
    public void slideAndMergeRight() {
        int[] expected = {0, 0, 0, 8};
        int[] before = {4, 4, 0, 0};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(8, board.getScore());
    }

    /**
     * Test calling mergeList while there is nothing change.
     */
    @Test
    public void doNothingRight() {
        int[] expected = {0, 0, 8, 16};
        int[] before = {0, 0, 8, 16};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(0, board.getScore());
    }

    /**
     * Test moving and one merging happens when there are more tiles involved.
     */
    @Test
    public void moreTilesRight() {
        int[] expected = {0, 0, 4, 4};
        int[] before = {2, 0, 2, 4};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(4, board.getScore());
    }

    /**
     * Test activities in tiles with other value.
     */
    @Test
    public void largerNumbersRight() {
        int[] expected = {0, 0, 2048, 512};
        int[] before = {1024, 0, 1024, 512};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(2048, board.getScore());
    }

    /**
     * Test activities with no zero(empty) tiles in the original line.
     */
    @Test
    public void noZeroesRight() {
        int[] expected = {0, 512, 2048, 512};
        int[] before = {256, 256, 2048, 512};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(512, board.getScore());
    }

    /**
     * Test activities in tiles with other values while there are two merges happening.
     */
    @Test
    public void moreNumbersTwoMergesRight() {
        int[] expected = {0, 0, 128, 256};
        int[] before = {64, 64, 128, 128};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "RIGHT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(384, board.getScore());
    }

    /**
     * Test activities in tiles with other values and the opposite direction.
     */
    @Test
    public void largerNumbersLeft() {
        int[] expected = {512, 2048, 0, 0};
        int[] before = {512, 1024, 0, 1024};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "LEFT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(2048, board.getScore());
    }

    /**
     * Test activities in tiles with other values and the opposite direction
     * while there are two merges happening.
     */
    @Test
    public void moreNumbersTwoMergesLeft() {
        int[] expected = {16, 32, 0, 0};
        int[] before = {8, 8, 16, 16};
        Game2048Tile[] actual = intToTile(before);
        setUpMergeList(actual, "LEFT");
        assertArrayEquals(expected, tileToInt(actual));
        assertEquals(48, board.getScore());
    }

    /**
     * Test merging the whole board up.
     */
    @Test
    public void testMergeWholeBoardUp() {
        setBoardCanMove();
        manager.move("UP");
        int[][] expectedRows = {{4, 4, 4, 4}, {4, 4, 4, 4}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedRows[i], tileToInt(board.getRow(i)));
        }
        int[][] expectedColumns = {{4, 4, 0, 0}, {4, 4, 0, 0}, {4, 4, 0, 0}, {4, 4, 0, 0}};
        assertTrue(board.isChanged());
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedColumns[i], tileToInt(board.getColumn(i)));
        }
        assertEquals(32, manager.getScore());
    }

    /**
     * Test merging the whole board down.
     */
    @Test
    public void testMergeWholeBoardDown() {
        setBoardCanMove();
        manager.move("DOWN");
        int[][] expectedRows = {{0, 0, 0, 0}, {0, 0, 0, 0}, {4, 4, 4, 4}, {4, 4, 4, 4}};
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedRows[i], tileToInt(board.getRow(i)));
        }
        int[][] expectedColumns = {{0, 0, 4, 4}, {0, 0, 4, 4}, {0, 0, 4, 4}, {0, 0, 4, 4}};
        assertTrue(board.isChanged());
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedColumns[i], tileToInt(board.getColumn(i)));
        }
        assertEquals(32, manager.getScore());
    }

    /**
     * Test merging the whole board left.
     */
    @Test
    public void testMergeWholeBoardLeft() {
        setBoardCanMove();
        manager.move("LEFT");
        int[][] expectedRows = {{4, 4, 0, 0}, {4, 4, 0, 0}, {4, 4, 0, 0}, {4, 4, 0, 0}};
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedRows[i], tileToInt(board.getRow(i)));
        }
        int[][] expectedColumns = {{4, 4, 4, 4}, {4, 4, 4, 4}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        assertTrue(board.isChanged());
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedColumns[i], tileToInt(board.getColumn(i)));
        }
        assertEquals(32, manager.getScore());
    }

    /**
     * Test merging the whole board right.
     */
    @Test
    public void testMergeWholeBoardRight() {
        setBoardCanMove();
        manager.move("RIGHT");
        int[][] expectedRows = {{0, 0, 4, 4}, {0, 0, 4, 4}, {0, 0, 4, 4}, {0, 0, 4, 4}};
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedRows[i], tileToInt(board.getRow(i)));
        }
        int[][] expectedColumns = {{0, 0, 0, 0}, {0, 0, 0, 0}, {4, 4, 4, 4}, {4, 4, 4, 4}};
        assertTrue(board.isChanged());
        for (int i = 0; i < board.getDimension(); i++) {
            assertArrayEquals(expectedColumns[i], tileToInt(board.getColumn(i)));
        }
        assertEquals(32, manager.getScore());
    }
}