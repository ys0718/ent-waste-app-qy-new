package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.grkj.ytjqy_pad.R;

/**
 * Created by yusam.
 * <p>加载动态图标
 * Date: 2023/8/3
 */
public class GifView extends View{
    private long movieStart;
    private Movie movie;

    //此处必须重写该构造方法
    public GifView(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//关闭硬件加速
        //以文件流（InputStream）读取进gif图片资源,此处的congratulation是我的gif图片名
        movie = Movie.decodeStream(getResources().openRawResource(R.raw.ic_test));
    }


    @Override
    protected void onDraw(Canvas canvas){

        long curTime = android.os.SystemClock.uptimeMillis();//获得当前时间
        //第一次播放
        if (movieStart == 0) {
            movieStart = curTime;
        }
        if (movie != null) {
            int duraction = movie.duration();//获取gif长度
            int relTime = (int) ((curTime-movieStart) % duraction);//运用取余数运算，设置reltime
            movie.setTime(relTime);//将gif跳转到reltime那一帧
            movie.draw(canvas,0,0);//画出那帧
        //强制重绘
            invalidate();//这个重画过程取决于硬件的反应速度，速度快的看到的gif更流畅
        }
        super.onDraw(canvas);
    }
}