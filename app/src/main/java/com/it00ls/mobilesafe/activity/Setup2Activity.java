package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        final SettingItemView siv_sim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = mPref.getString("sim", null);
        if (!TextUtils.isEmpty(sim)) {
            siv_sim.setChecked(true);
        } else {
            siv_sim.setChecked(false);
        }
        siv_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (siv_sim.isCheck()) {
                    siv_sim.setChecked(false);
                    mPref.edit().remove("sim");
                } else {
                    siv_sim.setChecked(true);
                    // 获取sim卡序列号
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    mPref.edit().putString("sim", simSerialNumber).commit();
                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup1Activity.class));
        overridePendingTransition(R.anim.translate_in_previous, R.anim.translate_out_previous);
        finish();
    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        overridePendingTransition(R.anim.translate_in_next, R.anim.translate_out_next);
        finish();
    }
}
