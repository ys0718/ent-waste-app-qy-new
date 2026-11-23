package com.grkj.ytjqy_pad.base;

/**
 * Created by yusam on 2018/7/19.
 * Presenter基类
 */
public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
