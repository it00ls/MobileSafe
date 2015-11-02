package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
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
 * 闪屏界面
 *
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DAILOG:
                    showUpdateDailog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
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

        // 获取版本信息
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        String version = "版本:" + getVersionName();
        tv_version.setText(version);
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean auto_update = preferences.getBoolean("auto_update", true);
        if (auto_update) {
            checkVersion();
        } else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
        }
        // 设置渐变效果
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1f);
        animation.setDuration(2000); // 延时2秒
        RelativeLayout rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        rl_root.startAnimation(animation);
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
                    URL url = new URL("http://192.168.6.1/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET"); // 设置请求方式
                    conn.setConnectTimeout(5000); // 设置连接超时
                    conn.setReadTimeout(5000); // 设置读取超时
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
                            // 没有更新，进入主界面
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
                    long useTime = endTime - startTime; // 计算子线程运行时间
                    // 使闪屏页面至少停留2秒
                    if (useTime < 2000) {
                        try {
                            Thread.sleep(2000 - useTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // 创建弹出对话框的builder对象
        builder.setTitle("最新版本:" + mVersionName); // 设置对话框标题
        builder.setMessage(mDescription); // 设置对话框内容
        // 设置对话框按钮的点击事件
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        // 若用户按返回键则直接进入主界面
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show(); // 显示对话框
    }

    /**
     * 下载apk文件
     */
    private void download() {
        // 首先判断手机内存卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // xUtils第三方插件
            HttpUtils httpUtils = new HttpUtils();
            // 将apk下载到什么位置
            String target = Environment.getExternalStorageDirectory() + "/Download/update.apk";
            final TextView tv_progress = (TextView) findViewById(R.id.tv_progress);
            tv_progress.setVisibility(View.VISIBLE); // 设置文本框属性为显示
            httpUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    String progress = "下载进度:" + current * 100 / total + "%";
                    tv_progress.setText(progress);
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
