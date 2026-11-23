package com.grkj.ytjqy_pad.mvp.view.activity.printer;

/**
 * Create by  on 2024/7/25
 * Description:
 **/

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.dascom.dc2300.DsCardPrint;
import com.dascom.dc2300.DsCardPrintLib;
import com.dascom.print.PrintCommands.ESCPOS;
import com.dascom.print.Utils.CanvasUtils;
import com.dascom.print.Utils.HalfTone;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.SimpleActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.OnClick;

/**
 * Created by yusam.
 * <p>
 * Date: 2023/6/13
 */
public class DsTestPrinterActivity extends SimpleActivity {

    //得实打印机
    private DsCardPrintLib mPrintLib = null;

    private String SelectedMac = "";

    private Bitmap mBitmap = null;

    @Override
    protected int getLayout(){
        return R.layout.activity_new_print_label;
    }

    @Override
    protected void initEventAndData(){

        KLog.e("密度：" + getDensity());


        mBitmap = createBitmap(mContext,getView(mContext),1200,1140);
//        mBitmap = createBitmap(mContext,getView(mContext),1600,1520);

        Matrix m = new Matrix();
        m.postRotate(180);
        mBitmap = Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(),mBitmap.getHeight(),m,
                true);

        ImageView bitmap_iv = findViewById(R.id.bitmap_iv);
        bitmap_iv.setVisibility(View.VISIBLE);
        bitmap_iv.setImageBitmap(mBitmap);

    }


    private float getDensity() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }



    @OnClick({R.id.ll_back,R.id.ll_print})
    public void onViewClicked(View view){
        switch (view.getId()) {
            //返回
            case R.id.ll_back:
                finish();
                break;

            // 打印
            case R.id.ll_print:


                BluetoothManager bluetoothmanger = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                BluetoothAdapter bluetoothadapter = bluetoothmanger.getAdapter();
                KLog.e("bluetoothadapter"+ GsonUtil.GsonToString(bluetoothadapter.getBondedDevices()));
                String SelectedBDAddress = "";

                Set<BluetoothDevice> bondedDevices = bluetoothadapter.getBondedDevices();
                final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (BluetoothDevice device : bondedDevices) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("DeviceName",device.getName());
                    map.put("BDAddress",device.getAddress());
                    map.put("BondState",device.getBondState()+"");
                    map.put("DPMAC",device.getAddress());
                    list.add(map);
                }

                if (list.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
//                            if (list.get(i).get("DeviceName").contains("CC4")) {
//                                SelectedBDAddress = list.get(i).get("BDAddress");
//                            } else
                        if (list.get(i).get("DeviceName").contains("DP")) {
                            SelectedMac = list.get(i).get("DPMAC");
                        }
                    }
                } else {
                    ToastUtil.showToast(mContext,getString(R.string.str_conn_state_disconnect));
                    return;
                }



                new Thread(new Runnable(){
                    @Override
                    public void run(){//DC:0D:30:CE:5D:8A

                        if (mPrintLib == null){
                            mPrintLib = DsCardPrint.init(mContext).bluetooth(SelectedMac).build().connection();
                        }


                        if (mPrintLib.isConnected()) {
                            ESCPOS smartPrint = new ESCPOS(mPrintLib);

//                            smartPrint.setPrintAreaWidth(1600);
                            smartPrint.setPrintAreaWidth(1600);

                            new Thread(()->{

//                                smartPrint.printLineFeed();

                                boolean b;
                                Bitmap aaa= HalfTone.FloydSteinberg(CanvasUtils.BackgroundWhite(mBitmap));
                                b=smartPrint.printBitmap((aaa), 0, 0,true,0);
                                b=smartPrint.feedPrintPosition();
//                                ToastUtil.showToast(mContext,b ? getResources().getString(R.string.str_conn_successful) : getResources().getString(R.string.str_conn_fail));
                            }).start();

//                            NewPrintContentUtilsV2.dsPrint(smartPrint,new LabelInfoBean());

                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    ToastUtil.showToast(mContext,"蓝牙设备连接成功");
                                }
                            });


                        } else {

                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    ToastUtil.showToast(mContext,"连接失败");
                                }
                            });


                        }
                    }
                }).start();


                break;
        }
    }


    public static View getView(Context context){
//        View view = LayoutInflater.from(context).inflate(R.layout.bitmap_test_20x20,null,false);
        View view = LayoutInflater.from(context).inflate(R.layout.bitmap_test_20x20,null,false);
        return view;
    }


    public Bitmap createBitmap(Context context,View v,int width,int height){
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width,View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height,View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth,measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0,0,v.getMeasuredWidth(),v.getMeasuredHeight());

        Bitmap bmp = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        saveBitmap(context,bmp,format.format(new Date())+".JPEG");
        return bmp;
    }


    public void saveBitmap(Context context,Bitmap bitmap,String bitName){

        String fileName;

        File file;

        if (Build.BRAND.equals("Xiaomi")) {// 小米手机

            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName;

        } else {// Meizu 、Oppo

            fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName;

        }


        file = new File(fileName);

        if (file.exists()) {

            file.delete();

        }


        FileOutputStream out;

        try {

            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG,1,out)) {
                out.flush();
                out.close();
                // 插入图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),bitName,null);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+fileName)));

    }


}
