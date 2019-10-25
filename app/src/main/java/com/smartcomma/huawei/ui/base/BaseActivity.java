package com.smartcomma.huawei.ui.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.RFID.RFIDReader;
import com.RFID.TAGINFO;
import com.smartcomma.huawei.MyApplication;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.ui.test.PowerMoniter;
import com.smartcomma.huawei.ui.test.SoundUtil;
import com.smartcomma.huawei.ui.test.UhfDev;
import com.smartcomma.huawei.utils.LocalUtils;
import com.smartcomma.huawei.utils.a20.barcode.ScanUtil;
import com.uk.tsl.rfid.asciiprotocol.commands.BarcodeCommand;
import com.uk.tsl.rfid.asciiprotocol.commands.InventoryCommand;
import com.uk.tsl.rfid.asciiprotocol.enumerations.TriState;
import com.uk.tsl.rfid.asciiprotocol.responders.IBarcodeReceivedDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.ICommandResponseLifecycleDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.ITransponderReceivedDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.TransponderData;
import com.uk.tsl.utils.HexEncoding;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends CommonActivity {
    //A8 UHF
    private UhfDev mDev;
    private String iInventoryMode = "1";  //0单标签  1 多标签
    // region 声音线程
    private Thread thSound = null; // 声音线程句柄
    private boolean runSoundTh = false; // 声音线程开关
    private boolean bSoundPlay = false; // 声音
    private SoundPool soundPool = null;
    //A8 UHF


    //TSL
    // Control
    private boolean mAnyTagSeen;
    // The command to use as a responder to capture incoming inventory responses
    private InventoryCommand mInventoryResponder;
    // The command used to issue commands
    private InventoryCommand mInventoryCommand;

    // The command to use as a responder to capture incoming barcode responses
    private BarcodeCommand mBarcodeResponder;
    //TSL


    //a20
    private ScanUtil scanUtil;

    protected abstract void BarcodeReceiver(String barcode);

    protected abstract void RFIDEpcReceiver(String barcode);

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
                Log.e("TAG", "handleMessage: " + epc);
                RFIDEpcReceiver(epc);

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
    //先施A8条码扫描监听
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            byte[] barocode = intent.getByteArrayExtra("barocode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            String barcodeStr = new String(barocode, 0, barocodelen);
            Log.e("TAG", "onReceive: " + barcodeStr);
            //sm.stopScan();
            BarcodeReceiver(barcodeStr);
        }

    };

    private BroadcastReceiver a20BarcodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] data = intent.getByteArrayExtra("data");
            if (data != null) {
                String barcode = new String(data);
                // String barcode = Tools.Bytes2HexString(data, data.length);
                BarcodeReceiver(barcode);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ("A8".equals(LocalUtils.getSystemModel())) {
            mDev = new UhfDev();
            initA8UHF();
            SoundUtil.initSoundPool(this);
            boolean bSucc = mDev.SetReadMode(Byte.parseByte(iInventoryMode), (byte) 0);
            mDev.SetHandleFunc(msgHandler);// 设置回调函数
            mDev.SetInvCallback(mInvCallback);
            StartSoundThread(); // 启动声音线程
        }
    }

    protected void initA8UHF() {
        int iModType = 1;//设置型号
        if (!mDev.InitDevice(iModType)) {
            Toast.makeText(getApplicationContext(), "初始化失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!mDev.Open()) {
            Toast.makeText(getApplicationContext(), "连接失败",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("scan.rcv.message");
        registerReceiver(mScanReceiver, filter);//先施A8条码广播

        //a20
        IntentFilter filterA20 = new IntentFilter();
        filterA20.addAction("com.rfid.SCAN");
        registerReceiver(a20BarcodeReceiver, filterA20);

        if (scanUtil == null) {
            scanUtil = new ScanUtil(this);
            //we must set mode to 0 : BroadcastReceiver mode
            scanUtil.setScanMode(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mScanReceiver);

        //a20
        unregisterReceiver(a20BarcodeReceiver);
        if (scanUtil != null) {
            // When exit, set mode to 1 : EditText input mode, for the background scanning
            scanUtil.setScanMode(1);
            scanUtil.close();
            scanUtil = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                // SendMsg(szTag);
                SendMsg(HexToString(tfs.EpcId));
                Log.e("Inventory", "发送数据完毕" + HexToString(tfs.EpcId));
            }
        }
    };

    // 开始盘存
    private void StartInventory() {
        SetScreenOn(true);
        SoundUtil.play(R.raw.pegconn, 0);
        mDev.inventory();
        handler.postDelayed(runnableTimer, 200);// 每X秒执行一次runnable.
        //this.setButtonClickable(cbBackTag, false);
    }

    // 停止盘存
    public void StopInventory() {
        SoundUtil.play(R.raw.pegconn, 0);
        handler.removeCallbacks(runnableTimer);
        // timer.cancel();
        mDev.pause();
        SetScreenOn(false);
        //this.setButtonClickable(cbBackTag, true);
    }

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
        if ("A8".equals(LocalUtils.getSystemModel())) {
            if ((134 == keyCode) || (275 == keyCode))
                StartInventory();
        }
        return super.onKeyDown(keyCode, event);
    }

    /* 释放按键事件 */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ("A8".equals(LocalUtils.getSystemModel())) {
            if ((134 == keyCode) || (275 == keyCode))
                StopInventory();
            else if (keyCode == KeyEvent.KEYCODE_BACK) {
                StopInventory();
                StopSoundThread(); // 停止声音线程
                finish();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    ///////////////////////////////////////////TSL////////////////////////////////////////////////////
    private void initTSL() {
        mInventoryCommand = new InventoryCommand();

        // Configure the type of inventory
        mInventoryCommand.setIncludeTransponderRssi(TriState.YES);
        mInventoryCommand.setIncludeChecksum(TriState.YES);
        mInventoryCommand.setIncludePC(TriState.YES);
        mInventoryCommand.setIncludeDateTime(TriState.YES);

        // Use an InventoryCommand as a responder to capture all incoming inventory responses
        mInventoryResponder = new InventoryCommand();

        // Also capture the responses that were not from App commands
        mInventoryResponder.setCaptureNonLibraryResponses(true);

        // Notify when each transponder is seen
        mInventoryResponder.setTransponderReceivedDelegate(new ITransponderReceivedDelegate() {

            int mTagsSeen = 0;

            @Override
            public void transponderReceived(TransponderData transponder, boolean moreAvailable) {
                mAnyTagSeen = true;

                String tidMessage = transponder.getTidData() == null ? "" : HexEncoding.bytesToString(transponder.getTidData());
                String infoMsg = String.format(Locale.US, "\nRSSI: %d  PC: %04X  CRC: %04X", transponder.getRssi(), transponder.getPc(), transponder.getCrc());
                //sendMessageNotification("EPC: " + transponder.getEpc() + infoMsg + "\nTID: " + tidMessage );
                Log.e("TAG", "transponderReceived:EPC: " + transponder.getEpc());
                RFIDEpcReceiver(transponder.getEpc());
                mTagsSeen++;
                if (!moreAvailable) {
                    Log.d("TagCount", String.format("Tags seen: %s", mTagsSeen));
                }
            }
        });

        mInventoryResponder.setResponseLifecycleDelegate(new ICommandResponseLifecycleDelegate() {

            @Override
            public void responseEnded() {
                if (!mAnyTagSeen && mInventoryCommand.getTakeNoAction() != TriState.YES) {
                    //sendMessageNotification("No transponders seen");
                }
                mInventoryCommand.setTakeNoAction(TriState.NO);
            }

            @Override
            public void responseBegan() {
                mAnyTagSeen = false;
            }
        });
        // This command is used to capture barcode responses
        mBarcodeResponder = new BarcodeCommand();
        mBarcodeResponder.setCaptureNonLibraryResponses(true);
        mBarcodeResponder.setUseEscapeCharacter(TriState.YES);
        mBarcodeResponder.setBarcodeReceivedDelegate(new IBarcodeReceivedDelegate() {
            @Override
            public void barcodeReceived(String barcode) {
                //sendMessageNotification("BC: " + barcode);
                Log.e("TAG", "barcodeReceived: BC: " + barcode);
                BarcodeReceiver(barcode);
            }
        });
        // Listen for transponders
        MyApplication.getCommander().addResponder(mInventoryResponder);
        // Listen for barcodes
        MyApplication.getCommander().addResponder(mBarcodeResponder);
    }
}
