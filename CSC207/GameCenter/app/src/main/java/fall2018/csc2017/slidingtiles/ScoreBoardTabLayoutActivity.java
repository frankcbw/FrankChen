package fall2018.csc2017.slidingtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class ScoreBoardTabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    private ScoreBoard personalScoreBoard;

    private ScoreBoard globalScoreBoard;




    private DatabaseHelper myDB;

    private String username;

    private String fragment;


    /**
     * Initialize activity with corresponding user and data
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDB = new DatabaseHelper(this);
        username = (String) DataHolder.getInstance().retrieve("current user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board_tab_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        appBarLayout = findViewById(R.id.appBar);
        viewPager = (ViewPager) findViewById(R.id.ScoreView);
        getUsers();
        getBoard();
        startFragment();

    }

    private Bundle passInfoToFragment(ScoreBoard s) {
        Bundle pass = new Bundle();
        pass.putSerializable("board", s);
        return pass;
    }

    private void startFragment() {
        ScoreBoardFragmentPagerAdaptor adaptor = new ScoreBoardFragmentPagerAdaptor(getSupportFragmentManager());
        ScoreBoardFragmentActivity g = new ScoreBoardFragmentActivity();
        ScoreBoardFragmentActivity p = new ScoreBoardFragmentActivity();
        g.setArguments(passInfoToFragment(this.globalScoreBoard));
        p.setArguments(passInfoToFragment(this.personalScoreBoard));
        adaptor.addFragment(g, "global");
        adaptor.addFragment(p, "personal");
        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * get user info and data from last activity
     */
    private void getUsers() {
        this.fragment = (String) DataHolder.getInstance().retrieve("current game");
    }

    /**
     * get board from last activity
     */
    private void getBoard() {
        Intent intentExtras = getIntent();
        Bundle extra = intentExtras.getExtras();
        personalScoreBoard = (ScoreBoard) extra.getSerializable("personalScoreBoard");
        globalScoreBoard = (ScoreBoard) extra.getSerializable("globalScoreBoard");
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, InfoPanelMainActivity.class);
        Bundle extra = new Bundle();
        extra.putString("fragment", fragment);
        intent.putExtras(extra);
        startActivity(intent);
    }


}
