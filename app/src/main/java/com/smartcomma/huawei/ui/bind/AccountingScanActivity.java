package com.smartcomma.huawei.ui.bind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.smartcomma.huawei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountingScanActivity extends AppCompatActivity {
    @OnClick(R.id.bind_rl)
    void getNext() {
        startActivity(new Intent(this, BindActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_scan);
        ButterKnife.bind(this);
    }
}
