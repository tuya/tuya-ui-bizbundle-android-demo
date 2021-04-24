package com.tuya.groupmanager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GroupManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manager);

        findViewById(R.id.btnCreateGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupListActivity.startCreateGroup(GroupManagerActivity.this);
            }
        });

        findViewById(R.id.btnEditGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupListActivity.startEditGroup(GroupManagerActivity.this);
            }
        });
    }
}