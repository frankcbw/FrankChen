package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * The swiping move operation controller
 */
class MovementController {

    private BoardManager boardBoardManager = null;

    /**
     * Set the boardBoardManager to boardBoardManager.
     *
     * @param boardBoardManager the BoardManger to set
     */
    void setBoardBoardManager(BoardManager boardBoardManager) {
        this.boardBoardManager = boardBoardManager;
    }

    /**
     * Process a tap movement
     *
     * @param context
     * @param position the position where a tap occurred
     */
    void processTapMovement(Context context, int position) {
        if (boardBoardManager instanceof SlidingBoardManager) {
            SlidingBoardManager b = (SlidingBoardManager) boardBoardManager;
            if (b.isValidTap(position)) {
                b.move(position);
                if (b.isWon()) {
                    Toasty.success(context, "YOU WIN!!", Toast.LENGTH_SHORT, true).show();
                }
            } else {
                Toasty.warning(context, "Invalid Tap!", Toast.LENGTH_SHORT, true).show();
            }
        } else if (boardBoardManager instanceof MineBoardManager) {
            MineBoardManager b = (MineBoardManager) boardBoardManager;
            b.move(position);
            if (b.isLost()) {
                Toasty.warning(context, "YOU LOST!", Toast.LENGTH_SHORT, true).show();
            } else if (b.isWon()) {
                Toasty.success(context, "YOU WIN!", Toast.LENGTH_SHORT, true).show();
            }
        }

    }

    /**
     * process a long pressed movement
     * @param context context of current activity
     * @param position position of touch
     */
    void processLongPressMovement(Context context, int position) {
        if (boardBoardManager instanceof MineBoardManager) {
            MineBoardManager b = (MineBoardManager) boardBoardManager;
            if (b.getBoard().getTile(position).isObscured()) {
                b.mark(position);
            }
        }
    }

    /**
     * process left swipe
     */
    void processLeftSwipe() {
        if (boardBoardManager instanceof Game2048BoardManager) {
            boardBoardManager.move("LEFT");
        }
    }

    /**
     * process right swipe
     */
    void processRightSwipe() {
        if (boardBoardManager instanceof Game2048BoardManager) {
            boardBoardManager.move("RIGHT");
        }
    }

    /**
     * process down swipe
     */
    void processDownSwipe() {
        if (boardBoardManager instanceof Game2048BoardManager) {
            boardBoardManager.move("DOWN");
        }
    }

    /**
     * process up swipe
     */
    void processUpSwipe() {
        if (boardBoardManager instanceof Game2048BoardManager) {
            boardBoardManager.move("UP");
        }
    }
}
