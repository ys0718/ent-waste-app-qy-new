package com.grkj.ytjqy_pad.utils.view;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grkj.module_hardware.weight.UsbWeighService;
import com.grkj.module_hardware.weight.WeightUtils2;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.view.activity.TareListActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.socks.library.KLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by yusam on 2022/3/7.
 * 自定义的带圆角的对话框
 */

public class GetWeightDialog extends AlertDialog implements View.OnClickListener {

    private LinearLayout llCancel;
    private LinearLayout llConfirm;
    private TextView tv_weight1;
    private TextView tv_weight2;
    private EditText tv_weight3;
    private Context mContext;
    private LinearLayout ll_qp;
    private LinearLayout ll_select_blt;

    /**
     * 一个参数的构造方法
     *
     * @param context 上下文对象
     */
    public GetWeightDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_get_weight);
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
        startWeight();

        Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            params.width = (int) (1800 * mContext.getResources().getDisplayMetrics().density);
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            params.width = (int) (360 * mContext.getResources().getDisplayMetrics().density);
        }
        params.flags = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        window.setAttributes(params);


    }


    @Override
    public void show() {
        super.show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }


    /**
     * 初始化布局文件及设置参数
     */
    private void initView() {
        //确定按钮和取消
        llConfirm = findViewById(R.id.ll_confirm);
        llCancel = findViewById(R.id.ll_cancel);
        tv_weight1 = findViewById(R.id.tv_weight1);
        tv_weight2 = findViewById(R.id.tv_weight2);
        tv_weight3 = findViewById(R.id.tv_weight3);
        ll_qp = findViewById(R.id.ll_qp);
        ll_select_blt = findViewById(R.id.ll_select_blt);
        registerRxBus();
    }

    /**
     * 确定及取消点击事件
     */
    private void initEvent() {
        llConfirm.setOnClickListener(this);//确定
        llCancel.setOnClickListener(this);//取消@Override
        ll_qp.setOnClickListener(this);

        //选择蓝牙称
        findViewById(R.id.ll_select_blt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDevice_Click();
            }
        });
        //清零
        findViewById(R.id.ll_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usbService != null) {
                    tv_weight3.setText("0.000");
                    isClear = true;
                    usbService.write(orderZero());
                }
            }
        });
//        tv_weight3.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                showSoftKeyboard();
//                return false;
//            }
//        });
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tv_weight3, InputMethodManager.SHOW_FORCED);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_confirm:
                stopWeight();
                /************通用设置***********/
                String weightStr = tv_weight1.getText().toString();
                if (TextUtils.isEmpty(weightStr) || Double.valueOf(weightStr) == 0 || Double.valueOf(weightStr) <= 0) {
                    ToastUtil.showToast(mContext, "请先获取正确的重量！");
                    return;
                }
                //点击确定按钮回调
                if (confirmListener != null) {
                    confirmListener.onConfirmClick(weightStr);
                }
                dismiss();
                break;
            case R.id.ll_cancel:
                stopWeight();
                //点击取消按钮回调
                if (confirmListener != null) {
                    confirmListener.onCancelClick();
                }

                unregister();
                dismiss();
                break;


            case R.id.ll_qp:
                mContext.startActivity(new Intent(mContext, TareListActivity.class));
                break;

        }
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
        void onConfirmClick(String weight);

        void onCancelClick();
    }


    private Subscription rxSubscription;        //    RxBus

    /**
     * 订阅
     */
    private void registerRxBus() {
        rxSubscription = RxBus.getDefault().toObservable(ChangeEvent.class).subscribe(new Action1<ChangeEvent>() {
            @Override
            public void call(ChangeEvent changeEvent) {
                try {
                    if (!TextUtils.isEmpty(changeEvent.getMsg())) {
                        tv_weight3.setText(changeEvent.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 取消订阅
     */
    private void unregister() {
        if (!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }


    /************************************************************称重相关************************************************************/

    protected List<ParamListBean> allParamList = new ArrayList<>();
    private BluetoothDevice _device = null;
    protected UsbWeighService usbService;

    //是否点击清除
    protected boolean isClear;



    //选择蓝牙秤设备
    protected void btnDevice_Click() {

        final BltSelectDialog _deviceDialog = new BltSelectDialog(mContext);
        _deviceDialog.onConfirmListener = new BltSelectDialog.OnConfirmListener() {
            @Override
            public void confirm(BluetoothDevice device) {
                _device = device;
                KLog.e(GsonUtil.GsonToString(_device));
                SPUtils.put(mContext, Constants.BLT_CONNECTED_ADDRESS, device.getAddress());
                SPUtils.put(mContext, "blt_connected_device", GsonUtil.GsonToString(device));

                PermissionUtils.requestBluetoothPermission(mContext, new PermissionUtils.OnBooleanListener() {
                    @Override
                    public void onSuccess() {
                        mWeightUtils.btnConnect_Click(_device);
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                _deviceDialog.dismiss();
            }
        };
        _deviceDialog.show();
    }


    private WeightUtils2 mWeightUtils = null;

    protected void startWeight() {
        mWeightUtils = WeightUtils2.getInstance();
        KLog.e("进来了");
        mWeightUtils.initView(App.getContext(), tv_weight1, tv_weight2, tv_weight3, tv_weight1, getJldw());
        mWeightUtils.setJldwInfo(getJldw());
        mWeightUtils.setGetWeight(true);
        mWeightUtils.setWeight(true);
        mWeightUtils.setNew(true);
        mWeightUtils.setClear(isClear);

        boolean isLandscape = false;
        Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            isLandscape = true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            isLandscape = false;
        }

        if (isLandscape) {
            if (usbhandler == null) {
                usbhandler = new UsbHandler(this);
            }
            mWeightUtils.initUsbWeight(usbhandler);
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            PermissionUtils.requestBluetoothPermission(mContext, new PermissionUtils.OnBooleanListener() {
                @Override
                public void onSuccess() {
                    mWeightUtils.initBltWeight();
                }

                @Override
                public void onCancel() {

                }
            });
        }

    }

    protected void stopWeight() {
        mWeightUtils.stopWeight();
    }

    private UsbHandler usbhandler;

    private class UsbHandler extends Handler {
        private final WeakReference<GetWeightDialog> mWeakReference;

        public UsbHandler(GetWeightDialog activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GetWeightDialog mActivity = mWeakReference.get();
            if (mActivity == null || !mActivity.isShowing()) return;
            switch (msg.what) {
                case UsbWeighService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    KLog.e("NotReverseData " + data);
                    KLog.e("devicePID " + mWeightUtils.devicePID);
                    KLog.e("NEW_USB_PID " + UsbWeighService.NEW_USB_PID);
                    if (data.contains("WS")) {
                        mWeightUtils.setUsbWeightView(data);
                    } else {
                        String reverseData = mWeightUtils.reverseString(data);
                        mWeightUtils.setBltWeightView(reverseData);
                    }

                    break;
                case UsbWeighService.CTS_CHANGE:
                    Toast.makeText(mContext, "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbWeighService.DSR_CHANGE:
                    Toast.makeText(mContext, "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    /**
     * 将秤盘上的重量置为零重基准
     */
    protected byte[] orderZero() {
        return mWeightUtils.orderZero();
    }

    protected String getJldw() {
        return SPUtils.get(mContext, "JLDW", "吨").toString();
    }


}
