package com.grkj.ytjqy_pad.utils.common;

import static com.grkj.ytjqy_pad.utils.update.UpdateApkUtil.writeFileToSDCard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiServer;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.update.DownloadHelper;
import com.grkj.ytjqy_pad.utils.update.NewProgressListener;
import com.grkj.ytjqy_pad.utils.update.ProgressListener;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.io.File;

import okhttp3.ResponseBody;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yusam on 2018/9/28.
 * 下载工具
 */

public class DownloadUtil {
    private static Activity mContext;
    private static String savePath;
    private static ProgressDialog progressDialog;

    public static void downloadFile(final Activity context, final String url, final String pathName) {
        mContext = context;
        PermissionUtils.requestStoragePermission(mContext, new PermissionUtils.OnBooleanListener() {
            @Override
            public void onSuccess() {
                showTipsDialog(url, pathName);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 确认下载
     *
     * @param url
     * @param pathName
     */
    private static void showTipsDialog(final String url, final String pathName) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定下载该文件？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage(mContext.getResources().getString(R.string.downloading2));
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.show();
                download(mContext, url, pathName);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {
            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    /**
     * 下载请求
     */
    private static void download(final Activity mContext, String url, String pathName) {
        //保存地址
        savePath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + pathName;
        ApiServer apiService = DownloadHelper.getInstance().createApiService(ApiServer.class, new DownloadProgressListener());//下载监听
        String token = SPUtils.get(mContext, Constants.TOKEN, "").toString();
        apiService.downloadFile(token, url).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(final ResponseBody responseBody) {
                writeFileToSDCard(responseBody, savePath, new MyProgressListener());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable != null && !TextUtils.isEmpty(throwable.getLocalizedMessage())) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            ToastUtil.showToast(mContext, "网络连接超时，请稍后再试");
                        }
                    });
                    throwable.printStackTrace();
                }
            }
        });

    }


    public static class MyProgressListener implements NewProgressListener {

        @Override
        public void onStart(final long totalSize) {
        }

        @Override
        public void onProgress(final int currentLength) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setProgress(currentLength);
                }
            });
        }

        @Override
        public void onFinish() {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    openFile();
                    progressDialog.dismiss();
                }
            });
        }

        @Override
        public void onFailure() {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    ToastUtil.showToast(mContext, "下载失败，请稍后再试");
                }
            });
        }
    }


    /**
     * 下载进度监听
     */
    private static class DownloadProgressListener implements ProgressListener {
        @Override
        public void onProgress(final long bytesRead, final long contentLength, final boolean done) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bytesRead != 0) {
                        int progress = (int) ((bytesRead * 100) / contentLength);
                        progressDialog.setProgress(Integer.parseInt(String.valueOf(bytesRead)));
                    }
                }
            });
        }
    }

    /**
     * 打开文件
     */
    public static void openFile() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(savePath);

        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileType = "";
        switch (prefix) {
            case "txt":
                fileType = "text/plain";
                break;
            case "pdf":
                fileType = "application/pdf";
                break;
            case "doc":
                fileType = "application/msword";
                break;
            case "xls":
                fileType = "application/vnd.ms-excel";
                break;
            case "ppt":
                fileType = "application/vnd.ms-powerpoint";
                break;
            case "png":
            case "jpeg":
            case "jpg":
                fileType = "image/*";
                break;
        }

        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri fileUri = FileProvider.getUriForFile(mContext, "com.grkj.ytjqy_pad", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(fileUri, fileType);
        } else {
            intent.setDataAndType(Uri.fromFile(file), fileType);
        }
        mContext.startActivity(intent);
    }

}
