package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 *
 * @author it00ls
 */
public class SettingsActivity extends Activity {

    private SettingItemView siv_Update;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        siv_Update = (SettingItemView) findViewById(R.id.siv_Update);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean auto_update = mPref.getBoolean("auto_update", true);
        if (auto_update) {
            siv_Update.setChecked(true);
        } else {
            siv_Update.setChecked(false);
        }

        siv_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (siv_Update.isCheck()) {
                    // 已勾选
                    siv_Update.setChecked(false);
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    // 未勾选
                    siv_Update.setChecked(true);
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
