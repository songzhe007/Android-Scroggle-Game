

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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import zhesong.madcourse.neu.edu.numad17s_b_zhesong.*;

public class GameActivity extends Activity {

    private MediaPlayer mMediaPlayer;



    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};

    private ArrayList<String> ninewords = new ArrayList<String>();
    private ArrayList<String> ninewords_strings=new ArrayList<String>();
    private ArrayList<String> dictionary = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ToggleButton toggle_btn2= (ToggleButton) findViewById(R.id.button_toggle2);

        toggle_btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

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

        InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
            reader.close();
            inputStreamReader.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : dictionary) {
            if (s.length() == 9) {
                char[] sarr = s.toCharArray();
                Arrays.sort(sarr);
                StringBuilder strbuilder = new StringBuilder();
                for (char a : sarr) {
                    strbuilder.append(a);
                }
                ninewords.add(strbuilder.toString());
            }
        }

        Set<Integer> used = new HashSet<Integer>();
        for (int i = 0; i < 9; i++) {
            int index = (int) Math.floor(Math.random() * ninewords.size());
            while (used.contains(index)) {
                index = (int) Math.floor(Math.random() * ninewords.size());
            }
            used.add(index);
            String c = ninewords.get(index);
            ninewords_strings.add(c);
        }
        //get the letters
        //Intent intent = getIntent();
        //ninewords_strings = intent.getStringArrayListExtra("ListString");
          startGame();
    }

    public void startGame(){
        for (int d = 0; d < 9; d++) {
            View outer = (View) findViewById(mLargeIds[d]);
            for (int j = 0; j < 9; j++) {
                Button inner = (Button) outer.findViewById(mSmallIds[j]);
                inner.setText(String.valueOf(ninewords_strings.get(d).charAt(j)));
            }

        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //moveTaskToBack(true);
            Intent intent = new Intent();
            intent.setClass(GameActivity.this, MainActivity2.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = MediaPlayer.create(this, R.raw.frankum_loop001e);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

}
