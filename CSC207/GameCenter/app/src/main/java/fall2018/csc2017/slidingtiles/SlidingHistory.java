package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

/**
 * The SlidingHistory linked list.
 */
public class SlidingHistory implements Serializable {

    /**
     * The front of the SlidingHistory.
     */
    private SlidingHistoryNode front = null;

    /**
     * The back of the SlidingHistory.
     */
    private SlidingHistoryNode back = null;

    /**
     * The size of the SlidingHistory.
     */
    private int size = 0;

    /**
     * Return the size of the SlidingHistory.
     *
     * @return the size of the SlidingHistory
     */
    public int getSize() {
        return size;
    }

    /**
     * Return the SlidingHistoryNode at the index of the SlidingHistory.
     *
     * @param index the index of the SlidingHistoryNode returned
     * @return the SlidingHistoryNode at the index
     */
    public SlidingHistoryNode get(int index) {
        int count = 0;
        SlidingHistoryNode curr = this.front;
        while (count < index) {
            curr = curr.next;
            count++;
        }
        return curr;
    }

    /**
     * Add a new SlidingHistoryNode node to the end of the SlidingHistory.
     *
     * @param node the new SlidingHistoryNode to be added
     */
    public void add(SlidingHistoryNode node) {
        if (this.size == 0) {
            this.front = node;
            this.back = node;
            this.size++;
        } else {
            this.back.next = node;
            this.back = node;
            this.size++;
        }
    }

    /**
     * Clear the SlidingHistory.
     */
    private void clear() {
        if (size != 0) {
            front = null;
            back = null;
            size = 0;
        }
    }

    /**
     * Remove the SlidingHistoryNode at the index of the SlidingHistory.
     *
     * @param index the index of the SlidingHistory where the SlidingHistoryNode to be removed
     */
    void remove(int index) {
        if (index == 0) {
            clear();
        } else if (index == this.size - 1) {
            this.back = this.get(this.size - 2);
            this.back.next = null;
            this.size--;
        } else {
            this.back = this.get(index - 1);
            this.back.next = null;
            this.size = index;
        }
    }
}
