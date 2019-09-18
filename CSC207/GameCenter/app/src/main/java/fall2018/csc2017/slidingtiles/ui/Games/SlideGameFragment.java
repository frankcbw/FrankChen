package fall2018.csc2017.slidingtiles.ui.Games;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fall2018.csc2017.slidingtiles.BoardManager;
import fall2018.csc2017.slidingtiles.DataHolder;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.ScoreBoard;
import fall2018.csc2017.slidingtiles.ScoreBoardTabLayoutActivity;
import fall2018.csc2017.slidingtiles.SlidingBoardManager;
import fall2018.csc2017.slidingtiles.SlidingDifficultyActivity;
import fall2018.csc2017.slidingtiles.SlidingMainActivity;
import fall2018.csc2017.slidingtiles.UserAccount;
import fall2018.csc2017.slidingtiles.UserAccountManager;
import fall2018.csc2017.slidingtiles.DatabaseHelper;

/**
 * Fragment for Slide Game
 */
public class SlideGameFragment extends Fragment {
    /**
     * database
     */
    DatabaseHelper myDB;

    /**
     * username for user
     */
    String username;

    /**
     * user that is operating system
     */
    private UserAccount user;

    /**
     * all users from database
     */
    private UserAccountManager users;

    /**
     * scoreboard of user
     */
    private ScoreBoard personalScoreBoard;

    /**
     * overall scoreboard of game
     */
    private ScoreBoard globalScoreBoard;

    /**
     * slidingBoardManager in user
     */
    private SlidingBoardManager slidingBoardManager;

    /**
     * scoreboard button
     */
    private Button Rank;

    /**
     * button starts game
     */
    private Button startButton;

    /**
     * button resumes game
     */
    private Button resumeButton;

    /**
     * button loads game
     */
    private Button loadButton;

    /**
     * size of board
     */
    private int boardSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getUser();
        View rootView = inflater.inflate(R.layout.slide_game_fragment, container, false);
        getAllComponents(rootView);
        addStartButtonListener();
        addLoadButtonListener();
        addResumeButtonListener();
        addScoreBoardListener();
        // set current game
        return rootView;
    }

    /**
     * get all components from view
     *
     * @param rootView current view
     */
    private void getAllComponents(View rootView) {
        Rank = rootView.findViewById(R.id.Rank);
        startButton = rootView.findViewById(R.id.NewGameButton1);
        resumeButton = rootView.findViewById(R.id.ResumeButton1);
        loadButton = rootView.findViewById(R.id.LoadButton1);
    }

    /**
     * get all user information and variables passed between activities
     */
    private void getUser() {
        myDB = new DatabaseHelper(this.getContext());
        username = (String) DataHolder.getInstance().retrieve("current user");
        user = myDB.selectUser(username);
        users = myDB.selectAccountManager();
        DataHolder.getInstance().save("current game", "Slide");
    }

    /**
     * add global scoreboard button
     */
    private void addScoreBoardListener() {
        Rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseLevel();
            }
        });
    }


    /**
     * choose game difficulty to view scoreboard
     */
    private void choseLevel() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an level");
        // add a list
        String[] memoryList = {"3x3", "4x4", "5x5"};
        builder.setItems(memoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 3x3
                        personalScoreBoard = user.getScoreBoard("history3x3");
                        globalScoreBoard = users.getGlobalScoreBoard("history3x3");
                        break;
                    case 1: // 4x4
                        personalScoreBoard = user.getScoreBoard("history4x4");
                        globalScoreBoard = users.getGlobalScoreBoard("history4x4");
                        break;
                    case 2: // 5x5
                        personalScoreBoard = user.getScoreBoard("history5x5");
                        globalScoreBoard = users.getGlobalScoreBoard("history5x5");
                        break;
                }
                if (personalScoreBoard != null && globalScoreBoard != null) {
                    switchToScoreBoard();
                } else {
                    builder.setMessage("No record :)");
                    AlertDialog d = builder.create();
                    d.show();
                }

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
     * add start button
     */
    private void addStartButtonListener() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDifficulty();
            }
        });
    }

    /**
     * add resume button
     */
    private void addResumeButtonListener() {
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingBoardManager = (SlidingBoardManager) user.getSpecificSlideHistory("resumeHistorySlide");
                if (slidingBoardManager != null) {
                    slidingBoardManager = (SlidingBoardManager) user.getSpecificSlideHistory("resumeHistorySlide");
                    user.setSlideHistory("resumeHistorySlide", null);
                    switchToGame();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder
                            (getActivity());
                    builder.setMessage("SlidingHistory not found");
                    AlertDialog d = builder.create();
                    d.show();
                }
            }
        });
    }

    /**
     * add load button
     */
    private void addLoadButtonListener() {
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    /**
     * start SlideDifficultActivity and pass useful info and data to next activity
     */
    private void switchToDifficulty() {
        Intent tmp = new Intent(getActivity(), SlidingDifficultyActivity.class);
        myDB.updateUser(username, this.user);
        myDB.updateAccountManager(this.users);
        startActivity(tmp);
    }

    /**
     * start playing game with SlidingMainActivity
     */
    private void switchToGame() {
        Intent tmp = new Intent(getActivity(), SlidingMainActivity.class);
        Bundle pass = new Bundle();
        myDB.updateUser(username, this.user);
        pass.putSerializable("slidingBoardManager", this.slidingBoardManager);
        pass.putInt("size", boardSize);
        tmp.putExtras(pass);
        startActivity(tmp);
    }

    /**
     * load game
     */
    private void load() throws ClassCastException {
        this.user = myDB.selectUser(username);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an memory");
        // add a list
        String[] memoryList = {"3x3", "4x4", "5x5"};
        builder.setItems(memoryList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 3x3
                        BoardManager manager = user.getSpecificSlideHistory("history3x3");
                        slidingBoardManager = (SlidingBoardManager) manager;
                        boardSize = 3;
                        break;
                    case 1: // 4x4
                        slidingBoardManager = (SlidingBoardManager) user.getSpecificSlideHistory("history4x4");
                        boardSize = 4;
                        break;
                    case 2: // 5x5
                        slidingBoardManager = (SlidingBoardManager) user.getSpecificSlideHistory("history5x5");
                        boardSize = 5;
                        break;
                }
                if (slidingBoardManager != null) {
                    switchToGame();
                } else {
                    builder.setMessage("SlidingHistory not found");
                    AlertDialog d = builder.create();
                    d.show();
                }

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
