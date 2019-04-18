package com.smartcomma.huawei.ui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartcomma.huawei.MyApplication;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.adapter.DeviceBluetoothConnectAdapter;
import com.smartcomma.huawei.adapter.PairDeviceBluetoothConnectAdapter;
import com.smartcomma.huawei.utils.SPUtils;
import com.uk.tsl.rfid.asciiprotocol.AsciiCommander;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceConnectActivity extends AppCompatActivity implements DeviceBluetoothConnectAdapter.OnItemClickListener, PairDeviceBluetoothConnectAdapter.OnPairedItemClickListener {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewPaired;
    private Button mbtReconnect;
    private ProgressDialog progressDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceBluetoothConnectAdapter adapter;
    private PairDeviceBluetoothConnectAdapter pairAdapter;
    private static final int REQUEST_ENABLE_BT = 1002;
    private boolean mScanning = false;
    private static final long SCAN_PERIOD = 1000 * 60;
    private Handler mHandler = new Handler();
    private List<BluetoothDevice> bondDeviceList = new ArrayList<>();

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                if (device.getName() != null && device.getAddress() != null) {
                    if (!adapter.containsItem(device)) {
                        adapter.addItem(device);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (adapter.getDataList().size() == 0) {
                    //Toast.makeText(DeviceConnectActivity.this, "未收到蓝牙设备", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private final BroadcastReceiver mDevicesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AsciiCommander.STATE_CHANGED_NOTIFICATION.equals(action)) {
                String connectionStateMsg = intent.getStringExtra(AsciiCommander.REASON_KEY);
                Log.e("TAG", "onReceive: " + connectionStateMsg);
                if (connectionStateMsg.contains("connecting")) {

                } else if (connectionStateMsg.contains("Connected to")) {
                    cancelConnectDialog();
                    Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    cancelConnectDialog();
                    scanLeDevice(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);
        mToolbar = findViewById(R.id.blue_connect_tool_bar);
        mRecyclerView = findViewById(R.id.device_connect_rv);
        mRecyclerViewPaired = findViewById(R.id.device_paired_rv);
        mbtReconnect = findViewById(R.id.device_connect_bt_reconnect);
        mToolbar.setTitle("蓝牙连接");
        setSupportActionBar(mToolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                adapter.clearItems();
                scanLeDevice(true);
            }
        });
        progressDialog.setTitle("请等待...");
        initBlue();
        initRecycler();
        getBondDevice();
        mbtReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeviceConnectActivity.this, "重连中...", Toast.LENGTH_LONG).show();
                MyApplication.getCommander().connect(null);
            }
        });
    }

    private void initBlue() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙不可用", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void initRecycler() {
        adapter = new DeviceBluetoothConnectAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        pairAdapter = new PairDeviceBluetoothConnectAdapter(this);
        mRecyclerViewPaired.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPaired.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerViewPaired.setAdapter(pairAdapter);
        pairAdapter.setOnItemClickListener(this);

    }

    public void showConnectDialog(String title) {
        if (progressDialog != null) {
            progressDialog.setMessage(title);
            if (!progressDialog.isShowing())
                progressDialog.show();
        }
    }

    public void cancelConnectDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanLeDevice(true);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, intentFilter);

        IntentFilter intent = new IntentFilter();
        intent.addAction(AsciiCommander.STATE_CHANGED_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDevicesReceiver, intent);
        adapter.clearItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDevicesReceiver);
        unregisterReceiver(mReceiver);
    }

    /**
     * 获取已配对的蓝牙设备
     */
    private void getBondDevice() {
        bondDeviceList.clear();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            bondDeviceList.add(device);
            pairAdapter.addItem(device);
        }

    }

    /**
     * 判断设备是否配对
     *
     * @param address
     * @return
     */
    private boolean isDevicePair(String address) {
        boolean isPair = false;
        for (int i = 0; i < bondDeviceList.size(); i++) {
            if (bondDeviceList.get(i).getAddress().equals(address)) {
                isPair = true;
                break;
            }
        }
        return isPair;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        if (mScanning) {
            menu.findItem(R.id.action_connecting).setActionView(R.layout.menu_action_refresh).setVisible(true);
            menu.findItem(R.id.action_refresh).setVisible(false);
        } else {
            menu.findItem(R.id.action_connecting).setActionView(null).setVisible(false);
            menu.findItem(R.id.action_refresh).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                adapter.clearItems();
                pairAdapter.clearItems();
                getBondDevice();
                scanLeDevice(true);
                break;

            case R.id.action_connecting:
                scanLeDevice(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        scanLeDevice(false);
        Log.e("TAG", "onItemClick:  连接中");
        BluetoothDevice device = adapter.getItemData(position);
        showConnectDialog("连接中...");
        if (device != null)
            MyApplication.getCommander().connect(device);
    }

    /**
     * 扫描蓝牙低功耗设备.
     *
     * @param enable 设为 true 开始扫描，false 立即停止扫描。
     */
    private void scanLeDevice(final boolean enable) {
        if (mBluetoothAdapter != null) {
            if (enable) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.cancelDiscovery();
                        invalidateOptionsMenu();
                    }
                }, 60000);

                mScanning = true;
                mBluetoothAdapter.startDiscovery();
            } else {
                mScanning = false;
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onPairItemClick(int position) {
        scanLeDevice(false);
        Log.e("TAG", "onItemClick已配对:  连接中");
        BluetoothDevice device = adapter.getItemData(position);
        showConnectDialog("连接中...");
        if (device != null)
            MyApplication.getCommander().connect(device);
    }
}
