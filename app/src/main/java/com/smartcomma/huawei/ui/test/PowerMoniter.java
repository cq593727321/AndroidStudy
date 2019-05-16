//PowerMoniter.java：禁止待机
package com.smartcomma.huawei.ui.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;

public class PowerMoniter {
	private static PowerMoniter m_PowerMoniter = new PowerMoniter();
	public static PowerMoniter getInstance() {
		return m_PowerMoniter;
	}

	private LocalBroadcastManager lbm = null;

	public static final String ActionOffScreen = "android.intent.action.SCREEN_OFF";// 屏幕被关闭之后的广播
	public static final String ActionOnScreen = "android.intent.action.SCREEN_ON";// 屏幕被打开之后的广播
	private final BroadcastReceiver mRecv = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ActionOffScreen)) {
				UhfDev.getInstance().Close();
			} else if (intent.getAction().equals(ActionOnScreen)) {
				UhfDev.getInstance().Open();
			}
		}
	};

	// 监控系统待机
	public void Register(ContextWrapper context) {
		lbm = LocalBroadcastManager.getInstance(context);

		IntentFilter filter = new IntentFilter();
		filter.addAction(ActionOffScreen);
		filter.addAction(ActionOnScreen);
		filter.setPriority(Integer.MAX_VALUE);
		// 动态注册BroadcastReceiver
		lbm.registerReceiver(mRecv, filter);
	}

	public void Unregister() {
		if (null != lbm) {
			lbm.unregisterReceiver(mRecv);
			lbm = null;
		}
	}

	/**
	 * 设置为竖屏
	 */
	public void SetScreenPorait(Activity activity) {
		if (activity.getRequestedOrientation() != android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	// 禁止待机
	WakeLock wakeLock = null;

	@SuppressWarnings("deprecation")
	public void acquireWakeLock(Activity activity) {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) activity
					.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this
					.getClass().getCanonicalName());
			wakeLock.acquire();
			// View.setKeepScreenOn(true);
		}
	}

	// 停止禁止待机
	public void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}

	}
}
