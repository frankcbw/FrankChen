package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The class extending Memory stores information of SlidingBoardManager and makes copy of it.
 */
class SlidingMemory extends Memory<SlidingBoardManager> {

    /**
     * The dimension of the SlidingBoardManager cloned.
     */
    private int dimension;

    /**
     * The time of playing of the SlidingBoardManager cloned.
     */
    private double timeSliding;

    /**
     * The list of tiles in the SlidingBoardManager cloned.
     */
    private List<Integer> slidingTiles = new ArrayList<>();

    @Override
    void makeCopy(SlidingBoardManager manager) {
        this.dimension = manager.getBoard().getDimension();
        this.timeSliding = manager.getTime();
        for (SlidingTile tile : manager.getTiles()) {
            this.slidingTiles.add(tile.getId());
        }
    }

    @Override
    SlidingBoardManager copy() {

        int dimension = this.dimension;
        double time = this.timeSliding;
        List<SlidingTile> tt = new ArrayList<>();
        for (int i : this.slidingTiles) {
            tt.add(new SlidingTile(i));
        }
        Factory f = new Factory();
        return f.loadSlidingManager(dimension, time, tt);
    }

    /**
     * Return the dimension.
     *
     * @return the dimension
     */
    int getDimension() {
        return dimension;
    }

    /**
     * Return the timeSliding.
     *
     * @return the timeSliding.
     */
    double getTimeSliding() {
        return timeSliding;
    }

    /**
     * Return the slidingTiles.
     *
     * @return the slidingTiles
     */
    List<Integer> getSlidingTiles() {
        return slidingTiles;
    }

}
