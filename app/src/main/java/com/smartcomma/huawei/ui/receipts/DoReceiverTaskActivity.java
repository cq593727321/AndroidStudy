package com.smartcomma.huawei.ui.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.smartcomma.huawei.Entity.ReceiverInfo;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.adapter.ReceiverGoodsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoReceiverTaskActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.receiver_task_rv)
    RecyclerView mRecyclerView;
    ReceiverGoodsAdapter mAdapter;

    @OnClick(R.id.receiver_bt_receiver_goods)
    void test() {
        Log.e("TAG", "test: " + new Gson().toJson(mAdapter.getData()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_receiver_task);
        ButterKnife.bind(this);
        mAdapter = new ReceiverGoodsAdapter(R.layout.item_receiver, getData());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);

    }

    private List<ReceiverInfo> getData() {
        List<ReceiverInfo> receiverInfos = new ArrayList<>();
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "数量建账", "17", "东莞研发基地10栋1403室", false));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "条码建账", "1", "东莞研发基地10栋73室", true));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "条码建账", "1", "东莞研发基地10栋1412室", false));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "条码建账", "1", "东莞研发基地10栋156室", true));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "条码建账", "1", "东莞研发基地10栋148室", false));
        return receiverInfos;
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.receiver_material_location:
                Toast.makeText(this, "位置=" + position, Toast.LENGTH_SHORT).show();
                break;
            case R.id.receiver_material_number:
                Toast.makeText(this, "数量=" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
