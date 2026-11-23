package com.grkj.ytjqy_pad.utils.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 统一设置glide的配置
 */

public class GlideUtils {

//    public static void setLogoConfig(Context context, String imgRes, ImageView targetIv) {
//        Glide.with(context).load(imgRes).
//                asBitmap().centerCrop().error(R.drawable.ic_mine_default_portrait).
//                placeholder(R.drawable.ic_mine_default_portrait).
//                into(new MyBitmapImageViewTarget(targetIv));
//    }
//
//    public static void setAvatarConfig(Context context, String imgRes, ImageView targetIv) {
//        Glide.with(context).load(imgRes).
//                asBitmap().centerCrop().error(R.drawable.ic_default_avatar).
//                placeholder(R.drawable.ic_default_avatar).
//                into(new MyBitmapImageViewTarget(targetIv));
//    }
//
//    public static void setCartoonAvatarConfig(Context context, String imgRes, ImageView targetIv) {
//        Glide.with(context).load(imgRes).
//                asBitmap().centerCrop().error(R.drawable.ic_cartoon_avatar).
//                placeholder(R.drawable.ic_cartoon_avatar).
//                into(new MyBitmapImageViewTarget(targetIv));
//    }
//
//    public static void setProductConfig(Context context, String imgRes, ImageView targetIv) {
//        Glide.with(context).load(imgRes).
//                asBitmap().centerCrop().error(R.drawable.ic_merchant_default_logo).
//                placeholder(R.drawable.ic_merchant_default_logo).
//                into(new MyBitmapImageViewTarget(targetIv));
//
//
//    }
//
//    public static void setAvatarConfig(Context context, String imgRes, ImageView targetIv, boolean isRound) {
//        if (isRound) {
//            Glide.with(context)
//                    .load(imgRes)
//                    .asBitmap()
//                    .centerCrop()
//                    .transform(new GlideCircleTransform(context))
//                    .error(R.drawable.ic_default_avatar)
//                    .placeholder(R.drawable.ic_default_avatar)
//                    .into(new MyBitmapImageViewTarget(targetIv));
//        } else {
//            setAvatarConfig(context, imgRes, targetIv);
//        }
//    }

    public static Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap bitmap;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bitmap = (Bitmap) msg.obj;
            Log.e("bitmap", "handleMessage: " + bitmap);
        }
    };

    public static Bitmap returnBitMap(final Handler handler, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

}
