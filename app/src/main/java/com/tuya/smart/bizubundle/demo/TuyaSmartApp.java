package com.tuya.smart.bizubundle.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.service.RedirectService;
import com.tuya.smart.api.service.RouteEventListener;
import com.tuya.smart.api.service.ServiceEventListener;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.wrapper.api.TuyaWrapper;

public class TuyaSmartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Please don't change the order.
        // 请不要修改初始化顺序
        Fresco.initialize(this);

        TuyaHomeSdk.init(this);

        TuyaWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                // urlBuilder.target is a router address, urlBuilder.params is a router params
                //点击无反应表示路由未现实，需要在此实现， urlBuilder.target 目标路由， urlBuilder.params 路由参数
                Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                Log.e("service not implement", serviceName);
            }
        });
        TuyaOptimusSdk.init(this);

        // register family service，mall bizbundle don't have to implement it.
        // 注册家庭服务，商城业务包可以不注册此服务
        TuyaWrapper.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());

        //Intercept existing routes and jump to custom implementation pages with parameters
        //拦截已存在的路由，通过参数跳转至自定义实现页面
        RedirectService service = MicroContext.getServiceManager().findServiceByInterface(RedirectService.class.getName());
        service.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
            @Override
            public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                //Such as:
                //Intercept the event of clicking the panel right menu and jump to the custom page with the parameters of urlBuilder
                //例如：拦截点击面板右上角按钮事件，通过 urlBuilder 的参数跳转至自定义页面
                if (urlBuilder.target.equals("panelAction") && urlBuilder.params.getString("action").equals("gotoPanelMore")) {
                    interceptorCallback.interceptor("interceptor");
                    Log.e("interceptor", urlBuilder.params.toString());
                } else {
                    interceptorCallback.onContinue(urlBuilder);
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
