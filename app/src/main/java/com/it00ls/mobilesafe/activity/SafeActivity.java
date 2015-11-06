package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.it00ls.mobilesafe.R;

public class SafeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean safe = preferences.getBoolean("safe", false);
        if (safe) {
            setContentView(R.layout.activity_safe);
            TextView tv_safe_phone = (TextView) findViewById(R.id.tv_safe_phone);
            String safe_phone = preferences.getString("safe_phone", "");
            tv_safe_phone.setText(safe_phone);
            ImageView iv_protect = (ImageView) findViewById(R.id.iv_protect);
            if (preferences.getBoolean("protect", false)) {
                iv_protect.setImageResource(R.drawable.lock);
            } else {
                iv_protect.setImageResource(R.drawable.unlock);
            }
        } else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }

    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
