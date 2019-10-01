package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

public class Game2048Tile extends Tile implements Serializable {
    private int value;

    private boolean fadeIn;

    /**
     * Construct a new Tile with value 0.
     */
    Game2048Tile() {
        this.background = R.drawable.tile_2048_0;
        this.value = 0;
        fadeIn = false;
    }

    /**
     * @return Whether the tile is empty.
     */
    boolean isEmpty() {
        return value == 0;
    }

    /**
     * Set value 2 or 4 to a tile.
     * 80% probability set value 2 to the tile.
     * 20% probability set value 4 to the tile.
     */
    void random() {
        double prob = Math.random();
        if (prob <= 0.2) {
            this.value = 4;
            setBackground();
        } else {
            this.value = 2;
            setBackground();
        }
    }

    @Override
    void setBackground() {
        switch (value) {
            case 0:
                background = R.drawable.tile_2048_0;
                break;

            case 2:
                background = R.drawable.tile_2048_2;
                break;

            case 4:
                background = R.drawable.tile_2048_4;
                break;

            case 8:
                background = R.drawable.tile_2048_8;
                break;

            case 16:
                background = R.drawable.tile_2048_16;
                break;

            case 32:
                background = R.drawable.tile_2048_32;
                break;

            case 64:
                background = R.drawable.tile_2048_64;
                break;

            case 128:
                background = R.drawable.tile_2048_128;
                break;

            case 256:
                background = R.drawable.tile_2048_256;
                break;

            case 512:
                background = R.drawable.tile_2048_512;
                break;

            case 1024:
                background = R.drawable.tile_2048_1024;
                break;

            case 2048:
                background = R.drawable.tile_2048_2048;
                break;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        setBackground();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Game2048Tile)) {
            return false;
        } else {
            return ((Game2048Tile) obj).getValue() == this.value;
        }
    }

    void setAnimation() {
        fadeIn = true;
    }

    boolean getFadeIn() {
        return fadeIn;
    }


    void removeFadeIn() {
        fadeIn = false;
    }
}

