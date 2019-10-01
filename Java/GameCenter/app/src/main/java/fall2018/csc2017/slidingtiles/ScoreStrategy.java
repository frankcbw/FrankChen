package fall2018.csc2017.slidingtiles;

public interface ScoreStrategy {
    /**
     * calculate score
     *
     * @param o object needed to be scored
     * @return score in integer
     */

    int calculateScore(Object o);
}
