package com.tuya.smart.bizbundle.activator.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.activator.plug.mesosphere.ThingDeviceActivatorManager;
import com.thingclips.smart.activator.plug.mesosphere.api.IThingDeviceActiveListener;
import com.thingclips.smart.activator.scan.qrcode.ScanManager;


public class DeviceActivatorActivity extends AppCompatActivity {
    private static final int INFO_MESSAGE = 1;
    private EditText infoEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_activator);
        infoEt = findViewById(R.id.et_info);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INFO_MESSAGE) {
                if (msg.obj != null)
                    infoEt.append(msg.obj + "\n");
                else
                    Toast.makeText(DeviceActivatorActivity.this, "msg null", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void actionConfig(View view) {
        ThingDeviceActivatorManager.INSTANCE.startDeviceActiveAction(this);

        ThingDeviceActivatorManager.INSTANCE.addListener(new IThingDeviceActiveListener() {
            @Override
            public void onDevicesAdd(List<String> list) {
                StringBuilder str = new StringBuilder();
                for (String id : list) {
                    str.append("add device success, id: " + id).append("\n");
                }
                Message msg = Message.obtain();
                msg.what = INFO_MESSAGE;
                msg.obj = str.toString();
                mHandler.sendMessage(msg);
            }

            @Override
            public void onRoomDataUpdate() {
                Toast.makeText(DeviceActivatorActivity.this, "please refresh room data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpenDevicePanel(String s) {
                Toast.makeText(DeviceActivatorActivity.this, "u can open the panel of the device: " + s, Toast.LENGTH_SHORT).show();
            }


        });
    }

    public void actionScan(View view){
        ScanManager.INSTANCE.openScan(this);
    }
}