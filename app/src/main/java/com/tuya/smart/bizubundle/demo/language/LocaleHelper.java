package com.tuya.smart.bizubundle.demo.language;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

/**
 * In-app language switching helper.
 *
 * Uses the official AndroidX per-app language API
 * {@link AppCompatDelegate#setApplicationLocales(LocaleListCompat)} (appcompat
 * 1.6+, resolved to 1.6.1 in this project). It applies the locale to ALL
 * activities — including SDK BizBundle pages in library modules — because
 * AppCompatDelegate intercepts attachBaseContext app-wide. On Android 13 (API
 * 33+) it goes through the system per-app language mechanism, so even plain
 * (non-AppCompat) activities are covered.
 *
 * This replaces the earlier updateConfiguration + wrap + restart approach,
 * which only updated the application resources and missed native BizBundle
 * activities whose base context kept the system locale.
 *
 * The choice is persisted automatically by appcompat / the system, so it
 * survives cold starts; read it back via {@link #getLanguage(Context)}.
 */
public class LocaleHelper {

    /** Empty string means "follow the system". */
    public static final String LANG_SYSTEM = "";
    public static final String LANG_ENGLISH = "en";
    public static final String LANG_CHINESE = "zh";

    private LocaleHelper() {
    }

    /**
     * Switch the app language app-wide. The current activity is recreated
     * automatically with the new locale; every other activity (including SDK
     * BizBundle pages) picks it up afterwards.
     *
     * @param language one of [LANG_ENGLISH], [LANG_CHINESE], [LANG_SYSTEM].
     */
    public static void switchLanguage(Context context, String language) {
        LocaleListCompat locales = LANG_SYSTEM.equals(language)
                ? LocaleListCompat.getEmptyLocaleList()           // follow system
                : LocaleListCompat.forLanguageTags(language);
        AppCompatDelegate.setApplicationLocales(locales);
    }

    /** Currently applied language ("" means follow the system). */
    public static String getLanguage(Context context) {
        LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
        if (locales.isEmpty()) {
            return LANG_SYSTEM;
        }
        Locale l = locales.get(0);
        if (l == null) {
            return LANG_SYSTEM;
        }
        String lang = l.getLanguage();
        if (LANG_CHINESE.equals(lang)) {
            return LANG_CHINESE;
        }
        if (LANG_ENGLISH.equals(lang)) {
            return LANG_ENGLISH;
        }
        return lang;
    }
}
