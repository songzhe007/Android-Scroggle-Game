package zhe.scrogglegame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;


/**
 * Created by SongZheDerrick on 17/3/12.
 */

public class Leaderboard_Activity extends Activity {
    private DatabaseReference mDatabase;
    private String token;
    private static final String SERVER_KEY= "key=AAAAIe0vroA:APA91bGmplLJOuBbkuZ-YXOCap-ob-8g1nXApFXi-31fUwLySsyix_Hry8FmzC3pDj-skq3Jg" +
            "hBQUzJeqfyddiQQVlXxlFqhNhAAh2xprz96YDzN14przYmnvg-crQkakE7ttwgh5S4r";
    private ViewGroup vg;
    private String sender;
    final class MyEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TextView currentText = (TextView)msg.obj;
            if(currentText.getText().toString().equals("Congrats sent!")){
                currentText.setTextColor(Color.BLACK);
                currentText.setText((String) currentText.getTag());
                currentText.setTag("");
            }
            else {
                currentText.setText((String) currentText.getTag());
                currentText.setTag(null);
            }
        }
    };
    // small top
    public class UsersInfoComparator implements Comparator<MyEntry<String,UsersInfo>> {
        @Override
        public int compare(MyEntry<String,UsersInfo> o1, MyEntry<String,UsersInfo> o2) {
            if(o1.getValue().score_total < o2.getValue().score_total)
                return -1;
            else if(o1.getValue().score_total == o2.getValue().score_total)
                return 0;
            return 1;
        }
    }

    private void sendCongrat(String to){
        System.out.println("Sender is:"+sender);
        if(sender.isEmpty()) {
            mDatabase.child("user").child(token).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String, List<UsersInfo>>> t = new GenericTypeIndicator<HashMap<String, List<UsersInfo>>>() {
                            };
                            Map<String, List<UsersInfo>> myMap = dataSnapshot.getValue(t);
                            String from = "nobody";
                            if (myMap != null) {
                                for (String name : myMap.keySet()) {
                                    from = name;
                                    break;
                                }
                            }
                            sender = from;
                            System.out.println("sender now:" + sender);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try{
            jNotification.put("title", "Google I/O 2016");
            while(sender.isEmpty()){}
            jNotification.put("body", sender + " sends you a congrats!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jPayload.put("to", to);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Leaderboard_Activity.this,resp,Toast.LENGTH_LONG);
                }
            });
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        vg = (ViewGroup) findViewById(R.id.scoreboard);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
        sender = "";
        if(token.isEmpty()){
            TextView tv = new TextView(this);
            tv.setText("Sorry your device is offline, could not retrieve data");
            vg.addView(tv);
            return;
        }
        mDatabase.child("user_score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vg.removeAllViews();
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_item, null);
                vg.addView(view);
                GenericTypeIndicator<HashMap<String, UsersInfo>> t = new GenericTypeIndicator<HashMap<String, UsersInfo>>() {};
                Map<String, UsersInfo> myMap = dataSnapshot.getValue(t);
                if(myMap == null){
                    return;
                }
                Comparator<MyEntry<String,UsersInfo>> comparator = new UsersInfoComparator();
                PriorityQueue<MyEntry<String,UsersInfo>> q = new PriorityQueue<>(10, comparator);
                for(String key : myMap.keySet()) {
                    if(q.size() == 10)
                        q.poll();
                    q.add(new MyEntry(key, myMap.get(key)));
                }
                List<MyEntry<String,UsersInfo>> list = new ArrayList<>();
                while(!q.isEmpty()){
                    list.add(q.poll());
                }
                for(int i = list.size()-1; i >= 0; i--){
                    View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_item, null);
                    final String token_to = myMap.get(list.get(i).getKey()).token;
                    ((TextView)v.findViewById(R.id.username)).setText(list.get(i).getKey());
                    ((TextView)v.findViewById(R.id.username)).setTextColor(Color.BLUE);
                    v.findViewById(R.id.username).setClickable(true);
                    v.findViewById(R.id.username).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(v.getTag() != null){
                                if(((TextView)v).getText().toString().equals("Click to send congrats")){

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendCongrat(token_to);
                                        }
                                    }).start();
                                    ((TextView)v).setText("Congrats sent!");
                                }
                                return;
                            }
                            v.setTag(((TextView) v).getText().toString());
                            ((TextView)v).setText("Click to send congrats");
                            Message msg = Message.obtain(mHandler);
                            msg.obj = v;
                            mHandler.sendMessageDelayed(msg, 5000);
                        }
                    });
                    ((TextView)v.findViewById(R.id.time)).setText(list.get(i).getValue().time);
                    ((TextView)v.findViewById(R.id.score)).setText(list.get(i).getValue().score_p1 + "+" + (list.get(i).getValue().score_total-list.get(i).getValue().score_p1) + "=" + list.get(i).getValue().score_total);
                    ((TextView)v.findViewById(R.id.word)).setText(list.get(i).getValue().highestword + ": " + list.get(i).getValue().score_wordhighest);
                    vg.addView(v);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
