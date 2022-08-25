package com.tuya.smart.bizubundle.control.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.control.PluginControlService;
import com.tuya.smart.control.plug.api.IPluginControlService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.wrapper.api.TuyaWrapper;

public class ControlDevListActivity extends AppCompatActivity {


    SimpleDevListAdapter adapter;
    IPluginControlService pluginControlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_dev_list);
        TuyaWrapper.registerService(IPluginControlService.class, new PluginControlService());
        pluginControlService = MicroServiceManager.getInstance().findServiceByInterface(IPluginControlService.class.getName());
        initView();
        loadData();
    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("设备列表（多控检查）");


        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleDevListAdapter(this);
        rvList.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleDevListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceBean item, int position) {
                String devId = item.getDevId();
                pluginControlService.gotoMultiControl(ControlDevListActivity.this, devId);
            }
        });

    }


    private void loadData() {
        TuyaHomeSdk.newHomeInstance(getService().getCurrentHomeId()).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(final HomeBean homeBean) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(homeBean.getDeviceList());
                    }
                });

            }

            @Override
            public void onError(String s, String s1) {
                Toast.makeText(ControlDevListActivity.this, s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }

    private AbsBizBundleFamilyService getService() {
        return MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
    }


    public static void goControl(Activity activity) {
        Intent intent = new Intent(activity, ControlDevListActivity.class);
        activity.startActivity(intent);
    }

}