package com.grkj.ytjqy_pad.gather;

import com.grkj.ytjqy_pad.http.HttpResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by yusam on 2018/7/19
 */

public interface ApiServer {


    //-------------------------------------------------------- 登录 ------------------------------------------------------------

    /**
     * 用户登录
     */
    @Headers("Content-Type:application/json")
    @POST("/ent-waste-qy/mobile/common/LoginForVersion1")
    Observable<HttpResult> login(@Body RequestBody requestBody);

    /**
     * 用户登录
     */
    @Headers("Content-Type:application/json")
    @POST("/ent-waste-qy/mobile/common/LoginForVersion2")
    Observable<HttpResult> loginDes(@Body RequestBody requestBody);

    /**
     * 获取用户信息
     */
    @Headers("Content-Type:application/json")
    @GET("/ent-waste-qy/sys/user/getNowUser")
    Observable<HttpResult> getUserInfo(@Header("X-Auth-Token-Qy") String token);



    @POST
    Call<HttpResult<Object>> getData(@Url String url, @Body Map<String, String> params, @HeaderMap Map<String,String> headerMap);
//
//    @POST
//    Call<ResponseBody> getData(@Url String url, @QueryMap Map<String, String> params);


    /**
     * 通用请求
     */
    @POST
    Observable<HttpResult<Object>> commonRequest(@Header("X-Auth-Token-Qy") String token, @Body RequestBody requestBody, @Url String url);


    /**
     * 请求列表
     */
    @GET
    Call<HttpResult<PageBean>> getList3(@Header("X-Auth-Token-Qy") String token, @Body RequestBody requestBody, @Url String url);

    /**
     * 请求列表
     */
    @GET
    Observable<HttpResult<Object>> CommonGet(@Header("X-Auth-Token-Qy") String token, @Url String url, @QueryMap Map<String, String> params);




    /**
     * 随机码认证
     *
     * @return
     */
    @POST("webService/rating/verifyRandom")
    Observable<HttpResult<String>> verifyRandom(@Body RequestBody requestBody);


    /**
     * 下载app
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Header("X-Auth-Token-Qy") String token,@Url String url);

    /**
     * 附件
     *
     * @param url
     * @return
     */
    @Streaming
    @POST
    Observable<ResponseBody> downloadFile(@Header("X-Auth-Token-Qy") String token, @Url String url);


    /**
     * 上传附件
     *
     * @return
     */
    @POST
    Call<ResponseBody> uploadImage(@Header("X-Auth-Token-Qy") String token, @Body RequestBody requestBody, @Url String url);



    @Multipart
    @POST("pronline/upload")
    Call<ResponseBody> upImg(@Part("FunName") RequestBody funName,
                             @Part("path") RequestBody path,
                             @Part("appfile") RequestBody appfile,
                             @Part MultipartBody.Part file);

}
