package com.grkj.ytjqy_pad.utils.update;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiServer;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.VersionInfoBean;
import com.grkj.ytjqy_pad.utils.common.AppUtils;
import com.grkj.ytjqy_pad.utils.common.FormatUtil;
import com.grkj.ytjqy_pad.utils.common.PermissionUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by yusam on 2018/7/20.
 */

public class UpdateApkUtil {

    private static final String TAG = "tag";
    private static MaterialDialog progressDialog;
    private static Activity mContext;
    //是否下载中
    private static boolean downloading;
    //本地保存地址
    private static String savePath;
    private static NotificationCompat.Builder builder;
    private static NotificationManager notificationManager;
    private static int preProgress;
    private static final int NOTIFY_ID = 1;
    private static final int TO_M = 1024 * 1024;
    private static String mVersionCode;

    //需要更改
    private static final String APK_NAME = "gdgf_pad";
    private static final String PROVIDER_PATH = "com.grkj.ytjqy_pad";

    public static boolean showUpdateDialog(final Activity context, final VersionInfoBean versionInfoBean, MaterialDialog.SingleButtonCallback callback) {
        mContext = context;
        mVersionCode = versionInfoBean.code;
        boolean isForce = !TextUtils.isEmpty(versionInfoBean.forceUpdateStatus) && "1".equals(versionInfoBean.forceUpdateStatus);

        if (callback == null) {//回调处理
            callback = new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                    if (which == DialogAction.POSITIVE) {
                        dialog.dismiss();
                        requestPermissionAndInstall(versionInfoBean.attachmentId);
                    } else {
                        dialog.dismiss();
                        if (isForce) {
                            System.exit(0);
                        }
                    }
                }
            };
        }

        //强制更新功能
        new MaterialDialog.Builder(context)
                .title(mContext.getString(R.string.app_name_2) + versionInfoBean.version)
                .content((isForce ? "请更新至最新版本，否则将无法正常使用本系统\n" : "为了更好的用户体验，请更新至最新版本\n") + versionInfoBean.content)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .iconRes(R.mipmap.ic_logo)
                .maxIconSize(20)
                .positiveText("立即更新")
                .negativeText("稍后更新")
                .widgetColor(context.getResources().getColor(R.color.colorPrimary))
                .onAny(callback)
                .show();


        return downloading;
    }


    public CancelCallback mCancelCallback;

    public interface CancelCallback {
        void onCancel();
    }

    /**
     * 请求权限并下载安装
     */
    public static void requestPermissionAndInstall(final String id) {
        PermissionUtils.requestStoragePermission(mContext, new PermissionUtils.OnBooleanListener() {
            @Override
            public void onSuccess() {
                if (!TextUtils.isEmpty(id)) {
                    downloadAndInstall((String) SPUtils.get(mContext, "URL", "") + ApiUrl.URL_DOWNLOAD_APK + id, mContext.getString(R.string.app_file, APK_NAME + mVersionCode));
                    initNotification();
                } else {
                    ToastUtil.showToast(mContext, "版本信息有误，请联系管理员！");
                }
            }

            @Override
            public void onCancel() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 显示下载dialog
     */
    private static void showUpdateProgressDialog(long size) {
        double size_m = size * 1.0f / TO_M;//将字节转成M
        progressDialog = new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.app_name_2) + AppUtils.getVersionName(mContext))
                .content(mContext.getString(R.string.downloading, FormatUtil.changeToDecimal(size_m)))
                .canceledOnTouchOutside(false)
                .progress(false, 100)
                .cancelable(false)
//                .progress(false, size_m > 0 ? (int) size_m : 100, false)
                .show();
    }


    /**
     * 下载Apk文件并安装
     */
    public static void downloadAndInstall(String apkURL, String name) {
        SPUtils.put(mContext, Constants.APP_VERSION_CODE, mVersionCode);//保存版本号
        ApiServer apiService = DownloadHelper.getInstance().createApiService(ApiServer.class, new DownloadProgressListener());//下载监听
        savePath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + name;//保存地址
        String token = SPUtils.get(mContext, Constants.TOKEN, "").toString();
        apiService.downloadApk(token, apkURL).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Action1<ResponseBody>() {
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
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            ToastUtil.showToast(mContext, "网络连接超时，请稍后再试");
                        }
                    });
                    throwable.printStackTrace();
                    finishedAndClear();
                }
            }
        });
    }


    /**
     * 下载出错清除
     */
    private static void finishedAndClear() {
        SPUtils.put(mContext, Constants.APP_VERSION_CODE, -1);
        downloading = false;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
        }
    }

    public static class MyProgressListener implements NewProgressListener {

        @Override
        public void onStart(final long totalSize) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showUpdateProgressDialog(totalSize);
                }
            });
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
                    progressDialog.dismiss();
                    installApk();
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
                    downloading = !done;
                    if (bytesRead != 0) {
                        int progress = (int) ((bytesRead * 100) / contentLength);
//                        progressDialog.setProgress(Integer.parseInt(String.valueOf(bytesRead)));
                        updateNotification(progress);
                    }
                }
            });
        }
    }

    /**
     * 保存文件到本地
     */
    public static boolean writeFileToSDCard(ResponseBody body, String path, NewProgressListener downloadListener) {
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    KLog.d(TAG, "文件存在:删除成功");
                } else {
                    KLog.d(TAG, "文件存在:删除失败");
                }
            }
            if (file.createNewFile()) {
                KLog.d(TAG, "新文件创建成功");
            } else {
                finishedAndClear();
                KLog.d(TAG, "新文件创建失败");
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                long totalLength = body.contentLength();
                downloadListener.onStart(totalLength);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    downloadListener.onProgress((int) (100 * fileSizeDownloaded / totalLength));
                    //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                    if ((int) (100 * fileSizeDownloaded / totalLength) == 100) {
                        downloadListener.onFinish(); //下载完成
                    }

                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                finishedAndClear();
                e.printStackTrace();
                downloadListener.onFailure();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            finishedAndClear();
            downloadListener.onFailure();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化Notification通知
     */
    public static void initNotification() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 生成图标
            Bitmap LargeBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_logo);
            builder = new NotificationCompat.Builder(mContext)
                    .setLargeIcon(LargeBitmap)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setContentText("0%")
                    .setContentTitle(mContext.getString(R.string.app_name))
                    .setProgress(100, 0, false);
            notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
    }

    /**
     * 更新通知
     */
    private static void updateNotification(long progress) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            int currProgress = (int) progress;
            if (preProgress < currProgress) {
                builder.setContentText(progress + "%");
                builder.setProgress(100, (int) progress, false);
                notificationManager.notify(NOTIFY_ID, builder.build());
            }
            preProgress = (int) progress;
        }
    }


    /**
     * 安装apk
     */
    public static void installApk() {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        StringBuffer apkPath = new StringBuffer(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator);
        apkPath.append("app-");
        apkPath.append(APK_NAME + mVersionCode);
        apkPath.append(".apk");

        File file = new File(apkPath.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, PROVIDER_PATH, file);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            it.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(it);

    }

}
