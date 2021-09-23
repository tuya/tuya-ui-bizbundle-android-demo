package com.tuya.appsdk.sample.lightscene;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tuya.appsdk.sample.lightscene.list.LightSceneListActivity;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.scene.lighting.api.SceneLightingService;

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
                if(null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }
                if (homeId == 0) {
                    return;
                }
                SceneLightingService sceneLightingService = MicroContext.findServiceByInterface(SceneLightingService.class.getName());
                if(null != sceneLightingService) {
                    sceneLightingService.gotoSceneLightingActivity(LightSceneManagerActivity.this, 100);
                }
            }
        });

        findViewById(R.id.btnLightSceneList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if(null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }
                if (homeId == 0) {
                    return;
                }
                SceneLightingService sceneLightingService = MicroContext.findServiceByInterface(SceneLightingService.class.getName());
                if(null != sceneLightingService) {
                    startActivity(new Intent(LightSceneManagerActivity.this, LightSceneListActivity.class));
                }
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