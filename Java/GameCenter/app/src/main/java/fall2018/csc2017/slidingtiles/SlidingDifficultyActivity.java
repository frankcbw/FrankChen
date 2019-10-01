package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class SlidingDifficultyActivity extends AppCompatActivity {

    String username;
    /**
     * The SlidingBoardManager
     */
    private SlidingBoardManager slidingBoardManager;
    /**
     * The UserAccount
     */
    private UserAccount user;
    private DatabaseHelper myDB;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_difficulty);
        getUser();
        addStart4x4Button();
        addStart3x3Button();
        addStart5x5Button();
    }

    /**
     * Clear the autosave history.
     */
    private void clearResumeHistory() {
        user.setSlideHistory("resumeHistorySlide", null);
    }

    /**
     * Receive UserAccount and UserAccountManager from the previous activity/view.
     */
    private void getUser() {
        myDB = new DatabaseHelper(this);
        username = (String) DataHolder.getInstance().retrieve("current user");
        this.user = myDB.selectUser(username);
    }

    /**
     * Add the 4x4 button for initializing a 4x4 game.
     */
    private void addStart4x4Button() {
        startButton = findViewById(R.id.button3);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Factory f = new Factory();
                SlidingBoard b = new BuilderBoard()
                        .setDimension(4)
                        .buildSlidingBoard();
                slidingBoardManager = (SlidingBoardManager) f.createNewManager(b);
                clearResumeHistory();
                switchToGame();
            }
        });
    }

    /**
     * Add the 3x3 button for initializing a 4x4 game.
     */
    private void addStart3x3Button() {
        startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Factory f = new Factory();
                SlidingBoard b = new BuilderBoard()
                        .setDimension(3)
                        .buildSlidingBoard();
                slidingBoardManager = (SlidingBoardManager) f.createNewManager(b);
                clearResumeHistory();
                switchToGame();
            }
        });
    }

    /**
     * Add the 5x5 button for initializing a 4x4 game.
     */
    private void addStart5x5Button() {
        startButton = findViewById(R.id.button4);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Factory f = new Factory();
                SlidingBoard b = new BuilderBoard()
                        .setDimension(5)
                        .buildSlidingBoard();
                slidingBoardManager = (SlidingBoardManager) f.createNewManager(b);
                clearResumeHistory();
                switchToGame();
            }
        });
    }

    /**
     * Switch to game activity/view.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingMainActivity.class);
        Bundle pass = new Bundle();
        pass.putSerializable("slidingBoardManager", slidingBoardManager);
        tmp.putExtras(pass);
        startActivity(tmp);
    }


}
