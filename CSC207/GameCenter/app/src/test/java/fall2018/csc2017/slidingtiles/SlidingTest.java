package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SlidingTest {

    /**
     * Use builder factory to set up sliding board and sliding manager to test.
     */
    private SlidingBoardManager setUpBoard(int dimension, List<SlidingTile> tiles) {
        Factory factory = new Factory();
        SlidingBoard board = new BuilderBoard().setDimension(dimension).buildSlidingBoard();
        SlidingBoardManager manager = (SlidingBoardManager) factory.createNewManager(board);
        manager.setTiles(tiles);
        board.setDimension(dimension);
        return manager;
    }

    /**
     * Test the node of history of sliding.
     */
    @Test
    public void testSlidingHistoryNode() {
        int[] result = new int[2];
        result[0] = 0;
        result[1] = 1;
        SlidingHistoryNode node = new SlidingHistoryNode(result);
        assertEquals(node.getData(), result);
        assertNull(node.next);
    }

    /**
     * Test the linked list of history function of sliding.
     */
    @Test
    public void testSlidingHistory() {
        int[] result1 = new int[2];
        result1[0] = 0;
        result1[1] = 1;
        int[] result2 = new int[2];
        result2[0] = 0;
        result2[1] = 2;
        SlidingHistoryNode node1 = new SlidingHistoryNode(result1);
        SlidingHistoryNode node2 = new SlidingHistoryNode(result2);
        SlidingHistory history = new SlidingHistory();
        assertEquals(history.getSize(), 0);
        history.add(node1);
        history.add(node2);
        assertEquals(history.getSize(), 2);
        assertEquals(history.get(0), node1);
        history.remove(0);
        assertEquals(history.getSize(), 0);
    }

    /**
     * Test whether findInversion returns the number of inversion when there's no inversion.
     */
    @Test
    public void testDimensionThreeNoInversion() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(0), new SlidingTile(1),
                        new SlidingTile(2), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertEquals(0, testCase.findInversion());
        assertEquals(3, testCase.getBoard().getDimension());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's only one inversion.
     */
    @Test
    public void testOneInversion() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(0), new SlidingTile(2),
                        new SlidingTile(1), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertEquals(1, testCase.findInversion());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's no inversion and the
     * last tile is number zero (empty) tile.
     */
    @Test
    public void testNoInversionEndWithZero() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(7), new SlidingTile(8), new SlidingTile(0)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertEquals(0, testCase.findInversion());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's no inversion and the
     * empty tile (number 0) is in the middle.
     */
    @Test
    public void testOneInversionZeroInMiddle() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(0), new SlidingTile(8), new SlidingTile(7)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertEquals(1, testCase.findInversion());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's more than one inversions.
     */
    @Test
    public void testMultipleInversions() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(8), new SlidingTile(0),
                        new SlidingTile(6), new SlidingTile(7),
                        new SlidingTile(5), new SlidingTile(4),
                        new SlidingTile(2), new SlidingTile(3), new SlidingTile(1)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertEquals(26, testCase.findInversion());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's no inversion and the
     * size of the board is 4*4. Test whether the manager returns the right dimension.
     */
    @Test
    public void testDimensionFourInversions() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(7), new SlidingTile(8),
                        new SlidingTile(0), new SlidingTile(9),
                        new SlidingTile(10), new SlidingTile(11),
                        new SlidingTile(12), new SlidingTile(13),
                        new SlidingTile(14), new SlidingTile(15)
                ));
        SlidingBoardManager testCase = setUpBoard(4, tilesToTest);
        assertEquals(0, testCase.findInversion());
        assertEquals(4, testCase.getBoard().getDimension());
    }

    /**
     * Test whether findInversion returns the number of inversion when there's no inversion and the
     * size of the board is 5*5. Test whether the manager returns the right dimension.
     */
    @Test
    public void testDimensionFiveInversions() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(7), new SlidingTile(8),
                        new SlidingTile(0), new SlidingTile(9),
                        new SlidingTile(10), new SlidingTile(11),
                        new SlidingTile(12), new SlidingTile(13),
                        new SlidingTile(14), new SlidingTile(15),
                        new SlidingTile(16), new SlidingTile(17),
                        new SlidingTile(18), new SlidingTile(19),
                        new SlidingTile(20), new SlidingTile(21),
                        new SlidingTile(22), new SlidingTile(23),
                        new SlidingTile(24)
                ));
        SlidingBoardManager testCase = setUpBoard(5, tilesToTest);
        assertEquals(0, testCase.findInversion());
        assertEquals(5, testCase.getBoard().getDimension());
    }

    /**
     * Test whether isWon returns false when all tiles are in row majored order but the empty tile
     * is at the beginning.
     */
    @Test
    public void testNotWonZeroAtBeginning() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(0), new SlidingTile(1),
                        new SlidingTile(2), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertFalse(testCase.isWon());
    }

    /**
     * Test whether isWon returns false when all tiles are in row majored order but the empty tile
     * is in the middle.
     */
    @Test
    public void testNotWonZeroInMiddle() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(0),
                        new SlidingTile(2), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertFalse(testCase.isWon());
    }

    /**
     * Test whether isWon returns true when all tiles are in row majored order but the empty tile
     * is at the end
     */
    @Test
    public void testWonZeroAtEnd() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(7), new SlidingTile(8), new SlidingTile(0)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertTrue(testCase.isWon());
    }

    /**
     * Test whether isWon returns false when there is one inversion.
     */
    @Test
    public void testNotWonOneInversion() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(5), new SlidingTile(6),
                        new SlidingTile(0), new SlidingTile(8), new SlidingTile(7)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertFalse(testCase.isWon());
    }

    /**
     * Test whether isWon returns false when there is more than one inversion.
     */
    @Test
    public void testNotWonMoreInversions() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(8), new SlidingTile(0),
                        new SlidingTile(6), new SlidingTile(7),
                        new SlidingTile(5), new SlidingTile(4),
                        new SlidingTile(2), new SlidingTile(3), new SlidingTile(1)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        assertFalse(testCase.isWon());
    }

    /**
     * Test calling touchMove to swap the empty tile with the tile on its right, when the empty tile
     * is on the corner of the board.
     */
    @Test
    public void testMoveCorner() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(0), new SlidingTile(1),
                        new SlidingTile(2), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        testCase.move(1);
        List<SlidingTile> tiles = testCase.getTiles();
        int[] expected = {1, 0, 2, 3, 4, 5, 6, 7, 8};
        int[] actual = new int[tilesToTest.size()];
        for (int i = 0; i < tilesToTest.size(); i++) {
            actual[i] = tiles.get(i).getId();
        }
        assertArrayEquals(expected, actual);
        assertTrue(testCase.getBoard().hasChanged());
    }

    /**
     * Test calling touchMove to swap the empty tile with the tile on its left, when the empty tile
     * is on the edge of the board.
     */
    @Test
    public void testMoveSide() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(0),
                        new SlidingTile(2), new SlidingTile(3),
                        new SlidingTile(4), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        testCase.move(0);
        List<SlidingTile> tiles = testCase.getTiles();
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int[] actual = new int[tilesToTest.size()];
        for (int i = 0; i < tilesToTest.size(); i++) {
            actual[i] = tiles.get(i).getId();
        }
        assertArrayEquals(expected, actual);
        assertTrue(testCase.getBoard().hasChanged());
    }

    /**
     * Test calling touchMove to swap the empty tile with the tile above it, when the empty tile
     * is in the middle of the board.
     */
    @Test
    public void testMoveMiddle() {
        ArrayList<SlidingTile> tilesToTest = new ArrayList<>(
                Arrays.asList(new SlidingTile(1), new SlidingTile(2),
                        new SlidingTile(3), new SlidingTile(4),
                        new SlidingTile(0), new SlidingTile(5),
                        new SlidingTile(6), new SlidingTile(7), new SlidingTile(8)
                ));
        SlidingBoardManager testCase = setUpBoard(3, tilesToTest);
        testCase.move(1);
        List<SlidingTile> tiles = testCase.getTiles();
        int[] expected = {1, 0, 3, 4, 2, 5, 6, 7, 8};
        int[] actual = new int[tilesToTest.size()];
        for (int i = 0; i < tilesToTest.size(); i++) {
            actual[i] = tiles.get(i).getId();
        }
        assertArrayEquals(expected, actual);
        assertTrue(testCase.getBoard().hasChanged());
    }
}