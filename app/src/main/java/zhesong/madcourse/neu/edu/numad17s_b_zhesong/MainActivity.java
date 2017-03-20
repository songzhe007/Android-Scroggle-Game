package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> ninewords = new ArrayList<String>();
    private ArrayList<String> ninewords_strings=new ArrayList<String>();
    private ArrayList<String> dictionary = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Zhe Song");


        Button about_button = (Button) findViewById(R.id.about_button);
        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Aboutme_Activity.class);
                startActivity(intent);
            }
        });
        Button Dictionary_button = (Button) findViewById(R.id.new_button);
        Dictionary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, testdictionary_Activity.class);
                startActivity(intent);

            }

        });

        Button ErrorGenerator_button = (Button) findViewById(R.id.ErrorGenerator_button);
        ErrorGenerator_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                RuntimeException e = new RuntimeException();
                throw e;
            }
        });

        Button wordgame_button = (Button) findViewById(R.id.button);
        wordgame_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity2.class);

              //  intent.putStringArrayListExtra("ListString", ninewords_strings);
                //intent.putStringArrayListExtra("ListString", dictionary);
                startActivity(intent);
            }

        });
        subscribeToNews();
    }

    public void subscribeToNews(){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("wordgame");
    }

    public void sendNotification(View view){
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, Scoreboard_Activity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setContentIntent(pIntent).build();
/*                .addAction(R.drawable.icon, "Call", pIntent)
                .addAction(R.drawable.icon, "More", pIntent)
                .addAction(R.drawable.icon, "And more", pIntent).build();*/

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL ;

        notificationManager.notify(0, noti);
    }
}
