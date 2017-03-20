/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MainActivity2 extends Activity {

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
//
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
