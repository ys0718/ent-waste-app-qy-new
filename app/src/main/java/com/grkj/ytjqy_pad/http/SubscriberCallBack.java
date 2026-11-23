package com.grkj.ytjqy_pad.http;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.mvp.view.activity.LoginActivity;
import com.grkj.ytjqy_pad.utils.common.DialogUtils;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * 这是请求
 *
 * @anthor yusam
 * created on 2018/7/19
 */
public class SubscriberCallBack extends Subscriber {

    private ApiCallback apiCallback;

    private Context context;

    private Dialog dialog;

    private boolean isShowLoading = true;//默认显示loading

    public static RequestBody requestBodyParam(Map<String, String> map) {
        String postInfoStr = GsonUtil.GsonToString(map);
        KLog.e("请求参数为：" + postInfoStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postInfoStr);
        return body;
    }

    /* *
     * @Description //检测Map空的key删除
     * @Date 17:31 2022/8/17
     * @Param [param]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String, Object> removeNullString(Map<String, Object> param) {
        try {
            Iterator<String> it = param.keySet().iterator();
            while (it.hasNext()) {
                Object ele = it.next();
                if (param.get(ele).toString().trim().equals("") || param.get(ele) == null || param.get(ele) instanceof Boolean) {
                    //hashMap.remove(ele);    //出错 修改了映射结构 影响了迭代器遍历
                    it.remove();              //用迭代器删除 则不会出错
                }
            }
            return param;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static RequestBody requestBodyParamObj(Map<String, Object> map) {
        map = removeNullString(map);
        String postInfoStr = GsonUtil.GsonToString(map);
        KLog.e("请求参数为：" + postInfoStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postInfoStr);
        return body;
    }

    public static RequestBody requestBodyParamObj(Object obj) {
        String postInfoStr = GsonUtil.GsonToString(obj);
        KLog.e("请求参数为：" + postInfoStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postInfoStr);
        return body;
    }


    public SubscriberCallBack(ApiCallback apiCallback, Context context,boolean isShowLoading) {
        this.apiCallback = apiCallback;
        this.context = context;
        this.isShowLoading = isShowLoading;
    }

    public SubscriberCallBack(ApiCallback apiCallback, Context context) {
        this.apiCallback = apiCallback;
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isShowLoading) {
            if (dialog == null) {
                dialog = DialogUtils.getInstance().createLoadingDialog(context);
            }
        }
    }

    @Override
    public void onCompleted() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        KLog.e(e);

        String msg;
        if (e instanceof SocketTimeoutException) {
            msg = "网络连接超时，请稍后再试";
            apiCallback.onFailure(msg);
        } else if (e instanceof ConnectException) {
            msg = "服务器开小差，请稍后再试";
            apiCallback.onFailure(msg);
        } else {

            KLog.e("发送错误:", e.getMessage());
            if (!TextUtils.isEmpty(e.getMessage())) {

                if(e.getMessage().contains("HTTP 401")){
                    RoundCornerDialog roundCornerDialog = new RoundCornerDialog(context);
                    //设置标题，描述，文本等参数
                    roundCornerDialog.setMessage("登录超时,请重新登录！");
                    roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_TIPS);
                    //确定按钮的点击回调方法
                    roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                            roundCornerDialog.dismiss();
                            App.getInstance().exitLiveApp();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        @Override
                        public void onCancelClick() {
                            roundCornerDialog.dismiss();
                            App.getInstance().exitLiveApp();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                    //显示对话框
                    roundCornerDialog.show();
                }else {
                    apiCallback.onFailure(e.getMessage());
                }
            } else {
                apiCallback.onFailure(e.getMessage());
            }

        }
    }

    @Override
    public void onNext(Object t) {
        try {
            apiCallback.onSuccess(t);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
