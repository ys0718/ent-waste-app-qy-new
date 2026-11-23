/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.grkj.ytjqy_pad.utils.scancode;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import  com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import  com.grkj.ytjqy_pad.utils.scancode.utils.Constant;
import  com.grkj.ytjqy_pad.utils.scancode.zxing.ScanListener;
import  com.grkj.ytjqy_pad.utils.scancode.zxing.ScanManager;
import  com.grkj.ytjqy_pad.utils.scancode.zxing.decode.DecodeThread;
import  com.grkj.ytjqy_pad.utils.scancode.zxing.decode.Utils;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 二维码扫描使用
 *
 * @author 刘红亮  2015年4月29日  下午5:49:45
 */
public final class CommonScanActivity extends Activity implements ScanListener, View.OnClickListener {
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;
    TextView iv_light;
    TextView qrcode_g_gallery;
    TextView qrcode_ic_back;
    final int PHOTOREQUESTCODE = 1111;

    @BindView(R.id.authorize_return)
    ImageView authorize_return;
    private int scanMode;//扫描模型（条形，二维码，全部）

    @BindView(R.id.common_title_TV_center)
    TextView title;
    @BindView(R.id.scan_hint)
    TextView scan_hint;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        scanMode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        initView();
    }

    void initView() {
        switch (scanMode) {
            case DecodeThread.BARCODE_MODE:
                title.setText("条形码扫描");
                scan_hint.setText("将条形码对入取景框，即可自动扫描");
                break;
            case DecodeThread.QRCODE_MODE:
                title.setText("二维码扫描");
                scan_hint.setText("将二维码对入取景框，即可自动扫描");
                break;
            case DecodeThread.ALL_MODE:
                title.setText("二维码或条形码扫描");
                scan_hint.setText("将二维码或条形码对入取景框，即可自动扫描");
                break;
        }
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);
        authorize_return.setOnClickListener(this);
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    private String subScanCodeResult(String result){
        String newResult = result;
        if (!TextUtils.isEmpty(newResult) && newResult.contains("b=")){
            newResult =  newResult.split("b=")[1];
        }
        KLog.e("newResult:"  + newResult);
        return newResult;
    }


    /**
     *
     */
    public void scanResult(Result rawResult, Bundle bundle) {
        String result = subScanCodeResult(rawResult.getText());
        setResult(Constants.SCAN_REQUEST_CODE, new Intent().putExtra("ScanCodeResult", result));
        finish();
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_g_gallery:
                showPictures(PHOTOREQUESTCODE);
                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                finish();
                break;
            case R.id.authorize_return:
                finish();
                break;
        }
    }

}