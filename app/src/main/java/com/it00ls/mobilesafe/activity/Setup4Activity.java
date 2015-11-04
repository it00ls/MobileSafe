package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.it00ls.mobilesafe.R;

public class Setup4Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void previous(View view) {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();
    }

    public void next(View view) {
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        preferences.edit().putBoolean("safe", true).commit();
        startActivity(new Intent(this, SafeActivity.class));
        finish();
    }
}
