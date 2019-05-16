package com.smartcomma.huawei.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartcomma.huawei.R;
import com.smartcomma.huawei.utils.LocalUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class A8UHFActivity extends AppCompatActivity {

    @BindView(R.id.uhf_connect)
    Button a8UhfConnect;
    @BindView(R.id.uhf_exit)
    Button a8UhfExit;
    private UhfDev mDev = UhfDev.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a8_uhf);
        ButterKnife.bind(this);
        Log.e("TAG", "onCreate: " +  LocalUtils.getDeviceBrand());
        Log.e("TAG", "onCreate: " +  LocalUtils.getSystemModel());
    }

    @OnClick({R.id.uhf_connect, R.id.uhf_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uhf_connect:
                ConnectReader();
                break;
            case R.id.uhf_exit:
                break;
        }
    }

    // 连接读写器
    private void ConnectReader() {
        int iModType = 1;//设置型号
        if (!mDev.InitDevice(iModType)) {
            Toast.makeText(getApplicationContext(), "初始化失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!mDev.Open()) {
            Toast.makeText(getApplicationContext(), "连接失败",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("Inventory", "iModType=" + iModType);
        SaveConfig();
        Intent localIntent = new Intent(this, A8UHFReceiveActivity.class);
        localIntent.putExtra("ModType", iModType);
        startActivity(localIntent);
        finish();
    }

    //保存型号
    private void SaveConfig() {
        DemoConfig.getInstance().iUHFModType = 1;
        DemoConfig.getInstance().SaveConfig();
    }
}
