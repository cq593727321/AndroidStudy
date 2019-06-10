package com.smartcomma.huawei.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcomma.huawei.MyApplication;
import com.smartcomma.huawei.R;

public final class ToastUtil {
    private Toast mToast = null;
    private Context mCtx;
    private static ToastUtil mToastUtil = null;

    public ToastUtil(Context ctx) {
        mCtx = ctx;
    }

    public static ToastUtil getInstance(Context ctx) {
        if (mToastUtil == null)
            mToastUtil = new ToastUtil(ctx);
        return mToastUtil;
    }

    public void showToast(String text, int duration) {
        if (mToast == null) {
            mToast = makeText(mCtx, text, duration);
        } else {
            ((TextView) mToast.getView().findViewById(R.id.TextViewInfo)).setText(text);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public Toast makeText(Context context, String msg, int duration) {
        @SuppressLint("WrongConstant") View toastRoot = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.my_toast, null);
        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
        tv.getBackground().setAlpha(100);
        tv.setText(msg);
        toast.setDuration(duration);
        return toast;
    }
}
