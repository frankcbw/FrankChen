package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;

class SlidingMainController {
    /**
     * Create the buttons for displaying the slidingTiles.
     *
     * @param context the context
     */
    ArrayList<Button> createTileButtons(Context context, SlidingBoardManager slidingBoardManager, ArrayList<Button> tileButtons) {
        SlidingBoard slidingBoard = slidingBoardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != slidingBoard.getDimension(); row++) {
            for (int col = 0; col != slidingBoard.getDimension(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(slidingBoard.getTile(row, col).getBackground());
                tileButtons.add(tmp);
            }
        }
        return tileButtons;
    }

    /**
     * Update the backgrounds on the buttons to match the slidingTiles.
     */
    ArrayList<Button> updateTileButtons(SlidingBoardManager slidingBoardManager, ArrayList<Button> tileButtons) {
        SlidingBoard slidingBoard = slidingBoardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / slidingBoardManager.getBoard().getDimension();
            int col = nextPos % slidingBoardManager.getBoard().getDimension();
            b.setBackgroundResource(slidingBoard.getTile(row, col).getBackground());
            nextPos++;
        }
        return tileButtons;
    }
}
