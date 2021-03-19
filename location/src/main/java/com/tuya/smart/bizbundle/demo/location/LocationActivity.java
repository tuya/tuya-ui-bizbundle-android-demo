package com.tuya.smart.bizbundle.demo.location;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.api.service.MicroServiceManager;
import com.tuyasmart.stencil.bean.location.LocationBean;
import com.tuyasmart.stencil.location.LocationService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private final String TAG = "LocationActivity";
    public static final int MAP_RESULT_OK = 10001;
    public static final int GEOSELECT_REQUEST_CORE = 0x17;
    private static final int REQUEST_PERMISSION = 0x12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        findViewById(R.id.start_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //唤起定位
                LocationService locationService = MicroServiceManager.getInstance().findServiceByInterface(LocationService.class.getName());
                if (locationService!=null) {
                    locationService.updateLocation();
                }
            }
        });

        findViewById(R.id.get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取定位信息
                LocationService locationService = MicroServiceManager.getInstance().findServiceByInterface(LocationService.class.getName());
                if (locationService!=null) {
                    LocationBean location = locationService.getLocation();
                    if (location != null) {
                        Log.d(TAG, "lon:" + location.getLon() + " lat:" + location.getLat());
                    }
                }
            }
        });

        findViewById(R.id.map_location_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_family_location", true);
                UrlRouter.execute(UrlRouter.makeBuilder(LocationActivity.this, "map_location_setting", bundle, MAP_RESULT_OK));
            }
        });

        findViewById(R.id.map_geofence_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", 0.0);
                bundle.putDouble("lng", 0.0);
                bundle.putInt("radius", 100);
                UrlRouter.execute(UrlRouter.makeBuilder(LocationActivity.this, "map_geofence", bundle, GEOSELECT_REQUEST_CORE));
            }
        });

        findViewById(R.id.request_permission_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkGeoLocationPermission(LocationActivity.this)) {
                    UrlRouter.execute(UrlRouter.makeBuilder(LocationActivity.this, "request_permission_activity", null, REQUEST_PERMISSION));
                }
            }
        });
        Log.e("sha1", "sha1:" + sHA1(LocationActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case MAP_RESULT_OK: {
                if (null != data) {
                    double lat = data.getDoubleExtra("lat", 0);
                    double lng = data.getDoubleExtra("lng", 0);
                    String mCountry = data.getStringExtra("country");
                    String mProvince = data.getStringExtra("province");
                    String mCity = data.getStringExtra("city");
                    String mAddress = data.getStringExtra("address");
                    TextView location = findViewById(R.id.map_location);
                    location.setText("lat:" + lat +
                            " lng:" + lng +
                            " country:" + mCountry +
                            " province:" + mProvince +
                            " city:" + mCity +
                            " address:" + mAddress);
                }
            }
            break;

            case GEOSELECT_REQUEST_CORE: {
                double lat = data.getDoubleExtra("lat", 0);
                double lng = data.getDoubleExtra("lng", 0);
                int mRadius = data.getIntExtra("radius", 0);
                String mAddress = TextUtils.isEmpty(data.getStringExtra("address")) ? "" : data.getStringExtra("address");
                TextView geofence = findViewById(R.id.map_geofence);
                geofence.setText("lat:" + lat +
                        " lng:" + lng +
                        " radius:" + mRadius +
                        " address:" + mAddress);
            }
            break;
            case REQUEST_PERMISSION:
                boolean locationPermissionGranted = data.getBooleanExtra("location_permission_granted", false);
                TextView result = findViewById(R.id.request_permission_result);
                result.setText("request permission result:" + locationPermissionGranted);
                break;
            default:
                break;
        }
    }

    /**
     * 检查是否具有粗略、精准和后台定位，并且开启GPS
     * 用作地理围栏的权限判断
     *
     * @return
     */
    public static boolean checkGeoLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasLocation = gpsProvider &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return hasLocation;
        } else { // android Q 以上需要判断后台定位权限
            return hasLocation &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}