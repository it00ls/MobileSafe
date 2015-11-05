package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.it00ls.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    public void previous(View view) {
        showPreviousPage();
    }

    public void next(View view) {
        showNextPage();
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        overridePendingTransition(R.anim.translate_in_previous, R.anim.translate_out_previous);
        finish();
    }

    @Override
    public void showNextPage() {
        mPref.edit().putBoolean("safe", true).commit();
        startActivity(new Intent(this, SafeActivity.class));
        overridePendingTransition(R.anim.translate_in_next, R.anim.translate_out_next);
        finish();
    }
}
