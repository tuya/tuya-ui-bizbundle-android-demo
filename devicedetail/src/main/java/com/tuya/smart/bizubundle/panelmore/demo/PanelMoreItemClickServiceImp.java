package com.tuya.smart.bizubundle.panelmore.demo;

import android.content.Context;
import android.widget.Toast;

import com.tuya.smart.panel.usecase.panelmore.service.PanelMoreItemClickService;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;
import com.tuya.smart.uispec.list.plug.text.TextBean;

public class PanelMoreItemClickServiceImp extends PanelMoreItemClickService {

    @Override
    public void devClickItem(Context context, int action, TextBean textBean, DeviceBean deviceBean, boolean b) {
        if (action == R.id.action_test_insert) {
            Toast.makeText(context, "action_test_insert", Toast.LENGTH_LONG).show();
        } else if (action == R.id.action_test_async_insert) {
            Toast.makeText(context, "action_test_async_insert", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void groupClickItem(Context context, int action, TextBean textBean, GroupBean groupBean, boolean b) {
        if (action == R.id.action_test_insert) {
            Toast.makeText(context, "action_test_insert", Toast.LENGTH_LONG).show();
        } else if (action == R.id.action_test_async_insert) {
            Toast.makeText(context, "action_test_async_insert", Toast.LENGTH_LONG).show();
        }
    }
}
