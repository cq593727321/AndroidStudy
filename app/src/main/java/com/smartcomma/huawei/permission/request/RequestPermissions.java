package com.smartcomma.huawei.permission.request;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.List;

public class RequestPermissions implements IRequestPermission {
    private static RequestPermissions requestPermissions;

    public static RequestPermissions getInstance() {
        if (requestPermissions == null) {
            requestPermissions = new RequestPermissions();
        }
        return requestPermissions;
    }


    @Override
    public boolean requestPermissions(Activity activity, String[] permissions, int resultCode) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return false;
    }

    private boolean requestAllPermission(Activity activity, String[] permissions, int resultCode) {
        //判断是否已赋予了全部权限
        boolean isAllGranted = CheckPermission.checkPermissionAllGranted(activity, permissions);
        if (isAllGranted) {
            return true;
        }
        ActivityCompat.requestPermissions(activity, permissions, resultCode);
        return false;
    }

    private boolean requestNeedPermission(Activity activity, String[] permissions, int resultCode) {
        List<String> list = CheckPermission.checkPermissionDenied(activity, permissions);
        if (list.size() == 0) {
            return true;
        }

        //请求权限
        String[] deniedPermissions = list.toArray(new String[list.size()]);
        ActivityCompat.requestPermissions(activity, deniedPermissions, resultCode);
        return false;
    }
}
