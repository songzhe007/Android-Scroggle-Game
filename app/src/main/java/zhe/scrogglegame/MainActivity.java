package zhe.scrogglegame;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;



public class MainActivity extends AppCompatActivity {



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
                intent.setClass(MainActivity.this, AboutMe_Activity.class);
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
                startActivity(intent);
            }

        });
        subscribeToNews();
    }

    public void subscribeToNews(){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("wordgame");
    }

}
