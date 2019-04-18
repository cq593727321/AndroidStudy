package com.smartcomma.huawei.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.Map;

/**
 * 本地化持久保存
 * Created by cq
 */

public class SPUtils {
    private static SharedPreferences sharedPreferences = null;

    private SPUtils() {

    }

    private static SharedPreferences getSharedPreferencesObject(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }

    public static int getSharedPreferences(Context context, String key, int defValue) {
        return getSharedPreferencesObject(context).getInt(key, defValue);
    }

    public static long getSharedPreferences(Context context, String key, long defValue) {
        return getSharedPreferencesObject(context).getLong(key, defValue);
    }

    public static Boolean getSharedPreferences(Context context, String key, boolean defValue) {
        return getSharedPreferencesObject(context).getBoolean(key, defValue);
    }

    public static String getSharedPreferences(Context context, String key, String defValue) {
        return getSharedPreferencesObject(context).getString(key, defValue);
    }

    public static Map<String, ?> getAll(Context context) {
        return getSharedPreferencesObject(context).getAll();
    }

    public static void setEditor(Context context, String key, int value) {
        getSharedPreferencesObject(context).edit().putInt(key, value).apply();
    }

    public static void setEditor(Context context, String key, long value) {
        getSharedPreferencesObject(context).edit().putLong(key, value).apply();
    }

    public static void setEditor(Context context, String key, Boolean value) {
        getSharedPreferencesObject(context).edit().putBoolean(key, value).apply();
    }

    public static void setEditor(Context context, String key, String value) {
        getSharedPreferencesObject(context).edit().putString(key, value).apply();
    }

    public static void clear(Context context) {
        getSharedPreferencesObject(context).edit().clear().apply();
    }

    public static void remove(Context context, String key) {
        getSharedPreferencesObject(context).edit().remove(key).commit();
    }

    /**
     * 保存获取服务器
     */
//    private static final String KEY_SERVER_MODEL = "local_server_model";
//
//    public static void saveServer(Context context, Server server) {
//        Gson gson = new Gson();
//        setEditor(context, KEY_SERVER_MODEL, gson.toJson(server));
//    }
//
//    public static Server getServer(Context context) {
//        Server server = new Gson().fromJson(getSharedPreferencesObject(context).getString(KEY_SERVER_MODEL, ""), Server.class);
//        return server;
//    }

    /**
     * 保存获取用户
     */
//    private static final String KEY_USER_MODEL = "local_user_model";
//
//    public static void saveUser(Context context, User user) {
//        setEditor(context, KEY_USER_MODEL, new Gson().toJson(user));
//    }
//
//    public static User getUser(Context context) {
//        User user = new Gson().fromJson(getSharedPreferencesObject(context).getString(KEY_USER_MODEL, ""), User.class);
//        return user;
//    }

    private static final String BLUETOOTH_DEVICE = "Bluetooth_device";

    public static void saveBluetoothDevice(Context context, BluetoothDevice device) {
        setEditor(context, BLUETOOTH_DEVICE, new Gson().toJson(device));
    }

    public static BluetoothDevice getBluetoothDevice(Context context) {
        BluetoothDevice device = new Gson().fromJson(getSharedPreferencesObject(context).getString(BLUETOOTH_DEVICE, ""), BluetoothDevice.class);
        return device;
    }
}
