package com.hhdb.hellostudio;

import com.hhdb.hellostudio.config.AppConfig;
import com.hhdb.hellostudio.utils.ToastUtil;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by Administrator on 2016/8/25.
 */
public class Application extends android.app.Application {

    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;

        NoHttp.initialize(this);

        ToastUtil.init(getApplicationContext());
        Logger.setTag("HelloStudio");
        Logger.setDebug(AppConfig.DEBUG);

    }

    public static Application getInstance(){
        return _instance;
    }
}
