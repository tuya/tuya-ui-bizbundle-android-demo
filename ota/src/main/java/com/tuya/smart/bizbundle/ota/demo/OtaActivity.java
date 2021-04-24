package com.tuya.smart.bizbundle.ota.demo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.panel.ota.api.IOtaBehaviorListener;
import com.tuya.smart.panel.ota.service.AbsOtaCallerService;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;
import com.tuya.smart.utils.ProgressUtil;
import com.tuya.smart.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OtaActivity extends AppCompatActivity {

    private SimpleDevListAdapter mAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private TextView familyNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        initOtaBehavior();
        initView();
        initData();
    }

    private void initOtaBehavior() {
        AbsOtaCallerService absOtaCallerService = MicroContext.getServiceManager().findServiceByInterface(AbsOtaCallerService.class.getName());
        if (absOtaCallerService != null) {
            absOtaCallerService.setOtaBehaviorListener(new IOtaBehaviorListener() {
                @Override
                public void onBackPressedWhenForceUpgrading(Activity activity) {
                    if (activity != null) {
                        activity.finish();
                    }
                    ToastUtil.shortToast(OtaActivity.this, getString(R.string.demo_ota_back_to_homepage));
                    finish();
                }
            });
        }
    }


    private void initData() {
        getCurrentHomeDetail();
    }

    private void initView() {
        mAdapter = new SimpleDevListAdapter();
        findViewById(R.id.btn_ota_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDevsInHomeByBottomSheetDialog();
            }
        });
        familyNameTv = findViewById(R.id.title_family_name);
    }

    private void showDevsInHomeByBottomSheetDialog() {
        View view = View.inflate(this, R.layout.demo_dialog_dev_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.demo_bottom_dialog_rv_dev);
        TextView textView = view.findViewById(R.id.demo_bottom_dialog_title);
        textView.setText(getString(R.string.demo_ota_devices_in_family));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SimpleDevListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SimpleDeviceBean bean, int position) {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                navigateToOtaUpgrade(bean.getDevId());
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        Objects.requireNonNull(bottomSheetDialog.getWindow()).findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final BottomSheetBehavior<View> dialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        dialogBehavior.setPeekHeight(600);
        dialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                    dialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheetDialog.show();
    }

    private void navigateToOtaUpgrade(String devId) {
        AbsOtaCallerService absOtaCallerService = MicroContext.getServiceManager().findServiceByInterface(AbsOtaCallerService.class.getName());
        if (absOtaCallerService != null) {
            if (absOtaCallerService.isSupportUpgrade(devId)) {
                absOtaCallerService.goFirmwareUpgrade(this, devId);
            } else {
                ToastUtil.shortToast(this, getString(R.string.demo_ota_not_support_tip));
            }
        }
    }

    private void getCurrentHomeDetail() {
        ProgressUtil.showLoading(this, "Loading...");
        TuyaHomeSdk.newHomeInstance(getService().getCurrentHomeId()).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(final HomeBean homeBean) {
                final List<SimpleDeviceBean> beans = new ArrayList<>(8);
//                for (GroupBean groupBean : homeBean.getGroupList()) {
//                    beans.add(getItemBeanFromGroup(groupBean));
//                }
                for (DeviceBean deviceBean : homeBean.getDeviceList()) {
                    beans.add(getItemBeanFromDevice(deviceBean));
                }
                mAdapter.setData(beans);
                ProgressUtil.hideLoading();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        familyNameTv.setText(getString(R.string.demo_ota_current_family_is) + homeBean.getName());
                        if (beans.size() == 0) {
                            ToastUtil.shortToast(OtaActivity.this, getString(R.string.demo_ota_current_family_no_device));
                        }
                    }
                });
            }

            @Override
            public void onError(String s, String s1) {
                ProgressUtil.hideLoading();
                Toast.makeText(OtaActivity.this, s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * you must implementation AbsBizBundleFamilyService
     *
     * @return AbsBizBundleFamilyService
     */
    private AbsBizBundleFamilyService getService() {
        return MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
    }

    private SimpleDeviceBean getItemBeanFromGroup(GroupBean groupBean) {
        SimpleDeviceBean itemBean = new SimpleDeviceBean();
        itemBean.setGroupId(groupBean.getId());
        itemBean.setTitle(groupBean.getName());
        itemBean.setIconUrl(groupBean.getIconUrl());
        itemBean.setType(groupBean.getType());

        List<DeviceBean> deviceBeans = groupBean.getDeviceBeans();
        if (deviceBeans == null || deviceBeans.isEmpty()) {
            return null;
        } else {
            DeviceBean onlineDev = null;
            for (DeviceBean dev : deviceBeans) {
                if (dev != null) {
                    if (dev.getIsOnline()) {
                        onlineDev = dev;
                        break;
                    } else {
                        onlineDev = dev;
                    }
                }
            }
            itemBean.setDevId(onlineDev.getDevId());
            return itemBean;
        }
    }

    private SimpleDeviceBean getItemBeanFromDevice(DeviceBean deviceBean) {
        SimpleDeviceBean itemBean = new SimpleDeviceBean();
        itemBean.setDevId(deviceBean.getDevId());
        itemBean.setIconUrl(deviceBean.getIconUrl());
        itemBean.setTitle(deviceBean.getName());
        return itemBean;
    }
}