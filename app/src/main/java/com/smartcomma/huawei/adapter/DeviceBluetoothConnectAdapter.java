package com.smartcomma.huawei.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartcomma.huawei.R;

public class DeviceBluetoothConnectAdapter extends BaseRecyclerAdapter<BluetoothDevice> {
    private OnItemClickListener listener;


    public DeviceBluetoothConnectAdapter(Context context) {
        super(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean containsItem(BluetoothDevice data) {
        boolean contain = false;
        for (BluetoothDevice entity : getDataList()) {
            if (entity.getAddress().equals(data.getAddress())) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BluetoothTypeViewHolder(mLayoutInflater.inflate(R.layout.item_print_connet, parent,false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BluetoothTypeViewHolder) {
            BluetoothTypeViewHolder viewHolder = (BluetoothTypeViewHolder) holder;
            BluetoothDevice bluetoothDevice = getItemData(position);
            viewHolder.mtvDeviceName.setText(bluetoothDevice.getName());
            viewHolder.mtvDeviceMac.setText(bluetoothDevice.getAddress());
        }
    }

    /**
     * 类型holder
     */
    class BluetoothTypeViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mtvDeviceName;
        TextView mtvDeviceMac;

        public BluetoothTypeViewHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.item_print_container);
            mtvDeviceName = itemView.findViewById(R.id.item_print_device_name);
            mtvDeviceMac = itemView.findViewById(R.id.item_print_device_mac);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}



