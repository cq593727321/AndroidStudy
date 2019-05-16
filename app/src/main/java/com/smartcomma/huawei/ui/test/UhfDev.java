//UhfDev.java： RFID处理类
package com.smartcomma.huawei.ui.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.RFID.RFIDReader;
import com.RFID.RFIDReader.InvCallBack;

//RFID处理类
public class UhfDev extends Object {
	private Handler msgHandler = null;
	public final static int MSG_INFO = 0;
	public final static int MSG_EPC = 1;
	public final static int MSG_STOP = 3;
	public final static int MSG_OPEN = 4;
	public final static int MSG_TIMER = 5; // 定时器
	public final static int MSG_PAUSE = 6; // 暂停
	public final static int MSG_RESUME = 7; // 继续

	private final int DEV_STAT_CLOSE = 0;
	private final int DEV_STAT_OPEN = 1;
	private int devStat = DEV_STAT_CLOSE;
	private final int CMD_CLOSE = 0; // 盘存状态:停止
	private final int CMD_PAUSE = 1; // 盘存状态:暂停
	private final int CMD_INV = 2; // 盘存状态:盘存
	public RFIDReader reader = null;
	private boolean runFlag = true;
	private static UhfDev instance = null;
	private Thread thInventory = null; // 盘存线程句柄
	private int m_iModType = -1; // A8-A模式
	private String m_ServerIP = ""; // 服务器IP
	private int m_ServerPort = 0; // 服务器端口

	public static synchronized UhfDev getInstance() {
		if (instance == null) {
			instance = new UhfDev();
			// instance.init();
		}
		return instance;
	}

	// 初始化设备
	public boolean InitDevice(int iModType) {
		reader = new RFIDReader();
		if (!reader.Init(iModType)) {
			sendMsg(MSG_INFO, "初始化失败");
			return false;
		}
		m_iModType = iModType;
		return true;
	}

	// 待机过程中初始化设备：仅仅A8-A需要
	public boolean InitDeviceResume(int iModType) {
		if (0 == iModType)
			return InitDevice(iModType);
		return true;
	}

	// region Inventory Thread
	// Start Device Inventory
	public void StartDev(Handler handler) {
		msgHandler = handler;
	}

	// Set to Inventory
	public void inventory() {
		reader.StartContinueInventory();
	}
	public void inventory(byte []antList) {
		reader.StartContinueInventory(antList, (byte)antList.length);
	}
	// Pause Inventory
	public void pause() {
		reader.StopContinueInventory();
		sendMsg(MSG_INFO, "已暂停");
	}

	// Pause Inventory
	public void PauseInventory() {
		sendMsg(MSG_PAUSE, "");
	}

	// Resume Inventory
	public void ResumeInventory() {
		sendMsg(MSG_RESUME, "继续");
	}

	// Hex Array --> String
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

	private void Sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// endregion

	// Open device
	public boolean Open() {
		if (devStat != DEV_STAT_CLOSE)
			return true;
		// 模块上电
		boolean bConn = reader.Connect();
		if (!bConn) {
			Sleep(1000);
			bConn = reader.Connect();
		}
		if (!bConn) {
			sendMsg(MSG_INFO, "连接失败" + reader.GetLastError());
			return false;
		}
		sendMsg(MSG_OPEN, "连接成功");
		devStat = DEV_STAT_OPEN;

		// 初始化配置
		// SetPower(30); // 设置功率30
		return true;
	}
	public boolean OpenBySerial() {
		if (devStat != DEV_STAT_CLOSE)
			return true;
		// 模块上电
		boolean bConn = reader.ConnectBySerial("/dev/ttyS4");
		if (!bConn) {
			sendMsg(MSG_INFO, "连接失败" + reader.GetLastError());
			return false;
		}
		sendMsg(MSG_OPEN, "连接成功");
		devStat = DEV_STAT_OPEN;
		return true;
	}
	// open device via tcp
	public boolean OpenByTcp(String IP, int port) {
		if (devStat != DEV_STAT_CLOSE)
			return true;
		// 模块上电
		boolean bConn = reader.ConnectByIP(IP, port);
		if (!bConn) {
			Sleep(1000);
			bConn = reader.ConnectByIP(IP, port);
		}
		if (!bConn) {
			sendMsg(MSG_INFO, "连接失败" + reader.GetLastError());
			return false;
		}
		sendMsg(MSG_OPEN, "连接成功");
		devStat = DEV_STAT_OPEN;

		m_ServerIP = IP;
		m_ServerPort = port;
		// 初始化配置
		// SetPower(30); // 设置功率30
		return true;
	}

	// 重新连接模块
	public void ReConnect() {
		InitDeviceResume(m_iModType);
		if (2 == m_iModType)
			OpenByTcp(m_ServerIP, m_ServerPort);
		else
			Open();
	}

	// disconnect to reader
	public boolean Close() {
		if (DEV_STAT_CLOSE != devStat)
			reader.Disconnect();
		devStat = DEV_STAT_CLOSE;
		return true;
	}

	// Set callback function
	public void SetHandleFunc(Handler handler) {
		msgHandler = handler;
	}

	// Set callback function
	public void SetInvCallback(InvCallBack callback) {
		reader.SetCallback(callback);
	}

	public boolean IsOpen() {
		return devStat == DEV_STAT_CLOSE ? false : true;
	}

	// update information
	public void sendMsg(int msgtype, String str) {
		if (msgHandler != null) {
			// 处理数据
			Message msg = new Message();
			msg.what = msgtype;
			Bundle bundle = new Bundle();
			bundle.putString("msg", str); // 往Bundle中存放数据
			msg.setData(bundle);// mes利用Bundle传递数据
			msgHandler.sendMessage(msg);// 用activity中的handler发送消息
		}
	}

	// 设置序号
	public boolean SetReaderID(String szRdName, String szRdID) {
		return reader.SetReaderID(szRdName, szRdID);
	}

	// 查询序号
	public String GetReaderID() {
		return reader.GetReaderID();
	}

	public int GetPortLoss() {
		return reader.GetPortLoss();
	}

	// set power
	public boolean SetPower(int power) {
		return reader.SetPower(power);
	}

	// query power
	public int GetPower() {
		return reader.GetPower();
	}

	// set ant
	public boolean SetWorkAntenna(byte bAntenna) {
		return reader.SetWorkAntenna(bAntenna);
	}

	// query ant
	public int GetWorkAntenna() {
		return reader.GetWorkAntenna();
	}

	// set Region
	public boolean SetRegion(int Region) {
		return reader.SetRegion(Region);
	}

	// query Region
	public int GetRegion() {
		return reader.GetRegion();
	}

	// Set ReadMode: 0-省电模式(单标签读) 1-盘存模式(多标签读) 2-多容量标签
	public boolean SetReadMode(int invMode) {
		return reader.SetReadMode((byte)invMode,(byte) 0);
	}
	public boolean SetReadMode(byte invMode, byte flag) {
		return reader.SetReadMode(invMode, flag);
	}

	// Set
	public boolean SetCustomReaderMode(byte bMode) {
		return reader.SetCustomReaderMode(bMode);
	}

	public int GetCustomReaderMode() {
		return reader.GetCustomReaderMode();
	}

	// Query Read Mode
	public int GetReadMode() {
		byte []invMode = new byte[2];
		byte []flag = new byte[2];
		boolean bGet = GetReadMode(invMode, flag);
		return bGet?1:(-1);
	}
	public boolean GetReadMode(byte []invMode, byte []flag) {
		return reader.GetReadMode(invMode, flag);
	}
}