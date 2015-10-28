package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    // 服务器返回的信息
    private String mVersionName; // 版本名
    private int mVersionCode; // 版本号
    private String mDescription; // 版本信息
    private String mDownloadUrl; // 下载地址

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DAILOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT);
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT);
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT);
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
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
        final long startTime = System.currentTimeMillis();
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                Message msg = Message.obtain();
                try {
                    URL url = new URL("http://120.203.57.245/update.json");
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
                    long endTime = System.currentTimeMillis();
                    long useTime = endTime - startTime;
                    if (useTime < 2000) {
                        try {
                            Thread.sleep(2000 - useTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(msg);
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
                download();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 下载apk文件
     */
    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            HttpUtils httpUtils = new HttpUtils();
            String target = Environment.getExternalStorageDirectory() + "/Download/update.apk";
            final TextView tv_progress = (TextView) findViewById(R.id.tv_progress);
            tv_progress.setVisibility(View.VISIBLE);
            httpUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.d("download", current + "/" + total);
                    tv_progress.setText("下载进度:" + current * 100 / total + "%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                    // 跳转到安装页面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(SplashActivity.this, "未找到SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用户取消安装，回调此方法
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        意图
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
    }

    /**
     * 进入主页面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
