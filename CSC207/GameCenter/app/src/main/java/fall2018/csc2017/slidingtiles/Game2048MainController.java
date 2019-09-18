package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Controller for 2048 game
 */
class Game2048MainController {
    /**
     * update tileButtons in game
     * @param boardManager boardManager
     * @param tileButtons arrayList Buttons corresponds to each tile status
     * @return updated tileButtons
     */
     ArrayList<Button> updateTileButtons(Game2048BoardManager boardManager, ArrayList<Button> tileButtons) {
        Game2048Board board = boardManager.getBoard();
        //If the board has been changed.
        if (board.isChanged()) {
            if (board.findEmpty().size() != 0) { //If no empty slot left, meaning cheated. Otherwise generate a new random tile.
                Game2048Tile newTile = board.addTile();
                if (newTile != null) {
                    newTile.setAnimation();
                }
            }
            int nextPos = 0;
            for (Button b : tileButtons) {
                int row = nextPos / boardManager.getBoard().getDimension();
                int col = nextPos % boardManager.getBoard().getDimension();
                if (board.getTile(row, col).getFadeIn()) {
                    addFadeInAnimation(b);
                    board.getTile(row, col).removeFadeIn();
                }
                b.setBackgroundResource(board.getTile(row, col).getBackground());
                nextPos++;
            }
        }
        return tileButtons;
    }

    /**
     * add fade in animation for new added tile
     * @param button button selected to set animation
     */
    private void addFadeInAnimation(Button button) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        AnimationSet animation = new AnimationSet(true); //change to false
        animation.addAnimation(fadeIn);
        button.setAnimation(animation);
    }

    /**
     * create an arrayList of buttons
     * @param context context of activity
     * @param boardManager boardManager
     * @return created arrayList of buttons
     */
     ArrayList<Button> createTileButtons(Context context, Game2048BoardManager boardManager) {
        Game2048Board board = boardManager.getBoard();
         ArrayList<Button> tileButtons = new ArrayList<>();
        for (int i = 0; i < board.getDimension() * board.getDimension(); i++) {
            Button tmp = new Button(context);
            tmp.setBackgroundResource(board.getTiles().get(i).getBackground());
            tileButtons.add(tmp);
        }
        return tileButtons;
    }
}
