package com.it00ls.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.service.LocationService;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        if (objects != null) {
            for (Object object : objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
                String address = message.getOriginatingAddress();// 获取短信号码
                String body = message.getMessageBody();// 获取短信内容
                Log.d("Android.out", address + " : " + body);
                if (body.equals("#*alarm*#")) { // 播放报警音乐
                    abortBroadcast();
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
                    mediaPlayer.setVolume(1f, 1f); // 设置音量
                    mediaPlayer.setLooping(false); // 循环播放
                    mediaPlayer.start();
                } else if (body.equals("#*location*#")) { // 获取经纬度坐标
                    context.startService(new Intent(context, LocationService.class)); // 启动服务更新坐标
                }
            }
        }
    }
}
