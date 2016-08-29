package com.hhdb.hellostudio.constants;

import com.hhdb.hellostudio.config.AppConfig;

public class ServerInterface {

    /**
     * 服务器地址
     */
    public static final String APP_SERVER_ADDR;

    static {
        if (AppConfig.DEBUG){
            APP_SERVER_ADDR = "http://www.ychhhdb.com/?";
        }else{
            APP_SERVER_ADDR = "http://www.ychhhdb.com/?";
        }
    }

    /**
     * 首页轮播广告
     */
    public final static String LoadAds = APP_SERVER_ADDR + "/app/mobile/slides";

    /**
     * 登录
     */
    public final static String LOGIN = "http://wvapi.study88.com/mainapi/" + "901";
    /**
     * 获取7牛Token
     */
    public final static String QINIU_TOKEN = "http://wvapi.study88.com/mainapi/" + "904";
    /**
     * 检测是否登录
     */
    public final static String CHECK_LOGIN = "http://wvapi.study88.com/mainapi/" + "907";

}
