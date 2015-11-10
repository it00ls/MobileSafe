package com.it00ls.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 获取保存经纬度坐标的服务
 */
public class LocationService extends Service {

    private SharedPreferences mPref;
    private LocationManager location;
    private LocationListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        location = (LocationManager) getSystemService(LOCATION_SERVICE); // 获取定位服务
        Criteria criteria = new Criteria(); // 创建标准对象
        criteria.setCostAllowed(true); // 是否允许付费
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置精确度
        String bestProvider = location.getBestProvider(criteria, true);// 获取最佳的provider
        listener = new MyLocationListener();
        location.requestLocationUpdates(bestProvider, 0, 0, listener); // 定位

    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) { // 位置发生改变时调用
            double longitude = location.getLongitude(); // 获取经度
            double latitude = location.getLatitude(); // 获取纬度
            mPref.edit().putString("location", "j:" + longitude + " " + "w:" + latitude).commit();
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { // 状态发生改变时调用
            Log.d("Android.out", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) { //用户打开GPS时调用
            Log.d("Android.out", "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) { // 用户关闭GPS时调用
            Log.d("Android.out", "onProviderDisabled");
        }
    }

    @Override
    public void onDestroy() {
        location.removeUpdates(listener);
    }
}
