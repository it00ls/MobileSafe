package com.it00ls.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 开机启动的广播接受者
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protect = preferences.getBoolean("protect", false);
        if (protect) { // 检测防盗保护是否开启
            String sim = preferences.getString("sim", null); // 获取绑定的SIM序列号
            if (!TextUtils.isEmpty(sim)) {
                // 获取当前SIM卡序列号
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentSim = tm.getSimSerialNumber();
                if (!sim.equals(currentSim)) {// SIM发生变化
                    // 获取安全号码
                    String safe_phone = preferences.getString("safe_phone", "");
                    // 发送报警短信
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safe_phone, null, "SIM Changed!", null, null);
                }
            }
        }
    }
}
