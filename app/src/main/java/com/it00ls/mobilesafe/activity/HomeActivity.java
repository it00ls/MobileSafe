package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.it00ls.mobilesafe.R;

/**
 * @author it00ls
 */
public class HomeActivity extends Activity {

    private GridView gv_Home;
    private String[] mItems = new String[] {"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int[] mPics = new int[] {R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gv_Home = (GridView) findViewById(R.id.gv_home);
        gv_Home.setAdapter(new HomeAdapter());
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
            View view = View.inflate(HomeActivity.this, R.layout.griditem_home, null);
            ImageView iv_griditem = (ImageView) view.findViewById(R.id.iv_griditem);
            TextView tv_griditem = (TextView) view.findViewById(R.id.tv_griditem);
            iv_griditem.setImageResource(mPics[position]);
            tv_griditem.setText(mItems[position]);
            Log.d("getView", "调用");
            return view;
        }
    }
}
