package com.tuya.smart.bizbundle.demo.socialbind;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.bind.TuyaSocialLoginBindManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;

public class AlexaGoogleBindActivity extends AppCompatActivity {
    private String TAG = "AlexaGoogleBindActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexa_google_bind);
        findViewById(R.id.bt_alexa_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if(null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }

                TuyaSocialLoginBindManager.Companion.getInstance().alexaBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new ITuyaResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        L.d(TAG, "alexa bind onSuccess : " + result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        L.d(TAG, "alexa bind onError : errorCode：" + errorCode + " errorMessage：" + errorMessage);
                    }
                });
            }
        });

        findViewById(R.id.bt_google_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if(null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }

                TuyaSocialLoginBindManager.Companion.getInstance().googleBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new ITuyaResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        L.d(TAG, "google bind onSuccess : " + result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        L.d(TAG, "google bind onError : errorCode：" + errorCode + " errorMessage：" + errorMessage);
                    }
                });
            }
        });
    }
}