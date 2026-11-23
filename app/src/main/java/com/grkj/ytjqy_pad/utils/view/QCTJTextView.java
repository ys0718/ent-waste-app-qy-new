package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class QCTJTextView extends AppCompatTextView {

    public QCTJTextView(Context context) {
        super(context);
        init(context);
    }

    public QCTJTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QCTJTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/font_qctj.TTF");
        setTypeface(tf);
    }

}