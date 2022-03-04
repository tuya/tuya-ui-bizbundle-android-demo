package com.tuya.smart.bizubundle.demo;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.logger.LogUtil;
import com.tuya.smart.api.module.ModuleApp;
import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.api.service.SchemeService;
import com.tuya.smart.api.tab.Constants;
import com.tuya.smart.bizubundle.panel.demo.PanelActivity;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;
import com.tuya.smart.sdk.bean.DeviceBean;


import java.util.Set;

/**
 * @author huyang
 * @date 2019/3/4
 */

public class RouterPresenter {
    private static final String TAG = "HomeRoutePresenter";
    private static final String PINNED_SHORTCUT_ALIAS = "com.tuya.smart.hometab.activity.shortcut";

    private String url;

    private Bundle extras;

    private RouterPresenter(String url, Bundle extras) {
        this.url = url;
        this.extras = extras;
    }

    @Nullable
    public static RouterPresenter parser(Intent intent) {
        if (intent == null || intent.getComponent() == null) {
            return null;
        }

        // 桌面快捷方式
        if (PINNED_SHORTCUT_ALIAS.equals(intent.getComponent().getClassName())) {
            String url = null;
            try {
                url = intent.getStringExtra("url");
            } catch (Throwable t) {
                LogUtil.e(TAG, "get url error", t);
            }
            L.d(TAG, "schemeJump: " + url);
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            if (isInnerRouter(url)) {
                return new RouterPresenter(url, intent.getExtras());
            }
            return null;

        }
        return null;
    }

    public void route(Context context) {
        UrlRouter.execute(context, url, extras);
    }

    private static boolean isInnerRouter(String url) {
        boolean isInnerRouter = false;
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();

        if (UrlRouter.isSchemeSupport(scheme)) {
            SchemeService schemeService = MicroContext.findServiceByInterface(SchemeService.class.getName());
            if (schemeService != null) {
                String host = uri.getHost();
                String moduleName = schemeService.getModuleClassByTarget(host);
                try {
                    Class<?> clz = Class.forName(moduleName);
                    isInnerRouter = ModuleApp.class.isAssignableFrom(clz);
                    L.d(TAG, url + " is inner router: " + isInnerRouter);
                } catch (ClassNotFoundException e) {
                    L.e(TAG, e.getMessage(), e);
                }
            }
        }
        return isInnerRouter;
    }

}
