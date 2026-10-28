package com.tuya.smart.bizbundle.aiassistant.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.thingclips.smart.ai.assistant.api.AIAssistantEnableService;
import com.thingclips.smart.ai.assistant.data.AIBarShowStyle;
import com.thingclips.smart.ai.assistant.data.AIRecomConfig;
import com.thingclips.smart.ai.assistant.data.AIRecomAIBarConfig;
import com.thingclips.smart.api.service.MicroServiceManager;

import java.util.ArrayList;
import java.util.List;

public final class AiAssistantDemoConfig {
    private static final String PREFS_NAME = "ai_assistant_demo_config";

    private AiAssistantDemoConfig() {}

    public static AIRecomConfig load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        AIRecomConfig config = new AIRecomConfig();
        config.setLogo(prefs.getString("logo", context.getString(R.string.ai_assistant_demo_logo_name)));
        config.setChatTitleLogoLight(prefs.getString("chat_title_logo_light", context.getString(R.string.ai_assistant_demo_chat_title_logo_light_name)));
        config.setChatTitleLogoDark(prefs.getString("chat_title_logo_dark", context.getString(R.string.ai_assistant_demo_chat_title_logo_dark_name)));
        config.setPlusBadgeLight(prefs.getString("plus_badge_light", context.getString(R.string.ai_assistant_demo_plus_badge_light_name)));
        config.setPlusBadgeDark(prefs.getString("plus_badge_dark", context.getString(R.string.ai_assistant_demo_plus_badge_dark_name)));
        config.setPlusStarBadge(prefs.getString("plus_star_badge", context.getString(R.string.ai_assistant_demo_plus_star_badge_name)));
        config.setWelcomeContent(prefs.getString("welcome_content", context.getString(R.string.ai_assistant_demo_welcome_content_name)));
        config.setInputPlaceholder(prefs.getString("input_placeholder", ""));
        config.setPlusOpen(prefs.getBoolean("plus_open", true));
        AIRecomAIBarConfig aibar = new AIRecomAIBarConfig();
        aibar.setSupportAIBar(prefs.getBoolean("aibar_enabled", true));
        aibar.setType("FLOAT".equals(prefs.getString("aibar_style", "APPEND"))
                ? AIBarShowStyle.FLOAT : AIBarShowStyle.APPEND);
        List<String> blacklist = new ArrayList<>();
        for (String item : prefs.getString("aibar_pid_blacklist", "").split(",")) {
            String pid = item.trim();
            if (!pid.isEmpty()) blacklist.add(pid);
        }
        aibar.setPidBlacklist(blacklist);
        config.setAibar(aibar);
        return config;
    }

    public static void save(Context context, AIRecomConfig config) {
        AIRecomAIBarConfig aibar = config.getAibar() != null ? config.getAibar() : new AIRecomAIBarConfig();
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putString("logo", config.getLogo())
                .putString("chat_title_logo_light", config.getChatTitleLogoLight())
                .putString("chat_title_logo_dark", config.getChatTitleLogoDark())
                .putString("plus_badge_light", config.getPlusBadgeLight())
                .putString("plus_badge_dark", config.getPlusBadgeDark())
                .putString("plus_star_badge", config.getPlusStarBadge())
                .putString("welcome_content", config.getWelcomeContent())
                .putString("input_placeholder", config.getInputPlaceholder())
                .putBoolean("plus_open", !Boolean.FALSE.equals(config.isPlusOpen()))
                .putBoolean("aibar_enabled", aibar.isSupportAIBar())
                .putString("aibar_style", aibar.getType() == AIBarShowStyle.FLOAT ? "FLOAT" : "APPEND")
                .putString("aibar_pid_blacklist", TextUtils.join(",", aibar.getPidBlacklist()))
                .commit();
    }

    public static void applyOnStartup(Context context) {
        AIAssistantEnableService service = MicroServiceManager.getInstance()
                .findServiceByInterface(AIAssistantEnableService.class.getName());
        if (service != null) service.setAIRecomConfig(load(context));
    }
}
