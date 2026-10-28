package com.tuya.smart.bizbundle.aiassistant.demo;

import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.ai.assistant.api.AIAssistantEnableService;
import com.thingclips.smart.ai.assistant.data.AIBarShowStyle;
import com.thingclips.smart.ai.assistant.data.AIRecomConfig;
import com.thingclips.smart.ai.assistant.data.AIRecomAIBarConfig;
import com.thingclips.smart.ai.assistant.data.AIRecomPageOptions;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.panelcaller.api.AbsPanelCallerService;
import com.thingclips.smart.sdk.bean.DeviceBean;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.tuya.smart.bizbundle.aiassistant.demo.R;

import java.util.ArrayList;
import java.util.List;

public class AiAssistantDemoActivity extends AppCompatActivity {
    private EditText pidInput;
    private EditText questionInput;
    private CheckBox autoSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_assistant_demo);
        pidInput = findViewById(R.id.ai_assistant_pid);
        questionInput = findViewById(R.id.ai_assistant_question);
        autoSend = findViewById(R.id.ai_assistant_send);
        restoreConfig();
        findViewById(R.id.apply_ai_assistant_config).setOnClickListener(view -> applyConfig());
        findViewById(R.id.show_ai_assistant_devices).setOnClickListener(view -> chooseDevice(null, "", false));
        findViewById(R.id.open_ai_assistant).setOnClickListener(view -> openAiAssistant());
    }

    private AIAssistantEnableService getService() {
        AIAssistantEnableService service = MicroServiceManager.getInstance()
                .findServiceByInterface(AIAssistantEnableService.class.getName());
        if (service == null) {
            Toast.makeText(this, R.string.ai_assistant_service_unavailable, Toast.LENGTH_SHORT).show();
        }
        return service;
    }

    private String value(int id) {
        return ((EditText) findViewById(id)).getText().toString().trim();
    }

    private void restoreConfig() {
        AIRecomConfig config = AiAssistantDemoConfig.load(this);
        ((EditText) findViewById(R.id.ai_assistant_logo)).setText(config.getLogo());
        ((EditText) findViewById(R.id.ai_assistant_chat_title_logo_light)).setText(config.getChatTitleLogoLight());
        ((EditText) findViewById(R.id.ai_assistant_chat_title_logo_dark)).setText(config.getChatTitleLogoDark());
        ((EditText) findViewById(R.id.ai_assistant_plus_badge_light)).setText(config.getPlusBadgeLight());
        ((EditText) findViewById(R.id.ai_assistant_plus_badge_dark)).setText(config.getPlusBadgeDark());
        ((EditText) findViewById(R.id.ai_assistant_plus_star_badge)).setText(config.getPlusStarBadge());
        ((EditText) findViewById(R.id.ai_assistant_welcome_content)).setText(config.getWelcomeContent());
        ((EditText) findViewById(R.id.ai_assistant_input_placeholder)).setText(config.getInputPlaceholder());
        ((CheckBox) findViewById(R.id.ai_assistant_plus_open)).setChecked(!Boolean.FALSE.equals(config.isPlusOpen()));
        ((CheckBox) findViewById(R.id.ai_assistant_aibar_enabled)).setChecked(config.getAibar().isSupportAIBar());
        ((RadioButton) findViewById(config.getAibar().getType() == AIBarShowStyle.FLOAT
                ? R.id.ai_assistant_aibar_float : R.id.ai_assistant_aibar_append)).setChecked(true);
        ((EditText) findViewById(R.id.ai_assistant_aibar_pid_blacklist))
                .setText(android.text.TextUtils.join(",", config.getAibar().getPidBlacklist()));
    }

    private void applyConfig() {
        AIBarShowStyle style = ((RadioButton) findViewById(R.id.ai_assistant_aibar_float)).isChecked()
                ? AIBarShowStyle.FLOAT : AIBarShowStyle.APPEND;
        AIAssistantEnableService service = getService();
        if (service == null) return;

        AIRecomConfig config = new AIRecomConfig();
        config.setLogo(value(R.id.ai_assistant_logo));
        config.setChatTitleLogoLight(value(R.id.ai_assistant_chat_title_logo_light));
        config.setChatTitleLogoDark(value(R.id.ai_assistant_chat_title_logo_dark));
        config.setPlusBadgeLight(value(R.id.ai_assistant_plus_badge_light));
        config.setPlusBadgeDark(value(R.id.ai_assistant_plus_badge_dark));
        config.setPlusStarBadge(value(R.id.ai_assistant_plus_star_badge));
        config.setWelcomeContent(value(R.id.ai_assistant_welcome_content));
        config.setInputPlaceholder(value(R.id.ai_assistant_input_placeholder));
        config.setPlusOpen(((CheckBox) findViewById(R.id.ai_assistant_plus_open)).isChecked());
        AIRecomAIBarConfig aibar = new AIRecomAIBarConfig();
        aibar.setSupportAIBar(((CheckBox) findViewById(R.id.ai_assistant_aibar_enabled)).isChecked());
        aibar.setType(style);

        List<String> blacklist = new ArrayList<>();
        for (String item : value(R.id.ai_assistant_aibar_pid_blacklist).split(",")) {
            String pid = item.trim();
            if (!pid.isEmpty()) blacklist.add(pid);
        }
        aibar.setPidBlacklist(blacklist);
        config.setAibar(aibar);
        service.setAIRecomConfig(config);
        AiAssistantDemoConfig.save(this, config);
        Toast.makeText(this, R.string.ai_assistant_config_applied, Toast.LENGTH_SHORT).show();
    }

    private void openAiAssistant() {
        String content = questionInput.getText().toString().trim();
        if (autoSend.isChecked() && content.isEmpty()) {
            questionInput.setError(getString(R.string.ai_assistant_question_required));
            questionInput.requestFocus();
            return;
        }
        String pid = pidInput.getText().toString().trim();
        if (!pid.isEmpty()) {
            chooseDevice(pid, content, autoSend.isChecked());
            return;
        }
        AIAssistantEnableService service = getService();
        if (service == null) return;

        AIRecomPageOptions options = new AIRecomPageOptions();
        if (!content.isEmpty()) options.setContent(content);
        options.setSend(autoSend.isChecked());
        service.goToAIRecomPage(options);
    }

    private void chooseDevice(String pid, String content, boolean send) {
        AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance()
                .findServiceByInterface(AbsBizBundleFamilyService.class.getName());
        if (familyService == null) {
            Toast.makeText(this, R.string.ai_assistant_home_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }
        ThingHomeSdk.newHomeInstance(familyService.getCurrentHomeId())
                .getHomeDetail(new IThingHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean home) {
                        if (isFinishing() || isDestroyed()) return;
                        List<DeviceBean> devices = new ArrayList<>();
                        if (home != null && home.getDeviceList() != null) {
                            for (DeviceBean device : home.getDeviceList()) {
                                if (pid == null || pid.equals(device.getProductId())) devices.add(device);
                            }
                        }
                        if (devices.isEmpty()) {
                            Toast.makeText(AiAssistantDemoActivity.this,
                                    pid == null ? R.string.ai_assistant_no_devices
                                            : R.string.ai_assistant_no_matching_device,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] labels = new String[devices.size()];
                        for (int i = 0; i < devices.size(); i++) {
                            DeviceBean device = devices.get(i);
                            labels[i] = device.getName() + " (PID: " + device.getProductId() + ")";
                        }
                        new AlertDialog.Builder(AiAssistantDemoActivity.this)
                                .setTitle(R.string.ai_assistant_choose_device)
                                .setItems(labels, (dialog, index) -> {
                                    DeviceBean device = devices.get(index);
                                    if (pid == null) {
                                        pidInput.setText(device.getProductId());
                                    } else {
                                        openDevicePanel(device, pid, content, send);
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onError(String code, String message) {
                        if (!isFinishing() && !isDestroyed()) {
                            Toast.makeText(AiAssistantDemoActivity.this,
                                    R.string.ai_assistant_device_load_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openDevicePanel(DeviceBean device, String pid, String content, boolean send) {
        AbsPanelCallerService service = MicroServiceManager.getInstance()
                .findServiceByInterface(AbsPanelCallerService.class.getName());
        if (service == null) {
            Toast.makeText(this, R.string.ai_assistant_panel_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle params = new Bundle();
        params.putString("pid", pid);
        if (!content.isEmpty()) params.putString("content", content);
        params.putString("send", send ? "1" : "0");
        service.goPanelWithCheckAndTip(this, device.getDevId(), params);
    }
}
