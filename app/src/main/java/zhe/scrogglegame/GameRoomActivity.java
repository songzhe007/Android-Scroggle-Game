package zhe.scrogglegame;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by songz on 12/23/2017.
 */

public class GameRoomActivity extends ListActivity {
    private Adapter adapter;
    private List<User> users;
    private String currentToken;
    private Map<String, Map<String, User>> UserMap;
    private List<Map<String, User>> ListMap;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentToken = FirebaseInstanceId.getInstance().getToken();

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getListMap(),
                R.layout.user_list_item, new String[]{"username"}, new int[]{R.id.username});
        setListAdapter(simpleAdapter);


    }

    public List<Map<String, User>> getListMap(){
      final Set<String> username_list = new HashSet<>();
       mDatabase.child("MultiPlayerMode").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               GenericTypeIndicator<Map<String,Map<String, User>>>
                       t = new GenericTypeIndicator<Map<String,Map<String, User>>>() {
               };
               UserMap = dataSnapshot.getValue(t);
               if(UserMap.containsKey(currentToken)){
                   UserMap.remove(currentToken);
              }
              for(String token: UserMap.keySet()){
                   for(String username : UserMap.get(token).keySet()){
                       username_list.add(username);
                   }
              }



           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
       for(String token: UserMap.keySet()){
           ListMap.add(UserMap.get(token));
       }
       return ListMap;
    }
}
