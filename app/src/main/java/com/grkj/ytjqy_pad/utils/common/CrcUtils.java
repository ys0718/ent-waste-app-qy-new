package com.grkj.ytjqy_pad.utils.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by chenerte on 2018/1/29.
 */

public class CrcUtils {

    /**
     * 获取当前apk的crc32值
     *
     * @return
     */
    public static Long getCrc32(Context context) {
        String apkPath = getApkPath(context);
        ZipFile zipfile;
        ZipEntry dexentry = null;
        try {
            zipfile = new ZipFile(apkPath);
            dexentry = zipfile.getEntry("classes.dex");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dexentry.getCrc();
    }

    public static String getApkPath(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo.publicSourceDir; // 获取当前apk包的绝对路径
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前apk包的hash值
     * @return
     */
    public static String getHash(Context context){

        MessageDigest msgDigest;

        String apkPath = getApkPath(context);
        FileInputStream fis = null;
        try {
            msgDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            fis= new FileInputStream(new File(apkPath));
            while ((byteCount = fis.read(bytes)) > 0)
            {
                msgDigest.update(bytes, 0, byteCount);
            }
            BigInteger bi = new BigInteger(1, msgDigest.digest());
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
