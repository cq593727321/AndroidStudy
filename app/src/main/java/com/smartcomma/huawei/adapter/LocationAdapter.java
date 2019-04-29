package com.smartcomma.huawei.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartcomma.huawei.Entity.Location;
import com.smartcomma.huawei.R;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationAdapter extends BaseQuickAdapter<Location, BaseViewHolder> {

    public LocationAdapter(int layoutResId, @Nullable List<Location> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Location item) {
        if (item.isDefault()) {
            helper.setGone(R.id.item_tv_default, true);
        } else {
            helper.setGone(R.id.item_tv_default, false);
        }
        helper.setText(R.id.item_tv_location, item.getLocation());
        helper.addOnClickListener(R.id.item_iv_edit);
    }
}
