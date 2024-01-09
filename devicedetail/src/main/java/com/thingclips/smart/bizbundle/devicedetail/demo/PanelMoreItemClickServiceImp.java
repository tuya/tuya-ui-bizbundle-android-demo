package com.thingclips.smart.bizbundle.devicedetail.demo;

import android.content.Context;
import android.widget.Toast;

import com.thingclips.smart.bizubundle.devicedetail.demo.R;
import com.thingclips.smart.panel.usecase.panelmore.service.PanelMoreItemClickService;
import com.thingclips.smart.sdk.bean.DeviceBean;
import com.thingclips.smart.sdk.bean.GroupBean;
import com.thingclips.smart.uispec.list.plug.text.TextBean;


@Deprecated
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
