package com.thingclips.smart.bizbundle.marketing.demo;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.advertisement.api.AdRoute;
import com.thingclips.smart.advertisement.api.AdvertisementManager;
import com.thingclips.smart.advertisement.api.OnADSplashViewActionListener;
import com.thingclips.smart.advertisement.api.view.IAdSplashView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        IAdSplashView view  = AdvertisementManager.getAdSplashView(this);
        if (view != null) {
            showAppAd(view);
        }
    }

    private void showAppAd(IAdSplashView view) {
        view.showSplashView(this, new OnADSplashViewActionListener() {
            @Override
            public void onADSplashImageClick(String actionUrl) {
                if (TextUtils.isEmpty(actionUrl)) {
                    return;
                }
                //Please set your app's scheme when initializing the app
                //UrlRouter.setScheme("smartlife");
                AdRoute.multiRoute(SplashActivity.this, actionUrl);
            }

            @Override
            public void onADSplashViewDismiss(boolean initiativeDismiss) {
                // do something by yourself
                finish();
            }
        });
    }
}