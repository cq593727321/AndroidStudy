package com.smartcomma.huawei.ui.test;

import java.io.File;

public class DemoConfig {
	private static DemoConfig instance = null;

	public static synchronized DemoConfig getInstance() {
		if (instance == null) {
			instance = new DemoConfig();
			instance.LoadConfig();
		}
		return instance;
	}
	
	private String DirPath = "/sdcard/config";	//路径

	public int iUHFModType = 0; // 模块类型
	public String ServerIP = "192.168.0.178";	//服务器IP
	public String ServerPort = "4001";			//端口号

	public boolean LoadConfig() {
		File folder = new File(DirPath);
		if (!folder.exists() && !folder.mkdir())
			return false;

		IniFile iniFile = new IniFile(new File(DirPath + "/DemoConfig.ini"));
		String szModType = (String) iniFile.get("UHF", "ModType", "0");
		iUHFModType = Integer.parseInt(szModType);
		ServerIP = (String) iniFile.get("UHF","ServerIP","192.168.0.178");
		ServerPort = (String) iniFile.get("UHF","ServerPort","4001");
		iniFile.save();
		return true;
	}

	public boolean SaveConfig() {
		File folder = new File(DirPath);
		if (!folder.exists() && !folder.mkdir())
			return false;

		IniFile iniFile = new IniFile(new File(DirPath + "/DemoConfig.ini"));
		iniFile.set("UHF", "ModType", "" + iUHFModType);
		iniFile.set("UHF", "ServerIP",  ServerIP);
		iniFile.set("UHF", "ServerPort",  ServerPort);
		iniFile.save(new File(DirPath + "/DemoConfig.ini"));
		return true;
	}
}
