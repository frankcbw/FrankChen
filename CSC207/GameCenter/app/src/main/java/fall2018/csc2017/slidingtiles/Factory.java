package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Factory {

    public Factory() {
    }

    /**
     * Create a new BoardManager corresponding to the type of Board b.
     * @param b the Board which a new BoardManager is built
     * @return the new-built BoardManager
     */
    public BoardManager createNewManager(Board b) {
        if (b instanceof MineBoard) {
            MineBoardManager m = new MineBoardManager();
            m.setTiles(b.getTiles());
            m.setDimension(b.getDimension());
            m.setMinePosition(((MineBoard) b).getMinePosition());
            m.setBoard((MineBoard) b);
            m.setUpBoard();
            return m;
        }
        if (b instanceof Game2048Board) {
            Game2048BoardManager m = new Game2048BoardManager();
            m.setDimension(4);
            m.setBoard((Game2048Board) b);
            ((Game2048Board) b).addTile();
            ((Game2048Board) b).addTile();
            m.setTiles(b.getTiles());
            return m;
        }
        if (b instanceof SlidingBoard) {
            SlidingBoardManager m = new SlidingBoardManager();
            int dimension = (b.getDimension());
            m.setDimension(dimension);
            m.setBoard((SlidingBoard) b);
            m.addTile();
            m.shuffle();
            b.setTiles(m.getTiles());
            m.getSlidingHistory().add(new SlidingHistoryNode(m.findBlankIndex()));
            return m;
        }
        return null;
    }

    /**
     * Rebuild a Game2048BoardManager.
     * @param time time of playing of the Game2048BoardManager
     * @param score score of the Game2048BoardManager
     * @param list list of Integer representing number of tiles in the Game2048BoardManager
     * @return new-built Game2048BoardManager.
     */
    Game2048BoardManager Load2048Manager(double time, int score, List<Integer> list) {
        Factory f = new Factory();
        Game2048Board board = new BuilderBoard().build2048Board();
        Game2048BoardManager manager = (Game2048BoardManager) f.createNewManager(board);
        manager.setDimension(4);
        manager.setTime(time);
        manager.setBoard(board);
        manager.getBoard().setScore(score);
        manager.getBoard().setUpTiles(list);
        return manager;
    }

    /**
     * Rebuild a MineBoardManager.
     * @param dimension dimension of the MineBoardManager
     * @param mineNum number of mines of the MineBoardManager
     * @param mineLeft number of mine left of the MineBoardManager
     * @param time time of playing of the MineBoardManager
     * @param tiles list of tiles in the MineBoardManager
     * @return new-built MineBoardManager
     */
    MineBoardManager loadMineManager(int dimension, int mineNum, int mineLeft, double time, List<MineTile> tiles) {
        Factory f = new Factory();
        MineBoard b = new BuilderBoard()
                .setMine(mineNum)
                .setMineLeft(mineLeft)
                .setDimension(dimension)
                .setMineTiles(tiles)
                .buildMineBoard();
        MineBoardManager m = (MineBoardManager) f.createNewManager(b);
        m.switchIsFirst();
        m.setDimension(dimension);
        m.setBoard(b);
        m.setTime(time);
        m.setMinePosition(b.getMinePosition());
        return m;
    }

    /**
     * Rebuild a SlidingBoardManager.
     * @param dimension dimension of the SlidingBoardManager
     * @param time time of playing of the SlidingBoardManager
     * @param slidingTiles list of tiles in the SlidingBoardManager
     * @return new-built SlidingBoardManager
     */
    SlidingBoardManager loadSlidingManager(int dimension, double time, List<SlidingTile> slidingTiles) {
        Factory f = new Factory();
        SlidingBoard b = new BuilderBoard()
                .setDimension(dimension)
                .setTiles((List<Tile>) (List<?>) slidingTiles)
                .buildSlidingBoard();
        SlidingBoardManager m = (SlidingBoardManager) f.createNewManager(b);

        m.setDimension(dimension);
        m.setTime(time);
        m.setBoard(new BuilderBoard()
                .setDimension(dimension)
                .buildSlidingBoard());
        m.setTiles(slidingTiles);
        m.setSlidingHistory(new SlidingHistory());
        m.getSlidingHistory().add(new SlidingHistoryNode(m.findBlankIndex()));
        return m;
    }

    /**
     * Create a new UserAccount with name and password and return it.
     * @param name name of the user
     * @param password password of the user
     * @return a new UserAccount
     */
    UserAccount createUserAccount(String name, String password) {
        UserAccount u = new UserAccount();
        u.setName(name);
        u.setPassword(password);
        u.setEmail("");
        HashMap<String, SlidingMemory> historySliding = new HashMap<>();
        historySliding.put("history3x3", null);
        historySliding.put("history4x4", null);
        historySliding.put("history5x5", null);
        historySliding.put("resumeHistorySlide", null);
        HashMap<String, MineMemory> historyMine = new HashMap<>();
        historyMine.put("resumeHistoryMine", null);
        historyMine.put("historyMine", null);
        HashMap<String, Game2048Memory> history2048 = new HashMap<>();
        history2048.put("resumeHistory2048", null);
        HashMap<String, ScoreBoard> personalScoreBoard = new HashMap<>();
        personalScoreBoard.put("history3x3", new ScoreBoard("SlidingTiles"));
        personalScoreBoard.put("history4x4", new ScoreBoard("SlidingTiles"));
        personalScoreBoard.put("history5x5", new ScoreBoard("SlidingTiles"));
        personalScoreBoard.put("2048", new ScoreBoard("2048"));
        personalScoreBoard.put("Mine", new ScoreBoard("MineSweeper"));
        ArrayList<String> games = new ArrayList<>();
        ArrayList<Integer> userScoreList = new ArrayList<>();
        u.setHistorySliding(historySliding);
        u.setHistory2048(history2048);
        u.setHistorySliding(historySliding);
        u.setGames(games);
        u.setHistoryMine(historyMine);
        u.setPersonalScoreBoard(personalScoreBoard);
        return u;
    }

    /**
     * Create a new UserAccountManager with a hash map containing five global ScoreBoards.
     * @return a new UserAccountManager
     */
    UserAccountManager createUserManager() {
        UserAccountManager m = new UserAccountManager();
        m.getGlobalScoreBoard().put("history3x3", new ScoreBoard("SlidingTiles"));
        m.getGlobalScoreBoard().put("history4x4", new ScoreBoard("SlidingTiles"));
        m.getGlobalScoreBoard().put("history5x5", new ScoreBoard("SlidingTiles"));
        m.getGlobalScoreBoard().put("2048", new ScoreBoard("2048"));
        m.getGlobalScoreBoard().put("Mine", new ScoreBoard("Mine"));
        return m;
    }
}

