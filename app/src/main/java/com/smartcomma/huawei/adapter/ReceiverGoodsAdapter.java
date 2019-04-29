package com.smartcomma.huawei.adapter;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smartcomma.huawei.Entity.ReceiverInfo;
import com.smartcomma.huawei.R;

import java.util.List;

public class ReceiverGoodsAdapter extends BaseQuickAdapter<ReceiverInfo, BaseViewHolder> {


    public ReceiverGoodsAdapter(int layoutResId, @Nullable List<ReceiverInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ReceiverInfo item) {
        if (item.isReceiver()) {
            helper.setVisible(R.id.receiver_cb, false);
            helper.setBackgroundColor(R.id.item_receiver_view, mContext.getResources().getColor(R.color.background_color));
        } else {
            helper.setVisible(R.id.receiver_cb, true);
            helper.setBackgroundColor(R.id.item_receiver_view, mContext.getResources().getColor(R.color.color_white));
        }
        helper.setText(R.id.receiver_material_code, item.getMaterial())
                .setText(R.id.receiver_material_barcode, item.getCompanyBarcode())
                .setText(R.id.receiver_material_type, item.getType())
                .setText(R.id.receiver_material_number, "数量: " + item.getNum())
                .setText(R.id.receiver_material_location, item.getLocation())
                .addOnClickListener(R.id.receiver_material_location)
                .addOnClickListener(R.id.receiver_material_number);
        helper.setOnCheckedChangeListener(R.id.receiver_cb, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
            }
        });

    }

    public void resetStatus() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSelect(false);
        }
    }
}