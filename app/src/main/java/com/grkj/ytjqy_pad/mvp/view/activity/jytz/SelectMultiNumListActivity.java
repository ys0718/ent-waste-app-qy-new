package com.grkj.ytjqy_pad.mvp.view.activity.jytz;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.JytzSzsbmBean;
import com.grkj.ytjqy_pad.mvp.presenter.NoPageListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2026/6/24.
 * 经营台账-数字识别码多选列表
 */

public class SelectMultiNumListActivity extends BaseActivity<NoPageListPresenter> implements NoPageListPresenter.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;


    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;


    private String number1;//联单编号

    private int totalPage;
    private int pageNumber = 1;
    private List<JytzSzsbmBean> mList;//产废列表
    private CommonAdapter<JytzSzsbmBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();

    //已选中的废物列表
    private List<JytzSzsbmBean> szsbmSelectedList = new ArrayList<>();

    @Override
    protected int getLayout(){
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData(){
        number1 = getIntent().getStringExtra("number1");

        //已选中的废物列表
        String wasteSelectedListStr = getIntent().getStringExtra("szsbmSelectedList");
        if (!TextUtils.isEmpty(wasteSelectedListStr)) {
            szsbmSelectedList = GsonUtil.JsonToList(wasteSelectedListStr, JytzSzsbmBean.class);

            //选中处理
            for (int i = 0; i < szsbmSelectedList.size(); i++) {
                szsbmSelectedList.get(i).SELECT = true;
            }
        } else {
            szsbmSelectedList = new ArrayList<>();
        }


        initRecycleView();
        refreshLayout.autoRefresh();
    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("关联废物信息");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save,R.id.ll_back})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                List<JytzSzsbmBean> selectList = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).SELECT) {
                        selectList.add(mList.get(i));
                    }
                }

                if (selectList.size() > 0) {
                    Intent result = new Intent();
                    result.putExtra("selectList",GsonUtil.GsonToString(selectList));
                    setResult(12,result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext,"请选择废物信息！");
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }


    @Override
    protected NoPageListPresenter createPresenter(){
        return new NoPageListPresenter();
    }


    @Override
    public void showError(String msg){
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }


    /**
     * 企业列表初始化
     */
    private void initRecycleView(){
        mList = new ArrayList<>();
        params.put("number1",number1);
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,ApiUrl.URL_JYTZ_SZSBM_LIST,params);
        adapter = new CommonAdapter<JytzSzsbmBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final JytzSzsbmBean bean,final int position){
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            bean.SELECT = !bean.SELECT;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1),bean.fwmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),bean.fwdm,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item3),bean.szsbm,R.string.str_jytz_list4);
                    setText(holder.getView(R.id.tv_item6),bean.jsl,R.string.empty_replace);
                    holder.setText(R.id.tv_item7,"废物重量("+bean.jldw+")");
                }
            }
        };

        mRecyclerView.setAdapter(adapter);
    }


    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value));
    }


    /**
     * 获取列表数据成功
     */
    @Override
    public void getListSuccess(final Object model,String url){
//        totalPage = model.getTotal();
        refreshLayout.finishRefresh();
//        tv_right_bottom2.setText(getString(R.string.str_page_total,model.getTotal(),model.getRecords()));

        if (model != null) {
            fillRecycleView(model);
        }
    }

    private List<JytzSzsbmBean> temList = new ArrayList<>();

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(Object model){
        mList.clear();
        String json = GsonUtil.GsonToString(model);
        temList = GsonUtil.JsonToList(json, JytzSzsbmBean.class);

        List<JytzSzsbmBean> commonList = new ArrayList<>();

        //剔除重复
        if (temList.size() != 0 && temList != null && szsbmSelectedList.size() != 0 && szsbmSelectedList != null) {
            for (int i = 0; i < temList.size(); i++) {
                for (int j = 0; j < szsbmSelectedList.size(); j++) {
                    if (szsbmSelectedList.get(j).szsbm.equals(temList.get(i).szsbm)){
                        commonList.add(temList.get(i));
                    }
                }
            }

        }

        temList.removeAll(commonList);
        mList.addAll(szsbmSelectedList);
        mList.addAll(temList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多列表成功
     */
    @Override
    public void getMoreListSuccess(Object model,String url){
        refreshLayout.finishLoadmore();
        if (totalPage == pageNumber) {
            refreshLayout.finishLoadmore();
        }
        if (model != null) {
            fillRecycleView(model);
        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
