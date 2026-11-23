package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.base.BasePresenter;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.mvp.model.BaseBean;
import com.grkj.ytjqy_pad.utils.common.ScanGunKeyEventHelper;
import com.grkj.ytjqy_pad.utils.common.ScanKeyManager;
import com.grkj.ytjqy_pad.utils.view.GrRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by yusam on 2022/3/21.
 * 批量扫码基类
 */

public abstract class ScanBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView{

    @BindView(R.id.iv_select_all)
    protected ImageView iv_select_all;
    protected ScanKeyManager scanKeyManager;

    //已扫描列表
    @BindView(R.id.mRecyclerView)
    protected GrRecyclerView mRecyclerView;

    protected boolean isSelectAll = true;

    protected T mPresenter;

    protected abstract T createPresenter();

    protected abstract void showScanDialog(long delayTime);

    private ScanGunKeyEventHelper mScanGunKeyEventHelper;

    private boolean canShowDialog = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
//        onKeyBoardListener();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();

        canShowDialog = true;
        //拦截扫码器回调,获取扫码内容
        scanKeyManager = new ScanKeyManager(new ScanKeyManager.OnScanValueListener() {
            @Override
            public void onScanValue(String value) {
                hideSoftKeyboard();
                if (canShowDialog && !isLandscape){
                    canShowDialog = false;
                    showLoading();
                    showScanDialog(3000);
                    Log.e("ScanValue", value);
                }
            }
        });

    }

    public void hideSoftKeyboard() {
        // 步骤一：获取当前焦点的View
        View currentFocus = this.getCurrentFocus();

        // 步骤二：创建InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (currentFocus!=null){
            // 步骤三：关闭软键盘
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }

        //隐藏软键盘
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        hideLoading();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        canShowDialog = true;
                    }
                });
            }
        },3000);

    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (scanKeyManager!=null){
            scanKeyManager = null;
        }

    }


    protected <T extends BaseBean> void checkIsAll(List<T> newList){
        isSelectAll = true;
        if (newList != null && newList.size() != 0) {
            for (int j = 0; j < newList.size(); j++) {
                if (!newList.get(j).SELECT) {
                    isSelectAll = false;
                }
            }
        }
        iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
    }


    /*监听键盘事件,除了返回事件都将它拦截,使用我们自定义的拦截器处理该事件*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK && !isLandscape) {
            scanKeyManager.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyBoard(this, v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }



//    //监听软件盘是否弹起
//    private void onKeyBoardListener() {
//
//       new SoftKeyBroadManager.SoftKeyboardStateListener(){
//            @Override
//            public void onSoftKeyboardOpened(int keyboardHeightInPx){
//                KLog.e("弹出");
//                isInput = true;
//            }
//
//            @Override
//            public void onSoftKeyboardClosed(){
//                KLog.e("收起");
//                isInput = false;
//            }
//        };

//        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                Log.e("软键盘", "键盘显示 高度" + height);
//
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                Log.e("软键盘", "键盘隐藏 高度" + height);
//
//            }
//        });
//    }


}
