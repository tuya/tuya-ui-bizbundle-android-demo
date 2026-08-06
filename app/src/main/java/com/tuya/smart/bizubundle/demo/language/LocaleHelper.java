package com.tuya.smart.bizubundle.demo.language;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.core.os.ConfigurationCompat;

import java.util.Locale;

/**
 * In-app language switching helper (Java port).
 *
 * Mirrors the Tuya SDK's internal
 * {@code com.thingclips.smart.base.utils.ThingLanguageUtils#switchLanguage(Locale)}:
 * it switches the language by updating the Application resources' Configuration
 * locale and then calling updateConfiguration to refresh the resources.
 *
 * On top of that, two additions make it work reliably with androidx.appcompat
 * 1.2.x and across app restarts:
 *  - Persistence (SharedPreferences): the choice survives app restarts.
 *  - {@link #wrap(Context)}: called in attachBaseContext so every Activity loads
 *    with the persisted language.
 */
public class LocaleHelper {

    private static final String SP_NAME = "locale_sp";
    private static final String KEY_LANGUAGE = "key_language";

    /** Empty string means "follow the system". */
    public static final String LANG_SYSTEM = "";
    public static final String LANG_ENGLISH = "en";
    public static final String LANG_CHINESE = "zh";

    private LocaleHelper() {
    }

    /**
     * Switch language: persists the choice and immediately updates app-level
     * resources (same as ThingLanguageUtils.switchLanguage).
     * The caller should then call {@link #restartApp(Context)} so the new locale
     * fully takes effect across the whole app.
     */
    @SuppressWarnings("DEPRECATION")
    public static void switchLanguage(Context context, String language) {
        // 1. Persist the user's choice
        saveLanguage(context, language);

        // 2. Update Application resources immediately, exactly like
        //    ThingLanguageUtils.switchLanguage:
        //      config.locale = locale
        //      resources.updateConfiguration(config, null)
        Resources res = context.getApplicationContext().getResources();
        Configuration config = new Configuration(res.getConfiguration());
        applyLocale(config, resolveLocale(language));
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * Relaunch the app so the switched language fully takes effect.
     *
     * Mirrors the Tuya SDK's {@code ApplicationUtil.relaunchApp(true)}: re-launch
     * the launcher activity with a clean task, then kill the current process so
     * the app restarts fresh and TuyaSmartApp re-applies the persisted locale in
     * attachBaseContext.
     */
    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        if (intent == null) {
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * Wrap a Context with the persisted language, for use in the attachBaseContext
     * of TuyaSmartApp / BaseActivity.
     */
    public static Context wrap(Context context) {
        String language = getLanguage(context);
        if (LANG_SYSTEM.equals(language)) {
            return context; // Follow system: do not override
        }
        Configuration config = new Configuration(context.getResources().getConfiguration());
        applyLocale(config, resolveLocale(language));
        return context.createConfigurationContext(config);
    }

    /** Currently persisted language ("" means follow the system). */
    public static String getLanguage(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANGUAGE, LANG_SYSTEM);
    }

    private static void saveLanguage(Context context, String language) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_LANGUAGE, language).apply();
    }

    private static Locale resolveLocale(String language) {
        if (LANG_ENGLISH.equals(language)) {
            return Locale.ENGLISH;
        }
        if (LANG_CHINESE.equals(language)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        return systemLocale();
    }

    /** Real system locale, unaffected by the app's own override. */
    private static Locale systemLocale() {
        LocaleList list = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
        Locale l = list.get(0);
        return l != null ? l : Locale.getDefault();
    }

    private static void applyLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(new LocaleList(locale));
        }
        config.setLayoutDirection(locale);
    }
}
