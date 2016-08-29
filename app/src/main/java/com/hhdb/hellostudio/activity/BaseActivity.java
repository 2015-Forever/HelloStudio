package com.hhdb.hellostudio.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.hhdb.hellostudio.R;
import com.hhdb.hellostudio.net.CallServer;
import com.hhdb.hellostudio.net.HttpListener;
import com.yolanda.nohttp.rest.Request;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/8/25.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Unbinder bind;

    public abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        bind = ButterKnife.bind(this);
        init(savedInstanceState);
    }

    protected abstract void init(Bundle savedInstanceState);

    /**
     * 文字提示dialog
     *
     * @param title   标题
     * @param message 内容
     */
    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }

    /**
     * 文字提示dialog
     *
     * @param title   标题
     * @param message 内容
     */
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }

    /**
     * 文字提示dialog
     *
     * @param title   标题
     * @param message 内容
     */
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    /**
     * 文字提示dialog
     *
     * @param title   标题
     * @param message 内容
     */
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this); //取消标记
        CallServer.getRequestQueue().add(this, what, request, callback, canCancel, isLoading);
    }

    @Override
    protected void onDestroy() {
        CallServer.getRequestQueue().cancelBySign(this);
        super.onDestroy();
        bind.unbind();
    }
}
