package com.tuya.groupmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.group_api.GroupState;
import com.tuya.group_api.TuyaGroupManager;
import com.tuya.groupmanager.adapter.SimpleDevListAdapter;
import com.tuya.groupmanager.bean.SimpleDeviceBean;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity {

    private int groupType = -1;
    SimpleDevListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initData();
        initView();
        loadData();
    }

    private void initData() {
        groupType = getIntent().getIntExtra(EXTRA_TYPE, -1);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (groupType == CREATE_GROUP) {
            toolbar.setTitle("设备列表");
        } else if (groupType == EDIT_GROUP) {
            toolbar.setTitle("群组列表");
        }

        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleDevListAdapter(this, groupType == CREATE_GROUP);
        rvList.setAdapter(adapter);
        adapter.setOnItemClickListener(new SimpleDevListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SimpleDeviceBean item, int position) {
                if (groupType == CREATE_GROUP) {
                    String devId = item.getId();
                    createGroup(devId);
                } else if (groupType == EDIT_GROUP) {
                    long groupId = Long.parseLong(item.getId());
                    editGroup(groupId);
                }
            }
        });

    }

    /**
     * 创建群组
     * @param devId
     */
    private void createGroup(String devId) {
        //  跳入创建群组
        GroupState groupState = TuyaGroupManager.getInstance().createGroup(GroupListActivity.this, devId);
        if (groupState == GroupState.SUPPORT) {
            // 设备支持创建群组
        } else if (groupState == GroupState.NOT_SUPPORT) {
            Toast.makeText(this, "该设备暂不支持群组创建", Toast.LENGTH_SHORT).show();
        } else if (groupState == GroupState.NONE) {
            Toast.makeText(this, "设备不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 编辑群组
     * @param groupId
     */
    private void editGroup(long groupId) {
        //  跳入编辑群组
        GroupState groupState = TuyaGroupManager.getInstance().editGroup(GroupListActivity.this, groupId);
        if (groupState == GroupState.SUPPORT) {
            // 群组支持编辑
        } else if (groupState == GroupState.NONE) {
            Toast.makeText(this, "群组不存在", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadData() {
        TuyaHomeSdk.newHomeInstance(getService().getCurrentHomeId()).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(final HomeBean homeBean) {
                final List<SimpleDeviceBean> beans = new ArrayList<>(8);
                transformData(homeBean, beans);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setData(beans);
                    }
                });

            }

            @Override
            public void onError(String s, String s1) {
                Toast.makeText(GroupListActivity.this, s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void transformData(HomeBean homeBean, List<SimpleDeviceBean> beans) {
        if (groupType == CREATE_GROUP) {
            for (DeviceBean deviceBean : homeBean.getDeviceList()) {
                SimpleDeviceBean simpleDeviceBean = new SimpleDeviceBean();
                simpleDeviceBean.setId(deviceBean.getDevId());
                simpleDeviceBean.setName(deviceBean.getName());
                simpleDeviceBean.setOnLine(deviceBean.getIsOnline());
                beans.add(simpleDeviceBean);
            }
        } else if (groupType == EDIT_GROUP) {
            for (GroupBean groupBean : homeBean.getGroupList()) {
                SimpleDeviceBean simpleDeviceBean = new SimpleDeviceBean();
                simpleDeviceBean.setId(String.valueOf(groupBean.getId()));
                simpleDeviceBean.setName(groupBean.getName());
                beans.add(simpleDeviceBean);
            }
        }
    }


    private AbsBizBundleFamilyService getService() {
        return MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
    }


    private static final String EXTRA_TYPE = "type";
    private static final int CREATE_GROUP = 1;
    private static final int EDIT_GROUP = 2;

    public static void startCreateGroup(Activity activity) {
        Intent intent = new Intent(activity, GroupListActivity.class);
        intent.putExtra(EXTRA_TYPE, CREATE_GROUP);
        activity.startActivity(intent);
    }

    public static void startEditGroup(Activity activity) {
        Intent intent = new Intent(activity, GroupListActivity.class);
        intent.putExtra(EXTRA_TYPE, EDIT_GROUP);
        activity.startActivity(intent);
    }
}