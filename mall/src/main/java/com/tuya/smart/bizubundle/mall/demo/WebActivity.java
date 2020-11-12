package com.tuya.smart.bizubundle.mall.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.jsbridge.base.webview.WebViewFragment;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("Uri", getIntent().getStringExtra("Uri"));
        args.putBoolean("enableLeftArea", true);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.web_content, fragment, WebViewFragment.class.getSimpleName())
                .commit();
    }
}
