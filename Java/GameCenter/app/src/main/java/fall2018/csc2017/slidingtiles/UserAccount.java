package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represent a user's user account.
 */
public class UserAccount implements Serializable {

    /**
     * The username of the UserAccount
     */
    private String name;

    /**
     * The password of the UserAccount.
     */
    private String password;

    /**
     * The age of the UserAccount.
     */
    private Integer age;

    /**
     * The email of the UserAccount.
     */
    private String email;


    /**
     * The sliding slidingTiles game history of the UserAccount.
     */
    private HashMap<String, SlidingMemory> historySliding;

    private HashMap<String, MineMemory> historyMine;

    private HashMap<String, Game2048Memory> history2048;

    /**
     * The archive of the game sliding slidingTiles.
     */
    private HashMap<String, ScoreBoard> personalScoreBoard;

    /**
     * The games the UserAccount has purchased.
     */
    private ArrayList<String> games;

    /**
     * The UserAccount class constructor.
     */
    public UserAccount() {
    }

    /**
     * The getter of the field email
     *
     * @return the email of the UserAccount
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setter of the field email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The getter of the field age.
     *
     * @return the age of the UserAccount.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * The setter of the field age.
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * The getter for the username of the UserAccount.
     *
     * @return the username
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter for the password of the UserAccount.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Change the password of the UserAccount.
     *
     * @param ps The new password to be updated.
     */
    void setPassword(String ps) {
        this.password = ps;
    }

    public void setSlideHistory(String key, SlidingBoardManager item) {
        if (item != null) {
            SlidingMemory memory = new SlidingMemory();
            memory.makeCopy(item);
            if (historySliding.get(key) == null) {
                historySliding.put(key, memory);
            } else {
                historySliding.replace(key, memory);
            }
        } else {
            historySliding.replace(key, null);
        }
    }

    public void setGame2048History(String key, Game2048BoardManager item) {
        if (item != null) {
            Game2048Memory memory = new Game2048Memory();
            memory.makeCopy(item);
            if (history2048.get(key) == null) {
                history2048.put(key, memory);
            } else {
                history2048.replace(key, memory);
            }
        } else {
            history2048.replace(key, null);
        }
    }

    void setMineHistory(String key, MineBoardManager item) {
        if (item != null) {
            MineMemory memory = new MineMemory();
            memory.makeCopy(item);
            if (historyMine.get(key) == null) {
                historyMine.put(key, memory);
            } else {
                historyMine.replace(key, memory);
            }
        } else {
            historyMine.replace(key, null);
        }
    }

    /**
     * The getter for the SlidingHistory of the UserAccount.
     *
     * @return the SlidingHistory
     */
    private HashMap<String, SlidingMemory> getSlideHistory() {
        return historySliding;
    }

    private HashMap<String, MineMemory> getMineHistory() {
        return historyMine;
    }

    private HashMap<String, Game2048Memory> get2048History() {
        return history2048;
    }

    /**
     * Get the right size of ScoreBoard wanted.
     *
     * @param tag the size of the ScoreBoard.
     * @return the ScoreBoard wanted
     */
    public ScoreBoard getScoreBoard(String tag) {
        return personalScoreBoard.get(tag);
    }

    /**
     * Add a new game to the UserAccount.
     *
     * @param game The name of the new game being added.
     */
    public void addGames(String game) {
        this.games.add(game);
    }

    public SlidingBoardManager getSpecificSlideHistory(String key) {
        SlidingMemory memory = this.getSlideHistory().get(key);
        if (memory == null) {
            return null;
        }
        return memory.copy();
    }

    MineBoardManager getSpecificMineHistory(String key) {
        MineMemory memory = this.getMineHistory().get(key);
        if (memory == null) {
            return null;
        }
        return memory.copy();
    }

    public Game2048BoardManager getSpecific2048History(String key) {
        Game2048Memory memory = this.get2048History().get(key);
        if (memory == null) {
            return null;
        }
        return memory.copy();
    }

    public ArrayList<String> getGames() {
        return games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }

    void setHistorySliding(HashMap<String, SlidingMemory> historySliding) {
        this.historySliding = historySliding;
    }

    void setHistoryMine(HashMap<String, MineMemory> historyMine) {
        this.historyMine = historyMine;
    }

    void setHistory2048(HashMap<String, Game2048Memory> history2048) {
        this.history2048 = history2048;
    }

    public void setPersonalScoreBoard(HashMap<String, ScoreBoard> personalScoreBoard) {
        this.personalScoreBoard = personalScoreBoard;
    }
}
