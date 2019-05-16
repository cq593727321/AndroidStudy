package com.smartcomma.huawei.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by cq on 2018/5/8.
 */

public class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("loading...");
    }

    public void showLoadingDialog(Fragment fragment, String title) {
        if (fragment.isAdded() && fragment.isVisible()) {
            if (progressDialog != null) {
                progressDialog.setMessage(title);
                progressDialog.show();
            }
        }

    }

    public void cancelLoadingDialog(Fragment fragment) {
        if (fragment.isAdded() && fragment.isVisible()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
