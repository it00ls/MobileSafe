package com.it00ls.mobilesafe.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences mPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        if (objects != null) {
            for (Object object : objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String address = message.getOriginatingAddress();// 获取短信号码
                String body = message.getMessageBody();// 获取短信内容
                Log.d("Android.out", address + " : " + body);
                if (body.equals("#*alarm*#")) { // 播放报警音乐
                    abortBroadcast(); // 拦截广播
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                    mediaPlayer.setVolume(1f, 1f); // 设置音量
                    mediaPlayer.setLooping(false); // 循环播放
                    mediaPlayer.start();
                } else if (body.equals("#*location*#")) { // 获取经纬度坐标
                    abortBroadcast(); // 拦截广播
                    context.startService(new Intent(context, LocationService.class)); // 启动服务更新坐标
                    String location = mPref.getString("location", "");
                    Log.d("Android.out", location);
                } else if (body.equals("#*lockscreen*#")) { // 锁屏
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager)
                            context.getSystemService(Context.DEVICE_POLICY_SERVICE); // 获取设备策略管理器
                    // 创建设备管组件
                    ComponentName componentName = new ComponentName(context, AdminReceiver.class);
                    // 激活设备管理器
                    Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            componentName);
                    intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "哈哈哈, 我们有了超级设备管理器, 好NB!");
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    devicePolicyManager.lockNow();
                } else if (body.equals("#*wipedata*#")) { // 删除数据
                    DevicePolicyManager devicePolicyManager = (DevicePolicyManager)
                            context.getSystemService(Context.DEVICE_POLICY_SERVICE); // 获取设备策略管理器
                    // 创建设备管组件
                    ComponentName componentName = new ComponentName(context, AdminReceiver.class);
                    // 激活设备管理器
                    Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            componentName);
                    intent1.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "哈哈哈, 我们有了超级设备管理器, 好NB!");
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    devicePolicyManager.wipeData(0);
                }
            }
        }
    }
}
