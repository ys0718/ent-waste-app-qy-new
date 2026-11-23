package com.grkj.ytjqy_pad.mvp.view.activity;

import android.view.View;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.mvp.presenter.VideoPresenter;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;

import butterknife.OnClick;
/**
 * Created by yusam on 2022/3/20.
 * 视频监控
 */

public class VideoActivity extends BaseActivity<VideoPresenter> implements VideoPresenter.View {


    @Override
    protected int getLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected void initEventAndData() {
        initTop();
    }


    @OnClick({ R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }


    @Override
    protected VideoPresenter createPresenter() {
        return new VideoPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
        finish();
    }


    @Override
    public void saveDataSuccess(String bean) {
        ToastUtil.showToast(mContext, "修改成功！");
    }
}
