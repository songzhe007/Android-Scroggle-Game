package zhe.scrogglegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by songz on 12/22/2017.
 */

public class PickUserName extends Activity {
    private static final String SERVER_KEY = "key=AAAAIe0vroA:APA91bGmplLJOuBbkuZ-YXOCap-ob-8g1n" +
            "XApFXi-31fUwLySsyix_Hry8FmzC3pDj-skq3JghBQUzJeqfyddiQQVlXxlFqhNhAAh2xprz96YDzN14przYmnvg-crQkakE7ttwgh5S4r";
    private DatabaseReference mDatabase;
    private String token;
    private EditText userInput;
    private String username;
    private Map<String,User> contentMap;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickusername);
        userInput = findViewById(R.id.username);
        Button button_submit = findViewById(R.id.button_submit);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        token = FirebaseInstanceId.getInstance().getToken();
        user = new User(username);
        mDatabase.child("MultiPlayerMode").child(token).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String,User>>
                                t = new GenericTypeIndicator<HashMap<String, User>>() {
                        };
                        contentMap = dataSnapshot.getValue(t);
                        if(contentMap == null){
                            contentMap = new HashMap<>();
                        } else{
                            System.out.println("afdsfsfsdfsdfdsfdssssssssssssfssssssss");
                            redirectToGameRoom();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        button_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                username=userInput.getText().toString();
                user = new User(username);
                user.setUserToken(token);
                mDatabase.child("MultiPlayerMode").child(token).child(username).setValue(user);
                redirectToGameRoom();
            }
        });
    }

    public void redirectToGameRoom(){
        Intent intent = new Intent();
        intent.setClass(PickUserName.this, GameRoomActivity.class);
        startActivity(intent);
    }

}
