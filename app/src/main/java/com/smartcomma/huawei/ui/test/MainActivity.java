package com.smartcomma.huawei.ui.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartcomma.huawei.MyApplication;
import com.smartcomma.huawei.R;
import com.smartcomma.huawei.ui.setting.DeviceConnectActivity;
import com.uk.tsl.rfid.asciiprotocol.commands.BarcodeCommand;
import com.uk.tsl.rfid.asciiprotocol.commands.InventoryCommand;
import com.uk.tsl.rfid.asciiprotocol.enumerations.TriState;
import com.uk.tsl.rfid.asciiprotocol.responders.IBarcodeReceivedDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.ICommandResponseLifecycleDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.ITransponderReceivedDelegate;
import com.uk.tsl.rfid.asciiprotocol.responders.TransponderData;
import com.uk.tsl.utils.HexEncoding;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.bt_device_connect)
    Button mbtConnect;
    @BindView(R.id.bt_test)
    Button mbtTest;
    @BindView(R.id.bt_test_1)
    Button btTest1;
    // Control
    private boolean mAnyTagSeen;
    // The command to use as a responder to capture incoming inventory responses
    private InventoryCommand mInventoryResponder;
    // The command used to issue commands
    private InventoryCommand mInventoryCommand;

    // The command to use as a responder to capture incoming barcode responses
    private BarcodeCommand mBarcodeResponder;

    @OnClick(R.id.bt_test)
    void test() {
        startActivity(new Intent(this, TestActivity.class));
    }

    @OnClick(R.id.bt_test_1)
    void test1() {
        Toast.makeText(this, "test00000", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //mbtConnect = findViewById(R.id.bt_device_connect);
        mbtConnect.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getCommander().isConnected()) {
            mbtConnect.setText("已连接");
        } else {
            mbtConnect.setText("未连接");
        }
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
            }
        });
        // Listen for transponders
        MyApplication.getCommander().addResponder(mInventoryResponder);
        // Listen for barcodes
        MyApplication.getCommander().addResponder(mBarcodeResponder);
    }

    @Override
    public void onClick(View view) {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //已授权
            Log.e("TAG", "onCreate: " + "已授权");
            startActivity(new Intent(MainActivity.this, DeviceConnectActivity.class));
        } else {
            //未授权
            Log.e("TAG", "onCreate: " + "未授权");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, DeviceConnectActivity.class));
                } else {
                    Toast.makeText(this, "请再设置里面打开权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
