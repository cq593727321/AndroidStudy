package com.smartcomma.huawei.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.smartcomma.huawei.Entity.HomeItem;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.adapter.HomeAdapter;
import com.smartcomma.huawei.ui.receipts.ReceiverScanActivity;
import com.smartcomma.huawei.view.ItemDecorationAlbumColumns;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunctionMenuActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;
    HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_menu);
        ButterKnife.bind(this);
        mAdapter = new HomeAdapter(R.layout.item_home, getData());
        mRecyclerView.setLayoutManager(new GridLayoutManager(FunctionMenuActivity.this, 3));
//        mRecyclerView.addItemDecoration(new ItemDecorationAlbumColumns(2, 3));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private List<HomeItem> getData() {
        List<HomeItem> homeItems = new ArrayList<>();
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "收货"));
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "建账"));
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "退库"));
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "周转收货"));
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "周转发货"));
        homeItems.add(new HomeItem(R.drawable.ic_module_receiver, "盘点"));
        return homeItems;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Log.e("TAG", "onItemClick: " + new Gson().toJson(adapter.getData()));
        switch (position) {
            case 0:
                startActivity(new Intent(FunctionMenuActivity.this, ReceiverScanActivity.class));
                break;
            case 1:
                Toast.makeText(this, position + "di00", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, position + "di00", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, position + "di00", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, position + "di00", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this, position + "di00", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
