package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author it00ls
 */
public class SplashActivity extends Activity {

    private static final int CODE_UPDATE_DAILOG = 0;
    private static final int CODE_ENTER_HOME = 1;
    private static final int CODE_URL_ERROR = 2;
    private static final int CODE_NET_ERROR = 3;
    private static final int CODE_JSON_ERROR = 4;

    private String mVersionName;
    private int mVersionCode;
    private String mDescription;
    private String mDownloadUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DAILOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT);
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT);
                    break;
                case CODE_ENTER_HOME:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本:" + getVersionName());
        checkVersion();
    }

    /**
     * 获取版本名
     *
     * @return 返回版本名
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 返回版本号
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                Message msg = Message.obtain();
                try {
                    URL url = new URL("http://192.168.6.1/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET"); // 设置请求方式
                    conn.setConnectTimeout(5000); // 设置连接超时
                    conn.setReadTimeout(5000); // 设置读取超时
                    conn.connect(); // 建立连接
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        String result = StreamUtils.readFromStream(is); // 获取服务器JSON信息
                        // 解析JSON
                        JSONObject json = new JSONObject(result);
                        mVersionName = json.getString("versionName");

                        mVersionCode = json.getInt("versionCode");

                        mDescription = json.getString("description");

                        mDownloadUrl = json.getString("downloadUrl");
                        // 校验版本
                        if (mVersionCode > getVersionCode()) {
                            // 有更新，弹出对话框
                            msg.what = CODE_UPDATE_DAILOG;
                        } else {
                            // 没有更新
                            msg.what = CODE_ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    // URL错误
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    // 网络错误
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    // JSON信息错误
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
//                    Log.d("SplashActivity", "有更新");
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();
    }

    /**
     * 弹出升级对话框
     */
    private void showUpdateDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + mVersionName);
        builder.setMessage(mDescription);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("showUpdateDailog", "立即更新");
            }
        });
        builder.setNegativeButton("稍后再说", null);
        builder.show();
    }
}
