package com.grkj.ytjqy_pad.utils.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;

/**
 * Created by yusam on 2018/7/19.
 */

public class DialogUtils {

    private DialogUtils(){
        
    }

    public static class DialogUtilsInstance {
        private static final DialogUtils INSTANCE = new DialogUtils();
    }

    public static DialogUtils getInstance() {
        return DialogUtils.DialogUtilsInstance.INSTANCE;
    }
    

    private static Dialog loadingDialog;

    /**
     * 加载中dialog（单例：已存在且正在展示时直接复用，不重复展示）
     */
    public Dialog createLoadingDialog(Context context) {
        //已有加载框且正在展示时，直接返回，避免重复创建展示
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return loadingDialog;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.custom_loading, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        // 创建自定义样式dialog
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);// 不可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout);// 设置布局
        //关闭时清空缓存，避免静态引用泄漏Activity
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadingDialog = null;
            }
        });
        dialog.show();
        loadingDialog = dialog;
        return dialog;
    }

}
