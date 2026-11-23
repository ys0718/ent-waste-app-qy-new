package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class SSZHTextView extends AppCompatTextView {

    public SSZHTextView(Context context) {
        super(context);
        init(context);
    }

    public SSZHTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SSZHTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/font_sszh.ttf");
        setTypeface(tf);
    }

}