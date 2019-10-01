package fall2018.csc2017.slidingtiles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Represent the fragment of a ScoreBoard page.
 */
public class ScoreBoardFragmentActivity extends Fragment {
    private View view;
    private ScoreBoard board;
    private TableLayout table;
    private DatabaseHelper myDB;
    private UserAccount user;

    /**
     * Constructs a ScoreBoardFragmentActivity
     */
    public ScoreBoardFragmentActivity() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.myDB = new DatabaseHelper(getContext());
        getUsers();
        view = inflater.inflate(R.layout.activity_score_board, container, false);
        table = view.findViewById(R.id.scoreboard_table);
        getBoard();
        return view;
    }

    /**
     * get the scorelist of the board
     */
    private void getBoard() {
        board = (ScoreBoard) getArguments().getSerializable("board");
        setBoard((ArrayList<Object[]>) board.getScoreList());
    }


    /**
     * get the userAccount of the current user.
     */
    private void getUsers() {
        String currentUser = (String) DataHolder.getInstance().retrieve("current user");
        this.user = myDB.selectUser(currentUser);


        // use this to set user name on global
    }

    /**
     * set the score list
     * @param scoreList the score list to be set.
     */
    private void setBoard(ArrayList<Object[]> scoreList) {
        String Name;
        String Score;

        table.addView(setLine("Rank", "Name", "Score"));

        for (int i = 0; i < 11; i++) {
            if (i < scoreList.size()) {
                Name = (String) scoreList.get(i)[0];
                Score = (String.valueOf(scoreList.get(i)[1]));
                table.addView(setLine(String.valueOf(i + 1), Name, Score));
            }

        }

    }

    /**
     * set the context of the TextViews.
     */
    private TableRow setLine(String a1, String a2, String a3) {
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        //Creating new tablerows and textviews
        TableRow row = new TableRow(getContext());
        TextView txt1 = new TextView(getContext());
        TextView txt2 = new TextView(getContext());
        TextView txt3 = new TextView(getContext());
        //setting the text
        txt1.setText(a1);
        txt2.setText(a2);
        txt3.setText(a3);
        txt1.setLayoutParams(params1);
        txt2.setLayoutParams(params1);
        txt3.setLayoutParams(params1);
        txt1.setGravity(Gravity.CENTER);
        txt2.setGravity(Gravity.CENTER);
        txt3.setGravity(Gravity.CENTER);
        if (a2.equals(user.getName())) {
            txt1.setTextColor(Color.BLUE);
            txt2.setTextColor(Color.BLUE);
            txt3.setTextColor(Color.BLUE);
        }
        //the textviews have to be added to the row created
        row.addView(txt1);
        row.addView(txt2);
        row.addView(txt3);
        row.setLayoutParams(params2);
        return row;
    }


}
