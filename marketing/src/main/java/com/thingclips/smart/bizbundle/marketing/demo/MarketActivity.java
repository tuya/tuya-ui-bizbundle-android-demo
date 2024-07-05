package com.thingclips.smart.bizbundle.marketing.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.activitypush.MarketingPushManager;
import com.thingclips.smart.activitypush.api.PushTargetEnum;
import com.thingclips.smart.advertisement.api.AdvertisementManager;
import com.thingclips.smart.privacy.auth.api.IAuthorizationStatusCheckCallback;
import com.thingclips.smart.privacy.auth.api.IAuthorizationStatusUpdateListener;
import com.thingclips.smart.privacy.auth.api.IStatusChangeCallback;
import com.thingclips.smart.privacy.auth.api.PrivacyAuthorizationManager;
import com.thingclips.smart.sdktrackcontrol.ThingEventStatManager;
import com.thingclips.smart.uispecs.component.toast.ThingToast;
import com.thingclips.smart.uispecs.component.util.FamilyDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class MarketActivity extends AppCompatActivity implements IAuthorizationStatusUpdateListener {

    private final String[] items = new String[]{"Home Tab",  "User Tab"};
    private TextView tabTitleView;
    private TextView openAuthResultTv;
    private TextView closeAuthResultTv;
    private LinearLayout bannerContainerView;
    private boolean hasGrantedDataAuthorization;
    private boolean hasAdDataInit;
    private PushTargetEnum currentTab = PushTargetEnum.HOME;
    private final Map<PushTargetEnum, View> bannerViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        initView();
        initData();
    }

    private void initView() {
        tabTitleView = findViewById(R.id.tab_title);
        openAuthResultTv = findViewById(R.id.tv_open_auth_result);
        closeAuthResultTv = findViewById(R.id.tv_close_auth_result);
        findViewById(R.id.tv_open_auth).setOnClickListener(view -> {
            if (!hasGrantedDataAuthorization) {
                openAuth();
            } else {
                ThingToast.show(this, "has already opened authorization", Toast.LENGTH_SHORT);
            }
        });

        findViewById(R.id.tv_close_auth).setOnClickListener(view -> {
            if (hasGrantedDataAuthorization) {
                closeAuth();
            } else {
                ThingToast.show(this, "has already closed authorization", Toast.LENGTH_SHORT);
            }
        });

        findViewById(R.id.tv_tab_change).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MarketActivity.this);
            builder.setTitle("Please select an item");
            boolean[] checkedItems = new boolean[]{false, false};
            builder.setSingleChoiceItems(items, -1, (dialog, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = (i == which);
                }
            });

            builder.setPositiveButton("confirm", (dialog, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        ThingToast.show(MarketActivity.this, "You have selected: " + items[i], Toast.LENGTH_SHORT);
                        notifyTabChange(i);
                        break;
                    }
                }
            });

            builder.setNegativeButton("cancel", null);
            builder.create().show();
        });

        findViewById(R.id.tv_splash).setOnClickListener(view -> {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        });

        bannerContainerView  = findViewById(R.id.banner_container);
    }

    private void notifyTabChange(int position) {
        if (position >= items.length) {
            return;
        }
        tabTitleView.setText(items[position]);

        if (position == 0) {
            currentTab = PushTargetEnum.HOME;
        } else if (position == 1) {
            currentTab = PushTargetEnum.USER_CENTER;
        }

        if (!bannerViewMap.containsKey(currentTab)) {
            View bannerView = AdvertisementManager.getADBannerView(this, currentTab);
            if (bannerView != null) {
                bannerViewMap.put(currentTab, bannerView);
            }
        }
        bannerContainerView.removeAllViews();
        if (bannerViewMap.containsKey(currentTab)) {
            bannerContainerView.addView(bannerViewMap.get(currentTab));
        }
        MarketingPushManager.pushTargetActivity(currentTab, this);
    }

    private void initData() {
        //Synchronize user authorization status from the cloud.
        checkAuth();
        //Register to initialize marketing service.
        MarketingPushManager.init();
        MarketingPushManager.pushTargetActivity(currentTab, this);
        //Register for authorization status change.
        PrivacyAuthorizationManager.getInstance().registerAuthStatusChangeObserver(this);
        //Initialize data tracking library. It is recommended to execute this after the user agrees to the privacy policy
        ThingEventStatManager.init(getApplication());

        if (PrivacyAuthorizationManager.getInstance().currentAuthorizationStatus()) {
            if (!hasAdDataInit) {
                AdvertisementManager.initData();
                hasAdDataInit = true;
            }
        }

        if (hasAdDataInit) {
            View bannerView = AdvertisementManager.getADBannerView(this, currentTab);
            if (bannerView != null) {
                bannerViewMap.put(currentTab, bannerView);
            }
            if (bannerViewMap.containsKey(currentTab)) {
                bannerContainerView.addView(bannerViewMap.get(currentTab));
            }
        }
    }
    private void openAuth() {
        PrivacyAuthorizationManager.getInstance().openAuthorization(new IStatusChangeCallback() {
            @Override
            public void onSuccess() {
                hasGrantedDataAuthorization = true;
                openAuthResultTv.setVisibility(View.VISIBLE);
                closeAuthResultTv.setVisibility(View.GONE);
                if (!hasAdDataInit) {
                    AdvertisementManager.initData();
                    hasAdDataInit = true;
                }
            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }

    private void closeAuth() {
        PrivacyAuthorizationManager.getInstance().closeAuthorization(new IStatusChangeCallback() {
            @Override
            public void onSuccess() {
                hasGrantedDataAuthorization = false;
                closeAuthResultTv.setVisibility(View.VISIBLE);
                openAuthResultTv.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }
    
    private void checkAuth() {
        PrivacyAuthorizationManager.getInstance().checkAuthorization(new IAuthorizationStatusCheckCallback() {
            @Override
            public void onSuccess(boolean hasAlreadyOperation, boolean authorizationStatus) {
                if (hasAlreadyOperation) {
                    //has already operated data authorization
                    hasGrantedDataAuthorization = authorizationStatus;
                    if (authorizationStatus) {
                        if (!hasAdDataInit) {
                            AdvertisementManager.initData();
                            hasAdDataInit = true;
                        }
                    }

                } else {
                    //You have never authorized data analysis operations. Please proceed to authorize.
                    notifyUserAuthorization();
                }
            }

            @Override
            public void onError(String errorCode, String errorMessage) {

            }
        });
    }

    private void notifyUserAuthorization() {
        FamilyDialogUtils.showConfirmAndCancelDialog(this,
                getString(R.string.authorization_title),
                getString(R.string.authorization_tips),
                new FamilyDialogUtils.ConfirmAndCancelListener() {
                    @Override
                    public void onConfirmClick() {
                        openAuth();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PrivacyAuthorizationManager.getInstance().unRegisterAuthStatusChangeObserver(this);
    }

    @Override
    public void onAuthorizationStatusUpdate(boolean isAuth) {
        if (hasGrantedDataAuthorization == isAuth) {
            return;
        }
        hasGrantedDataAuthorization = isAuth;
        if (isAuth) {
            openAuthResultTv.setVisibility(View.VISIBLE);
            closeAuthResultTv.setVisibility(View.GONE);
        } else {
            openAuthResultTv.setVisibility(View.GONE);
            closeAuthResultTv.setVisibility(View.VISIBLE);
        }
    }
}