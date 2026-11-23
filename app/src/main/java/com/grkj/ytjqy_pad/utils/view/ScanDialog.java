package com.grkj.ytjqy_pad.utils.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.socks.library.KLog;

/**
 * Created by yusam on 2022/3/7.
 * 自定义的带圆角的对话框
 */

public class ScanDialog extends AlertDialog implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvDes;
    private TextView tvCancel;
    private LinearLayout llCancel;
    private TextView tvConfirm;
    private LinearLayout llConfirm;
    private View view_margin;
    private ImageView iv_logo;
    //private Context context;

    private EditText et_scan;

    //重置Edittext
    public void resetEtScan() {
        et_scan.setText("");
    }

    private String title = "温馨提示", message, confirmText = "确定", cancelText = "取消";

    public static final int TYPE_SCAN = 0;
    public static final int TYPE_RFID = 1;
    private int dialogType = TYPE_SCAN;
    //默认的标题栏背景色
    private int titleColorBg = Color.parseColor("#FF8200");
    //默认的确定和取消按钮背景色
    private int confirmColorBg = Color.parseColor("#F8F8F8");
    private int cancelColorBg = Color.parseColor("#F8F8F8");

    boolean isJyTz = false;
    boolean isLandscape = false;

    /**
     * 一个参数的构造方法
     *
     * @param context 上下文对象
     */
    public ScanDialog(@NonNull Context context, boolean isJyTz) {
        super(context);
        this.isJyTz = isJyTz;
        //this.context=context;
    }

    public ScanDialog(@NonNull Context context) {
        super(context);
        //this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_scan);
        //设置背景透明，不然会出现白色直角问题
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        //初始化布局控件
        initView();
        //确定和取消按钮的事件监听
        initEvent();
        //设置参数必须在show之后，不然没有效果
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.TOP | Gravity.START; // 例如，底部对齐结束边缘
        params.x = 100; // X轴偏移量
        params.y = 100; // Y轴偏移量
        window.setAttributes(params);

        getWindow().setAttributes(params);
    }


    /**
     * 初始化布局文件及设置参数
     */
    private void initView() {
        //对话框标题
        tvTitle = findViewById(R.id.tv_title);
        //对话框描述信息
        tvDes = findViewById(R.id.tv_des);
        //确定按钮和取消
        tvConfirm = findViewById(R.id.tv_confirm);
        tvCancel = findViewById(R.id.tv_cancel);
        llConfirm = findViewById(R.id.ll_confirm);
        llCancel = findViewById(R.id.ll_cancel);
        view_margin = findViewById(R.id.view_margin);
        iv_logo = findViewById(R.id.iv_logo);
        et_scan = findViewById(R.id.et_scan);

        et_scan.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        scan(et_scan);

        Configuration mConfiguration = App.getContext().getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            isLandscape = true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            isLandscape = false;
        }

        switch (dialogType){
            case TYPE_SCAN:
                iv_logo.setImageResource(R.mipmap.bg_scaning);
                if (isLandscape){
                    tvDes.setText("请使用扫码枪并对准二维码");
                }else {
                    tvDes.setText("请长按设备侧边按钮并对准二维码");
                }
                break;
            case TYPE_RFID:
                iv_logo.setImageResource(R.mipmap.ic_card_login);
                if (isLandscape){
                    tvDes.setText("请使用读卡器扫描Rfid卡片");
                }else {
                    tvDes.setText("请将Rfid卡片靠近手持终端背面");
                }
                break;
        }


        /********************通用设置*********************/
        //设置标题、描述及确定按钮的文本内容
//        tvTitle.setText(title);
//        tvDes.setText(message);
//        tvConfirm.setText(confirmText);
//        tvCancel.setText(cancelText);

    }

    /**
     * 确定及取消点击事件
     */
    private void initEvent() {
        llConfirm.setOnClickListener(this);//确定
        llCancel.setOnClickListener(this);//取消@Override
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_confirm:
                /************通用设置***********/
                //点击确定按钮回调
                if (confirmListener != null) {
                    confirmListener.onConfirmClick();
                } else {
                    dismiss();
                }
                break;
            case R.id.ll_cancel:
                dismiss();
                //点击取消按钮回调
                if (confirmListener != null) {
                    confirmListener.onCancelClick();
                }
                break;
        }
    }


    /**
     * 设置标题栏文本
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 设置描述信息
     *
     * @param message 描述信息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置确定按钮上的文本
     *
     * @param confirmText 文本
     */
    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    /**
     * 设置取消按钮上的文本
     *
     * @param cancelText 文本
     */
    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    /**
     * 设置标题栏的背景
     *
     * @param titleColorBg 背景色int
     */
    public void setTitleBg(int titleColorBg) {
        this.titleColorBg = titleColorBg;
    }

    /**
     * 确定按钮背景色
     *
     * @param confirmColorBg int背景色
     */
    public void setConfirmBg(int confirmColorBg) {
        this.confirmColorBg = confirmColorBg;
    }

    /**
     * 取消按钮背景色
     *
     * @param cancelColorBg int背景色
     */
    public void setCancelBg(int cancelColorBg) {
        this.cancelColorBg = cancelColorBg;
    }


    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    private ConfirmListener confirmListener;

    /**
     * 设置确定按钮的监听
     *
     * @param confirmListener
     */
    public void setConfirmListener(ConfirmListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    /**
     * 确定按钮点击的监听接口
     */
    public interface ConfirmListener {
        void onConfirmClick();

        void onCancelClick();
    }

    /**
     * 显示的/隐藏的 EditText 获得光标，准备扫码
     */
    public void scan(final EditText editText) {
        //获得光标
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(false);
        editText.requestFocus();
        //关闭软键盘：防止顺序乱码
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(
                editText == null ? null : editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        //隐藏软键盘
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //增加软键盘监听，扫出来内容会自己填充到editText中去的
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                KLog.e("进来了");

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    KLog.e("进来了2" + editText.getText().toString().trim());
                    if (onScanResultCallBack != null) {
                        onScanResultCallBack.OnScanSuccess(getQrcode(editText.getText().toString().trim()));//返回结果值，看需要使用了
                        editText.setText("");
                        dismiss();
                    }
                    return true;
                }

                return false;
            }
        });
    }


    //判断是否包含"&b="  测试链接：http://103.203.219.137:20002/main/view/storage/batchInfo.html?v=1&b=91510800MA62R8G7863210264820230720004
    private String getQrcode(String scanResult) {
        String newResult = scanResult;
        if (!TextUtils.isEmpty(newResult)) {
            if (newResult.contains("b=")) {
                newResult = newResult.split("b=")[1];
            } else if (newResult.contains("ewmwb") && isJyTz) {
//            } else if (newResult.contains("ewmwb")) {
                CommonProductInfoBean bean = GsonUtil.GsonToBean(scanResult, CommonProductInfoBean.class);
                newResult = bean.ewmwb;
            }
        }
        KLog.e("newResult:" + newResult);
        return newResult;
    }


    //记录用户首次扫码的时间
    private long mFirstTime = 0;


    /**
     * 扫码枪接口：有timeout设置，当然光标移除的时候关闭计时
     * <p>
     * 成功 返回扫码结果
     * <p>
     * 失败 返回错误信息
     */
    public interface OnScanResultCallBack {

        public void OnScanSuccess(String result);

        public void OnScanFail(String errorMsg);
    }

    private OnScanResultCallBack onScanResultCallBack = new OnScanResultCallBack() {
        @Override
        public void OnScanSuccess(String result) {
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
