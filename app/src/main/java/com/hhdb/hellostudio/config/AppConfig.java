package com.hhdb.hellostudio.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.hhdb.hellostudio.Application;
import com.hhdb.hellostudio.utils.FileUtil;

import java.io.File;
import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class AppConfig {

    private static AppConfig appConfig;

    private SharedPreferences preferences;

    /**
     * 是否测试环境.
     */
    public static final boolean DEBUG = true;

    /**
     * App根目录.
     */
    public String APP_PATH_ROOT;

    public List<HttpCookie> loginCookies;

    private AppConfig() {
        preferences = Application.getInstance().getSharedPreferences("HelloStudio", Context.MODE_PRIVATE);
        APP_PATH_ROOT = FileUtil.getRootPath().getAbsolutePath() + File.separator + "HelloStudio";
        FileUtil.initDirectory(APP_PATH_ROOT);
    }

    public static AppConfig getInstance() {
        if (appConfig == null) {
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    public List<HttpCookie> getLoginCookies() {
        return loginCookies;
    }

    public void setLoginCookies(List<HttpCookie> loginCookies) {
        this.loginCookies = loginCookies;
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public Long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    public Float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

}
