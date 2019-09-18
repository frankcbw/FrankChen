package fall2018.csc2017.slidingtiles.ui.Games;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import fall2018.csc2017.slidingtiles.DataHolder;
import fall2018.csc2017.slidingtiles.MineBoardManager;
import fall2018.csc2017.slidingtiles.MineDifficultyActivity;
import fall2018.csc2017.slidingtiles.R;
import fall2018.csc2017.slidingtiles.UserAccount;
import fall2018.csc2017.slidingtiles.UserAccountManager;
import fall2018.csc2017.slidingtiles.DatabaseHelper;

/**
 * Fragment for Minesweeper game
 */
public class MinesweeperFragment extends Fragment {
    private UserAccount user;
    private View view;
    private UserAccountManager users;
    private ImageView thomas;
    private MineBoardManager boardManager;
    private DatabaseHelper myDB;
    private String username;

    public MinesweeperFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.minesweeper_fragment, container, false);
        getUser();
        addImageAnim();
        addStartButton();
        return view;
    }

    /**
     * set start button
     */
    private void addStartButton() {
        thomas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent tmp = new Intent(getActivity(), MineDifficultyActivity.class);
                startActivity(tmp);
            }

        });
    }

    /**
     * get all user information and variables passed between activities
     */
    private void getUser() {
        myDB = new DatabaseHelper(this.getContext());
        username = (String) DataHolder.getInstance().retrieve("current user");
        user = myDB.selectUser(username);
        users = myDB.selectAccountManager();
        // set current game
        DataHolder.getInstance().save("current game", "Mine");
    }

    /**
     * set animation to ImageView
     */
    private void addImageAnim() {
        thomas = (ImageView) view.findViewById(R.id.Thomas);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.anims);
        thomas.setAnimation(rotate);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toasty.success(getContext(), "Press smiley THO-MAX to start", Toast.LENGTH_LONG, true).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
