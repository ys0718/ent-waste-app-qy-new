package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;

/**
 * Created by yusam.
 * <p> 带拦截功能 LinearLayout
 * Date: 2023/8/4
 */
public class MyLinearLayout extends LinearLayout{

    //是否拦截，默认不拦截
    private boolean onInterceptTouchEvent = false;
    private Context mContext;


    public MyLinearLayout(Context context){
        super(context);
        this.mContext = context;
    }

    public MyLinearLayout(Context context,@Nullable @androidx.annotation.Nullable AttributeSet attrs){
        super(context,attrs);
        this.mContext = context;
        initAttrs(attrs);
    }

    public MyLinearLayout(Context context,@Nullable @androidx.annotation.Nullable AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
    }

    public MyLinearLayout(Context context,AttributeSet attrs,int defStyleAttr,int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        this.mContext = context;
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray a = mContext.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyLinearLayoutAttrs,
                0, 0);
        try {
            // 获取自定义属性值
            int bgColor = a.getColor(R.styleable.MyLinearLayoutAttrs_backgroundColor, Color.TRANSPARENT);
            boolean isEnable = a.getBoolean(R.styleable.MyLinearLayoutAttrs_isEnable,false);
            setOnInterceptTouchEvent(isEnable,bgColor);
        } finally {
            a.recycle();
        }
    }


    public void setOnInterceptTouchEvent(boolean onInterceptTouchEvent,@ColorInt int color){
        this.setOnInterceptTouchEvent(onInterceptTouchEvent);

        if (this.onInterceptTouchEvent){
            setBackgroundColor(color);
            getBackground().setAlpha(122);
        }
    }


    public void setOnInterceptTouchEvent(boolean onInterceptTouchEvent){
        this.onInterceptTouchEvent = onInterceptTouchEvent;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return onInterceptTouchEvent;
    }
}
