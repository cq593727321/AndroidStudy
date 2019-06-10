package com.smartcomma.huawei.ui.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.smartcomma.huawei.MyApplication;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.ui.base.BaseActivity;
import com.smartcomma.huawei.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ToastUtils;

public class TestActivity extends BaseActivity {


    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.bt3)
    Button bt3;
    @BindView(R.id.bt4)
    Button bt4;
    @BindView(R.id.bt5)
    Button bt5;

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

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                ToastUtil.getInstance(MyApplication.getContext()).showToast("1111111111",10);
                break;
            case R.id.bt2:
                ToastUtil.getInstance(MyApplication.getContext()).showToast("2222222222",5);
                break;
            case R.id.bt3:
                ToastUtil.getInstance(MyApplication.getContext()).showToast("33333333333",20);
                break;
            case R.id.bt4:
                ToastUtil.getInstance(MyApplication.getContext()).showToast("44444444444",40);
                break;
            case R.id.bt5:
                ToastUtil.getInstance(MyApplication.getContext()).showToast("555555555555",60);
                break;
        }
    }
}
