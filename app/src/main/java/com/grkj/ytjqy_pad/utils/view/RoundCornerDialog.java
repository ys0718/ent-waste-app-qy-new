package com.grkj.ytjqy_pad.utils.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;

/**
 * Created by yusam on 2022/3/7.
 * 自定义的带圆角的对话框
 */

public class RoundCornerDialog extends AlertDialog implements View.OnClickListener {

    //当前正在展示的实例，用于单例去重
    private static RoundCornerDialog showingDialog;

    private TextView tvTitle;
    private TextView tvDes;
    private TextView tvCancel;
    private LinearLayout llCancel;
    private TextView tvConfirm;
    private LinearLayout llConfirm;
    private View view_margin;
    private ImageView iv_logo;
    //private Context context;


    private String title = "温馨提示", message, confirmText = "确定", cancelText = "取消";

    public static final int TYPE_TIPS = 0;
    public static final int TYPE_SAVE = 1;
    public static final int TYPE_DELETE = 2;
    private int dialogType = TYPE_SAVE;
    //默认的标题栏背景色
    private int titleColorBg = Color.parseColor("#FF8200");
    //默认的确定和取消按钮背景色
    private int confirmColorBg = Color.parseColor("#F8F8F8");
    private int cancelColorBg = Color.parseColor("#F8F8F8");

    /**
     * 一个参数的构造方法
     *
     * @param context 上下文对象
     */
    public RoundCornerDialog(@NonNull Context context) {
        super(context);
        //this.context=context;
    }

    @Override
    public void show() {
        //已有提示框正在展示时，不重复展示
        if (showingDialog != null && showingDialog.isShowing()) {
            return;
        }
        super.show();
        showingDialog = this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (showingDialog == this) {
            showingDialog = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_test);
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

        switch (dialogType){
            case TYPE_TIPS:
                cancelText = "确定";
                llConfirm.setVisibility(View.GONE);
                view_margin.setVisibility(View.GONE);
                iv_logo.setImageResource(R.mipmap.ic_error_tips);
                break;
            case TYPE_SAVE:
                break;
            case TYPE_DELETE:
                iv_logo.setImageResource(R.mipmap.ic_error_tips);
                break;
        }


        /********************通用设置*********************/
        //设置标题、描述及确定按钮的文本内容
        tvTitle.setText(title);
        tvDes.setText(message);
        tvConfirm.setText(confirmText);
        tvCancel.setText(cancelText);



//        //设置标题栏及确定、取消按钮背景色
//        tvTitle.setBackgroundColor(titleColorBg);
//        tvConfirm.setBackgroundColor(confirmColorBg);
//        tvCancel.setBackgroundColor(cancelColorBg);

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


}
