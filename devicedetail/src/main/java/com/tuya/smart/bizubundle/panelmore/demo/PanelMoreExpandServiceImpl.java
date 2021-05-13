package com.tuya.smart.bizubundle.panelmore.demo;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.tuya.smart.panel.usecase.panelmore.bean.IMenuBean;
import com.tuya.smart.panel.usecase.panelmore.service.AbsPanelMoreExpandService;
import com.tuya.smart.panel.usecase.panelmore.service.IMenuItemCallback;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;
import com.tuya.smart.uispec.list.plug.text.TextBean;

public class PanelMoreExpandServiceImpl extends AbsPanelMoreExpandService {

    public PanelMoreExpandServiceImpl() {
        super();
    }

    @Override
    public void devClickItem(Context context, int action, DeviceBean deviceBean, boolean isAdmin) {
        if (action == R.id.action_test_insert) {
            Toast.makeText(context, "action_test_insert", Toast.LENGTH_LONG).show();
        } else if (action == R.id.action_test_async_insert) {
            Toast.makeText(context, "action_test_async_insert", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public IMenuBean insertDevMenuItem(Context context, String type, boolean isAdmin, DeviceBean deviceBean, float order) {
        if (isAdmin && TextUtils.equals(type, "c_test_insert")) {
            return new IMenuBean("title", "subtitle", "1",
                    java.lang.String.valueOf(R.id.action_test_insert),
                    order);
        }
        return null;
    }

    @Override
    public void insertDevMenuItemAsync(Context context, String type, boolean isAdmin, DeviceBean deviceBean, float order, IMenuItemCallback callback) {
        if (isAdmin && TextUtils.equals(type, "c_test_async_insert")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.setMenuItem(new IMenuBean("async_title", "async_subtitle", "1",
                            java.lang.String.valueOf(R.id.action_test_async_insert),
                            order));
                }
            }, 1000 * 5);
        }
    }

    @Override
    public void groupClickItem(Context context, int action, TextBean data, GroupBean groupBean, boolean isAdmin) {
        if (action == R.id.action_test_insert) {
            Toast.makeText(context, "action_test_insert", Toast.LENGTH_LONG).show();
        } else if (action == R.id.action_test_async_insert) {
            Toast.makeText(context, "action_test_async_insert", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public IMenuBean insertGroupMenuItem(Context context, String type, boolean isAdmin, GroupBean deviceBean, float order) {
        if (isAdmin && TextUtils.equals(type, "c_test_insert")) {
            return new IMenuBean("title", "subtitle", "1",
                    java.lang.String.valueOf(R.id.action_test_insert),
                    order);
        }
        return null;
    }

    @Override
    public void insertGroupMenuItemAsync(Context context, String type, boolean isAdmin, GroupBean deviceBean, float order, IMenuItemCallback callback) {
        if (isAdmin && TextUtils.equals(type, "c_test_async_insert")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.setMenuItem(new IMenuBean("async_title", "async_subtitle", "1",
                            java.lang.String.valueOf(R.id.action_test_async_insert),
                            order));
                }
            }, 1000 * 5);
        }
    }
}
