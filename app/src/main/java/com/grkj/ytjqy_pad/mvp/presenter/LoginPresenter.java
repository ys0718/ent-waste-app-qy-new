package com.grkj.ytjqy_pad.mvp.presenter;


import android.content.res.Configuration;
import android.os.Debug;
import android.os.Handler;
import android.text.TextUtils;

import com.grkj.ytjqy_pad.BuildConfig;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.base.RxPresenter;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.http.ApiCallback;
import com.grkj.ytjqy_pad.http.HttpResultFunc;
import com.grkj.ytjqy_pad.http.SubscriberCallBack;
import com.grkj.ytjqy_pad.mvp.model.LoginBean;
import com.grkj.ytjqy_pad.mvp.model.MenuBean;
import com.grkj.ytjqy_pad.mvp.model.PrinterTypeBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import rx.Observable;

public class LoginPresenter extends RxPresenter<LoginPresenter.View>{

    private UserBean userInfoBean = new UserBean();


    /*** 登录*/
    public void login(RequestBody requestBody){
        SPUtils.put(mContext,Constants.TOKEN,"");
        Observable observable = apiServer.login(requestBody).map(new HttpResultFunc());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback(){
            @Override
            public void onSuccess(Object model){
                String json = GsonUtil.GsonToString(model);
                json = json.replace("X-Auth-Token-Qy","token");
                LoginBean loginBean = GsonUtil.GsonToBean(json,LoginBean.class);
                SPUtils.put(mContext,Constants.TOKEN,loginBean.token);
                getUserInfo(loginBean.token);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }

    /*** 登录*/
    public void loginDes(RequestBody requestBody){
        SPUtils.put(mContext,Constants.TOKEN,"");
        Observable observable = apiServer.loginDes(requestBody).map(new HttpResultFunc());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback(){
            @Override
            public void onSuccess(Object model){
                String json = GsonUtil.GsonToString(model);
                json = json.replace("X-Auth-Token-Qy","token");
                LoginBean loginBean = GsonUtil.GsonToBean(json,LoginBean.class);
                SPUtils.put(mContext,Constants.TOKEN,loginBean.token);
                SPUtils.put(mContext,Constants.ENVIRONMENT,loginBean.environment);
//                SPUtils.put(mContext,Constants.ENVIRONMENT,"gz");
                getUserInfo(loginBean.token);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(String token){
        Observable observable = apiServer.getUserInfo(token).map(new HttpResultFunc());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback(){
            @Override
            public void onSuccess(Object model){
                String json = GsonUtil.GsonToString(model);
                userInfoBean = GsonUtil.GsonToBean(json,UserBean.class);
                if (!TextUtils.isEmpty(userInfoBean.dwlx) && (userInfoBean.dwlx.contains("cz") || userInfoBean.dwlx.contains("sj"))) {
                    userInfoBean.isCzdw = true;
                }

//                getPrinterInfo();

                PrinterTypeBean printerTypeBean = GsonUtil.GsonToBean(model,PrinterTypeBean.class);
                printerTypeBean.bqlx = "n";
                printerTypeBean.dyjgg = "1";
                SPUtils.put(mContext,"PRINTER",GsonUtil.GsonToString(printerTypeBean));
                getMenuList(token);

//                //todo 调试模式下不请求菜单
//                if (BuildConfig.LOG_DEBUG){//
//                    mView.testLoginSuccess(userInfoBean);
//                }else {
//                    getMenuList(token);
//                }


            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }

        },mContext,false));
    }


    /**
     * 获取系统配置信息
     */
    public void getSystemInfo(){
        Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        String param = "";
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            param = "3";
        } else {
            param = "4";
        }
        String url = ApiUrl.URL_SYSTEM_INFO+param;
        Observable observable = apiServer.CommonGet(token,url,new HashMap<>()).map(new HttpResultFunc());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback(){
            @Override
            public void onSuccess(Object model){
                mView.getSystemInfoSuccess(model);
            }

            @Override
            public void onFailure(String msg){
//                mView.showError(msg);
                SPUtils.remove(mContext,"SYSTEM_INFO");
            }
        },mContext,false));
    }


