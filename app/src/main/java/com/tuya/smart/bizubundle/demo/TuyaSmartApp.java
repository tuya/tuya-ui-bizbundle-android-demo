package com.tuya.smart.bizubundle.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.router.UrlBuilder;
import com.thingclips.smart.api.service.RedirectService;
import com.thingclips.smart.api.service.RouteEventListener;
import com.thingclips.smart.api.service.ServiceEventListener;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.thingpackconfig.PackConfig;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class TuyaSmartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        // Please don't change the order.
//        // 请不要修改初始化顺序
//        FrescoManager.initFresco(this);
//        ThingHomeSdk.init(this);
//        ThingWrapper.init(this, new RouteEventListener() {
//            @Override
//            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
//                // urlBuilder.target is a router address, urlBuilder.params is a router params
//                //点击无反应表示路由未现实，需要在此实现， urlBuilder.target 目标路由， urlBuilder.params 路由参数
//                Log.e("router not implement", urlBuilder.target + " : " + urlBuilder.params.toString());
//            }
//        }, new ServiceEventListener() {
//            @Override
//            public void onFaild(String serviceName) {
//                Log.e("service not implement", serviceName);
//            }
//        });
//        ThingThemeInitializer.INSTANCE.init(this);
//        ThingOptimusSdk.init(this);
        PackConfig.addValueDelegate(AppConfig.class);

        // todo replace the above code with the following code
        // todo 用下面的代码替换上面的初始化代码
        BizBundleInitializer.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                // urlBuilder.target is a router address, urlBuilder.params is a router params
                //点击无反应表示路由未现实，需要在此实现， urlBuilder.target 目标路由， urlBuilder.params 路由参数
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                Log.e("service not implement", serviceName);
            }
        });
        // 如果你的应用没有提供应用内切换主题模式的功能，那么就在启动时强制设置一个模式，就可以启用下面这段代码
        // NightModeUtil.INSTANCE.setAppNightMode(AppUiMode.MODE_FOLLOW_SYSTEM);

        // register family service，mall bizbundle don't have to implement it.
        // 注册家庭服务，商城业务包可以不注册此服务
        BizBundleInitializer.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());

        //Intercept existing routes and jump to custom implementation pages with parameters
        //拦截已存在的路由，通过参数跳转至自定义实现页面
        RedirectService service = MicroContext.getServiceManager().findServiceByInterface(RedirectService.class.getName());
        service.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
            @Override
            public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                //Such as:
                //Intercept the event of clicking the panel right menu and jump to the custom page with the parameters of urlBuilder
                //例如：拦截点击面板右上角按钮事件，通过 urlBuilder 的参数跳转至自定义页面
                // if (urlBuilder.target.equals("panelAction") && urlBuilder.params.getString("action").equals("gotoPanelMore")) {
                //     interceptorCallback.interceptor("interceptor");
                //     Log.e("interceptor", urlBuilder.params.toString());
                // } else {
                interceptorCallback.onContinue(urlBuilder);
                // }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
