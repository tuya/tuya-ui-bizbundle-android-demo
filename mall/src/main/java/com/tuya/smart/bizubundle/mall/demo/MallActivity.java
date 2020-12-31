package com.tuya.smart.bizubundle.mall.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.api.MicroContext;
import com.tuya.smart.jsbridge.base.webview.WebViewActivity;
import com.tuya.smart.tuyamall.api.IGetMallUrlCallback;
import com.tuya.smart.tuyamall.api.TuyaMallService;
import com.tuya.smart.utils.ToastUtil;

public class MallActivity extends AppCompatActivity {

    private static boolean isActivity;
    private EditText mUrlEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Mall");
        final SwitchCompat switchCompat = findViewById(R.id.container_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isActivity = isChecked;
                switchCompat.setText(isActivity ? "open by WebViewActivity" : "open by WebViewFragment");
            }
        });
        mUrlEdit = findViewById(R.id.input_web);
        mUrlEdit.setSelection(mUrlEdit.getText().length());
        findViewById(R.id.openWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mUrlEdit.getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    openWeb(MallActivity.this, url);
                }
            }
        });
        TuyaMallService service = MicroContext.getServiceManager().findServiceByInterface(TuyaMallService.class.getName());
        ToastUtil.showToast(this, "mall enable is " + service.isSupportMall());
        service.requestMallHome(new IGetMallUrlCallback() {
            @Override
            public void onSuccess(String url) {
                mUrlEdit.setText(url);
            }

            @Override
            public void onError(String code, String error) {

            }
        });
        service.requestMallUserCenter(new IGetMallUrlCallback() {
            @Override
            public void onSuccess(String url) {
                Log.e("mall user center url ", url);
            }

            @Override
            public void onError(String code, String error) {

            }
        });
    }

    private void openWeb(Context context, String url) {
        Intent intent = new Intent(context, isActivity ? WebViewActivity.class : WebActivity.class);
        intent.putExtra("Uri", url);
        context.startActivity(intent);
    }
}
