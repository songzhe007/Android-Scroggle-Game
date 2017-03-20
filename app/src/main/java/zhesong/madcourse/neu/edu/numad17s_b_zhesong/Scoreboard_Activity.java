package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import zhesong.madcourse.neu.edu.numad17s_b_zhesong.UsersInfo;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by SongZheDerrick on 17/3/11.
 */

/**
 * Created by SongZheDerrick on 17/3/11.
 */

public class Scoreboard_Activity extends Activity {
    private DatabaseReference mDatabase;
    private String token;
    private ViewGroup vg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        vg = (ViewGroup) findViewById(R.id.scoreboard);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
        if(token.isEmpty()){
            TextView tv = new TextView(this);
            tv.setText("Sorry your device is offline, could not retrieve data");
            vg.addView(tv);
            return;
        }
        mDatabase.child("user").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, List<UsersInfo>>> t = new GenericTypeIndicator<HashMap<String, List<UsersInfo>>>() {};
                Map<String, List<UsersInfo>> myMap = dataSnapshot.getValue(t);
                if(myMap == null){
                    myMap = new HashMap<>();
                }
                for(String key : myMap.keySet()) {
                    for(UsersInfo uinfo : myMap.get(key)){
                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.score_item, null);
                        ((TextView)v.findViewById(R.id.username)).setText(key);
                        ((TextView)v.findViewById(R.id.time)).setText(uinfo.time);
                        ((TextView)v.findViewById(R.id.score)).setText(uinfo.score_p1 + "+" + (uinfo.score_total-uinfo.score_p1) + "=" + uinfo.score_total);
                        ((TextView)v.findViewById(R.id.word)).setText(uinfo.highestword + ": " + uinfo.score_wordhighest);
                        vg.addView(v);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}