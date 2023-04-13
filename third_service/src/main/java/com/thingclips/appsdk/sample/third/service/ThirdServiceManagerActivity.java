package com.thingclips.appsdk.sample.third.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.jsbridge.base.webview.WebViewActivity;
import com.thingclips.smart.personal.core.bean.ThirdIntegrationBean;
import com.thingclips.smart.personal.third.service.api.AbsPersonalThirdService;
import com.thingclips.smart.personal.third.service.api.callback.IPersonalThirdServiceCallback;
import com.thingclips.smart.personal.third.service.api.enums.PersonalThirdServiceType;

/**
 * @author qinyun.miao
 */
public class ThirdServiceManagerActivity extends AppCompatActivity {

    private static final String TAG = "ThirdServiceManagerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_service_manager);
        findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routePushSmsService();
            }
        });
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routePushCallService();
            }
        });
    }

    private void routePushSmsService() {
        AbsBizBundleFamilyService familyService = MicroContext.getServiceManager()
                .findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        if (familyService == null) {
            Log.e(TAG, "AbsBizBundleFamilyService == null");
            return;
        }

        AbsPersonalThirdService thirdService = MicroContext.getServiceManager()
                .findServiceByInterface(AbsPersonalThirdService.class.getName());
        if (thirdService == null) {
            Log.e(TAG, "AbsPersonalThirdService == null");
            return;
        }

        thirdService.requestPersonalThirdService(
                familyService.getCurrentHomeId(),
                PersonalThirdServiceType.PUSH_SMS_SERVICE,
                new IPersonalThirdServiceCallback() {
                    @Override
                    public void onSuccess(ThirdIntegrationBean bean) {
                        if (bean != null) {
                            routeWebActivity(bean.getUrl());
                        } else {
                            Log.e(TAG, "not support PUSH_SMS_SERVICE");
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        Log.e(TAG, "errorCode:" + errorCode + " errorMessage" + errorMessage);
                    }
                }
        );
    }

    private void routePushCallService() {
        AbsBizBundleFamilyService familyService = MicroContext.getServiceManager()
                .findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        if (familyService == null) {
            Log.e(TAG, "AbsBizBundleFamilyService == null");
            return;
        }

        AbsPersonalThirdService thirdService = MicroContext.getServiceManager()
                .findServiceByInterface(AbsPersonalThirdService.class.getName());
        if (thirdService == null) {
            Log.e(TAG, "AbsPersonalThirdService == null");
            return;
        }

        thirdService.requestPersonalThirdService(
                familyService.getCurrentHomeId(),
                PersonalThirdServiceType.PUSH_CALL_SERVICE,
                new IPersonalThirdServiceCallback() {
                    @Override
                    public void onSuccess(ThirdIntegrationBean bean) {
                        if (bean != null) {
                            routeWebActivity(bean.getUrl());
                        } else {
                            Log.e(TAG, "not support PUSH_CALL_SERVICE");
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        Log.e(TAG, "errorCode:" + errorCode + " errorMessage" + errorMessage);
                    }
                }
        );
    }

    private void routeWebActivity(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("Uri", url);
        startActivity(intent);
    }
}
