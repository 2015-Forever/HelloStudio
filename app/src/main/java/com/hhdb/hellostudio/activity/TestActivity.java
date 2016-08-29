package com.hhdb.hellostudio.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhdb.hellostudio.R;
import com.hhdb.hellostudio.config.AppConfig;
import com.hhdb.hellostudio.constants.ServerInterface;
import com.hhdb.hellostudio.modle.FocusAdBean;
import com.hhdb.hellostudio.net.HttpListener;
import com.hhdb.hellostudio.utils.ToastUtil;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.cookie.DiskCookieStore;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/25.
 */
public class TestActivity extends BaseActivity {

    private List<FocusAdBean> mFocusAdData;

    @Override
    public int getContentViewId() {
        return R.layout.activity_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @OnClick(R.id.load_btn) void loadHttp() {
        Request<String> request = NoHttp.createStringRequest(ServerInterface.LoadAds, RequestMethod.POST);
        request(0, request, httpListener, false, true);
    }

    @OnClick(R.id.login_btn) void login() {
        Request<String> request = NoHttp.createStringRequest(ServerInterface.LOGIN, RequestMethod.POST);
        String ts = getTS();
        String mm = getMM("901", ts);
        request.add("ts", ts);
        request.add("mm", mm);
        request.add("a", "oobRnw5dj8l9Jj1fQzMw3L61f_VQ");
        request(1, request, loginListener, false, true);
    }

    @OnClick(R.id.get_qiniu_token_btn) void getToken() {
        Request<String> request = NoHttp.createStringRequest(ServerInterface.QINIU_TOKEN, RequestMethod.POST);
        String ts = getTS();
        String mm = getMM("904", ts);
        request.add("ts", ts);
        request.add("mm", mm);
        request(2, request, httpListener, false, true);
    }

    @OnClick(R.id.check_login_btn) void checkLogin() {
        Request<String> request = NoHttp.createStringRequest(ServerInterface.CHECK_LOGIN, RequestMethod.POST);
        String ts = getTS();
        String mm = getMM("907", ts);
        request.add("ts", ts);
        request.add("mm", mm);
        request(3, request, httpListener, false, true);
    }

    @OnClick(R.id.login_out_btn) void loginOut() {
        CookieStore cookieStore = NoHttp.getDefaultCookieManager().getCookieStore();

//        List<HttpCookie> cookies = cookieStore.getCookies();
//        List<HttpCookie> loginCookies = AppConfig.getInstance().getLoginCookies(); //登录返回的
//
//        for (HttpCookie cookie : cookies) {
//            Logger.d("所有：" + cookie);
//        }
//        for (HttpCookie cookie : loginCookies) {
//            Logger.d("登录：" + cookie);
//        }
//        //我只想删除登录的cookies，为什么不行呢？
//        if (cookies.removeAll(loginCookies)) {
//            ToastUtil.getInstance().show("退出成功");
//        }else {
//            ToastUtil.getInstance().show("退出失败");
//        }
        //直接删除全部是可以。
        if (cookieStore.removeAll()) {
            ToastUtil.getInstance().show("退出成功");
        } else {
            ToastUtil.getInstance().show("退出失败");
        }
    }

    public static String getTS() {
        return String.valueOf(System.currentTimeMillis() * 10000);
    }

    public static String getMM(String method, String ts) {
        String mm = method + "sd8H02lsd3" + ts;
        return MD5(mm);
    }

    public static String MD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private HttpListener<String> loginListener = new HttpListener<String>() {
        @Override public void onSucceed(int what, Response<String> response) {
            //HttpCookie
            List<HttpCookie> loginCookies = response.getHeaders().getCookies();
            Logger.d("Cookies: " + response.getHeaders().getCookies());
            AppConfig.getInstance().setLoginCookies(loginCookies);
            Logger.d(response.get());
        }

        @Override public void onFailed(int what, Response<String> response) {
            showMessageDialog(R.string.request_failed, response.getException().getMessage());
        }
    };

    private HttpListener<String> httpListener = new HttpListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            switch (what) {
                case 0:
                    int responseCode = response.getHeaders().getResponseCode();
                    if (responseCode == 200) {
                        Logger.d(response.get());
                        try {
                            JSONObject jsonObject = new JSONObject(response.get());
                            String data = jsonObject.optString("data");
                            Type typeOfT = new TypeToken<List<FocusAdBean>>() {
                            }.getType();
                            mFocusAdData = new Gson().fromJson(data, typeOfT);
                            for (FocusAdBean bean : mFocusAdData) {
                                Logger.d(bean.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    Logger.d(response.get());
                    break;
            }
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            showMessageDialog(R.string.request_failed, response.getException().getMessage());
        }
    };
}
