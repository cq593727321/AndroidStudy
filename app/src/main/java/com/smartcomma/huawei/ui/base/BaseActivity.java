package com.smartcomma.huawei.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseActivity extends CommonActivity {

    protected abstract void BarcedeReceiver(String barcode);

    protected abstract void RFIDEpcReceiver(String barcode);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
