/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package zhe.scrogglegame;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.support.v7.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

   MediaPlayer mMediaPlayer;
   private ToggleButton toggle_btn;
   // ...

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.ativity_main2);

      toggle_btn= (ToggleButton) findViewById(R.id.button_toggle);

      toggle_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

         @Override
         public void onCheckedChanged(CompoundButton buttonView,
                                      boolean isChecked) {
            // TODO Auto-generated method stub
            if(isChecked){
               mMediaPlayer.setVolume(0.5f, 0.5f);
               mMediaPlayer.setLooping(true);
               mMediaPlayer.start();
            }else{
                 mMediaPlayer.pause();
            }
         }

      });
      }


   @Override
   protected void onResume() {
      super.onResume();
      if (toggle_btn.isChecked()) {
         mMediaPlayer = MediaPlayer.create(this, R.raw.a_guy_1_epicbuilduploop);
         mMediaPlayer.setVolume(0.5f, 0.5f);
         mMediaPlayer.setLooping(true);
         mMediaPlayer.start();
      }
      else{
         mMediaPlayer.pause();
      }
   }

   @Override
   protected void onPause() {
      super.onPause();
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
   }
}
