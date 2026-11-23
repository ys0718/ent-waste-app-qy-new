package com.grkj.ytjqy_pad.mvp.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.mvp.presenter.ChangePasswordPresenter;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * Created by yusam on 2022/3/20.
 * 修改密码
 */

public class ChangePswActivity extends BaseActivity<ChangePasswordPresenter> implements ChangePasswordPresenter.View {

    //旧密码
    @BindView(R.id.et_item1)
    EditText et_item1;

    //新密码
    @BindView(R.id.et_item2)
    EditText et_item2;

    //确认密码
    @BindView(R.id.et_item3)
    EditText et_item3;

    @Override
    protected int getLayout() {
        return R.layout.activity_change_psw;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("修改密码");
    }

    @OnClick({R.id.ll_save, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 修改密码
            case R.id.ll_save:

                if (TextUtils.isEmpty(et_item1.getText().toString())) {
                    toastShow("请输入旧密码！");
                } else if (TextUtils.isEmpty(et_item2.getText().toString())) {
                    toastShow("请输入新密码！");
                } else if (TextUtils.isEmpty(et_item3.getText().toString())) {
                    toastShow("请再次输入新密码！");
                } else if (!et_item2.getText().toString().equals(et_item3.getText().toString())) {
                    toastShow("两次输入的密码不一致，请确认并重新输入！");
                } else {
                    showSaveDialog();
                }


                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void showSaveDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                mPresenter.saveData(new HashMap<>(), ApiUrl.URL_UPDATE_PASSWORD + "?oldPassword=" + et_item1.getText().toString() + "&newPassword=" + et_item2.getText().toString());
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    @Override
    protected ChangePasswordPresenter createPresenter() {
        return new ChangePasswordPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "修改成功！");
        finish();
    }
}
