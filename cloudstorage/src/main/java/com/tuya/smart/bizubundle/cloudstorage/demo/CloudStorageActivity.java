package com.tuya.smart.bizubundle.cloudstorage.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thingclips.smart.android.common.utils.L;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.camera.cloud.purchase.AbsCameraCloudPurchaseService;
import com.thingclips.smart.camera.cloud.purchase.AbsCloudCallback;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.sdk.bean.DeviceBean;

public class CloudStorageActivity extends AppCompatActivity {

    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_storage);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Panel");

        edt = findViewById(R.id.input_deviceid);
        findViewById(R.id.buy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String devId = edt.getText().toString().trim();
                DeviceBean deviceBean = ThingHomeSdk.getDataInstance().getDeviceBean(devId);
                if (null == deviceBean) {
                    L.e("CloudStorageActivity", "please make sure the deviceBean cache exists");
                    return;
                }

                long homeId = getService().getCurrentHomeId();
                if (homeId <= 0) {
                    return;
                }
                AbsCameraCloudPurchaseService cameraCloudService = MicroServiceManager.getInstance().findServiceByInterface(AbsCameraCloudPurchaseService.class.getName());
                if (cameraCloudService != null) {
                    cameraCloudService.buyCloudStorage(CloudStorageActivity.this,
                            deviceBean, String.valueOf(homeId), new AbsCloudCallback() {
                                @Override
                                public void onError(String errorCode, String errorMessage) {
                                    super.onError(errorCode, errorMessage);
                                }
                            });
                }
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
}
