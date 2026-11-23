package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.socks.library.KLog;

/**
 * Created by yusam.
 * <p>
 * Date: 2023/8/7
 */
public class GrRecyclerView extends RecyclerView{

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    public GrRecyclerView(Context context){
        super(context);
    }

    public GrRecyclerView(Context context,@Nullable @androidx.annotation.Nullable AttributeSet attrs){
        super(context,attrs);
    }

    public GrRecyclerView(Context context,@Nullable @androidx.annotation.Nullable AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        curP.x = event.getX();
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//
//                downP.x = event.getX();
//                KLog.i("TEST", "downP.x-=-=-=--=-" + downP.x);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                KLog.i("TEST", "downP.x-=-=-=--=-" + curP.x);
//
//                if (downP.x < 100) {
//
//                    if (curP.x - downP.x > 200) {
//
//                        return false;
//
//                    }
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//
//            default:
//                break;
//        }
        return super.onTouchEvent(event);
    }

}
