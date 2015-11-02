package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it00ls.mobilesafe.R;
import com.it00ls.mobilesafe.utils.MD5Utils;

import org.w3c.dom.Text;

/**
 * 主界面
 *
 * @author it00ls
 */
public class HomeActivity extends Activity {

    private static final int HOME_SETTINGS = 8;
    private static final int HOME_SAFE = 0;
    private GridView gv_home;
    private SharedPreferences mPref;
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] mPics = new int[]{R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case HOME_SAFE:
                        // 手机防盗
                        showPasswordDialog();
                        break;
                    case HOME_SETTINGS:
                        // 设置中心
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }

    /**
     * 弹出密码对话框
     */
    private void showPasswordDialog() {
        // 判断是否有设置密码
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        String password = mPref.getString("password", null);
        if (!TextUtils.isEmpty(password)) {
            // 有密码则弹出输入密码对话框
            showGetPasswordDialog(password);
        } else {
            // 未设置密码则弹出设置密码对话框
            showSetPasswordDialog();
        }
    }

    /**
     * 弹出校验密码对话框
     */
    private void showGetPasswordDialog(final String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_get_password, null);
        dialog.setView(view, 0, 0, 0, 0);
        // 设置确定按钮点击侦听
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_password = (EditText) view.findViewById(R.id.et_password);
                String pass = et_password.getText().toString();
                if (password.equals(MD5Utils.encode(pass))) {
                    Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 设置取消按钮点击侦听
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 弹出设置密码对话框
     */
    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_password, null);
        dialog.setView(view, 0, 0, 0, 0);

        // 设置确定按钮点击侦听
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_password = (EditText) view.findViewById(R.id.et_password);
                EditText et_repassword = (EditText) view.findViewById(R.id.et_repassword);
                String password = et_password.getText().toString();
                String repassword = et_repassword.getText().toString();
                if (!TextUtils.isEmpty(repassword)) {
                    if (password.equals(repassword)) {
                        Toast.makeText(HomeActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        mPref.edit().putString("password", MD5Utils.encode(password)).commit();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 设置取消按钮点击侦听
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.item_home_grid, null);
            ImageView iv_griditem = (ImageView) view.findViewById(R.id.iv_griditem);
            TextView tv_griditem = (TextView) view.findViewById(R.id.tv_griditem);
            iv_griditem.setImageResource(mPics[position]);
            tv_griditem.setText(mItems[position]);
            return view;
        }
    }
}
