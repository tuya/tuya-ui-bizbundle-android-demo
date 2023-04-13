package com.thingclips.smart.bizbundle.miniapp.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.thing.smart.miniappclient.ThingMiniAppClient;

/**
 * @author xiaotan.yu
 * @date 2023/3/6
 */
public class ActMiniAppSample extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miniapp_sample);

        findViewById(R.id.btn_miniapp_init).setOnClickListener(this::init);
        findViewById(R.id.btn_open_miniapp).setOnClickListener(this::openMiniAPP);
        findViewById(R.id.btn_clea_miniapp_cache).setOnClickListener(this::clearCache);
    }

    public void init(View view){
        ThingMiniAppClient
                .initialClient()
                .initialize();
    }

    public void openMiniAPP(View view){
        ThingMiniAppClient
                .coreClient()
                .openMiniAppByAppId(this, "tydxwunc8rjqvh4gaw", null, null);
    }

    public void clearCache(View view){
        ThingMiniAppClient
                .coreClient()
                .clearCache();
    }
}
