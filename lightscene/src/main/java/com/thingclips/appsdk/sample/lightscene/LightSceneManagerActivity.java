package com.thingclips.appsdk.sample.lightscene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.appsdk.sample.lightscene.list.LightSceneListActivity;
import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.light.scene.plug.api.AbsLightScenePlugService;

public class LightSceneManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_scene_manager);

        findViewById(R.id.btnCreateLightScene).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if (null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }
                if (homeId == 0) {
                    return;
                }
                ThingHomeSdk.newHomeInstance(homeId).getHomeDetail(new IThingHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        AbsLightScenePlugService sceneLightingService = MicroContext.findServiceByInterface(AbsLightScenePlugService.class.getName());
                        if (null != sceneLightingService) {
                            sceneLightingService.launchLightSceneCreate(LightSceneManagerActivity.this, 100);
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
            }
        });

        findViewById(R.id.btnLightSceneList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if (null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }
                if (homeId == 0) {
                    return;
                }
                ThingHomeSdk.newHomeInstance(homeId).getHomeDetail(new IThingHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        AbsLightScenePlugService sceneLightingService = MicroContext.findServiceByInterface(AbsLightScenePlugService.class.getName());
                        if (null != sceneLightingService) {
                            startActivity(new Intent(LightSceneManagerActivity.this, LightSceneListActivity.class));
                        }
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "create light scene success", Toast.LENGTH_SHORT).show();
            }
        }
    }
}