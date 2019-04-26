package com.smartcomma.huawei.ui.receipts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.smartcomma.huawei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiverScanActivity extends AppCompatActivity {

    @BindView(R.id.receiver_rl)
    RelativeLayout receiverRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_scan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.receiver_rl)
    public void onViewClicked() {
        startActivity(new Intent(this, DoReceiverTaskActivity.class));
    }
}
