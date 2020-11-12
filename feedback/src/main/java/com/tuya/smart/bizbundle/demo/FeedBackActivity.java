package com.tuya.smart.bizbundle.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.android.user.api.IQurryDomainCallback;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.bizbundle.feedback.demo.R;
import com.tuya.smart.feedback.api.FeedbackService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        Button mFeedbackServiceBtn = findViewById(R.id.btn_feedback_service);
        Button mFeedbackRouteBtn = findViewById(R.id.btn_feedback_route);
        mFeedbackServiceBtn.setOnClickListener(this);
        mFeedbackRouteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_feedback_service) {
            TuyaHomeSdk.getUserInstance().queryDomainByBizCodeAndKey("help_center", "main_page", new IQurryDomainCallback() {
                @Override
                public void onSuccess(String domain) {
                    FeedbackService feedbackService = MicroContext.findServiceByInterface(FeedbackService.class.getName());
                    if (feedbackService != null) {
                        feedbackService.jumpToWebHelpPage(FeedBackActivity.this);
                    }
                }

                @Override
                public void onError(String code, String error) {
                    return;
                }
            });

        } else if (v.getId() == R.id.btn_feedback_route) {
            TuyaHomeSdk.getUserInstance().queryDomainByBizCodeAndKey("help_center", "main_page", new IQurryDomainCallback() {
                @Override
                public void onSuccess(String domain) {
                    UrlRouter.execute(UrlRouter.makeBuilder(FeedBackActivity.this, "helpCenter"));
                }

                @Override
                public void onError(String code, String error) {
                    return;
                }
            });
        }
    }
}
