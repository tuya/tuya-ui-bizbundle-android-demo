package com.tuya.smart.bizbundle.demo.speech;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.api.router.UrlRouter;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.speech.api.AbsThingAssisantGuideService;

/**
 * Dscription:
 *
 * @Autour: sunlulu
 * @Date 2022/1/6 2:15 下午
 */
public class SpeechDemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_demo);
        TextView tvIsSportSpeech = findViewById(R.id.tv_is_sport_speech);

        //是否支持语音
        AbsThingAssisantGuideService assistantGuideService = MicroServiceManager.getInstance().findServiceByInterface(AbsThingAssisantGuideService.class.getName());
        if (assistantGuideService != null) {
            boolean isSupportSpeech = assistantGuideService.isSupportAssisantSpeech();
            tvIsSportSpeech.setText("is sport speech: " + isSupportSpeech);
        }

        //跳转语音
        findViewById(R.id.bt_speech_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlRouter.execute(UrlRouter.makeBuilder(SpeechDemoActivity.this, "speech"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AbsThingAssisantGuideService assistantGuideService = MicroServiceManager.getInstance().findServiceByInterface(AbsThingAssisantGuideService.class.getName());
        if (assistantGuideService != null) {
            //弹出添加桌面小组件 弹窗
            assistantGuideService.checkAssisantGuideDialog(this);
        }
    }
}
