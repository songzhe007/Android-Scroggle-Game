package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import zhesong.madcourse.neu.edu.numad17s_b_zhesong.UsersInfo;
/**
 * Created by SongZheDerrick on 17/3/11.
 */

public class UploadDialog extends DialogFragment {

   private static final String SERVER_KEY = "key=AAAAQwpeuG4:APA91bHhUvjqB2E63NIxXZq_TD_HYoqWCy6BCl5V1u1uCcS5t-0HjAiW6LTKKKmVk37E-VW_11binQPBx2xOe3VJVV4ff9gTeMOO8uCuayKuIjnnY8y4qCvSz2xQc-e4pMrN3mWjsxOk";
    private DatabaseReference mDatabase;
    private String token;
    private String word_highestscore;
    private int score_total;
    private int score_wordhighest;
    private int score_p1;
    private TextView score_text;
    private TextView name_text;
    private EditText username;
    private Spinner splist;
    private Button button_sumbit;
    private Button button_cancel;
    private Set<String> current_names;
    private Map<String, List<UsersInfo>> content_map;

    public void loadDialog(int score_total, int score_p1, int score_wordhighest, String word_highestscore){
        this.score_total = score_total;
        this.score_p1 = score_p1;
        this. score_wordhighest = score_wordhighest;
        this. word_highestscore = word_highestscore;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onGetNewHighScore(int score, String name){
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try{
            jNotification.put("title", "Google I/O 2016");
            jNotification.put("body", name + " got a new high score of " + score + "!");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jPayload.put("to", "/topics/wordgame");
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
//                    Toast.makeText(getActivity(),resp,Toast.LENGTH_LONG);
                    System.out.println("resp is: "+resp);
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

    // database: user{$token{$name{...}} }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = getActivity().getLayoutInflater();
        View view = inflator.inflate(R.layout.activity_upload, null);
        splist = (Spinner)view.findViewById(R.id.spinner);
        button_sumbit = (Button)view.findViewById(R.id.button_submit);
        button_cancel = (Button)view.findViewById(R.id.button_cancel);
        score_text = (TextView)view.findViewById(R.id.textView_score);
        name_text = (TextView)view.findViewById(R.id.textView_name);
        username = (EditText)view.findViewById(R.id.editText);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
        if(token.isEmpty()){
            name_text.setText("Sorry your device is currently offline");
            button_sumbit.setEnabled(false);
            builder.setView(view);
            return builder.create();
        }
        score_text.setText("Congratulations you have got a total score of " + score_total + " with " + score_p1 + " in phase 1 and " + (score_total - score_p1) + " in phase 2!");
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm::ss");
        final UsersInfo userinfo = new UsersInfo();
        userinfo.highestword = word_highestscore;
        userinfo.score_p1 = score_p1;
        userinfo.score_total = score_total;
        userinfo.score_wordhighest = score_wordhighest;
        userinfo.highestword = word_highestscore;
        userinfo.time = df.format(date);
        mDatabase.child("user").child(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, List<UsersInfo>>> t = new GenericTypeIndicator<HashMap<String, List<UsersInfo>>>() {};
                content_map = (Map<String, List<UsersInfo>>)dataSnapshot.getValue(t);
                if(content_map == null){
                    current_names = new HashSet<>();
                    content_map = new HashMap<>();
                }
                else
                    current_names = content_map.keySet();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, current_names.toArray(new String[current_names.size()]));
                splist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        splist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("there!"+(String)parent.getItemAtPosition(position));
                username.setText((String)parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = username.getText().toString();
                if(name.length() < 4){
                    name_text.setText("User name must be at least 4 characters");
                    return;
                }
                if(name.length() > 8){
                    name_text.setText("User name shoule be at most 8 characters");
                    return;
                }
                mDatabase.child("user_score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, UsersInfo>> t = new GenericTypeIndicator<HashMap<String, UsersInfo>>() {};
                        Map<String, UsersInfo> name_map = dataSnapshot.getValue(t);
                        if(name_map == null)
                            name_map = new HashMap<>();
                        if(current_names.contains(name)){
                            content_map.get(name).add(userinfo);
                            mDatabase.child("user").child(token).setValue(content_map);
                            UsersInfo lastInfo = name_map.get(name);
                            if(userinfo.score_total > lastInfo.score_total){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onGetNewHighScore(score_total, name);
                                    }
                                }).start();
                                userinfo.token = token;
                                name_map.put(name, userinfo);
                                mDatabase.child("user_score").setValue(name_map);
                            }
                        }
                        else if(name_map.keySet().contains(name)){
                            username.setText("");
                            name_text.setText("Sorry the user name is already in use, please select another name");
                        }
                        else{
                            List<UsersInfo> list = new ArrayList<>();
                            list.add(userinfo);
                            content_map.put(name, list);
                            mDatabase.child("user").child(token).setValue(content_map);
                            userinfo.token = token;
                            name_map.put(name, userinfo);
                            mDatabase.child("user_score").setValue(name_map);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(username.getText().length() > 0)
                    UploadDialog.this.dismiss();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDialog.this.dismiss();
            }
        });
//        Map<String, List<UsersInfo>> content_map = new HashMap<>();
//
//        List<UsersInfo> list = new ArrayList<>();
//        list.add(userinfo);
//        content_map.put("name1", list);
//        mDatabase.child("user").child(token).setValue(content_map);
//        Query q = mDatabase.child("user").child("u1").orderByKey();
//        System.out.println(q.toString());
//        String[] arraySpinner = new String[] {
//                q.toString()
//        };
//        Calendar c = Calendar.getInstance();
//        int seconds = c.get(Calendar.YEAR);
//        System.out.println(c.toString());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, arraySpinner);
//        splist.setAdapter(adapter);
        builder.setView(view);
        return builder.create();
    }
}

