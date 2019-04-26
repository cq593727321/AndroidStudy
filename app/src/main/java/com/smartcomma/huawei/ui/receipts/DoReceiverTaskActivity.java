package com.smartcomma.huawei.ui.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

//        mAdapter.resetStatus();
        List<ReceiverInfo> receiverInfos = new ArrayList<>();
        receiverInfos.add(new ReceiverInfo("0", "regdfsgsdfgrg", "A", "1", false));
        receiverInfos.add(new ReceiverInfo("1", "regdfsgsdfgrg", "A", "1", true));
        receiverInfos.add(new ReceiverInfo("2", "regdfsgsdfgrg", "A", "1", true));
        receiverInfos.add(new ReceiverInfo("3", "regdfsgsdfgrg", "A", "1", true));
        receiverInfos.add(new ReceiverInfo("4", "regdfsgsdfgrg", "A", "1", true));
        mAdapter.addData(receiverInfos);
        mAdapter.notifyDataSetChanged();
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
        // mAdapter.setOnItemChildClickListener(this);

    }

    private List<ReceiverInfo> getData() {
        List<ReceiverInfo> receiverInfos = new ArrayList<>();
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "A", "1", false));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "A", "1", true));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "A", "1", false));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "A", "1", true));
        receiverInfos.add(new ReceiverInfo("48946512", "regdfsgsdfgrg", "A", "1", false));
        return receiverInfos;
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<ReceiverInfo> datas = adapter.getData();
        datas.get(position).setSelect(true);
        mAdapter.setNewData(datas);
    }
}
