package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The scoreBoard to display rankings.
 */
public class ScoreBoard implements Serializable {
    private int scoreBoardSize;
    private List<Object[]> scoreList = new ArrayList<>();
    private ScoreStrategy scoreStrategy;
    private ScoreSorter scoreSorter;

    public ScoreBoard(String name) {
        setScoreBoardSize(10);
        setScoreSorter(new ScoreSorter());
        switch (name) {
            case "2048":
                setScoreStrategy(new Game2048ScoreStrategy());
                break;
            case "MineSweeper":
                setScoreStrategy(new MineScoreStrategy());
                break;
            case "SlidingTiles":
                setScoreStrategy(new SlidingScoreStrategy());
                break;
        }
    }

    /**
     * Set the scoreBoardSize to size.
     *
     * @param size the new size to set
     */
    void setScoreBoardSize(int size) {
        scoreBoardSize = size;
    }

    /**
     * Return a score calculated by using slidscoreStrategy.
     *
     * @param boardBoardManager the boardBoardManager that is finished and to be calculated for score
     * @return a score calculated by using slidscoreStrategy
     */
    int calculateScore(BoardManager boardBoardManager) {
        return scoreStrategy.calculateScore(boardBoardManager);
    }

    /**
     * Set the slidscoreStrategy to strategy. Open to modification.
     *
     * @param strategy the ScoreStrategy to set
     */
    private void setScoreStrategy(ScoreStrategy strategy) {
        scoreStrategy = strategy;
    }

    /**
     * Set the scoreSorter to sorter. Open to modification.
     *
     * @param sorter the Sorter to set
     */
    private void setScoreSorter(ScoreSorter sorter) {
        scoreSorter = sorter;
    }

    /**
     * Add a new result to the scoreList and sort the list to get the new ranking.
     *
     * @param scoreArray the new result to add
     */
    public void addAndSort(Object[] scoreArray) {
        scoreList.add(scoreArray);
        scoreSorter.sort(scoreList);
        if (scoreList.size() == scoreBoardSize + 1) {
            scoreList.remove(scoreBoardSize);
        }
    }

    /**
     * Return the scoreList.
     *
     * @return the scoreList
     */
    List<Object[]> getScoreList() {
        return scoreList;
    }

    void setScoreList(List<Object[]> nameAndScoreList) {
        scoreList = nameAndScoreList;
    }

}


