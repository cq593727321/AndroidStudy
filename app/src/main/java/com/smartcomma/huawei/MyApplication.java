package com.smartcomma.huawei;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.device.ScanDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.smartcomma.huawei.utils.LocalUtils;
import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;

public class MyApplication extends Application {
    private static Context mContext;
    private static AsciiCommander commander = null;
    private static ScanDevice mScanDevice;

    public static ScanDevice getmScanDevice() {
        return mScanDevice;
    }


    public static Context getContext() {
        return mContext;
    }

    public static AsciiCommander getCommander() {
        return commander;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //TSSL
        try {
            commander = new AsciiCommander(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to create AsciiCommander!", Toast.LENGTH_SHORT).show();
        }
        Log.e("TAG", "000000: " + LocalUtils.getDeviceBrand());
        Log.e("TAG", "111111: " + LocalUtils.getSystemModel());


        //先施条码扫描
        try {
            if ("A8".equals(LocalUtils.getSystemModel())) {
                mScanDevice = new ScanDevice();
                mScanDevice.setOutScanMode(0);
                mScanDevice.setScanLaserMode(8);//开始连续扫描
                if (mScanDevice.openScan()) {
                    Log.e("TAG", "onCreate: " + "00000000");
                } else {
                    Log.e("TAG", "onCreate: " + "1111111111");
                }
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "先施PDA扫码不可用", Toast.LENGTH_SHORT).show();
        }
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mScanDevice != null) {
                    mScanDevice.setScanLaserMode(8);//停止连续扫描
                    mScanDevice.stopScan();
                }
            }
        });

    }
}
