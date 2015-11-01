package com.it00ls.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.it00ls.mobilesafe.R;

/**
 * 设置中心自定义组合控件
 *
 * @author it00ls
 */
public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private TextView tv_title;
    private TextView tv_desc;
    private CheckBox cb_check;
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTitle = attrs.getAttributeValue(NAMESPACE, "siv_title");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_settingitem, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        cb_check = (CheckBox) findViewById(R.id.cb_check);

        setTitle(mTitle);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置描述
     *
     * @param desc 描述
     */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }

    /**
     * 判断单选框是否勾选
     *
     * @return true 已勾选，false 未勾选
     */
    public boolean isCheck() {
        return cb_check.isChecked();
    }

    /**
     * 设置单选框勾选
     *
     * @param check true 勾选，false 不勾选
     */
    public void setChecked(boolean check) {
        cb_check.setChecked(check);
        if (check) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
    }
}
