package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * The Memory class
 *
 * @param <T>
 */
abstract class Memory<T> implements Serializable {

    /**
     * Takes in a BoardManager and store all the information needed for clone the BoardManager.
     *
     * @param manager the BoardManager to be cloned
     */
    abstract void makeCopy(T manager);

    /**
     * Return a new BoardManager cloned from the manager in makeCopy().
     *
     * @return a new BoardManager cloned from the manager in makeCopy()
     */
    abstract T copy();


}
