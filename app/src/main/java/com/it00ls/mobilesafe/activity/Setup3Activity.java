package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.it00ls.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        et_phone = (EditText) findViewById(R.id.et_phone);
        String safe_phone = mPref.getString("safe_phone", "");
        et_phone.setText(safe_phone);
    }

    public void selectContact(View view) {
        startActivityForResult(new Intent(this, ContactActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", "");
            et_phone.setText(phone);
        }
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        overridePendingTransition(R.anim.translate_in_previous, R.anim.translate_out_previous);
        finish();
    }

    @Override
    public void showNextPage() {
        String phone = et_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            mPref.edit().putString("safe_phone", phone).commit();
            startActivity(new Intent(this, Setup4Activity.class));
            overridePendingTransition(R.anim.translate_in_next, R.anim.translate_out_next);
            finish();
        } else {
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
