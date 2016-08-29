package com.hhdb.hellostudio.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.hhdb.hellostudio.R;

/**
 * 网络请求默认弹出框.
 */
public class WaitDialog extends ProgressDialog {

    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage(context.getText(R.string.wait_dialog_title));
    }
}
