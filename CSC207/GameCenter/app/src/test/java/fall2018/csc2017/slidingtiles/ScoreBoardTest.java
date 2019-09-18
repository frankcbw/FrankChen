package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for java class scoreBoard.
 */
public class ScoreBoardTest {

    /**
     * This scoreboard to test.
     */
    private ScoreBoard scoreBoard;

    /**
     * Set up the test.
     */
    private void setUpScoreBoard(String game) {
        scoreBoard = new ScoreBoard(game);
    }

    /**
     * Test the add and sort function when new score is higher than all other scores in the
     * rank.
     */
    @Test
    public void testAddAndSortRankedFirst() {
        setUpScoreBoard("test"); //Create a ScoreBoard without ScoreStrategy
        Object[] originalScore = {"Player", 100};
        Object[] newScore = {"Player", 102};
        List<Object[]> expected = new ArrayList<>(
                Arrays.asList(newScore, originalScore)
        );
        List<Object[]> actual = new ArrayList<>();
        actual.add(originalScore);
        scoreBoard.setScoreList(actual);
        scoreBoard.addAndSort(newScore);
        for (int i = 0; i < expected.size(); i++) {
            int expectedScore = (int) expected.get(i)[1];
            int actualScore = (int) scoreBoard.getScoreList().get(i)[1];
            assertEquals(expectedScore, actualScore);
            String expectedName = (String) expected.get(i)[0];
            String actualName = (String) scoreBoard.getScoreList().get(i)[0];
            assertEquals(expectedName, actualName);
        }
    }

    /**
     * Test the add and sort function when new score is the lowest in the rank.
     */
    @Test
    public void testAddAndSortRankedLast() {
        setUpScoreBoard("test");
        Object[] originalScore = {"Player", 100};
        Object[] score = {"Player", 102};
        Object[] newScore = {"Player", 99};
        List<Object[]> actual = new ArrayList<>(
                Arrays.asList(score, originalScore)
        );
        List<Object[]> expected = new ArrayList<>(
                Arrays.asList(score, originalScore, newScore)
        );
        scoreBoard.setScoreList(actual);
        scoreBoard.addAndSort(newScore);
        for (int i = 0; i < expected.size(); i++) {
            int expectedScore = (int) expected.get(i)[1];
            int actualScore = (int) scoreBoard.getScoreList().get(i)[1];
            assertEquals(expectedScore, actualScore);
            String expectedName = (String) expected.get(i)[0];
            String actualName = (String) scoreBoard.getScoreList().get(i)[0];
            for (int j = 0; j < Math.min(expectedName.length(), actualName.length()); j++) {
                assertEquals(expectedName.charAt(j), actualName.charAt(j));
            }
        }
    }

    /**
     * Test the add and sort function in the case that new score is in the middle of the rank.
     */
    @Test
    public void testAddAndSortRankedMiddle() {
        setUpScoreBoard("test");
        Object[] originalScore = {"Player", 100};
        Object[] score1 = {"Player", 102};
        Object[] score2 = {"Player", 99};
        Object[] newScore = {"Player", 101};
        List<Object[]> actual = new ArrayList<>(
                Arrays.asList(score1, originalScore, score2)
        );
        List<Object[]> expected = new ArrayList<>(
                Arrays.asList(score1, newScore, originalScore, score2)
        );
        scoreBoard.setScoreList(actual);
        scoreBoard.addAndSort(newScore);
        for (int i = 0; i < expected.size(); i++) {
            int expectedScore = (int) expected.get(i)[1];
            int actualScore = (int) scoreBoard.getScoreList().get(i)[1];
            assertEquals(expectedScore, actualScore);
            String expectedName = (String) expected.get(i)[0];
            String actualName = (String) scoreBoard.getScoreList().get(i)[0];
            for (int j = 0; j < Math.min(expectedName.length(), actualName.length()); j++) {
                assertEquals(expectedName.charAt(j), actualName.charAt(j));
            }
        }
    }

    /**
     * Test the add and sort function when new score is lower than all other scores on the scoreboard
     * and the scoreboard has reached its size.
     */
    @Test
    public void testAddAndSortNoRank() {
        setUpScoreBoard("test");
        Object[] originalScore = {"Lucia", 100};
        Object[] score1 = {"Lucia", 102};
        Object[] score2 = {"Xinyi", 99};
        Object[] score3 = {"May", 101};
        Object[] newScore = {"Xinyi Zhang", 70};
        List<Object[]> actual = new ArrayList<>(
                Arrays.asList(score1, score3, originalScore, score2)
        );
        List<Object[]> expected = new ArrayList<>(
                Arrays.asList(score1, score3, originalScore, score2)
        );
        scoreBoard.setScoreList(actual);
        scoreBoard.setScoreBoardSize(4);
        scoreBoard.addAndSort(newScore);
        for (int i = 0; i < expected.size(); i++) {
            int expectedScore = (int) expected.get(i)[1];
            int actualScore = (int) scoreBoard.getScoreList().get(i)[1];
            assertEquals(expectedScore, actualScore);
            String expectedName = (String) expected.get(i)[0];
            String actualName = (String) scoreBoard.getScoreList().get(i)[0];
            for (int j = 0; j < Math.min(expectedName.length(), actualName.length()); j++) {
                assertEquals(expectedName.charAt(j), actualName.charAt(j));
            }
        }
    }
}