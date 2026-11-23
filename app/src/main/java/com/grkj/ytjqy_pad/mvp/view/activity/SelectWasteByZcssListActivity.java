package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectWasteFromZcssListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.GrRecyclerView;
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
 * Created by yusam on 2022/3/20.
 * 根据贮存设施选择废物类别
 */

public class SelectWasteByZcssListActivity extends BaseActivity<SelectWasteFromZcssListPresenter> implements SelectWasteFromZcssListPresenter.View{

    @BindView(R.id.mRecyclerView)
    GrRecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //废物总量
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    //库存总量描述
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;

    //当前页
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //贮存设备名称-文字
    @BindView(R.id.et_item1)
    TextView et_item1;

    //贮存设备名称-布局
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //废物名称-文字
    @BindView(R.id.et_item2)
    TextView et_item2;

    //废物名称-布局
    @BindView(R.id.ll_search2)
    LinearLayout ll_search2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    // 入库批次搜索
    @BindView(R.id.et_search)
    EditText et_search;

    //入库批次搜索
    @BindView(R.id.ll_input)
    LinearLayout ll_input;



    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//产废列表
    private List<CommonProductInfoBean> allList;//全部列表数据
    private CommonAdapter<CommonProductInfoBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();
    private CommonProductInfoBean selectBean = new CommonProductInfoBean();

    private String title = "";
    private String url = "";
    private boolean isFromInput = true;

    @Override
    protected int getLayout(){
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData(){
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        String zcssId = getIntent().getStringExtra("zcssId");
        String zcssbm = getIntent().getStringExtra("zcssbm");
        String id = getIntent().getStringExtra("id");

        //传zcssId入口是入库
        if (!TextUtils.isEmpty(zcssId)) {
            params.put("mainid",zcssId);
            isFromInput = true;
        }

        //传zcssbm入口是出库
        if (!TextUtils.isEmpty(zcssbm)) {
            params.put("zcssbm",zcssbm);
            isFromInput = false;
        }

        if (!TextUtils.isEmpty(id)) {
            params.put("id",id);
        }
        ll_input.setVisibility(View.VISIBLE);

        initRecycleView();
        refreshLayout.autoRefresh();
    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop(title);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar(title);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save,R.id.ll_back, R.id.ll_search_click})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean",GsonUtil.GsonToString(selectBean));
                    setResult(11,result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext,"请选择"+title);
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;


            // 搜索
            case R.id.ll_search_click:
//                String searchStr = et_search.getText().toString();
//                if (allList.size() != 0 && !TextUtils.isEmpty(searchStr)) {
//                    List<CommonProductInfoBean> newList = new ArrayList<>();
//                    for (int i = 0; i < allList.size(); i++) {
//                        allList.get(i).SELECT = false;
//                        if (allList.get(i).rkpch.contains(searchStr)) {
//                            newList.add(allList.get(i));
//                        }
//                    }
//                    if (newList.size() == 0) {
//                        ToastUtil.showToast(mContext, "未查询到匹配数据！");
//                    }
//                    mList.clear();
//                    mList.addAll(newList);
//                } else {
//                    mList.clear();
//                    mList.addAll(allList);
//                }
//
//                adapter.getDatas().clear();
//                adapter.getDatas().addAll(mList);
//                adapter.notifyDataSetChanged();

                params.put("fwxxmc", et_search.getText().toString().trim());
                refreshLayout.autoRefresh();

                break;


        }
    }


    @Override
    protected SelectWasteFromZcssListPresenter createPresenter(){
        return new SelectWasteFromZcssListPresenter();
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
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,url,params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final CommonProductInfoBean bean,final int position){
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).SELECT = false;
                            }
                            bean.SELECT = true;
                            selectBean = bean;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),bean.fwdlbh,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwdlmc,R.string.str_record_list1_2);
                    setText(holder.getView(R.id.tv_item4),bean.fwxlbh,R.string.str_record_list2);

                    if (!isFromInput){
                        setText(holder.getView(R.id.tv_item5),bean.rksj,R.string.str_record_list4);
                        setText(holder.getView(R.id.tv_item8),bean.rkpch,R.string.str_record_list15);
                    }

//                    setText(holder.getView(R.id.tv_item7),isFromInput?"产生量(吨)":"入库量(吨)",R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7),bean.jldw,isFromInput?R.string.str_jldw_record:R.string.str_jldw_input);

                    setText(holder.getView(R.id.tv_item6),isFromInput?bean.csl:bean.rkl,R.string.empty_replace);
                    holder.getView(R.id.rl_weight).setVisibility(isFromInput?View.GONE:View.VISIBLE);
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
     * 向列表视图填充数据
     */
    private void fillRecycleView(List<CommonProductInfoBean> model){
        mList.clear();
        mList.addAll(model);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


    @Override
    public void getListSuccess(List<CommonProductInfoBean> list){
        refreshLayout.finishRefresh();
        adapter.getDatas().clear();
        String json = GsonUtil.GsonToString(list);
        mList = GsonUtil.JsonToList(json,CommonProductInfoBean.class);
        adapter.getDatas().addAll(mList);
        adapter.notifyDataSetChanged();
    }
}
