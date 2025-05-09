package com.tuya.smart.bizubundle.demo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.basic.split.LargeScreen;
import com.thingclips.smart.android.common.utils.L;
import com.thingclips.smart.android.user.api.ILogoutCallback;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.demo_login.base.utils.LoginHelper;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.api.IThingHomeChangeListener;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.sdk.bean.DeviceBean;
import com.thingclips.smart.sdk.bean.GroupBean;
import com.thingclips.smart.theme.ThingTheme;
import com.thingclips.smart.utils.ProgressUtil;
import com.thingclips.smart.utils.ToastUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mCurrentFamilyName;
    private RouterPresenter routePresenter;
    private IThingHomeChangeListener mHomeChangeListener = new IThingHomeChangeListener() {
        @Override
        public void onHomeAdded(long homeId) {
            requestHomeDetail(homeId);
        }

        @Override
        public void onHomeInvite(long homeId, String homeName) {

        }

        @Override
        public void onHomeRemoved(long l) {

        }

        @Override
        public void onHomeInfoChanged(long l) {

        }

        @Override
        public void onSharedDeviceList(List<DeviceBean> list) {

        }

        @Override
        public void onSharedGroupList(List<GroupBean> list) {

        }

        @Override
        public void onServerConnectSuccess() {
        }
    };

    private void requestHomeDetail(long id) {
        ThingHomeSdk.newHomeInstance(id).getHomeDetail(new IThingHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {

            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 此处只是演示代码，集成时请在登录成功后调用
        //This method must be called after successful login
        BizBundleInitializer.onLogin();

        Log.i("SceneMainActivity", "onCreate");
        LargeScreen.INSTANCE.modeChanged(this);
        // sample code
        mCurrentFamilyName = findViewById(R.id.current_family_name);
        mCurrentFamilyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FamilyDialogFragment dialogFragment = FamilyDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "FamilyDialogFragment");
            }
        });
        ProgressUtil.showLoading(this, "Loading...");
        getHomeList();
        ThingHomeSdk.getHomeManagerInstance().registerThingHomeChangeListener(mHomeChangeListener);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThingHomeSdk.getUserInstance().logout(new ILogoutCallback() {
                    @Override
                    public void onSuccess() {
                        //演示代码
                        //demo use only start
                        LoginHelper.reLogin(MainActivity.this, false);
                        //demo use only end

                        //退出成功后必须调用此方法
                        //This method must be called on exit.
                        BizBundleInitializer.onLogout(MainActivity.this);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        L.e("tuya logout", errorCode + " " + errorMsg);
                    }
                });

            }
        });
    }

    private void getHomeList() {
        ThingHomeSdk.getHomeManagerInstance().queryHomeList(new IThingGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> list) {
                if (!list.isEmpty()) {
                    setCurrentFamily(list.get(0));
                } else {
                    ToastUtil.showToast(MainActivity.this, "home list is null,plz create home");
                }
                ProgressUtil.hideLoading();
                openUIBizBundle();
                for (HomeBean homeBean : list) {
                    requestHomeDetail(homeBean.getHomeId());
                }
            }

            @Override
            public void onError(String s, String s1) {
                ProgressUtil.hideLoading();
                Toast.makeText(MainActivity.this, s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 业务包接入后必须实现家庭服务(商城业务包可以不接入)
     * you should implementation AbsBizBundleFamilyService(mall bizbundle can not implementation)
     */
    public void setCurrentFamily(HomeBean homeBean) {
        mCurrentFamilyName.setText(homeBean.getName());
        AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        familyService.shiftCurrentFamily(homeBean.getHomeId(), homeBean.getName());
    }

    private void setDarkMode() {
        ThingTheme.INSTANCE.enableDarkMode();
    }

    private void setLightMode() {
        ThingTheme.INSTANCE.enableNormalMode();
    }

    private void setSystemDefaultMode() {
        ThingTheme.INSTANCE.enableFollowSystem();
    }

    private void showThemeSelectionDialog() {
        // 选项内容
        final String[] options = {"深色模式", "浅色模式", "跟随系统"};
        // 当前选中的选项索引
        final int[] selectedOptionIndex = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择主题模式");
        builder.setSingleChoiceItems(options, selectedOptionIndex[0], (dialog, which) -> {
            // 记录用户选择的选项索引
            selectedOptionIndex[0] = which;
        });
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 根据用户选择的选项执行相应的操作
            switch (selectedOptionIndex[0]) {
                case 0:
                    setDarkMode();
                    break;
                case 1:
                    setLightMode();
                    break;
                case 2:
                    setSystemDefaultMode();
                    break;
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            // 用户点击取消按钮后的处理逻辑
            dialog.dismiss();
        });
        builder.show();
    }

    private void openUIBizBundle() {
        AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        if (familyService.getCurrentHomeId() == 0) {
            ToastUtil.showToast(this, "homeId is must not 0");
            return;
        }
        TextView tv = findViewById(R.id.theme_switch);
        String text = tv.getText().toString();
        tv.setText(text + "| appUiMode: " + ThingTheme.INSTANCE.getAppUiMode() + "| supportDarkMode: " + ThingTheme.INSTANCE.isSupportDarkMode());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示带有多个选项的弹窗
                showThemeSelectionDialog();
            }
        });
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 打开新界面
                Intent i = new Intent();
                i.setClassName(MainActivity.this, ThemeActivity.class.getName());
                startActivity(i);
                return true;
            }
        });
        findViewById(R.id.panel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizubundle.panel.demo.PanelActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.mall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizubundle.mall.demo.MallActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.scene).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.scene.demo.SceneActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.ipc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizubundle.ipc.demo.IPCPanelActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.cloud_storage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizubundle.cloudstorage.demo.CloudStorageActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.activator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.activator.demo.DeviceActivatorActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.message.demo.MessageActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.demo.FeedBackActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.ota).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.smart.bizbundle.ota.demo.OtaActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.family).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.demo.family.FamilyManageActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.device_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.smart.bizbundle.devicedetail.demo.DeviceDetailActivity");
                startActivity(i);
            }
        });

        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.demo.location.LocationActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.groupmanager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.groupmanager.GroupManagerActivity");
                startActivity(i);
            }
        });

        findViewById(R.id.alexa_google_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.demo.socialbind.AlexaGoogleBindActivity");
                startActivity(i);
            }
        });

        findViewById(R.id.light_scene).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.appsdk.sample.lightscene.LightSceneManagerActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.share.ShareActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizubundle.control.demo.ControlManagerActivity");
                startActivity(i);
            }
        });

        findViewById(R.id.speech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.tuya.smart.bizbundle.demo.speech.SpeechDemoActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.third_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.appsdk.sample.third.service.ThirdServiceManagerActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.miniapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.smart.bizbundle.miniapp.demo.ActMiniAppSample");
                startActivity(i);
            }
        });
        findViewById(R.id.market_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(MainActivity.this, "com.thingclips.smart.bizbundle.marketing.demo.MarketActivity");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThingHomeSdk.getHomeManagerInstance().unRegisterThingHomeChangeListener(mHomeChangeListener);
        ThingHomeSdk.getHomeManagerInstance().onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        schemeJump(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (routePresenter != null) {
            routePresenter.route(this);
            routePresenter = null;
        }
    }

    private boolean schemeJump(Intent intent) {
        routePresenter = RouterPresenter.parser(intent);
        return routePresenter != null;
    }
}