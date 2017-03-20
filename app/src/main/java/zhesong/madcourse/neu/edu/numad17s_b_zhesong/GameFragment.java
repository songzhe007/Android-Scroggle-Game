
/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
 ***/
package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.app.Fragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Bundle;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class GameFragment extends Fragment {
    static private int mLargeIds[] = {R.id.large1, R.id.large2, R.id.large3,
            R.id.large4, R.id.large5, R.id.large6, R.id.large7, R.id.large8,
            R.id.large9,};
    static private int mSmallIds[] = {R.id.small1, R.id.small2, R.id.small3,
            R.id.small4, R.id.small5, R.id.small6, R.id.small7, R.id.small8,
            R.id.small9,};
    private Tile mEntireBoard = new Tile(this);
    private Tile mLargeTiles[] = new Tile[9];
    private Tile mSmallTiles[][] = new Tile[9][9];
    private Set<Tile> mAvailable = new HashSet<Tile>();
    private int mSoundX, mSoundO, mSoundMiss, mSoundRewind;
    private SoundPool mSoundPool;
    private float mVolume = 1f;
    private int mLastLarge;
    private int mLastSmall;
    private TextView timer_tv;
    private TextView current_tv;
    private TextView score_tv;
    private Set<String> dictionary = new HashSet<String>();
    private int current_group = -1;
    private String current_text;
    private ArrayList<String> ls = new ArrayList<String>();
    private int current_score = 0;
    private String e;
    private View rootView;
    private Button phase2;
    private int z = 0;
    private List<Integer> group_list;
    private List<Integer> pos_list;
    private String word_highestscore;
    private int score_wordhighest;
    private int current_group2 = -1;
    private String current_text2;
    private int score_p1;
    private int count_down=183;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        group_list = new ArrayList<>();
        pos_list = new ArrayList<>();
        word_highestscore = "";
        score_wordhighest = -1;
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        mSoundX = mSoundPool.load(getActivity(), R.raw.sergenious_movex, 1);
        mSoundO = mSoundPool.load(getActivity(), R.raw.sergenious_moveo, 1);
        mSoundMiss = mSoundPool.load(getActivity(), R.raw.erkanozan_miss, 1);
        mSoundRewind = mSoundPool.load(getActivity(), R.raw.joanne_rewind, 1);
        // get the letters

    }

   private void clearAvailable() {
        mAvailable.clear();
    }

    private void addAvailable(Tile tile) {
        tile.animate();
        mAvailable.add(tile);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.large_board, container, false);
        this.rootView = rootView;
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
        initGame();
        initViews(rootView);
        timer.schedule(timerTask, 0, 1000);
        return rootView;
    }

    private int getScore() {
        int score = 0;
        for (String c : ls) {
            score += addScore(c.charAt(0));
        }
        return score;
    }

    private int addScore(Character c) {
        if (c.equals('a') || c.equals('e') || c.equals('i') || c.equals('o') || c.equals('n') || c.equals('r') || c.equals('t') || c.equals('l') || c.equals('s'))
            return 1;
        if (c.equals('d') || c.equals('g'))
            return 2;
        if (c.equals('b') || c.equals('c') || c.equals('m') || c.equals('p'))
            return 3;
        if (c.equals('f') || c.equals('h') || c.equals('v') || c.equals('w') || c.equals('y'))
            return 4;
        if (c.equals('k'))
            return 5;
        if (c.equals('j') || c.equals('x'))
            return 8;
        return 10;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            count_down--;
            if(count_down%60<10 && count_down%60>=0) {
                timer_tv.setText(Integer.toString(count_down / 60) + ":0" + Integer.toString(count_down % 60));
            }
            else{
                timer_tv.setText(Integer.toString(count_down / 60) + ":" + Integer.toString(count_down % 60));
            }
            //System.out.println("now:"+timer_tv.getText());
            if (timer_tv.getText().toString().equals("1:30")) {
                timerTask.cancel();
                EndPhase1();
                String phase1_end = getString(R.string.phase1_end);
                Toast.makeText(getActivity(), phase1_end, Toast.LENGTH_SHORT).show();
            }
            if (timer_tv.getText().toString().equals("0:00")) {
                timer_tv.setText("Time up!");
                timerTask.cancel();
                EndGame();
            }

        }
    };

    private void EndGame() {
        UploadDialog dlg = new UploadDialog();
        if(score_wordhighest < 1){
            word_highestscore = "N/A";
        }
        dlg.loadDialog(current_score, score_p1, score_wordhighest, word_highestscore);
        dlg.show(getFragmentManager(), "test");
    }

    private void EndPhase1() {
        score_p1 = current_score;
        rootView.findViewById(R.id.button_phase2).setVisibility(View.VISIBLE);
        current_tv.setText("");
        rootView.findViewById(R.id.button_phase2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setEnabled(false);
                        for (int i = 0; i < 9; i++) {
                            View outer = rootView.findViewById(mLargeIds[i]);
                            mLargeTiles[i].setView(outer);
                            for (int j = 0; j < 9; j++) {
                                final Button inner = (Button) outer.findViewById(mSmallIds[j]);
                                  inner.setTag(i);
                                final Tile smallTile = mSmallTiles[i][j];
                                smallTile.setView(inner);

                                if (!inner.getText().toString().equals("")) {
                                    inner.setEnabled(true);
                                    inner.setTextSize(14);
                                    inner.setSelected(false);
                                }
                                inner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Button current_button2 = (Button) view;
                                        current_text2 = current_button2.getText().toString();
                                        int group = (int) inner.getTag();
//                                        System.out.println(group+" "+current_group2);
                                        if (current_group2 == -1) {
                                            ls.add(current_text2);
                                            current_group2 = group;
                                            group_list.add(group);
                                            current_tv.setText(current_text2);
                                            current_button2.setSelected(true);
                                            current_button2.setTextSize(20);
                                            smallTile.animate();
                                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                                        }
                                        else if (group != current_group2) {
                                            if(current_button2.isSelected())
                                                return;
                                            ls.add(current_text2);
                                            group_list.add(group);
                                            StringBuilder strbuilder = new StringBuilder();
                                            for (String s : ls) {
                                                strbuilder.append(s);
                                            }
                                            e = strbuilder.toString();

                                            //     System.out.println("current ls:" + e + " current char:" + current_text);
                                            current_tv.setText(e);
                                            current_group2 = group;
                                            smallTile.animate();
                                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                                            current_button2.setSelected(true);
                                            current_button2.setTextSize(20);
                                        }
                                        else {
                                            if (current_button2.isSelected()) {
                                                System.out.println(current_text2 + " " + ls.toString());
                                                if (current_text2.equals(ls.get(ls.size() - 1))) {
                                                    mSoundPool.play(mSoundO, mVolume, mVolume, 1, 0, 1f);
                                                    ls.remove(ls.size() - 1);
                                                    StringBuilder strbuilder = new StringBuilder();
                                                    for (String s : ls) {
                                                        strbuilder.append(s);
                                                    }
                                                    e = strbuilder.toString();
                                                    current_tv.setText(e);
                                                    current_button2.setTextSize(14);
                                                    current_button2.setSelected(false);
                                                    group_list.remove(group_list.size()-1);
                                                    if (group_list.isEmpty())
                                                        current_group2 = -1;
                                                    else
                                                        current_group2 = group_list.get(group_list.size() - 1);
                                                } else {
                                                    mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                                                }
                                            }
                                            else
                                                mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                                        }
                                    }

                                });
                            }
                        }
                        ls.clear();
                        timerTask.cancel();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(1);
                            }
                        };
                        timer.schedule(timerTask, 0, 1000);

                    }

                });
        if(!ls.isEmpty()){
            View outer = rootView.findViewById(mLargeIds[current_group]);
            for(int i = 0; i < 9; i++)
                ((Button) outer.findViewById(mSmallIds[i])).setSelected(false);
        }
        for (int i = 0; i < 9; i++) {
            View outer = rootView.findViewById(mLargeIds[i]);
            mLargeTiles[i].setView(outer);
            for (int j = 0; j < 9; j++) {
                final Button inner = (Button) outer.findViewById(mSmallIds[j]);
                final Tile smallTile = mSmallTiles[i][j];
                smallTile.setView(inner);
                if (!inner.isSelected()) {
                    inner.setText("");
                }
                inner.setEnabled(false);
            }
        }
        // set to an non-valid group number
        current_group = 10;
    }


    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    };

    private void doHide(boolean hide){
        for(int i = 0; i < 9; i++){
            View outer = rootView.findViewById(mLargeIds[i]);
            outer.setVisibility(hide?View.INVISIBLE:View.VISIBLE);
        }
    }

    private boolean isAdj(int a, int b){
        int ax = a/3;
        int ay = a%3;
        int bx = b/3;
        int by = b%3;
        return (ax-bx)*(ax-bx)+(ay-by)*(ay-by) <= 2;
    }

    private void initViews(final View rootView) {
        Log.d("UT3", "init view");
        mEntireBoard.setView(rootView);
        score_tv = (TextView) rootView.findViewById(R.id.text_score);
        timer_tv = (TextView) rootView.findViewById(R.id.text_timer);
        current_tv = (TextView) rootView.findViewById(R.id.text_word);
        timer_tv.setText("3:00");
        rootView.findViewById(R.id.button_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button pause_button = (Button) v;
                if(pause_button.getText().toString().equals("Pause")){
                    timerTask.cancel();
                    pause_button.setText("Resume");
                    doHide(true);
                }
                else{
                    timerTask.cancel();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(1);
                        }
                    };
                    timer.schedule(timerTask, 0, 1000);
                    pause_button.setText("Pause");
                    doHide(false);
                }
            }
        });
        rootView.findViewById(R.id.button_submit).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ls.size() < 3) {
                            return;
                        }
                        if (dictionary.contains(e)) {
                            ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
                            beep.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 200);
                            int score = getScore();
                            if(score > score_wordhighest){
                                score_wordhighest = score;
                                word_highestscore = e;
                            }
                            current_score += score;
                            score_tv.setText(Integer.toString(current_score));
                            ls.clear();
                            z++;
                            if (phase2.getVisibility() == View.VISIBLE) {
                                for (int i = 0; i < 9; i++) {
                                    View outer2 = rootView.findViewById(mLargeIds[i]);
                                    for (int j = 0; j < 9; j++) {
                                        Button inner = (Button) outer2.findViewById(mSmallIds[j]);
                                        if (!inner.isSelected()) {
                                            inner.setText("");
                                        }
                                        inner.setEnabled(false);
                                    }
                                }
                                timerTask.cancel();
                                String phase2_end = getString(R.string.phase2_end);
                                Toast.makeText(getActivity(), phase2_end, Toast.LENGTH_SHORT).show();
                                EndGame();
                            }
                            else {
                                View outer = rootView.findViewById(mLargeIds[current_group]);
                                for (int i = 0; i < 9; i++) {
                                    Button inner = (Button) outer.findViewById(mSmallIds[i]);
                                    if (!inner.isSelected()) {
                                        inner.setText("");
                                        inner.setEnabled(false);
                                    }
                                }
                                if (z == 9) {
                                    timerTask.cancel();
                                    timer_tv.setText("1:30");
                                    count_down=90;
                                    EndPhase1();
                                    String phase1_end = getString(R.string.phase1_end);
                                    Toast.makeText(getActivity(), phase1_end, Toast.LENGTH_SHORT).show();
                                }
                            }
                            current_group = -1;
                        }
                        else {
                            current_tv.setText("Not Legal!");
                        }

                    }
                });
        phase2 = (Button) rootView.findViewById(R.id.button_phase2);
        phase2.setVisibility(View.INVISIBLE);


        for (int large = 0; large < 9; large++) {
            View outer = rootView.findViewById(mLargeIds[large]);
            mLargeTiles[large].setView(outer);
            for (int small = 0; small < 9; small++) {
                Button inner = (Button) outer.findViewById(mSmallIds[small]);
                inner.setTag(large+small*10);
                final Tile smallTile = mSmallTiles[large][small];
                smallTile.setView(inner);

                inner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Button current_button = (Button) view;
                        int group = ((int) current_button.getTag())%10;
                        if (current_group == -1) {

                            smallTile.animate();
                            current_group = group;
                            current_button.setSelected(true);
                            current_button.setTextSize(20);
                            current_text = current_button.getText().toString();
                            ls.add(current_text);
                            current_tv.setText(ls.get(0).toString());
                            pos_list.add(((int) current_button.getTag())/10);

                        } else if (current_group == group) {
                            current_text = current_button.getText().toString();
                            if (current_button.isSelected()) {
                                if (current_text.equals(ls.get(ls.size() - 1))) {
                                    mSoundPool.play(mSoundO, mVolume, mVolume, 1, 0, 1f);
                                    ls.remove(ls.size() - 1);
                                    pos_list.remove(pos_list.size() - 1);
                                    StringBuilder strbuilder = new StringBuilder();
                                    for (String s : ls) {
                                        strbuilder.append(s);
                                    }
                                    e = strbuilder.toString();
                                    current_tv.setText(e);
                                    current_button.setTextSize(14);
                                    current_button.setSelected(false);
                                    if (ls.isEmpty())
                                        current_group = -1;
                                } else {
                                    mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                                }
                                return;
                            }
                            if(!isAdj(((int) current_button.getTag())/10, pos_list.get(pos_list.size() - 1)))
                                return;
                            ls.add(current_text);
                            pos_list.add(((int) current_button.getTag())/10);
                            StringBuilder strbuilder = new StringBuilder();
                            for (String s : ls) {
                                strbuilder.append(s);
                            }
                            e = strbuilder.toString();
                            //  System.out.println("current ls:" + e + " current char:" + current_text);
                            current_tv.setText(e);

                            smallTile.animate();
                            mSoundPool.play(mSoundX, mVolume, mVolume, 1, 0, 1f);
                            current_button.setSelected(true);
                            current_button.setTextSize(20);

                        } else
                            mSoundPool.play(mSoundMiss, mVolume, mVolume, 1, 0, 1f);
                    }
                    // ...
                });
            }
        }
    }




   public void initGame() {

        Log.d("UT3", "init game");
        mEntireBoard = new Tile(this);
        // Create all the tiles
        for (int large = 0; large < 9; large++) {
            mLargeTiles[large] = new Tile(this);
            for (int small = 0; small < 9; small++) {
                mSmallTiles[large][small] = new Tile(this);
            }
            mLargeTiles[large].setSubTiles(mSmallTiles[large]);
        }
        mEntireBoard.setSubTiles(mLargeTiles);

        // If the player moves first, set which spots are available
        mLastSmall = -1;
        mLastLarge = -1;
        setAvailableFromLastMove(mLastSmall);
    }

    private void setAvailableFromLastMove(int small) {
        clearAvailable();
        // Make all the tiles at the destination available
        if (small != -1) {
            for (int dest = 0; dest < 9; dest++) {
                Tile tile = mSmallTiles[small][dest];
                if (tile.getOwner() == Tile.Owner.NEITHER)
                    addAvailable(tile);
            }
        }
        // If there were none available, make all squares available
        if (mAvailable.isEmpty()) {
            setAllAvailable();
        }
    }

    private void setAllAvailable() {
        for (int large = 0; large < 9; large++) {
            for (int small = 0; small < 9; small++) {
                Tile tile = mSmallTiles[large][small];
                if (tile.getOwner() == Tile.Owner.NEITHER)
                    addAvailable(tile);
            }
        }
    }





}

