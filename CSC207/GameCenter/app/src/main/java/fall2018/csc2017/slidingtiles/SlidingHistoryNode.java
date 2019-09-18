package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * The SlidingHistoryNode.
 */
class SlidingHistoryNode implements Serializable {

    /**
     * The next SlidingHistoryNode linked.
     */
    SlidingHistoryNode next;

    /**
     * the data that the node stores.
     */
    private int[] data;

    /**
     * A new SlidingHistoryNode with data being dat.
     *
     * @param dat the data that the node stores
     */
    SlidingHistoryNode(int[] dat) {
        this.data = dat;
        this.next = null;
    }

    /**
     * Return the data.
     *
     * @return the data
     */
    int[] getData() {
        return this.data;
    }
}
