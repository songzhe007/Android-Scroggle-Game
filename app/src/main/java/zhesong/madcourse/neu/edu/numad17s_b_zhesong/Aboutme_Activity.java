package zhesong.madcourse.neu.edu.numad17s_b_zhesong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.TextView;


public class Aboutme_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutme_activity);
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();

        TextView ID = (TextView) findViewById(R.id.imei);
        ID.setText(IMEI);
    }
}


