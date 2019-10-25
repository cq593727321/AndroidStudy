package com.smartcomma.a20;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uhf.scanlable.UHfData;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int MESSAGE_SUCCESS = 0;
    private int MESSAGE_FAIL = 1;
    private ConnectHandler mHandlerSendConnect = new ConnectHandler(this);
    private String devport = "/dev/ttyMT1";

    Button scan;
    TextView mtvEPC;
    private static boolean Scanflag = false;
    private Handler mHandler;
    private boolean isCanceled = true;
    private static final int SCAN_INTERVAL = 5;
    private static final int MSG_UPDATE_LISTVIEW = 0;
    private Timer timer;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new UHfData(this);
        try {
            scan = findViewById(R.id.bt_scan);
            mtvEPC = findViewById(R.id.tv_scan_rfid);
            scan.setOnClickListener(this);


            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (isCanceled)
                        return;
                    switch (msg.what) {
                        case MSG_UPDATE_LISTVIEW:
                            List<UHfData.InventoryTagMap> datas = UHfData.lsTagList;
                            //Log.e("TAG", "handleMessageGson: " + new Gson().toJson(datas));
                            for (UHfData.InventoryTagMap data : datas) {
                                // Log.e("TAG", "handleMessage: " + data.strEPC);
                                mtvEPC.setText(data.strEPC);
                            }
                            Log.e("TAG", "mtvEPC: " + mtvEPC.getText().toString());
//                            UHfData.lsTagList.clear();
//                            UHfData.dtIndexMap.clear();
                            if (UHfData.mIsNew) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        if (!Util.soundfinished)
                                            Util.play(1, 0);
                                    }
                                }).start();
                                UHfData.mIsNew = false;
                            }
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }

            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.initSoundPool(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            public void run() {
                try {
                    int result = UHfData.UHfGetData.OpenUHf(devport, 57600);
                    if (result == 0) {
                        mHandlerSendConnect.sendEmptyMessage(MESSAGE_SUCCESS);
                    } else {
                        mHandlerSendConnect.sendEmptyMessage(MESSAGE_FAIL);
                    }
                } catch (Exception e) {
                    mHandlerSendConnect.sendEmptyMessage(MESSAGE_FAIL);
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelScan();
    }

    private void cancelScan() {
        isCanceled = true;
        mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
        if (timer != null) {
            timer.cancel();
            timer = null;
            scan.setText("扫描");
            UHfData.lsTagList.clear();
            UHfData.dtIndexMap.clear();
        }
    }

    @Override
    protected void onStop() {
        UHfData.UHfGetData.CloseUHf();
        super.onStop();
    }


    @Override
    public void onClick(View view) {
        try {
            if (timer == null) {
                isCanceled = false;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (Scanflag)
                            return;
                        Scanflag = true;
                        UHfData.Inventory_6c(1, 1);
//						UHfData.Inventory_6c_Mask((byte)0, 16, 0, UHfGetData.hexStringToBytes("E200"));
                        mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
                        mHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
                        Scanflag = false;
                    }
                }, 0, SCAN_INTERVAL);
                scan.setText("停止");
            } else {
                isCanceled = true;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    scan.setText("扫描");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class ConnectHandler extends Handler {
        private WeakReference<MainActivity> mReference;
        private MainActivity mActivity;

        ConnectHandler(MainActivity activity) {
            mReference = new WeakReference<MainActivity>(activity);
            mActivity = mReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == mActivity.MESSAGE_SUCCESS) {
                Toast.makeText(mActivity.getApplicationContext(), "串口连接成功",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == mActivity.MESSAGE_FAIL) {
                Toast.makeText(mActivity.getApplicationContext(), "串口连接失败",
                        Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    }
}
