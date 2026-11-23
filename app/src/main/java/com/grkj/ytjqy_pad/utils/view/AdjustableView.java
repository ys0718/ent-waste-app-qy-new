package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;


/**
 * Create by seny on 2024/4/12
 * Description: 收起/展开控件
 **/
public class AdjustableView extends LinearLayout{

    private LinearLayout mLayout;
    private TextView tv_title;
    private ImageView iv_click;

    private boolean isOpen;//是否展开

    private static final String OPEN_STR = "收起";
    private static final String CLOSE_STR = "展开";


    public AdjustableView(Context context){
        super(context);
        init(context,null);
    }

    public AdjustableView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public AdjustableView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }


    private void init(Context mContext,@NonNull AttributeSet attrs){
        if (attrs == null) {
            init(mContext);
            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.AdjustableViewAttrs);
        this.isOpen = array.getBoolean(R.styleable.AdjustableViewAttrs_isOpen,false);
        array.recycle();
        init(mContext);
    }


    //初始化
    private void init(Context mContext){
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_adjustable_view,this,true);
        mLayout = view.findViewById(R.id.mLayout);
        tv_title = view.findViewById(R.id.tv_title);
        iv_click = view.findViewById(R.id.iv_click);
        doOnClick();
        mLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                doOnClick();
                if (onClickLayoutListener!=null){
                    onClickLayoutListener.onClick(isOpen);
                }
            }
        });
    }

    private void doOnClick() {
        if (isOpen){
            tv_title.setText(OPEN_STR);
            iv_click.setImageResource(R.mipmap.ic_fold);
        }else {
            tv_title.setText(CLOSE_STR);
            iv_click.setImageResource(R.mipmap.ic_unfold);
        }
    }


    private OnClickLayoutListener onClickLayoutListener;

    public interface OnClickLayoutListener{
        void onClick(boolean isOpen);
    }

    public void setOnClickLayoutListener(OnClickLayoutListener onClickLayoutListener) {
        this.onClickLayoutListener = onClickLayoutListener;
    }
}