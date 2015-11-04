package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.it00ls.mobilesafe.R;

public class SafeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean safe = preferences.getBoolean("safe", false);
        if (safe) {
            setContentView(R.layout.activity_safe);
        } else {
            startActivity(new Intent(this, Setup1Activity.class));
            finish();
        }
    }
}
