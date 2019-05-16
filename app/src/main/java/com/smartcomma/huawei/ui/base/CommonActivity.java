package com.smartcomma.huawei.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class CommonActivity extends AppCompatActivity implements IBaseAction {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("loading...");
        initView();
        initController();
        initView();
        initController();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    public void showLoadingDialog(Activity activity, String title) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            if (progressDialog != null) {
                progressDialog.setMessage(title);
                progressDialog.show();
            }
        }
    }

    public void cancelLoadingDialog(Activity activity) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
        }
    }

}
