
package zhe.scrogglegame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;


public class Tile {


   private final GameFragment mGame;

   private View mView;
   private Tile mSubTiles[];

   public Tile(GameFragment game) {
      this.mGame = game;
   }



   public View getView() {
      return mView;
   }

   public void setView(View view) {
      this.mView = view;
   }




   public void setSubTiles(Tile[] subTiles) {
      this.mSubTiles = subTiles;
   }


   public void animate() {
      Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(),
            R.animator.tictactoe);
      if (getView() != null) {
         anim.setTarget(getView());
         anim.start();
      }
   }
}
