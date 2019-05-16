package com.smartcomma.huawei.ui.test;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.RFID.RFIDReader;
import com.RFID.TAGINFO;
import com.smartcomma.huawei.R;

import java.util.Hashtable;
import java.util.List;

public class A8UHFReceiveActivity extends AppCompatActivity {
    private UhfDev mDev = UhfDev.getInstance();
    private String iInventoryMode = "1";  //0单标签  1 多标签
    // region 声音线程
    private Thread thSound = null; // 声音线程句柄
    private boolean runSoundTh = false; // 声音线程开关
    private boolean bSoundPlay = false; // 声音
    private SoundPool soundPool = null;


    @SuppressLint("HandlerLeak")
    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = msg.getData().getString("msg");
            if ((null == str || 0 == str.length())
                    && ((UhfDev.MSG_TIMER != msg.what)
                    && (msg.what != UhfDev.MSG_PAUSE) && (msg.what != UhfDev.MSG_RESUME)))
                return;
            if (msg.what == UhfDev.MSG_EPC) { // 标签内容
                bSoundPlay = true;
                String epc = str;
            }// MSG_EPC
            else if (msg.what == UhfDev.MSG_OPEN) { // 连接
            } else if (msg.what == UhfDev.MSG_TIMER) { // 定时器

            } else if (msg.what == UhfDev.MSG_PAUSE) { // 暂停
                StopInventory();
            } else if (msg.what == UhfDev.MSG_RESUME) { // 继续
                StartInventory();
            } else { // 消息
                //textViewLog.setText(str);
            }
        }
    };

    private RFIDReader.InvCallBack mInvCallback = new RFIDReader.InvCallBack() {
        public String HexToString(byte[] bArray) {
            StringBuffer sb = new StringBuffer(bArray.length);
            String sTemp;
            for (int i = 0; i < bArray.length; i++) {
                sTemp = Integer.toHexString(0xFF & bArray[i]);
                if (sTemp.length() < 2)
                    sb.append(0);
                sb.append(sTemp.toUpperCase());
            }
            return sb.toString();
        }

        private void SendMsg(String EpcID) {
            Message msg = new Message();
            msg.what = UhfDev.MSG_EPC;
            Bundle bundle = new Bundle();
            bundle.putString("msg", EpcID); // 往Bundle中存放数据
            msg.setData(bundle); // mes利用Bundle传递数据
            msgHandler.sendMessage(msg);// 用activity中的handler发送消息
        }

        @Override
        public void execute(List<TAGINFO> lstTagInfo) {
            if (null == lstTagInfo || lstTagInfo.size() == 0)
                Log.e("Inventory", "错误内容");
            // 处理数据
            Hashtable<byte[], TAGINFO> htNewTagInfo = new Hashtable<byte[], TAGINFO>();
            String szTag = "";

            for (int i = 0; i < lstTagInfo.size(); i++) {
                TAGINFO tfs = lstTagInfo.get(i);
                if (null == tfs)
                    continue;
                // 处理数据
                szTag = HexToString(tfs.EpcId) + ";" + tfs.ReadCnt + ";"
                        + tfs.RSSI;
                SendMsg(szTag);
            }
            Log.e("Inventory", "发送数据完毕" + szTag);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a8_uhfreceive);
        boolean bSucc = mDev.SetReadMode(Byte.parseByte(iInventoryMode), (byte) 0);
        mDev.SetHandleFunc(msgHandler);// 设置回调函数
        mDev.SetInvCallback(mInvCallback);
        StartSoundThread(); // 启动声音线程
    }

    // 启动声音线程
    private void StartSoundThread() {
        if (null == thSound) {
            runSoundTh = true;
            thSound = new Thread(SoundThread);
            thSound.start();
        }
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);// AudioManager.STREAM_SYSTEM
        soundPool.load(this, R.raw.beep51, 1);// chimes
    }

    // 停止声音线程
    private void StopSoundThread() {

        try {
            if (null != thSound) {
                bSoundPlay = false;
                runSoundTh = false;
                thSound.join(2000);
                thSound.interrupt();
                thSound = null;
                soundPool.release();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    Runnable SoundThread = new Runnable() {
        @Override
        public void run() {
            int iSoundTimes = 0;
            while (runSoundTh) {
                try {
                    if (bSoundPlay) { // 响两次
                        bSoundPlay = false;
                        iSoundTimes = 6;
                    }
                    if (iSoundTimes > 0) {
                        soundPool.play(1, 1, 1, 0, 0, 1);
                        // SoundUtil.play(R.raw.c himes, 0);
                        iSoundTimes--;
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    Handler handler = new Handler();
    Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            // 要做的事情
            handler.postDelayed(this, 200);
            Message message = new Message();
            message.what = UhfDev.MSG_TIMER;
            msgHandler.sendMessage(message);
        }
    };

    // 开始盘存
    private void StartInventory() {
        SetScreenOn(true);
        mDev.inventory();
        handler.postDelayed(runnableTimer, 200);// 每X秒执行一次runnable.
        //this.setButtonClickable(cbBackTag, false);
    }

    // 停止盘存
    public void StopInventory() {
        handler.removeCallbacks(runnableTimer);
        // timer.cancel();
        mDev.pause();
        SetScreenOn(false);
        //this.setButtonClickable(cbBackTag, true);
    }

    private void SetScreenOn(boolean bOn) {
        if (bOn)
            PowerMoniter.getInstance().acquireWakeLock(this);
        else
            PowerMoniter.getInstance().releaseWakeLock();
    }

    // region 按键事件
    /* 键按下事件 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((134 == keyCode) || (275 == keyCode))
            StartInventory();
        return super.onKeyDown(keyCode, event);
    }

    /* 释放按键事件 */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((134 == keyCode) || (275 == keyCode))
            StopInventory();
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            StopInventory();
            StopSoundThread(); // 停止声音线程
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
