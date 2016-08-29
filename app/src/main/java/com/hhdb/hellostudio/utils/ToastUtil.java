package com.hhdb.hellostudio.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtil {

    public static volatile ToastUtil mUtil = null;
    private static Toast mToast = null;
    private static Context mContext;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        if (mUtil == null) {
            synchronized (ToastUtil.class) {
                if (mUtil == null) {
                    mUtil = new ToastUtil();
                }
            }
        }
        return mUtil;
    }

    public static void init(Context context) {
        mContext = context;
    }

    public void show(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void show(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

}
