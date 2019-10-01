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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The game activity.
 */
public class SlidingMainActivity extends AppCompatActivity implements IObserver {
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private static int columnWidth, columnHeight;
    DatabaseHelper myDB;
    /**
     * The per-user scoreboard
     */
    private ScoreBoard personalScoreBoard;
    /**
     * The per-game scoreboard
     */
    private ScoreBoard globalScoreBoard;
    /**
     * If a game is paused.
     */
    private boolean isPaused;
    /**
     * The board manager.
     */
    private SlidingBoardManager slidingBoardManager;
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * Grid View and calculated column height and width based on device size.
     */
    private GestureDetectGridView gridView;
    /**
     * Time count.
     */
    private double count = 0;

    /**
     * Time count for autosave purpose.
     */
    private double tempCount = 0; //

    /**
     * UserAccount associated to the game.
     */
    private UserAccount user;

    /**
     * Dimension of the board game.
     */
    private int size;

    /**
     * UserAccountManager associated to the UserAccount.
     */
    private UserAccountManager users;
    private String username;
    private TextView time;
    private SlidingMainController slidingMainController;


    /**
     * Initialize all buttons.
     * @param savedInstanceState all saved information bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDB = new DatabaseHelper(this);
        username = (String) DataHolder.getInstance().retrieve("current user");
        super.onCreate(savedInstanceState);
        getAllInfo(); // pass in all useful data from last activity, including slidingBoardManager
        getAllComponents();
        tileButtons = slidingMainController.createTileButtons(getApplicationContext(), slidingBoardManager, tileButtons);
        Thread t = time();
        t.start();

        // Add View to activity
        setGridView();
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        tileButtons = slidingMainController.updateTileButtons(slidingBoardManager, tileButtons);
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * Switch to MainPanelActivity.
     */
    private void switchToGameCenter() {
        Intent intent = new Intent(this, InfoPanelMainActivity.class);
        Bundle pass = new Bundle();
        pass.putString("fragment", "Slide");
        intent.putExtras(pass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        isPaused = true;
        user.setSlideHistory("resumeHistorySlide", null);// clear resume memory
        slidingBoardManager.setTime(count);
        if (!slidingBoardManager.isWon()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to save/override this game?")

                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveHistory(dialog);
                            switchToGameCenter();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            switchToGameCenter();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isPaused = false;
                    dialog.cancel();
                }
            });
            alert.show();
        } else {
            switchToGameCenter();
        }

    }

    /**
     * Return the score of the finished game.
     * @return the score of the finished game.
     */
    private int getScore() {
        int score;
        score = personalScoreBoard.calculateScore(slidingBoardManager);
        Object[] result = new Object[2];
        result[0] = user.getName();
        result[1] = score;
        personalScoreBoard.addAndSort(result);
        globalScoreBoard.addAndSort(result);
        myDB.updateUser(user.getName(), this.user);
        myDB.updateAccountManager(users);
        return score;

    }

    /**
     * Save the game history.
     * @param dialog
     */
    private void saveHistory(DialogInterface dialog) {
        switch (size) {
            case 3:
                user.setSlideHistory("history3x3", slidingBoardManager);
                break;
            case 4:
                user.setSlideHistory("history4x4", slidingBoardManager);
                break;
            case 5:
                user.setSlideHistory("history5x5", slidingBoardManager);
                break;
        }
        myDB.updateUser(username, user);
        dialog.cancel();
    }

    /**
     * Automatically save the game for resuming.
     */
    private void autoSave() {
        slidingBoardManager.setTime(count);
        user.setSlideHistory("resumeHistorySlide", slidingBoardManager);
        myDB.updateUser(username, user);
    }


    private void getAllComponents() {
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.grid);
        time = findViewById(R.id.textView6);
        slidingMainController = new SlidingMainController();
    }

    @NonNull
    private Thread time() {
        count = slidingBoardManager.getTime();
        isPaused = false;
        return new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    if (!isPaused) {
                        try {
                            Thread.sleep(10);
                            if (slidingBoardManager.isWon()) {
                                this.interrupt();
                            }
                            runOnUiThread(new InGame(time));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        };
    }

    /**
     * Set up the grid view.
     */
    void setGridView() {
        gridView.setNumColumns(slidingBoardManager.getBoard().getDimension());
        gridView.setBoardBoardManager(slidingBoardManager);
        slidingBoardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / slidingBoardManager.getBoard().getDimension();
                        columnHeight = displayHeight / slidingBoardManager.getBoard().getDimension();

                        display();

                    }
                });
        addRedoButton();
        addUndoButton();
        addCheatButton();
    }

    /**
     * Alert when a puzzle is solved.
     */
    private void winAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int score = getScore();
        builder.setMessage("you got " + String.valueOf(score) + " !")

                .setPositiveButton("See my rank", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switchToScoreBoard();
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SlidingMainActivity.this.finish();
                        user.setSlideHistory("resumeHistory", null);
                        switchToGameCenter();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Add the cheat button.`
     */
    private void addCheatButton() {
        Button cheat = findViewById(R.id.Cheating);
        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheat();
                display();
            }
        });
    }

    /**
     * Add the undo button.
     */
    private void addUndoButton() {
        Button undo = findViewById(R.id.Undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingBoardManager.readHistory(-1);
                display();
            }
        });
    }

    /**
     * Add the redo button.
     */
    private void addRedoButton() {
        Button redo = findViewById(R.id.Redo);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingBoardManager.readHistory(1);
                display();
            }

        });
    }

    /**
     * Manage the board to a ready-to-solve state. The cheat operation is only for testing and will
     * not be saved into history. This functionality will be removed in Phase 2.
     */
    private void cheat() {
        List<SlidingTile> slidingTiles = new ArrayList<>();
        int numTiles = size * size;
        for (int tileNum = 1; tileNum != numTiles - 1; tileNum++) {
            slidingTiles.add(new SlidingTile(tileNum));
        }
        slidingTiles.add(new SlidingTile(0));
        slidingTiles.add(new SlidingTile(numTiles - 1));
        slidingBoardManager.setTiles(slidingTiles);
    }

    /**
     * Switch to ScoreBoard activity/view.
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(this, ScoreBoardTabLayoutActivity.class);
        Bundle pass = new Bundle();
        pass.putSerializable("personalScoreBoard", personalScoreBoard);
        pass.putSerializable("globalScoreBoard", globalScoreBoard);
        tmp.putExtras(pass);
        startActivity(tmp);
    }

    /**
     * Receive all the info(User, Size, SlidingBoardManager, ScoreBoards)from previous activity/view.
     */
    private void getAllInfo() {
        Intent intentExtras = getIntent();
        Bundle extra = intentExtras.getExtras();

        assert extra != null;
        this.user = myDB.selectUser(username);
        this.users = myDB.selectAccountManager();

        this.slidingBoardManager = (SlidingBoardManager) extra.getSerializable("slidingBoardManager");
        this.size = extra.getInt("size");
        if (slidingBoardManager.getBoard() != null) {
            this.size = slidingBoardManager.getBoard().getDimension();
        }
        switch (size) {
            case 3:
                this.personalScoreBoard = this.user.getScoreBoard("history3x3");
                this.globalScoreBoard = this.users.getGlobalScoreBoard("history3x3");
                break;

            case 4:
                this.personalScoreBoard = this.user.getScoreBoard("history4x4");
                this.globalScoreBoard = this.users.getGlobalScoreBoard("history4x4");
                break;

            case 5:
                this.personalScoreBoard = this.user.getScoreBoard("history5x5");
                this.globalScoreBoard = this.users.getGlobalScoreBoard("history5x5");
                break;

            default:
                this.personalScoreBoard = this.user.getScoreBoard("history4x4");
                this.globalScoreBoard = this.users.getGlobalScoreBoard("history4x4");
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void update(IObservable o) {
        display();
    }

    private class InGame implements Runnable {
        TextView time;

        public InGame(TextView time) {
            this.time = time;
        }

        @Override
        public void run() {
            if (slidingBoardManager.isWon() && !isPaused) {
                slidingBoardManager.setTime(count);
                gridView.freeze();
                isPaused = true;
                user.setSlideHistory("resumeHistorySlide", null);
                winAlert();
            } else {
                count += 0.01;
                if (tempCount < 2) {
                    tempCount += 0.01;
                } else {
                    tempCount = 0;
                    autoSave();
                }
                time.setText(String.valueOf(df2.format(count)) + " s");
            }
        }
    }
}
