package com.thingclips.appsdk.sample.lightscene.list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.appsdk.sample.lightscene.R;
import com.thingclips.appsdk.sample.lightscene.list.adapter.LightSceneListAdapter;
import com.thingclips.light.android.callback.IThingLightResultCallback;
import com.thingclips.light.android.scene.bean.ThingLightSceneBean;
import com.thingclips.sdk.scene.ThingLightSceneSdk;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;

import java.util.ArrayList;

/**
 * create by nielev on 4/28/21
 */
public class LightSceneListActivity extends AppCompatActivity {
    Toolbar mToolbar;
    LightSceneListAdapter adapter;
    RecyclerView rvList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightscene_activity_list);
        initView();
        initData();

    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar_main);
        rvList = findViewById(R.id.rvList);
    }

    private void initData() {
        mToolbar.setTitle(R.string.lightscene_list);

        mToolbar.setNavigationOnClickListener(v -> finish());

        // Set list
        adapter = new LightSceneListAdapter(LightSceneListActivity.this);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(adapter);

        getLightSceneList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "edit light scene success", Toast.LENGTH_SHORT).show();
                getLightSceneList();
            }
        }
    }

    private void getLightSceneList() {
        AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        long homeId = 0;
        if(null != familyService) {
            homeId = familyService.getCurrentHomeId();
        }
        if (homeId == 0) {
            return;
        }
        ThingLightSceneSdk.getThingLightSceneManagerInstance().getAllLightSceneList(homeId, new IThingLightResultCallback<ArrayList<ThingLightSceneBean>>() {
            @Override
            public void onSuccess(ArrayList<ThingLightSceneBean> tuyaLightSceneBeans) {
                adapter.data = (ArrayList<ThingLightSceneBean>) tuyaLightSceneBeans;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String code, String detail) {

            }
        });
    }


}
