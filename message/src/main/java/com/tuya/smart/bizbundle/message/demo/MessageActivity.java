package com.tuya.smart.bizbundle.message.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tuya.smart.api.MicroContext;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.message.base.activity.message.MessageContainerActivity;
import com.tuyasmart.stencil.utils.ActivityUtils;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
    }
    private void initView() {
        Button mActivityMessageBtn = findViewById(R.id.btn_message_start);
        Button mRouteMessageBtn = findViewById(R.id.btn_message_route);
        mActivityMessageBtn.setOnClickListener(this);
        mRouteMessageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_message_start) {
            ActivityUtils.gotoActivity(MessageActivity.this,
                    MessageContainerActivity.class,
                    ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM,
                    false);
        } else if (v.getId() == R.id.btn_message_route) {
            UrlRouter.execute(new UrlBuilder(MessageActivity.this, "messageCenter"));
        }
    }
}
