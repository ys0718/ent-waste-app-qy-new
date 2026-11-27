package com.grkj.ytjqy_pad.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.utils.common.DecimalDigitsInputFilter;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.socks.library.KLog;

import java.math.BigDecimal;

public class AddNumberView extends LinearLayout {

    private EditText tv_num;
    private boolean isInteger = true;
    private int maxNum = 999;
    private int minNum = 1;
    private Context mContext;
    private boolean isEnabled = false;

    private LinearLayout mLayout;

    public AddNumberView(Context context) {
        super(context);
        this.mContext = context;
        init(mContext);
    }

    public AddNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(mContext);
    }

    public AddNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(mContext);
    }

    long startTime = 0;
    long endTime = 0;

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_number, this, true);
        ImageView iv_divide = view.findViewById(R.id.iv_left);
        ImageView iv_add = view.findViewById(R.id.iv_right);
        mLayout = findViewById(R.id.mLayout);
        tv_num = view.findViewById(R.id.tv_num);
        tv_num.setText(minNum + "");
        isInteger = true;
        result = Double.valueOf(tv_num.getText().toString());

        tv_num.setSelection(tv_num.getText().toString().length());
        tv_num.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(!isEnabled){
            iv_add.setEnabled(false);
            tv_num.setEnabled(isEnabled);
            iv_divide.setEnabled(false);

            Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
            int ori = mConfiguration.orientation; //获取屏幕方向

            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                mLayout.setBackground(mContext.getDrawable(R.mipmap.bg_edittext_disable));
            } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
                mLayout.setBackground(mContext.getDrawable(R.drawable.bg_edittext_disable));
            }
        }

        iv_add.setEnabled(isEnabled);
        tv_num.setEnabled(isEnabled);
        iv_divide.setEnabled(isEnabled);


        iv_divide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                divideNum();
            }
        });


        iv_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                if (result < maxNum) {
                    result++;
                    setText();
                } else {
                    ToastUtil.showToast(mContext, "该值最大限定为" + maxNum);
                }
            }
        });



        iv_divide.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startTime = System.currentTimeMillis();
                }
                endTime = System.currentTimeMillis() - startTime;
                if (endTime>1500){
                    divideNum();
                }
                return false;
            }
        });

        iv_add.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startTime = System.currentTimeMillis();
                }
                endTime = System.currentTimeMillis() - startTime;
                if (endTime>1500){
                    addNum();
                }

                return false;
            }
        });


        tv_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1)) {
                    result = minNum;
                } else {
                    result = Double.valueOf(s1);

                    if (result > maxNum) {
                        result = maxNum;
                        setText();
                        ToastUtil.showToast(mContext, "该值最大限定为" + maxNum);
                    }

                }

                if (onTextChangedListener != null) {
                    onTextChangedListener.afterTextChanged(result);
                }

            }
        });
    }

    //自增
    private void addNum(){
        clearFocus();
        if (result < maxNum) {
            result++;
            setText();
        } else {
            ToastUtil.showToast(mContext, "该值最大限定为" + maxNum);
        }
    }

    //自减
    private void divideNum(){
        clearFocus();
        if (result > minNum) {
            result--;
            setText();
        } else {
            ToastUtil.showToast(mContext, "该值最小限定为" + minNum);
        }
    }


    public interface OnTextChangedListener {
        void afterTextChanged(double result);
    }

    private OnTextChangedListener onTextChangedListener;


    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }


    private double result = minNum;

    public double getResult() {
        return result;
    }

    public String getResultString() {
        return String.valueOf(result);
    }

    public void setResult(double result) {
        this.result = result;
//        tv_num.setText(result + "");
        setText();
    }

    public void setEnable(boolean isEnable) {
        tv_num.setEnabled(isEnable);
        isEnabled = isEnable;
        init(mContext);
    }


    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
        this.result = minNum;
        setText();
    }

    public void setInputType() {
        isInteger = false;
        tv_num.setLongClickable(false); //禁用粘贴
        tv_num.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_num.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});//限定两位小数
    }


    private void setText() {
        tv_num.setText(isInteger ? (int) this.result + "" : this.result + "");
    }

    public void setResult(String result) {
        if (TextUtils.isEmpty(result)) {
            this.result = minNum;
        } else {
            this.result = new BigDecimal(result).doubleValue();
        }

        setText();
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:

                    tv_num.setText(result + ""); //将改变的数值显示到TextView上

                    break;

                default:

            }

        }

    };


}