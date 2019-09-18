package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MineMainController {
    /**
     * Update the backgrounds on the buttons to match the slidingTiles.
     */
    public ArrayList<Button> updateTileButtons(MineBoardManager boardManager, ArrayList<Button> tileButtons) {
        MineBoard board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            b.setBackgroundResource(board.getTile(nextPos).getBackground());
            nextPos++;
        }
        return tileButtons;
    }

    /**
     * Create the buttons for displaying the slidingTiles.
     *
     * @param context the context
     */
    public ArrayList<Button> createTileButtons(final Context context, MineBoardManager boardManager, ArrayList<Button> tileButtons, final ImageButton face) {
        MineBoard board = boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int i = 0; i < boardManager.getDimension() * boardManager.getDimension(); i++) {
            Button tmp = new Button(context);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    face.setImageResource(R.drawable.surprise);
                }
            });
            tmp.setBackgroundResource(board.getTile(i).getBackground());
            tileButtons.add(tmp);
        }
        return tileButtons;
    }
}
