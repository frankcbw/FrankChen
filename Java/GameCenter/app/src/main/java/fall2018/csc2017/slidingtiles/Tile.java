package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * The Tile class.
 */
abstract public class Tile implements Serializable {
    /**
     * Background photo id.
     */
    int background;

    /**
     * Return background.
     *
     * @return background
     */
    int getBackground() {
        return background;
    }

    abstract void setBackground();
}
