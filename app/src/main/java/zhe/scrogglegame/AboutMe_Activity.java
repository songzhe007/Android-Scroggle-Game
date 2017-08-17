package zhe.scrogglegame;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

/**
 * Created by songz on 12/22/2017.
 */

public class AboutMe_Activity extends AppCompatActivity {
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
