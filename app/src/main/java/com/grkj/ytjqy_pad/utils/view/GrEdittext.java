package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;

import java.util.ArrayList;
import java.util.List;

/*
 * 可清空下拉框
 * */
public class GrEdittext extends LinearLayout {
    private Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    private int ori = mConfiguration.orientation; //获取屏幕方向
    private LinearLayout mLayout;
    private String hint = "请输入";

    public GrEdittext(Context context) {
        super(context);
        init(context, null);
    }

    public GrEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GrEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private List<Select> datas = new ArrayList<>();
    private AutoCompleteTextView text;
    private boolean enabled;
    private String contentText = "";


    public void setHint(String hint) {
        this.hint = hint;
        text.setHint(this.hint);
    }

    //是否可输入
    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
        text.setCursorVisible(isEnabled);
    }

    private void init(Context mContext, @NonNull AttributeSet attrs) {
        if (attrs == null) {
            init(mContext);
            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CommonAttrs);
        this.hint = array.getString(R.styleable.CommonAttrs_hint);
        this.enabled = array.getBoolean(R.styleable.CommonAttrs_enabled, true);
        array.recycle();
        init(mContext);
    }


    //初始化
    private void init(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_input_select, this, true);
        text = (AutoCompleteTextView) view.findViewById(R.id.repeateMode);
        view.findViewById(R.id.repeateModeBtn).setVisibility(GONE);
        mLayout = view.findViewById(R.id.mLayout);

        //监听布局长度/宽度
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = view.getMeasuredWidth();
                //设置弹窗长度
                text.setDropDownWidth(width);
            }
        });

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            text.setDropDownBackgroundResource(R.mipmap.bg_select);
        } else {
            text.setTextColor(mContext.getResources().getColor(R.color.colorFontB));
            text.setDropDownBackgroundResource(R.drawable.bg_edittext);
        }


        text.setHint(this.hint);
        text.setCursorVisible(enabled);
        ImageView iv_clear = view.findViewById(R.id.iv_clear);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString())) {
                    iv_clear.setVisibility(GONE);
                } else {
                    iv_clear.setVisibility(VISIBLE);
                }

                if (onTextChangedListener != null) {
                    onTextChangedListener.textChange(s.toString());
                }

                contentText = text.getText().toString();
            }
        });


        //清空
        iv_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                contentText = "";
                text.setText("");
                if (onClearListener != null) {
                    onClearListener.onClick();
                }
            }
        });
    }


    public void setText(String text) {
        this.contentText = text;
        this.text.setText(text);
    }

    public String getText() {
        return this.contentText;
    }

    public interface OnClearListener {
        void onClick();
    }

    public interface OnTextChangedListener {
        void textChange(String changeText);
    }

    private OnClearListener onClearListener;

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

    private OnTextChangedListener onTextChangedListener;

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

}