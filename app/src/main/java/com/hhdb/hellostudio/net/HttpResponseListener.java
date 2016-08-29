package com.hhdb.hellostudio.net;

import android.content.DialogInterface;

import com.hhdb.hellostudio.R;
import com.hhdb.hellostudio.activity.BaseActivity;
import com.hhdb.hellostudio.dialog.WaitDialog;
import com.hhdb.hellostudio.utils.ToastUtil;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ParseError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.net.ProtocolException;
import java.util.Locale;

public class HttpResponseListener<T> implements OnResponseListener {

    private BaseActivity mActivity;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<?> callback;

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(BaseActivity activity, Request<?> request, HttpListener<T> httpCallback,
                                boolean canCancel, boolean isLoading) {
        this.mActivity = activity;
        this.mRequest = request;
        if (activity != null && isLoading) {
            mWaitDialog = new WaitDialog(mActivity);
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRequest.cancel(); // 取消请求
                }
            });
        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求，这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing()) {
            mWaitDialog.show();
        }
    }

    /**
     * 结束请求，这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 成功回调
     */
    @Override
    public void onSucceed(int what, Response response) {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400 && mActivity != null) {
            if (responseCode == 405) { //405表示服务器不支持这种请求方法，比如GET、POST、TRACE中的TRACE就很少有服务器支持。
                mActivity.showMessageDialog(R.string.request_succeed, R.string.request_method_not_allow);
            } else {
                String title = String.format(Locale.getDefault(), mActivity.getString(R.string.error_response_code), responseCode);
                String content = String.format(Locale.getDefault(), mActivity.getString(R.string.error_response_code_dex), responseCode);
                mActivity.showMessageDialog(title, content);
            }
        }
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    @Override
    public void onFailed(int what, Response response) {
        Exception exception = response.getException();
        if (exception instanceof NetworkError) { //网络不好
            ToastUtil.getInstance().show(R.string.error_please_check_network);
        } else if (exception instanceof TimeoutError) { //请求超时
            ToastUtil.getInstance().show(R.string.error_timeout);
        } else if (exception instanceof UnKnownHostError) { //找不到服务器
            ToastUtil.getInstance().show(R.string.error_not_found_server);
        } else if (exception instanceof URLError) { //URL错误
            ToastUtil.getInstance().show(R.string.error_url_error);
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅查找缓存时没有找到缓存时返回
            ToastUtil.getInstance().show(R.string.error_not_found_cache);
        } else if (exception instanceof ProtocolException) {
            ToastUtil.getInstance().show(R.string.error_system_unsupport_method);
        } else if (exception instanceof ParseError) { //解析出错
            ToastUtil.getInstance().show(R.string.error_parse_data);
        } else {
            ToastUtil.getInstance().show(R.string.error_unknown);
        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null){
            callback.onFailed(what, response);
        }
    }
}
