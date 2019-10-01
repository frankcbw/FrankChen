package fall2018.csc2017.slidingtiles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MineDifficultyActivity extends AppCompatActivity {
    UserAccount user;
    MineBoardManager boardManager;
    DatabaseHelper myDB;
    String username;
    private UserAccountManager users;
    private Button start;
    private Button load;
    private Button rank;

    /**
     * scoreboard of user
     */
    private ScoreBoard personalScoreBoard;

    /**
     * scoreboard of game all users
     */
    private ScoreBoard globalScoreBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUser();
        getAllComponents();
        addStartButton();
        addLoadButton();
        addRankButton();
    }

    /**
     * get all UI components from UI
     */
    private void getAllComponents() {
        setContentView(R.layout.activity_minesweeper_difficulty);
        start = findViewById(R.id.start);
        load = findViewById(R.id.load);
        rank = findViewById(R.id.rank);
    }

    /**
     * set start button
     */
    private void addStartButton() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDifficulty();
            }
        });
    }

    /**
     * set load button
     */
    private void addLoadButton() {
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = user.getSpecificMineHistory("historyMine");
                if (boardManager != null) {
                    switchToGame();
                } else {
                    Toasty.info(getApplicationContext(), "No game history", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    /**
     * set scoreBoard button
     */
    void addRankButton() {
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalScoreBoard = user.getScoreBoard("Mine");
                globalScoreBoard = users.getGlobalScoreBoard("Mine");
                if (personalScoreBoard != null && globalScoreBoard != null) {
                    switchToScoreBoard();
                }
            }
        });
    }

    /**
     * switch to scoreBoard activity
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(this, ScoreBoardTabLayoutActivity.class);
        Bundle pass = new Bundle();
        myDB.updateUser(username, this.user);
        myDB.updateAccountManager(this.users);
        pass.putSerializable("personalScoreBoard", this.personalScoreBoard);
        pass.putSerializable("globalScoreBoard", this.globalScoreBoard);
        tmp.putExtras(pass);
        startActivity(tmp);
    }


    /**
     * select game difficulty and start game activity
     */
    private void selectDifficulty() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an difficulty");
        // add a list
        String[] memoryList = {"Easy", "Medium", "Hard"};
        builder.setItems(memoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 3x3
                        Factory f = new Factory();
                        MineBoard b = new BuilderBoard()
                                .setMine(10)
                                .setMineLeft(10)
                                .setDimension(9)
                                .setMineTiles()
                                .buildMineBoard();
                        boardManager = (MineBoardManager) f.createNewManager(b);
                        switchToGame();
                        break;
                    case 1: // 4x4
                        Factory f2 = new Factory();
                        MineBoard b2 = new BuilderBoard()
                                .setMine(40)
                                .setMineLeft(40)
                                .setDimension(16)
                                .setMineTiles()
                                .buildMineBoard();
                        boardManager = (MineBoardManager) f2.createNewManager(b2);
                        switchToGame();
                        break;
                    case 2: // 5x5
                        Factory f3 = new Factory();
                        MineBoard b3 = new BuilderBoard()
                                .setMine(80)
                                .setMineLeft(80)
                                .setDimension(20)
                                .setMineTiles()
                                .buildMineBoard();
                        boardManager = (MineBoardManager) f3.createNewManager(b3);
                        switchToGame();
                        break;
                }

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Receive UserAccount and UserAccountManager from the previous activity/view.
     */
    private void getUser() {
        myDB = new DatabaseHelper(this);
        username = (String) DataHolder.getInstance().retrieve("current user");
        this.user = myDB.selectUser(username);
        this.users = myDB.selectAccountManager();

    }

    /**
     * start game activity
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, MineMainActivity.class);
        Bundle pass = new Bundle();
        pass.putSerializable("boardManager", boardManager);
        tmp.putExtras(pass);
        startActivity(tmp);
    }
}

