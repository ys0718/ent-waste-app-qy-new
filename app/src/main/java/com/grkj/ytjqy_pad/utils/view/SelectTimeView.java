package com.grkj.ytjqy_pad.utils.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.DecimalDigitsInputFilter;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;

import java.math.BigDecimal;
import java.util.Date;

public class SelectTimeView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mLayout;//主布局
    private TextView tv_start_time;//开始时间-文字
    private LinearLayout ll_start_time;//开始时间-布局
    private ImageView iv_start_clear;//开始时间-清空按钮
    private TextView tv_end_time;//结束时间-文字
    private LinearLayout ll_end_time;//结束时间-布局
    private ImageView iv_end_clear;//结束时间-清空按钮

    public SelectTimeView(Context context) {
        super(context);
        init(context);
    }

    public SelectTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private TimePickerView timePicker;

    private Activity mActivity;


    /*
    * 获取开始时间
    * */
    public String getStartTime(){
        return  tv_start_time.getText().toString().trim();
    }

    /*
    * 获取结束时间
    * */
    public String getEndTime(){
        return  tv_end_time.getText().toString().trim();
    }

    private static Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    private void init(Context mContext) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_select_time, this, true);
        mLayout = view.findViewById(R.id.mLayout);
        tv_start_time = view.findViewById(R.id.tv_start_time);
        ll_start_time = view.findViewById(R.id.ll_start_time);
        iv_start_clear = view.findViewById(R.id.iv_start_clear);
        tv_end_time = view.findViewById(R.id.tv_end_time);
        ll_end_time = view.findViewById(R.id.ll_end_time);
        iv_end_clear = view.findViewById(R.id.iv_end_clear);
        mActivity = scanForActivity(mContext);

        //选择开始时间
        ll_start_time.setOnClickListener(this);
        //清空开始时间
        iv_start_clear.setOnClickListener(this);

        //选择结束时间
        ll_end_time.setOnClickListener(this);
        //清空结束时间
        iv_end_clear.setOnClickListener(this);

    }

    private Date date_start;
    private Date date_end;

    public boolean isDialog = false;

    public void setDialog(boolean dialog) {
        this.isDialog = dialog;
    }

    public void setOrientation(int orientation){
        mLayout.setOrientation(orientation);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //结束时间
            case R.id.ll_end_time:
                if (!TextUtils.isEmpty(tv_start_time.getText().toString())) {//如果开始时间还未选择，则先弹出选择开始时间
                    timePicker = DateTimeUtil.showDatePicker(mActivity, "请选择结束时间", new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            date_end = date;
                            callbackMethod();
                        }
                    },isDialog);

                    break;
                }
                //开始时间
            case R.id.ll_start_time:
                timePicker = DateTimeUtil.showDatePicker(mActivity, "请选择开始时间", new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        date_start = date;
                        tv_start_time.setText(DateTimeUtil.formatDateTime(date, DateTimeUtil.DF_YYYY_MM_DD));
                        iv_start_clear.setVisibility(VISIBLE);
                        timePicker = DateTimeUtil.showDatePicker(mActivity, "请选择结束时间", new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date2, View v) {
                                date_end = date2;
                                callbackMethod();
                            }
                        },isDialog);
                    }
                },isDialog);
                break;
            //清空开始时间
            case R.id.iv_start_clear:
                tv_start_time.setText("");
                tv_end_time.setText("");
                iv_start_clear.setVisibility(GONE);
                iv_end_clear.setVisibility(GONE);
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.startTimeChanged(tv_start_time.getText().toString(), tv_end_time.getText().toString());
                }
                break;

            //清空结束时间
            case R.id.iv_end_clear:
                tv_end_time.setText("");
                iv_end_clear.setVisibility(GONE);
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.startTimeChanged(tv_start_time.getText().toString(), tv_end_time.getText().toString());
                }
                break;


        }

    }

    private void callbackMethod() {

        if (!DateTimeUtil.compareDate(date_start, date_end)) {
            ToastUtil.showToast(mActivity, "结束时间不能比开始时间早，请选择正确的时间段！");
            tv_end_time.setText("");
            timePicker.show();
        } else {
            iv_end_clear.setVisibility(VISIBLE);
            tv_end_time.setText(DateTimeUtil.formatDateTime(date_end, DateTimeUtil.DF_YYYY_MM_DD));
            if (onTimeChangedListener != null) {
                onTimeChangedListener.startTimeChanged(tv_start_time.getText().toString(), tv_end_time.getText().toString());
            }
        }
    }


    public interface OnTimeChangedListener {
        void startTimeChanged(String startTime, String endTime);
    }

    private OnTimeChangedListener onTimeChangedListener;

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }
}