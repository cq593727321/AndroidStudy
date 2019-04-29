package com.smartcomma.huawei.ui.setting;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smartcomma.huawei.Entity.Location;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.adapter.LocationAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.location_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.location_recycler)
    RecyclerView mRecyclerView;

    LocationAdapter mAdapter;

    @OnClick(R.id.location_bt_add)
    void addLocation() {
        startActivity(new Intent(this, EditAndSaveLocationActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        ButterKnife.bind(this);
        mAdapter = new LocationAdapter(R.layout.item_location, getData());
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
    }

    private List<Location> getData() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("东莞研发基地12栋09楼12房001号", true));
        locations.add(new Location("东莞研发基地12栋09楼12房002号", false));
        locations.add(new Location("东莞研发基地12栋09楼12房003号", false));
        locations.add(new Location("东莞研发基地12栋09楼12房004号", false));
        locations.add(new Location("东莞研发基地12栋09楼12房005号", false));
        locations.add(new Location("东莞研发基地12栋09楼12房006号", false));
        return locations;
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<Location> a = mAdapter.getData();
        Toast.makeText(this, "第" + position, Toast.LENGTH_SHORT).show();
    }
}
