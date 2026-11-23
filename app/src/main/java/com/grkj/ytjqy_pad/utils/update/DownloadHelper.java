package com.grkj.ytjqy_pad.utils.update;


import android.app.Activity;

import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.SSLSocketClient;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 包含进度监听
 * Created by yusam on 2018/7/20.
 */

public class DownloadHelper {
    //这是连接网络的时间
    private static final int DEFAULT_TIMEOUT = 10;//原来是5s

    private Retrofit retrofit;

    private static OkHttpClient.Builder builder;

    private static Activity mContext;

    private DownloadHelper() {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);

    }

    //在访问时创建单例
    private static class SingletonHolder {
        private static final DownloadHelper INSTANCE = new DownloadHelper();
    }


    //获取单例
    public static DownloadHelper getInstance() {

        return SingletonHolder.INSTANCE;

    }


    public <S>S createApiService(Class<S> serviceClass, final ProgressListener progressListener){

        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //本身的response
                Response proceed = chain.proceed(chain.request());
                //创建自定义的ProgressResponseBody
                ProgressResponseBody progressResponseBody = new ProgressResponseBody(proceed.body(),progressListener);
                return proceed.newBuilder()
                        .body(progressResponseBody) //替换我们自己的进度监听的progressResponseBody
                        .build();
            }
        });

        //设置缓存
        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl((String) SPUtils.get (App.getContext(),"URL",Constants.DEFAULT_IP + ":" +  Constants.DEFAULT_PORT)).build();

        return retrofit.create(serviceClass);
    }

}
