package fall2018.csc2017.slidingtiles;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MineTest {

    private MineBoard board;
    private MineBoardManager manager;

    /**
     * Set up a new MineBoardManager with 5 mines and dimension being 5, and make a tap move at
     * position 12.
     */
    @Before
    public void setUp() {
        Factory f = new Factory();
        board = new BuilderBoard().setMine(5).setMineLeft(5).setDimension(5).setMineTiles()
                .buildMineBoard();
        manager = (MineBoardManager) f.createNewManager(board);
        manager.move(12);
    }

    /**
     * Test getSurround() function.
     */
    @Test
    public void testGetSurround() {
        setUp();
        // test getSurround at four corns.
        assertEquals(3, board.getSurround(0).size());
        assertEquals(3, board.getSurround(4).size());
        assertEquals(3,board.getSurround(20).size());
        assertEquals(3, board.getSurround(24).size());
        // test getSurround at the centre.
        assertEquals(8, board.getSurround(12).size());
        // test each tile returned by getSurround is located around the tile at the position
        for (MineTile tile : board.getSurround(12)) {
            int tileRow = tile.getPosition() / board.getDimension();
            int tileCol = tile.getPosition() % board.getDimension();
            int row = 12 / board.getDimension();
            int col = 12 % board.getDimension();
            assertTrue(tileRow - row <= 1 && tileRow - row >= -1 && tileCol - col <= 1 &&
                    tileCol - col >= -1);
        }
    }

    /**
     * Test the first tap move. Mines are randomly generated but they should not be inside
     * the surrounding of the tile at the position.
     */
    @Test
    public void testFirstMove() {
        setUp();
        assertEquals(board.getMineNum(), 5);
        // test if the correct number of mines are generated
        assertEquals(board.getMinePosition().size(), 5);
        // test the current position tile cannot be a mine
        assertEquals(board.getTile(12).getNumber(), 0);
        // test the surrounding tiles cannot be mines
        for (MineTile tile : board.getSurround(12)) {
            assertFalse(tile.isMine());
        }
        // test every mine in MinePosition is a mine
        for (MineTile tile : board.getMinePosition()) {
            assertTrue(tile.isMine());
        }
    }

    /**
     * Test marking a flag.
     */
    @Test
    public void testFlag() {
        setUp();
        manager.mark(12);
        assertEquals(board.getMineLeft(), board.getMineNum() - 1);
        manager.mark(12);
        assertEquals(board.getMineLeft(), board.getMineNum());
    }

    /**
     * Test tapping on a mine.
     */
    @Test
    public void testTapOnMine() {
        setUp();
        manager.move(board.getMinePosition().get(0).getPosition());
        // test game over
        assertEquals(manager.isLost(), true);
        // test mines background updating
        assertEquals(board.getMinePosition().get(0).getBackground(), R.drawable.mine_b);
        assertEquals(board.getMinePosition().get(1).getBackground(), R.drawable.mine_b2);
    }

    /**
     * Test if numbers are correctly set on each tile. -1 for mines, number of mines at surrounding
     * for others
     */
    @Test
    public void testMineNumOfTiles() {
        setUp();
        for (MineTile tile : manager.getTiles()) {
            if (tile.isMine()) {
                assertEquals(tile.getNumber(), -1);
            } else {
                int mineNum = 0;
                for (MineTile surroundTile : board.getSurround(tile.getPosition())) {
                    if (surroundTile.isMine()) {
                        mineNum++;
                    }
                }
                assertEquals(tile.getNumber(), mineNum);
            }
        }
    }

    /**
     * Test a game is finished when all none-mine tiles are revealed.
     */
    @Test
    public void testIsWon() {
        setUp();
        for (MineTile tile : manager.getTiles()) {
            if (!tile.isMine()) {
                board.reveal(tile.getPosition());
            }
        }
        assertTrue(manager.isWon());
    }
}