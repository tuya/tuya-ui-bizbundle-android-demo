package com.tuya.smart.bizbundle.scene.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tuya.smart.api.MicroContext;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.scene.SceneBean;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.scene.business.api.ITuyaSceneBusinessService;
import com.tuya.smart.utils.ToastUtil;

import java.util.List;

public class SceneActivity extends AppCompatActivity implements View.OnClickListener {

    private View mAddScene;
    private View mEditScene;
    private View mSetLocation;
    private ITuyaSceneBusinessService iTuyaSceneBusinessService;
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
        iTuyaSceneBusinessService = MicroContext.findServiceByInterface(ITuyaSceneBusinessService.class.getName());
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

        if (mServiceByInterface.getCurrentHomeId() == 0) return;
        TuyaHomeSdk.getSceneManagerInstance().getSceneList(mServiceByInterface.getCurrentHomeId(), new ITuyaResultCallback<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> result) {
                if (!result.isEmpty()) {
                    SceneBean sceneBean = result.get(0);
                    if (null != iTuyaSceneBusinessService) {
                        iTuyaSceneBusinessService.editScene(SceneActivity.this, mServiceByInterface.getCurrentHomeId(), sceneBean, EDIT_SCENE_REQUEST_CODE);
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
        if (null != iTuyaSceneBusinessService && mServiceByInterface.getCurrentHomeId() != 0) {
            iTuyaSceneBusinessService.addScene(this, mServiceByInterface.getCurrentHomeId(), ADD_SCENE_REQUEST_CODE);
        }
    }


    /**
     * set lng and lat use your map sdk in app
     */
    private void setLocation() {
        double lng = 120.06420814321443;
        double lat = 30.302782241301667;
        if (null != iTuyaSceneBusinessService) {
            iTuyaSceneBusinessService.setAppLocation(lng, lat);
        }
    }


    /**
     * Scene condition's location page
     * Note: Chinese city list default. Use it when your account is not a Chinese account.
     */
    private void setMapClass() {
        if (null != iTuyaSceneBusinessService) {
            //TODO business map Activity
            iTuyaSceneBusinessService.setMapActivity(null);
        }
    }

    /**
     * You can use the method to set location information after use custom map class impl
     */
    private void saveMapData() {
        if (null != iTuyaSceneBusinessService) {
            //TODO save map data
            double lng = 120.06420814321443;
            double lat = 30.302782241301667;
            String city = "hangzhou";
            String address = "address";
            iTuyaSceneBusinessService.saveMapData(lng, lat, city, address);
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
        SceneBean sceneBean = (SceneBean) data.getSerializableExtra("sceneBean");
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
        SceneBean sceneBean = (SceneBean) data.getSerializableExtra("sceneBean");
        if (null != sceneBean) {
            ToastUtil.shortToast(this, "Scene：" + sceneBean.getName() + "create success!");
        }
    }
}
