package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The Builder for the Board class.
 */
public class BuilderBoard {
    /**
     * The dimension of the Builder
     */
    private int dimension;

    /**
     * The number of mines of the Builder
     */
    private int mine;

    /**
     * The number of mines left of the Builder
     */
    private int mineLeft;

    /**
     * The List of Tiles of the Builder.
     */
    private List<Tile> tiles = new ArrayList<>();

    /**
     * The List of Mine's position of the Builder.
     */
    private List<MineTile> minePosition = new ArrayList<>();

    /**
     * Sets the dimension of the Builder.
     * @param dimension the dimension to be set.
     * @return the builder.
     */
    public BuilderBoard setDimension(int dimension) {
        this.dimension = dimension;
        return this;
    }

    /**
     * Sets the number of Mines of the Builder.
     * @param mine the number of Mines to be set.
     * @return the builder.
     */
    public BuilderBoard setMine(int mine) {
        this.mine = mine;
        return this;
    }

    /**
     * Sets the number of Mines left of the Builder.
     * @param mineLeft the number of Mines left to be set.
     * @return the builder.
     */
    public BuilderBoard setMineLeft(int mineLeft) {
        this.mineLeft = mineLeft;
        return this;
    }

    /**
     * set the List of Tiles to tiles.
     * @param tiles the tiles to be set
     * @return the Builder.
     */
    public BuilderBoard setTiles(List<Tile> tiles) {
        this.tiles = tiles;
        return this;
    }

    /**
     * set the List of MineTiles to tiles.
     * @param tiles the list of MileTiles to be set
     * @return the Builder.
     */
    BuilderBoard setMineTiles(List<MineTile> tiles) {
        for (int i = 0; i < dimension * dimension; i++) {
            this.tiles.add(tiles.get(i));
            if (tiles.get(i).isMine()) {
                minePosition.add(tiles.get(i));
            }
        }
        return this;
    }

    /**
     * set the List of 2048Tiles to tiles.
     * @return the Builder.
     */
    private BuilderBoard set2048Tiles() {
        tiles = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            tiles.add(new Game2048Tile());

        }
        return this;
    }

    /**
     * Set the list of MineTiles.
     * @return
     */
    BuilderBoard setMineTiles() {
        for (int i = 0; i < dimension * dimension; i++) {
            MineTile tile = new MineTile();
            tiles.add(tile);
            tile.setPosition(i);
            tile.setBackground();
        }
        return this;
    }

    /**
     * Builder a new Sliding Board.
     * @return the new Sliding Board built.
     */
    public SlidingBoard buildSlidingBoard() {
        SlidingBoard slidingBoard = new SlidingBoard();
        slidingBoard.setDimension(dimension);
        slidingBoard.setTiles((List<SlidingTile>) (List<?>) tiles);
        return slidingBoard;
    }

    /**
     * Builder a new 2048 Board.
     * @return the new 2048 Board built.
     */
    public Game2048Board build2048Board() {
        Game2048Board game2048Board = new Game2048Board();
        set2048Tiles();
        game2048Board.setDimension(4);
        game2048Board.setTiles((List<Game2048Tile>) (List<?>) tiles);
        return game2048Board;
    }

    /**
     * Builder a Mine Board.
     * @return the new Mine Board built.
     */
    public MineBoard buildMineBoard() {
        MineBoard mineBoard = new MineBoard();
        mineBoard.setDimension(dimension);
        mineBoard.setMineNum(mine);
        mineBoard.setMineLeft(mineLeft);
        mineBoard.setTiles((List<MineTile>) (List<?>) tiles);
        mineBoard.setMinePosition(minePosition);
        return mineBoard;
    }
}
