package com.grkj.ytjqy_pad.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.xujiaji.happybubble.BubbleDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2018/7/19.
 * MVP activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView {
    protected T mPresenter;

    protected abstract T createPresenter();

    protected List<ParamListBean> allParamList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }


    //点击显示详情气泡弹窗
    protected void showTipsCallback(TextView textView){
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                View tips_view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips,null);
                TextView tv_title = tips_view.findViewById(R.id.tv_title);
                tv_title.setText(textView.getText().toString());
                BubbleDialog mBubbleDialog = new BubbleDialog(mContext)
                        .addContentView(tips_view)
                        .setClickedView(textView)
                        .setOffsetY(8);
                mBubbleDialog.show();

            }
        });
    }

}