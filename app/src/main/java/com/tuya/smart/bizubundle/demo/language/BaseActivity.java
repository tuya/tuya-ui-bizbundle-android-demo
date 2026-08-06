package com.tuya.smart.bizubundle.demo.language;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Base activity that injects the persisted language.
 *
 * Subclass it (instead of {@link AppCompatActivity}) to make the switched
 * language take effect on the screen. Because androidx.appcompat 1.2.x wraps an
 * activity's base context, only injecting in attachBaseContext reliably
 * overrides the language.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase));
    }
}
