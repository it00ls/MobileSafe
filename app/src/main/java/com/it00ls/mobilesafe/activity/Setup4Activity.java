package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.it00ls.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        cb_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_protect.isChecked()) {
                    cb_protect.setText("你已经开启防盗保护");
                    mPref.edit().putBoolean("protect", true).commit();
                } else {
                    cb_protect.setText("你没有开启防盗保护");
                    mPref.edit().putBoolean("protect", false).commit();
                }
            }
        });
        boolean protect = mPref.getBoolean("protect", false);
        if (protect) {
            cb_protect.setChecked(true);
            cb_protect.setText("你已经开启防盗保护");
        } else {
            cb_protect.setChecked(false);
            cb_protect.setText("你没有开启防盗保护");
        }
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
