package com.grkj.ytjqy_pad.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.view.activity.LoginActivity;
import com.grkj.ytjqy_pad.utils.common.ActivityUtil;
import com.grkj.ytjqy_pad.utils.common.DialogUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by yusam on 2018/7/19.
 * 无MVP的Fragment基类
 */

public abstract class SimpleFragment extends SupportFragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected Dialog dialog;
    protected String token;

    protected abstract int getLayoutId();

    protected abstract void initEventAndData(View view);

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setClickable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        token = (String) SPUtils.get(mContext, Constants.TOKEN, "");
        initEventAndData(view);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }

    public void toastShow(String text) {
        if ("HTTP 504 Unsatisfiable Request (only-if-cached)".equals(text)) {
            showMessageDialog("网络连接超时，请稍后再试");
        } else if ("未先登录，或token已过期，拒绝访问！".equals(text)) {
            showLogoutDialog(text);
        } else {
            showMessageDialog(text);
        }
    }

    /**
     * 提示窗口
     */
    private void showLogoutDialog(String mMessage) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().exitLiveApp();
                        ActivityUtil.jumpToActivity(getActivity(), LoginActivity.class);
                        getActivity().finish();
                    }
                }, 100);
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 提示窗口
     */
    protected void showMessageDialog(String mMessage) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //设置为只显示内容
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_TIPS);
        //显示对话框
        roundCornerDialog.show();
    }


    /**
     * 显示无文字加载进度条
     */
    protected void dialogShow() {
        dialog = DialogUtils.getInstance().createLoadingDialog(mContext);
    }

    /**
     * 取消加载进度条
     */
    protected void dialogCancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

}
