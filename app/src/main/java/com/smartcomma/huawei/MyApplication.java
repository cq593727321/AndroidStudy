package com.smartcomma.huawei;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;

public class MyApplication extends Application {
    private static Context mContext;
    private static AsciiCommander commander = null;


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
        try {
            commander = new AsciiCommander(getApplicationContext());
        } catch (Exception e) {
            Toast.makeText(mContext, "Unable to create AsciiCommander!", Toast.LENGTH_SHORT).show();
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

            }
        });

    }
}
