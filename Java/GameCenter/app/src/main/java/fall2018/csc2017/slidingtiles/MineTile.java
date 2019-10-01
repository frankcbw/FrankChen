package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

public class MineTile extends Tile implements Serializable {
    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * Whether the tile is a mine or flagged or obscured.
     */
    private boolean isMine, isFlagged, isObscured;

    /**
     * The number of mines around the tile. -1 if the tile is a mine.
     */
    private int number;

    /**
     * The position of the tile in a MineBoard.
     */
    private int position;

    /**
     * Construct a new Mine Tile.
     */
    MineTile() {
        isMine = false;
        isFlagged = false;
        isObscured = true;
        number = 0;
    }

    /**
     * Set Parameter values to a new Mine Tile.
     *
     * @param isObscured whether the tile is obscured
     * @param number     if the tile has mine number is -1, otherwise number equals surrounding mine numbers.
     * @param isMine     whether the tile has mine
     * @param isFlagged  whether the tile is flagged
     */
    MineTile(boolean isObscured, int number, boolean isMine, boolean isFlagged) {
        this.isObscured = isObscured;
        this.number = number;
        this.isMine = isMine;
        this.isFlagged = isFlagged;
        setBackground();
    }

    void setBackground() {
        if (!isObscured()) {
            switch (number) {
                case -1:
                    background = R.drawable.mine_b;
                    break;
                case 0:
                    background = R.drawable.mine_base;
                    break;
                case 1:
                    background = R.drawable.mine_1;
                    break;
                case 2:
                    background = R.drawable.mine_2;
                    break;
                case 3:
                    background = R.drawable.mine_3;
                    break;
                case 4:
                    background = R.drawable.mine_4;
                    break;
                case 5:
                    background = R.drawable.mine_5;
                    break;
                case 6:
                    background = R.drawable.mine_6;
                    break;
                case 7:
                    background = R.drawable.mine_7;
                    break;
                case 8:
                    background = R.drawable.mine_8;
                    break;
            }
        } else if (isFlagged()) {
            background = R.drawable.mine_d;
        } else {
            background = R.drawable.mine_mask;
        }
    }

    void showMine() {
        if (isObscured()) {
            background = R.drawable.mine_b2;
        } else {
            background = R.drawable.mine_b;
        }
    }

    public int getBackground() {
        return background;
    }

    /**
     * Set a mine to the tile.
     */
    void setMine() {
        isMine = true;
        number = -1;
        setBackground();
    }

    /**
     * Set a flag to the tile.
     */
    void flag() {
        isFlagged = !isFlagged;
        setBackground();
    }

    /**
     * Show the inner tile to the player.
     */
    void reveal() {
        isObscured = false;
        setBackground();
    }

    /**
     * @return whether the tile has mine
     */
    boolean isMine() {
        return isMine;
    }

    /**
     * @return whether the tile is flagged
     */
    boolean isFlagged() {
        return isFlagged;
    }

    /**
     * @return whether the tile is obscured
     */
    boolean isObscured() {
        return isObscured;
    }

    int getNumber() {
        return number;
    }

    public void setNumber(int i) {
        number = i;
        setBackground();
    }

    int getPosition() {
        return position;
    }

    /**
     * Set the tile position.
     *
     * @param position tile position.
     */
    void setPosition(int position) {
        this.position = position;
    }
}
