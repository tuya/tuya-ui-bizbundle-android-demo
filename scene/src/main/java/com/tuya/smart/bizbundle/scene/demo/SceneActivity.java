package com.tuya.smart.bizbundle.scene.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.map.generalmap.ui.GeneralMapActivity;
import com.thingclips.smart.scene.api.IResultCallback;
import com.thingclips.smart.scene.business.api.IThingSceneBusinessService;
import com.thingclips.smart.scene.model.NormalScene;
import com.thingclips.smart.utils.ToastUtil;

import java.util.List;

public class SceneActivity extends AppCompatActivity implements View.OnClickListener {

    private View mAddScene;
    private View mEditScene;
    private View mSetLocation;
    private IThingSceneBusinessService iThingSceneBusinessService;
    private static final int ADD_SCENE_REQUEST_CODE = 1001;
    private static final int EDIT_SCENE_REQUEST_CODE = 1002;
    private View mSetMap;
    private View mSaveMapData;
    private AbsBizBundleFamilyService mServiceByInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle(R.string.app_name);

        mAddScene = findViewById(R.id.add_scene);
        mSetLocation = findViewById(R.id.set_location);
        mEditScene = findViewById(R.id.edit_scene);
        mSetMap = findViewById(R.id.set_map);
        mSaveMapData = findViewById(R.id.save_map_data);

        mSetLocation.setOnClickListener(this);
        mAddScene.setOnClickListener(this);
        mEditScene.setOnClickListener(this);
        mSetMap.setOnClickListener(this);
        mSaveMapData.setOnClickListener(this);
        // Get scene business service
        iThingSceneBusinessService = MicroContext.findServiceByInterface(IThingSceneBusinessService.class.getName());
        mServiceByInterface = MicroContext.getServiceManager().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.set_location) {
            setLocation();
        } else if (id == R.id.add_scene) {
            addScene();
        } else if (id == R.id.edit_scene) {
            editScene();
        } else if (id == R.id.set_map) {
            setMapClass();
        } else if (id == R.id.save_map_data) {
            saveMapData();
        }
    }

    /**
     * 编辑场景，如果要创建天气相关条件自动化需要接入地图地位业务包
     * 国内包：
     * api 'com.tuya.smart:tuyasmart-bizbundle-map_amap:x.x.x-x'
     * api 'com.tuya.smart:tuyasmart-bizbundle-location_amap:x.x.x-x'
     * 国际包：
     * api 'com.tuya.smart:tuyasmart-bizbundle-map_google:x.x.x-x'
     * api 'com.tuya.smart:tuyasmart-bizbundle-location_google:x.x.x-x'
     */
    private void editScene() {

        if (mServiceByInterface.getCurrentHomeId() == 0) {
            return;
        }

        ThingHomeSdk.getSceneServiceInstance().baseService().getSimpleSceneAll(mServiceByInterface.getCurrentHomeId(),
                new IResultCallback<List<NormalScene>>() {

            @Override
            public void onSuccess(List<NormalScene> normalScenes) {
                if (!normalScenes.isEmpty()) {
                    NormalScene sceneBean = normalScenes.get(0);
                    if (null != iThingSceneBusinessService) {
                        iThingSceneBusinessService.editSceneBean(SceneActivity.this, mServiceByInterface.getCurrentHomeId(), sceneBean, EDIT_SCENE_REQUEST_CODE);
                    }
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 创建场景，如果要创建天气相关条件自动化需要接入地图地位业务包
     * 国内包：
     * api 'com.tuya.smart:tuyasmart-bizbundle-map_amap:x.x.x-x'
     * api 'com.tuya.smart:tuyasmart-bizbundle-location_amap:x.x.x-x'
     * 国际包：
     * api 'com.tuya.smart:tuyasmart-bizbundle-map_google:x.x.x-x'
     * api 'com.tuya.smart:tuyasmart-bizbundle-location_google:x.x.x-x'
     */
    private void addScene() {
        if (null != iThingSceneBusinessService && mServiceByInterface.getCurrentHomeId() != 0) {
            iThingSceneBusinessService.addSceneBean(this, mServiceByInterface.getCurrentHomeId(), ADD_SCENE_REQUEST_CODE);
        }
    }


    /**
     * set lng and lat use your map sdk in app
     */
    private void setLocation() {
        double lng = 120.06420814321443;
        double lat = 30.302782241301667;
        if (null != iThingSceneBusinessService) {
            iThingSceneBusinessService.setAppLocation(lng, lat);
        }
    }


    /**
     * Scene condition's location page
     * Note: Chinese city list default. Use it when your account is not a Chinese account.
     */
    private void setMapClass() {
        if (null != iThingSceneBusinessService) {
            //TODO business map Activity
            iThingSceneBusinessService.setMapActivity(GeneralMapActivity.class);
        }
    }

    /**
     * You can use the method to set location information after use custom map class impl
     */
    private void saveMapData() {
        if (null != iThingSceneBusinessService) {
            //TODO save map data
            double lng = 120.06420814321443;
            double lat = 30.302782241301667;
            String city = "hangzhou";
            String address = "address";
            iThingSceneBusinessService.saveMapData(lng, lat, city, address);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_SCENE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onAddSuc(data);
                }
                break;
            case EDIT_SCENE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onEditSuc(data);
                }
                break;
            default:
                break;
        }
    }

    /**
     * edit scene success
     *
     * @param data
     */
    private void onEditSuc(Intent data) {
        NormalScene sceneBean = (NormalScene) data.getSerializableExtra("NormalScene");
        if (null != sceneBean) {
            ToastUtil.shortToast(this, "Scene：" + sceneBean.getName() + "edit success!");
        }
    }


    /**
     * add scene success
     *
     * @param data
     */
    private void onAddSuc(Intent data) {
        NormalScene sceneBean = (NormalScene) data.getSerializableExtra("NormalScene");
        if (null != sceneBean) {
            ToastUtil.shortToast(this, "Scene：" + sceneBean.getName() + "create success!");
        }
    }
}
