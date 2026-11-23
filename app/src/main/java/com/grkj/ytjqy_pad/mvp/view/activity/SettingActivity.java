package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.PrinterTypeBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.presenter.SettingPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 系统设置
 */

public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingPresenter.View{

    //用户名
    @BindView(R.id.tv_item1)
    TextView tv_item1;

    //所属部门
    @BindView(R.id.tv_item2)
    TextView tv_item2;

    //用户类型
    @BindView(R.id.tv_item3)
    TextView tv_item3;

    //性别
    @BindView(R.id.tv_item4)
    TextView tv_item4;

    //联系电话
    @BindView(R.id.tv_item5)
    TextView tv_item5;

    //旧标签
    @BindView(R.id.iv_old)
    ImageView iv_old;

    //新标签
    @BindView(R.id.iv_new)
    ImageView iv_new;

    @Override
    protected int getLayout(){
        return R.layout.activity_setting;
    }

    private boolean isNewLabel = false;

    @Override
    protected void initEventAndData(){
        getBqlxFromDevice();
        UserBean userInfoBean = GsonUtil.GsonToBean(SPUtils.get(mContext,Constants.USER_INFO,"").toString(),UserBean.class);
        tv_item1.setText(userInfoBean.name);
        tv_item2.setText(userInfoBean.officeName);
//        tv_item3.setText(); 默认企业用户
        tv_item4.setText(userInfoBean.sex);
        tv_item5.setText(userInfoBean.mobile);

        setCheckView();
    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("设置");
    }

    private void setCheckView(){
        iv_new.setImageResource(isNewLabel?R.mipmap.ic_selected:R.mipmap.ic_unselected);
        iv_old.setImageResource(!isNewLabel?R.mipmap.ic_selected:R.mipmap.ic_unselected);
    }


    @OnClick({R.id.ll_old,R.id.ll_new,R.id.rl_menu1,R.id.rl_menu2,R.id.rl_menu3,R.id.rl_menu4,R.id.ll_back})
    public void onViewClicked(View view){
        switch (view.getId()) {

            //旧标签
            case R.id.ll_old:

                if (isNewLabel && isClickable) {
                    showSaveDialog();
                }
                break;

            //新标签
            case R.id.ll_new:
                if (!isNewLabel && isClickable) {
                    showSaveDialog();
                }
                break;

            // 修改密码
            case R.id.rl_menu1:
                startActivity(new Intent(mContext,ChangePswActivity.class));
                break;
            // 关于
            case R.id.rl_menu2:
                startActivity(new Intent(mContext,SettingAboutActivity.class));
                break;

            // 重新登录 todo
            case R.id.rl_menu3:
                backLoginActivity();
//                startActivity(new Intent(mContext, CollectListActivity.class));
                break;

            // 退出 todo
            case R.id.rl_menu4:
                App.getInstance().exitLiveApp();
                System.exit(0);
//                startActivity(new Intent(mContext, SettingActivity.class));
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;


        }
    }

    private boolean isClickable = true;

    //保存弹窗
    public void showSaveDialog(){
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定修改标签类型？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener(){
            @Override
            public void onConfirmClick(){
                roundCornerDialog.dismiss();
                isNewLabel = !isNewLabel;
                isClickable = false;
                setCheckView();
                Map<String, String> param = new HashMap<>();
                param.put("id",printerTypeBean.id);
                param.put("bqlx",isNewLabel?"n":"o");
                mPresenter.changeLabel(param);
            }

            @Override
            public void onCancelClick(){
                roundCornerDialog.dismiss();
            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    private PrinterTypeBean printerTypeBean;

    /**
     * 获取标签类型
     *
     * @return
     */
    private void getBqlxFromDevice(){
        String printerStr = SPUtils.get(mContext,"PRINTER","").toString();
        printerTypeBean = GsonUtil.GsonToBean(printerStr,PrinterTypeBean.class);
        if ("n".equals(printerTypeBean.bqlx)) {
            isNewLabel = true;
        } else {
            isNewLabel = false;
        }
    }


    @Override
    protected SettingPresenter createPresenter(){
        return new SettingPresenter();
    }

    @Override
    public void showError(String msg){
        isClickable = true;
        isNewLabel = !isNewLabel;
        setCheckView();
        ToastUtil.showToast(mContext,msg);
    }

    @Override
    public void changeLabelSuccess(String bean){
        isClickable = true;
        ToastUtil.showToast(mContext,"操作成功！");
//        printerTypeBean.bqlx = isNewLabel? "n" : "o";
        SPUtils.put(mContext,"PRINTER",GsonUtil.GsonToString(printerTypeBean));
    }
}
