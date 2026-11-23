package com.grkj.ytjqy_pad.mvp.view.activity.czdw;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.presenter.InputScanPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductInputInfoActivity;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 扫码入库
 */
public class InputScanActivity extends BaseActivity<InputScanPresenter> implements InputScanPresenter.View {
    @BindView(R.id.et_scan)
    EditText et_scan;

    @Override
    protected int getLayout() {
        return R.layout.activity_output_scan;
    }

    @Override
    protected void initEventAndData() {
        et_scan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        scan(et_scan);
        setOnScanResultCallBack(new OnScanResultCallBack() {
            @Override
            public void OnScanSucccess(String result) {
                mPresenter.getScanInfo(result);
            }

            @Override
            public void OnScanFail(String errorMsg) {
                
            }
        });

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("正在扫描...");
    }

    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected InputScanPresenter createPresenter() {
        return new InputScanPresenter();
    }

    @Override
    public void showError(String msg) {
        et_scan.setText("");
        toastShow(msg);
    }

    @Override
    public void getScanInfoSuccess(String bean) {

        if (!TextUtils.isEmpty(bean)) {
            finish();
            startActivity(new Intent(mContext, ProductInputInfoActivity.class)
                    .putExtra("IS_SCAN", true)
                    .putExtra(Constants.JUMP_KEY, bean));
        } else {
            ToastUtil.showToast(mContext, "请扫描正确的二维码！");
        }
    }

    /**
     * 显示的/隐藏的 EditText 获得光标，准备扫码
     */
    public void scan(final EditText editText) {
        //获得光标
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(false);
//        editText.requestFocus();
        //关闭软键盘：防止顺序乱码
        InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(
                editText == null ? null : editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        //隐藏软键盘
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //增加软键盘监听，扫出来内容会自己填充到editText中去的
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onScanResultCallBack != null) {
                        onScanResultCallBack.OnScanSucccess(editText.getText().toString().trim());//返回结果值，看需要使用了
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 扫码枪接口：有timeout设置，当然光标移除的时候关闭计时
     * <p>
     * 成功 返回扫码结果
     * <p>
     * 失败 返回错误信息
     */
    public interface OnScanResultCallBack {

        public void OnScanSucccess(String result);

        public void OnScanFail(String errorMsg);
    }

    private OnScanResultCallBack onScanResultCallBack = new OnScanResultCallBack() {
        @Override
        public void OnScanSucccess(String result) {
        }

        @Override
        public void OnScanFail(String errorMsg) {
        }
    };

    /**
     * 子类实现回调函数
     */
    public void setOnScanResultCallBack(OnScanResultCallBack onScanResultCallBack) {
        if (onScanResultCallBack == null)
            throw new IllegalArgumentException("empty onScanResultCallBack");
        this.onScanResultCallBack = onScanResultCallBack;
    }

}
