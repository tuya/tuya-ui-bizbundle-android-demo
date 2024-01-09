package com.tuya.smart.bizubundle.control.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ControlManagerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_manager);
        findViewById(R.id.btnGoMultiControl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlDevListActivity.goControl(ControlManagerActivity.this);
            }
        });
    }


}