    /**
     * 获取角色菜单
     */
    private void getMenuList(String token){
        Configuration mConfiguration = mContext.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        String param = "";
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            param = "YTJ";
        } else {
            param = "YDD";
        }
        // TODO: 2026/8/13  
        param = "YTJ2";
//        String param = isLandscape ? "YTJ" : "YDD";
        String url = ApiUrl.URL_MENU_LIST+param;
        Observable observable = apiServer.CommonGet(token,url,new HashMap<>()).map(new HttpResultFunc());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback(){
            @Override
            public void onSuccess(Object model){
                String menuListStr = GsonUtil.GsonToString(model);
                List<MenuBean> mList = GsonUtil.JsonToList(menuListStr,MenuBean.class);
                if (mList == null || mList.size() == 0) {
                    mView.showError("该用户未绑定菜单，请联系管理员！");
                } else {
                    mView.LoginSuccess(userInfoBean,GsonUtil.GsonToString(model));
                }

//                mView.LoginSuccess(userInfoBean,GsonUtil.GsonToString(model));
            }

            @Override 
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }


    /**
     * 认证随机码
     */
    public void verifyRandom(RequestBody requestBody){
        Observable<String> observable = apiServer.verifyRandom(requestBody).map(new HttpResultFunc<String>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<String>(){
            @Override
            public void onSuccess(String model){
                mView.verifyRandomSuccess(model.toString());
            }

            @Override
            public void onFailure(String msg){
                if ("HTTP 504 Unsatisfiable Request (only-if-cached)".equals(msg)) {
                    showMessageAndExitApp("网络连接超时，请稍后再试");
                } else {
                    showMessageAndExitApp(msg);
                }
            }
        },mContext));
    }

    /**
     * 检查更新
     *
     * @param isLandscape
     */
    public void checkVersion(boolean isLandscape){
//        String isDebug = Constants.IS_DEBUG?"&isDebug=1":"";
        String isDebug = BuildConfig.LOG_DEBUG?"&isDebug=1":"";
        String dwid = SPUtils.get(mContext,Constants.DWID,"").toString();
        String deviceCode = SPUtils.get(mContext,Constants.DEVICE_CODE,"").toString();
        String url = ApiUrl.URL_GET_NEW_VERSION+"?dwid="+dwid+"&deviceCode="+deviceCode;
        String type = isLandscape?"":"&type=3";//一体机传空，移动端传3
//        KLog.e("dwid:" + dwid);
//        KLog.e("deviceCode:" + SerialNumber);
        token = SPUtils.get(mContext,Constants.TOKEN,"").toString();
        Observable<Object> observable = apiServer.CommonGet(token,url+type+isDebug,new HashMap<>()).map(new HttpResultFunc<Object>());
        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
            @Override
            public void onSuccess(Object model){
                mView.checkVersionSuccess(model);
            }

            @Override
            public void onFailure(String msg){
                mView.showError(msg);
            }
        },mContext,false));
    }


//    /**
//     * 获取打印机信息
//     *
//     * @param
//     */
//    public void getPrinterInfo(){
//        String SerialNumber = android.os.Build.SERIAL;
//        String url = ApiUrl.URL_GET_PRINTER_INFO+SerialNumber;
//        token = SPUtils.get(mContext,Constants.TOKEN,"").toString();
//        Observable<Object> observable = apiServer.CommonGet(token,url,new HashMap<>()).map(new HttpResultFunc<Object>());
//        addSubscription(observable,new SubscriberCallBack(new ApiCallback<Object>(){
//            @Override
//            public void onSuccess(Object model){
//);
//                if (TextUtils.isEmpty(printerTypeBean.bqlx)){//以防bqlx为空
//                    printerTypeBean.bqlx =  "n";
//                    printerTypeBean.dyjgg = "1";
//                    SPUtils.put(mContext,"PRINTER",GsonUtil.GsonToString(printerTypeBean));
//                }else {
//                    SPUtils.put(mContext,"PRINTER",GsonUtil.GsonToString(model));
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(String msg){
//                PrinterTypeBean defaultType = new PrinterTypeBean();
//                defaultType.bqlx = "n";
//                defaultType.dyjgg = "1";
//                SPUtils.put(mContext,"PRINTER",GsonUtil.GsonToString(defaultType));
//                getMenuList(token);
////                mView.showError(msg);
//            }
//        },mContext,false));
//    }


    /*** 提示窗口并退出程序*/
    public void showMessageAndExitApp(String mMessage){
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener(){
            @Override
            public void onConfirmClick(){
                roundCornerDialog.dismiss();
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        App.getInstance().exitLiveApp();
                    }
                },100);
            }

            @Override
            public void onCancelClick(){
            }
        });
        //显示对话框
        roundCornerDialog.show();
    }

    public interface View extends BaseView{
        void LoginSuccess(UserBean model,String menuBeanList);

        void verifyRandomSuccess(String string);

        void checkVersionSuccess(Object model);

        void getSystemInfoSuccess(Object model);

        void testLoginSuccess(UserBean model);



    }
}


