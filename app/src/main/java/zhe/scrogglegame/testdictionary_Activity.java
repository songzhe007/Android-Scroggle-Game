package zhe.scrogglegame;


import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class testdictionary_Activity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testdictionary);
        setTitle("Test Dictionary");

       final List<String> ls =new ArrayList<String>();
        final ListView lv = (ListView) findViewById(R.id.lv);
        final ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ls);
         lv.setAdapter(arrayAdapter);


        InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        final Set<String> dictionary = new HashSet<String>();
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


        final EditText editText=(EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(dictionary.contains(s.toString()))
                {
                    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
                        beep.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 200);
                      ls.add(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button clear = (Button) findViewById(R.id.clear_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                ls.clear();
                arrayAdapter.notifyDataSetChanged();

            }
        });
        Button return_button =(Button) findViewById(R.id.retrun_button);
        return_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(testdictionary_Activity.this, MainActivity.class));
        }
    });
        Button ac_button =(Button) findViewById(R.id.acbutton);
        ac_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(testdictionary_Activity.this, Acknowledgement_Activity.class));
            }
        });
}
}



