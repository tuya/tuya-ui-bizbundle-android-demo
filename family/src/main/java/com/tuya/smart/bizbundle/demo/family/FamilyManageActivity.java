package com.tuya.smart.bizbundle.demo.family;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.api.router.UrlRouter;
import com.thingclips.smart.family.listener.HomeInviteListener;
import com.thingclips.smart.home.sdk.ThingHomeSdk;


public class FamilyManageActivity extends AppCompatActivity {

    private HomeInviteListener homeInviteListener = new HomeInviteListener() {
        @Override
        public void onHomeInvite(long homeId, String homeName) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FamilyManageActivity.this);
            builder.setTitle(getString(R.string.demo_family_receive_invitation))
                    .setMessage(String.format(getString(R.string.demo_family_invitation_message), homeName))
                    .setPositiveButton(getString(R.string.demo_family_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // accept invitation
                        }
                    })
                    .setNegativeButton(getString(R.string.demo_family_reject), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // reject invitation
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_manage);
        initializeInvitationListener();
        findViewById(R.id.btn_family_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrlRouter.execute(UrlRouter.makeBuilder(FamilyManageActivity.this, "family_manage"));
            }
        });
    }

    private void initializeInvitationListener() {
        ThingHomeSdk.getHomeManagerInstance().registerThingHomeChangeListener(homeInviteListener);
    }

    @Override
    protected void onDestroy() {
        ThingHomeSdk.getHomeManagerInstance().unRegisterThingHomeChangeListener(homeInviteListener);
        super.onDestroy();
    }
}