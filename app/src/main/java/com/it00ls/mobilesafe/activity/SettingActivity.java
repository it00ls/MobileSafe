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
//        sivUpdate.setTitle("自动更新设置");

        mPref = getSharedPreferences("Setting_Config", MODE_PRIVATE);
        boolean auto_update = mPref.getBoolean("auto_update", true);
        if (auto_update) {
//            sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
//            sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }

        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isCheck()) {
                    // 已勾选
                    sivUpdate.setChecked(false);
//                    sivUpdate.setDesc("自动更新已关闭");
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    // 未勾选
                    sivUpdate.setChecked(true);
//                    sivUpdate.setDesc("自动更新已开启");
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }
}
