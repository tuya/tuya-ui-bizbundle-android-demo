package com.tuya.smart.bizbundle.demo.socialbind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuya.smart.bind.TuyaSocialLoginBindManager;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.callback.ITuyaResultCallback;
import com.tuya.smart.social.auth.manager.api.AuthorityBean;
import com.tuya.smart.social.auth.manager.api.ResultCallback;
import com.tuya.smart.social.auth.manager.api.SocialAuthManagerClient;

import java.util.ArrayList;

public class AlexaGoogleBindActivity extends AppCompatActivity {
    private String TAG = "AlexaGoogleBindActivity";
    private BindAdapter bindAdapter;

    private static final String KEY_ACTION = "action";
    private static final String AUTHORITY_BEAN = "authority_bean";
    private static final String GO_TO_DE_AUTHORIZA = "gotoDeAuthorize";
    private static final int REQUEST_REFRESH_MANAGER_AUTHORIZATION = 151;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexa_google_bind);
        initView();
        findViewById(R.id.bt_alexa_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if (null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }

                TuyaSocialLoginBindManager.Companion.getInstance().alexaBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new ITuyaResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        L.d(TAG, "alexa bind onSuccess : " + result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        L.d(TAG, "alexa bind onError : errorCode：" + errorCode + " errorMessage：" + errorMessage);
                    }
                });
            }
        });

        findViewById(R.id.bt_google_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if (null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }

                TuyaSocialLoginBindManager.Companion.getInstance().googleBind(AlexaGoogleBindActivity.this, String.valueOf(homeId), new ITuyaResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        L.d(TAG, "google bind onSuccess : " + result);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        L.d(TAG, "google bind onError : errorCode：" + errorCode + " errorMessage：" + errorMessage);
                    }
                });
            }
        });

        findViewById(R.id.bt_get_bind_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBindSkillList();
            }
        });
    }

    private void initView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_bind_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindAdapter = new BindAdapter();
        mRecyclerView.setAdapter(bindAdapter);
        bindAdapter.setOnItemClickListener(new BindAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AuthorityBean authorityBean, int var2) {
                try {
                    String route = "SocialAuthManagerAppAction";
                    UrlBuilder mBuilder = UrlRouter.makeBuilder(AlexaGoogleBindActivity.this, route);
                    Bundle newBundle = new Bundle();
                    newBundle.putString(KEY_ACTION, GO_TO_DE_AUTHORIZA);
                    newBundle.putParcelable(AUTHORITY_BEAN, authorityBean);
                    mBuilder.setRequestCode(REQUEST_REFRESH_MANAGER_AUTHORIZATION);
                    mBuilder.putExtras(newBundle);
                    UrlRouter.execute(mBuilder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onItemClick(Context context, AuthorityBean authorityBean) {
        UrlBuilder mBuilder = UrlRouter.makeBuilder(context, "SocialAuthManagerAppAction");
        Bundle newBundle = new Bundle();
        newBundle.putString("action", "gotoDeAuthorize");
        newBundle.putParcelable("authority_bean", authorityBean);
        mBuilder.setRequestCode(REQUEST_REFRESH_MANAGER_AUTHORIZATION);
        mBuilder.putExtras(newBundle);
        UrlRouter.execute(mBuilder);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REFRESH_MANAGER_AUTHORIZATION:
                if (RESULT_OK == resultCode) {
                    getBindSkillList();
                }
                break;
            default:
                break;
        }
    }

    /**
     * get bound list
     * 获取管理授权列表
     */
    public void getBindSkillList() {
        SocialAuthManagerClient.INSTANCE.getInstance(MicroContext.getApplication())
                .getAuthorityPlatforms(new ResultCallback<ArrayList<AuthorityBean>>() {
                    @Override
                    public void onSuccess(ArrayList<AuthorityBean> authorityBeans) {
                        //get bound list success
                        bindAdapter.setData(authorityBeans);
                    }

                    @Override
                    public void onFailure(@Nullable String s, @Nullable String s1) {
                        //get bound list onFailure
                        bindAdapter.setData(null);
                    }
                });
    }
}