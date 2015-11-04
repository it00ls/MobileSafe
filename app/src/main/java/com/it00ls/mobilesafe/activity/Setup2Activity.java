package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.it00ls.mobilesafe.R;

public class Setup2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    public void previous(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        overridePendingTransition(R.anim.translate_in_previous, R.anim.translate_out_previous);
        finish();
    }

    public void next(View view) {
        startActivity(new Intent(this, Setup3Activity.class));
        overridePendingTransition(R.anim.translate_in_next, R.anim.translate_out_next);
        finish();
    }
}
