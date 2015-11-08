package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 基础的步骤Activity
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector mDetector;
    public SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        // 创建手势识别对象
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            // 滑动时调用
            @Override               //按下时的事件    松开是的事件
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 300) {
                    Toast.makeText(BaseSetupActivity.this, "不能这样划哦!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNextPage();
                    return true;
                }
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPreviousPage();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 上一步按钮的点击事件
     *
     * @param view
     */
    public void previous(View view) {
        showPreviousPage();
    }

    /**
     * 下一步按钮的点击事件
     *
     * @param view
     */
    public void next(View view) {
        showNextPage();
    }

    /**
     * 显示上一个页面
     */
    public abstract void showPreviousPage();

    /**
     * 显示下一个页面
     */
    public abstract void showNextPage();

    /**
     * 触摸屏幕时调用
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
