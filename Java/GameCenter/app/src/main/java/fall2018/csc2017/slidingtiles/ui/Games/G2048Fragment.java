package fall2018.csc2017.slidingtiles.ui.Games;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import fall2018.csc2017.slidingtiles.BuilderBoard;
import fall2018.csc2017.slidingtiles.DataHolder;
import fall2018.csc2017.slidingtiles.Factory;
import fall2018.csc2017.slidingtiles.Game2048Board;
import fall2018.csc2017.slidingtiles.Game2048BoardManager;
import fall2018.csc2017.slidingtiles.Game2048MainActivity;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;
import fall2018.csc2017.slidingtiles.ScoreBoardTabLayoutActivity;
import fall2018.csc2017.slidingtiles.UserAccount;
import fall2018.csc2017.slidingtiles.UserAccountManager;
import fall2018.csc2017.slidingtiles.DatabaseHelper;

/**
 * Fragment for game 2048
 */

public class G2048Fragment extends Fragment {
    /**
     * database
     */
    DatabaseHelper myDB;
    /**
     * Button get in game center
     */
    private ImageButton Image2048;
    /**
     * boardManager for 2048
     */
    private Game2048BoardManager boardManager;

    /**
     * current user
     */
    private UserAccount user;

    /**
     * userManager
     */
    private UserAccountManager users;

    /**
     * Button enter scoreBoard
     */
    private Button scoreBoard;

    /**
     * Personal scoreBoard
     */
    private ScoreBoard personalScoreBoard;

    /**
     * Overall scoreBoard for game
     */
    private ScoreBoard globalScoreBoard;

    /**
     * username for user
     */
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.myDB = new DatabaseHelper(getContext());
        getUser();
        View view = inflater.inflate(R.layout.g2048_fragment, container, false);
        Image2048 = view.findViewById(R.id.g_2048);
        scoreBoard = view.findViewById(R.id.score_board);
        Toasty.success(getContext(), "Press 2048 logo to start", Toast.LENGTH_LONG, true).show();
        addStartButton();
        addScoreBoardButton();
        DataHolder.getInstance().save("current game", "2048");
        return view;
    }

    /**
     * set start game button
     */
    private void addStartButton() {
        Image2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = user.getSpecific2048History("resumeHistory2048");
                if (boardManager != null) {
                    switchToGame();
                } else {
                    Factory f = new Factory();
                    Game2048Board board = new BuilderBoard().build2048Board();
                    boardManager = (Game2048BoardManager) f.createNewManager(board);
                    user.setGame2048History("resumeHistory2048", null);
                    switchToGame();
                }
            }
        });
    }

    /**
     * start scoreboard activity
     */
    private void switchToScoreBoard() {
        Intent tmp = new Intent(getActivity(), ScoreBoardTabLayoutActivity.class);
        Bundle pass = new Bundle();
        myDB.updateUser(username, this.user);
        myDB.updateAccountManager(this.users);
        pass.putSerializable("personalScoreBoard", this.personalScoreBoard);
        pass.putSerializable("globalScoreBoard", this.globalScoreBoard);
        tmp.putExtras(pass);
        startActivity(tmp);
    }

    /**
     * set scoreBoard button
     */
    public void addScoreBoardButton() {
        scoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreBoard();
            }
        });
    }

    /**
     * start game activity
     */
    private void switchToGame() {
        Intent tmp = new Intent(getContext(), Game2048MainActivity.class);
        Bundle pass = new Bundle();
        pass.putSerializable("boardManager", boardManager);
        tmp.putExtras(pass);
        startActivity(tmp);
    }

    /**
     * get all user information and variables passed between activities
     */
    private void getUser() {
        username = (String) DataHolder.getInstance().retrieve("current user");
        this.user = myDB.selectUser(username);
        this.users = myDB.selectAccountManager();
        personalScoreBoard = user.getScoreBoard("2048");
        globalScoreBoard = users.getGlobalScoreBoard("2048");

    }

}

