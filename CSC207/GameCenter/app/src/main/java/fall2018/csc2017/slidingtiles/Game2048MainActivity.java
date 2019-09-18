package fall2018.csc2017.slidingtiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Main activity view running 2048 game
 */
public class Game2048MainActivity extends AppCompatActivity implements IObserver {
    static boolean isPaused;
    double tempCount;
    private ScoreBoard personalScoreBoard;
    private ScoreBoard globalScoreBoard;
    private Game2048BoardManager boardManager;
    private GestureDetectGridView gridView;
    private ArrayList<Button> tileButtons;
    private UserAccount user;
    private UserAccountManager users;
    private int columnHeight;
    private int columnWidth;
    private boolean isWin;
    private DatabaseHelper myDB;
    private String username;
    private TextView scorePlace;
    private Game2048MainController game2048MainController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getAllInfo(); // pass in all useful data from last activity, including boardManager
        getAllComponents();
        super.onCreate(savedInstanceState);
        tileButtons = game2048MainController.createTileButtons(this, boardManager);
        isPaused = false;
        Thread t = time();
        t.start();
        setGridView();
        addCheatButton();
        addNewGameButton();
    }

    /**
     * get all components from view
     */
    public void getAllComponents() {
        setContentView(R.layout.activity_main2048);
        scorePlace = findViewById(R.id.score);
        gridView = findViewById(R.id.grid2048);
        game2048MainController = new Game2048MainController();
    }

    /**
     * time thread
     * @return time thread
     */
    @NonNull
    private Thread time() {
        return new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    if (!isPaused) {
                        try {
                            Thread.sleep(500);
                            if (boardManager.isWon()) {
                                this.interrupt();
                            }
                            runOnUiThread(new InGame());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    /**
     * setup gridView
     */
    void setGridView() {
        gridView.setNumColumns(boardManager.getBoard().getDimension());
        gridView.setBoardBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / boardManager.getBoard().getDimension();
                        columnHeight = displayHeight / boardManager.getBoard().getDimension();

                        display();

                    }
                });
    }

    /**
     * set up cheat button for quick debugging
     */
    private void cheat() {
        boardManager.getBoard().cheat();
    }

    /**
     * get all user information and variables
     */
    private void getAllInfo() {
        myDB = new DatabaseHelper(this);
        username = (String) DataHolder.getInstance().retrieve("current user");
        Intent intentExtras = getIntent();
        Bundle extra = intentExtras.getExtras();

        assert extra != null;
        this.user = myDB.selectUser(username);
        this.users = myDB.selectAccountManager();

        personalScoreBoard = user.getScoreBoard("2048");
        globalScoreBoard = users.getGlobalScoreBoard("2048");
        this.boardManager = (Game2048BoardManager) extra.getSerializable("boardManager");
    }

    /**
     * set up cheat button for quick debugging
     */
    private void addCheatButton() {
        Button cheat = findViewById(R.id.Cheat2048);
        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheat();
                display();
            }
        });
    }

    /**
     * set up new game button
     */
    void addNewGameButton() {
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game2048MainActivity.this.finish();
                Intent tmp = new Intent(getApplicationContext(), Game2048MainActivity.class);
                Bundle bundle = new Bundle();

                Factory f = new Factory();
                Game2048Board board = new BuilderBoard().build2048Board();
                bundle.putSerializable("boardManager", f.createNewManager(board));
                tmp.putExtras(bundle);
                startActivity(tmp);
            }
        });
    }

    /**
     * display on gridView
     */
    private void display() {
        tileButtons = game2048MainController.updateTileButtons(boardManager, tileButtons);
        // add a tile if the board has been modified and it has empty spot
        scorePlace.setText("Score: " + String.valueOf(personalScoreBoard.calculateScore(boardManager)));
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * set the game status to win
     */
    private void setIsWin() {
        if (boardManager.isWon()) {
            isWin = true;
        }
    }

    /**
     * auto save for game
     */
    private void autoSave() {
        user.setGame2048History("resumeHistory2048", boardManager);
        myDB.updateUser(username, user);
    }

    /**
     * calculate score of game and set up scoreBoard
     * @param boardManager boardManager
     * @return score of the game
     */
    private int getScore(Game2048BoardManager boardManager) {
        int score;
        score = personalScoreBoard.calculateScore(boardManager);
        Object[] result = new Object[2];
        result[0] = username;
        result[1] = score;
        personalScoreBoard.addAndSort(result);
        globalScoreBoard.addAndSort(result);
        myDB.updateUser(username, user);
        myDB.updateAccountManager(users);
        return score;
    }

    /**
     * triggers alert dialog when the game is end (win or lose)
     */
    private void endAlert() {
        int score = getScore(boardManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (isWin) {
            builder.setMessage("You Win!");
        } else {
            builder.setMessage("You Lose, get better next time. Your scored " + String.valueOf(score));
        }
        builder.setPositiveButton("Back To Game Center", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switchToGameCenter();
            }
        })
                .setNegativeButton("See My Rank ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Game2048MainActivity.this.finish();
                        user.setGame2048History("resumeHistory2048", null);
                        switchToScoreBoard();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * start scoreBoard activity
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(this, ScoreBoardTabLayoutActivity.class);
        Bundle pass = new Bundle();
        pass.putSerializable("personalScoreBoard", this.personalScoreBoard);
        pass.putSerializable("globalScoreBoard", this.globalScoreBoard);
        tmp.putExtras(pass);
        startActivity(tmp);
    }

    /**
     * start game center activity
     */
    private void switchToGameCenter() {
        Intent intent = new Intent(this, InfoPanelMainActivity.class);
        Bundle pass = new Bundle();
        myDB.updateUser(user.getName(), this.user);
        myDB.updateAccountManager(users);
        pass.putString("fragment", "2048");
        intent.putExtras(pass);
        startActivity(intent);
    }

    @Override
    public void update(IObservable o) {
        display();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        autoSave();
        switchToGameCenter();
    }

    /**
     * customized runnable for UI thread and logic
     */
    private class InGame implements Runnable {
        @Override
        public void run() {
            if ((boardManager.isWon() || boardManager.isLost())
                    && !isPaused) {
                gridView.freeze();
                user.setGame2048History("resumeHistory2048", null);
                setIsWin();
                isPaused = true;
                endAlert();
            } else {
                if (tempCount < 2) {
                    tempCount += 0.5;
                } else {
                    tempCount = 0;
                    autoSave();
                }
            }
        }
    }

}