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
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sivUpdate = (SettingItemView) findViewById(R.id.sivUpdate);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean auto_update = mPref.getBoolean("auto_update", true);
        if (auto_update) {
            sivUpdate.setChecked(true);
        } else {
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isCheck()) {
                    // 已勾选
                    sivUpdate.setChecked(false);
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    // 未勾选
                    sivUpdate.setChecked(true);
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
