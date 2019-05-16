package com.smartcomma.huawei.ui.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.smartcomma.huawei.R;
import com.smartcomma.huawei.ui.base.BaseActivity;

import java.util.logging.Logger;

import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {


    @Override
    protected void BarcodeReceiver(String barcode) {
        Log.e("00000000", "BarcodeReceiver: " + barcode);
    }

    @Override
    protected void RFIDEpcReceiver(String barcode) {
        Log.e("11111111", "RFIDEpcReceiver: " + barcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initController() {

    }
}
