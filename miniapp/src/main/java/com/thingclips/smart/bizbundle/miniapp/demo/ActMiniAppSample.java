package com.thingclips.smart.bizbundle.miniapp.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.soloader.SoLoader;
import com.thing.smart.miniappclient.ThingMiniAppClient;

import java.io.IOException;

/**
 * @author xiaotan.yu
 * @date 2023/3/6
 */
public class ActMiniAppSample extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miniapp_sample);
        try {
            if (!SoLoader.isInitialized()) {
                SoLoader.init(this, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        findViewById(R.id.btn_miniapp_init).setOnClickListener(this::init);
        findViewById(R.id.btn_open_miniapp).setOnClickListener(this::openMiniAPP);
        findViewById(R.id.btn_clea_miniapp_cache).setOnClickListener(this::clearCache);
    }

    public void init(View view){
    // 该初始化接口不要放在Application或者主Activity中初始化，否则会导致初始化失败
        ThingMiniAppClient
                .initialClient()
                .initialize();
    }

    public void openMiniAPP(View view){
        // 如遇到SoLoader.init初始化报错导致小程序无法正常运行，可以在Application中调用SoLoader.init()方法进行初始化。
        // 同时需要卸载旧App，重新打包安装。
        ThingMiniAppClient
                .coreClient()
                .openMiniAppByAppId(this, "tydxwunc8rjqvh4gaw", null, null);
    }

    public void openMiniAPPTabs(View view){
        startActivity(new Intent(this, MiniAppTabSampleActivity.class));
    }

    public void clearCache(View view){
        ThingMiniAppClient
                .coreClient()
                .clearCache();
    }
}
