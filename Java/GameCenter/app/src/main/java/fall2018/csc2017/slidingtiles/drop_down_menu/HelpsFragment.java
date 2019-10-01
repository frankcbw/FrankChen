package fall2018.csc2017.slidingtiles.drop_down_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fall2018.csc2017.slidingtiles.R;

/**
 * The Fragment of the Helps page.
 */
public class HelpsFragment extends Fragment {
    private View view;
    private TextView instM;
    private TextView instS;
    private TextView inst2048;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helps_fragment, container, false);
        instM = view.findViewById(R.id.instructionM);
        instS = view.findViewById(R.id.instructionS);
        inst2048 = view.findViewById(R.id.instruction2048);
        setInstructions();
        return view;
    }

    /**
     * Set the Game Instructions TextField.
     */
    public void setInstructions() {
        instM.setText("Mine Sweeper: click to reveal tiles, long press to flag and unflag. " +
                "\n For more instructions, please see http://www.freeminesweeper.org/help/minehelpinstructions.html");
        instS.setText("Swipe a tile to the empty tile to move it. Make the numbers in order to win. " +
                "\n For more instructions, please see https://en.wikipedia.org/wiki/Sliding_puzzle");
        inst2048.setText("Swipe to merge two tiles of the same number. Achieve 2048 to win. " +
                "For more instructions, please see https://en.wikipedia.org/wiki/2048_(video_game)#Gameplay");
    }
}
