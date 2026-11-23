package com.grkj.ytjqy_pad.http;

import android.app.Activity;

import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yusam on 2018/7/19.
 */
public class HttpMethods {

    private Retrofit retrofit;


    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        File cacheFile = new File(Constants.PATH_DATA);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    // 无网络时强制从缓存读取，如果缓存中没有数据会返回 504
                    // FORCE_CACHE 里设置了 only-if-cached=true 和 maxStale
                    // only-if-cached 顾名思义就是只有缓存中有数据才返回，没有时返回 504
                    // maxStale 指可以取过期多久的数据，FORCE_CACHE 中设置为了Int最大值
                }

                Response response = chain.proceed(request);

                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        builder.cache(cache);

        /**
         * 这是设置连接时间
         * */
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);

        //错误重连
        builder.retryOnConnectionFailure(true);

        String url = (String) SPUtils.get (App.getContext (),"URL",Constants.DEFAULT_IP + ":" +  Constants.DEFAULT_PORT);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
//        (String) SPUtils.get (App.getContext (),"URL","")
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final Retrofit INSTANCE = new HttpMethods().getServer();
    }

    //获取单例
    public static Retrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Retrofit getServer() {
        return retrofit;
    }
}
