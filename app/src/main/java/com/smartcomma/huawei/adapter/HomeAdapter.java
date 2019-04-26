package com.smartcomma.huawei.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartcomma.huawei.Entity.HomeItem;
import com.smartcomma.huawei.R;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<HomeItem, BaseViewHolder> {


    public HomeAdapter(int layoutResId, @Nullable List<HomeItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeItem item) {
        helper.setText(R.id.item_home_name, item.getMenuName());
        helper.setImageResource(R.id.item_home_image, item.getmDrawable());
    }
}
