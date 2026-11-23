package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MarqueeView extends AppCompatTextView {

    public MarqueeView(Context context) {
        super(context);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}