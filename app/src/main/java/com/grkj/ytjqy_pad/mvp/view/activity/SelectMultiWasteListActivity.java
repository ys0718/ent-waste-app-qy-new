package com.grkj.ytjqy_pad.mvp.view.activity;

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
import com.grkj.ytjqy_pad.mvp.model.ZxlyListBean.ZxlyFwListBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectWasteListPresenter;
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
 * Created by yusam on 2022/3/20.
 * 产生信息选择列表
 */

public class SelectMultiWasteListActivity extends BaseActivity<SelectWasteListPresenter> implements SelectWasteListPresenter.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

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


    private int totalPage;
    private int pageNumber = 1;
    private List<ZxlyFwListBean> mList;//产废列表
    private CommonAdapter<ZxlyFwListBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();

    private ZxlyFwListBean wasteSelectBean = null;

    private boolean isSingleSelected = false;

    //已选中的废物列表
    private List<ZxlyFwListBean> wasteSelectedList = new ArrayList<>();

    @Override
    protected int getLayout(){
        return R.layout.activity_select_waste_list;
    }

    @Override
    protected void initEventAndData(){

        //是否单选
        isSingleSelected = getIntent().getBooleanExtra("isSingleSelected",false);

        //产废0 出库1
        String source = getIntent().getStringExtra("source");
        params.put("source",source);

        //选中废物信息
        String wasteSelectBeanStr = getIntent().getStringExtra("wasteSelectBean");
        if (!TextUtils.isEmpty(wasteSelectBeanStr)) {
            wasteSelectBean = GsonUtil.GsonToBean(wasteSelectBeanStr, ZxlyFwListBean.class);
        } else {
            wasteSelectBean = new ZxlyFwListBean();
        }

        //已选中的废物列表
        String wasteSelectedListStr = getIntent().getStringExtra("wasteSelectedList");
        if (!TextUtils.isEmpty(wasteSelectedListStr)) {
            wasteSelectedList = GsonUtil.JsonToList(wasteSelectedListStr, ZxlyFwListBean.class);

            //选中处理
            for (int i = 0; i < wasteSelectedList.size(); i++) {
                wasteSelectedList.get(i).SELECT = true;

                if (!TextUtils.isEmpty(wasteSelectedList.get(i).wwlyczl)) {
                    wasteSelectedList.get(i).zxlyczl = wasteSelectedList.get(i).wwlyczl;
                }

            }
        } else {
            wasteSelectedList = new ArrayList<>();
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
        initTopToolbar("选择产废信息");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save,R.id.ll_back})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                List<ZxlyFwListBean> selectList = new ArrayList<>();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).SELECT) {
                        mList.get(i).wwlyczl = mList.get(i).zxlyczl;
                        selectList.add(mList.get(i));
                    }
                }

                if (selectList.size() > 0) {
                    Intent result = new Intent();
                    result.putExtra("selectList",GsonUtil.GsonToString(selectList));
                    setResult(10,result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext,"请选择产废信息！");
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }


    @Override
    protected SelectWasteListPresenter createPresenter(){
        return new SelectWasteListPresenter();
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
        if (!TextUtils.isEmpty(wasteSelectBean.fwdlbh)) {
            params.put("fwdlbh",wasteSelectBean.fwdlbh);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.fwxlbh)) {
            params.put("fwxlbh",wasteSelectBean.fwxlbh);
        }
//        if (!TextUtils.isEmpty(wasteSelectBean.fwxxmc)) {
//            params.put("fwxxmc",wasteSelectBean.fwxxmc);
//        }
        if (!TextUtils.isEmpty(wasteSelectBean.jldw)) {
            params.put("jldw",wasteSelectBean.jldw);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.fwtx)) {
            params.put("fwtx",wasteSelectBean.fwtx);
        }
        if (!TextUtils.isEmpty(wasteSelectBean.rqbzlx)) {
            params.put("rqbzlx",wasteSelectBean.rqbzlx);
        }

        if (!TextUtils.isEmpty(wasteSelectBean.wlxz)) {
            params.put("wlxz",wasteSelectBean.wlxz);
        }

        if (!TextUtils.isEmpty(wasteSelectBean.jldw)) {
            params.put("jldw",wasteSelectBean.jldw);
        }


        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,ApiUrl.URL_SELECT_MULTI_WASTE_LIST,params);
        adapter = new CommonAdapter<ZxlyFwListBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final ZxlyFwListBean bean,final int position){
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if (isSingleSelected){//单选
                                for (int i = 0; i < mList.size(); i++) {
                                    mList.get(i).SELECT = false;
                                }
                                mList.get(position).SELECT = true;
                            }else {//多选
                                bean.SELECT = !bean.SELECT;
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),bean.fwdlbh,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4),getPcbm(bean),R.string.str_record_list10);
                    setText(holder.getView(R.id.tv_item6),bean.zxlyczl,R.string.empty_replace);
                    holder.setText(R.id.tv_item7,"废物重量("+bean.jldw+")");
                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }

    //获取批次编码
    private String getPcbm(ZxlyFwListBean bean){
        String result = bean.ckpch;
        if (TextUtils.isEmpty(bean.ckid)) {
            result = bean.cspch;
        }
        return result;
    }

    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value));
    }


    /**
     * 获取列表数据成功
     */
    @Override
    public void getListSuccess(final PageBean model,String url){
        totalPage = model.getTotal();
        refreshLayout.finishRefresh();
        tv_right_bottom2.setText(getString(R.string.str_page_total,model.getTotal(),model.getRecords()));

        if (model != null) {
            fillRecycleView(model);
        }
    }

    private List<ZxlyFwListBean> temList = new ArrayList<>();

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<ZxlyFwListBean> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        temList = GsonUtil.JsonToList(json, ZxlyFwListBean.class);

        List<ZxlyFwListBean> commonList = new ArrayList<>();

        //剔除重复
        if (temList.size() != 0 && temList != null && wasteSelectedList.size() != 0 && wasteSelectedList != null) {
            for (int i = 0; i < temList.size(); i++) {
                for (int j = 0; j < wasteSelectedList.size(); j++) {
                    if (wasteSelectedList.get(j).csfwid.equals(temList.get(i).csfwid)){
                        commonList.add(temList.get(i));
                    }
                }
            }

//            Iterator<ZxlyFwListBean> iterator = temList.iterator();
//
//            for (int i = 0; i < wasteSelectedList.size(); i++) {
//                while (iterator.hasNext()) {
//                    if (wasteSelectedList.get(i).csfwid.equals(iterator.next().csfwid)) {
//                        iterator.remove();
//                    }
//                }
//            }

        }

        temList.removeAll(commonList);
        mList.addAll(wasteSelectedList);
        mList.addAll(temList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多列表成功
     */
    @Override
    public void getMoreListSuccess(PageBean model,String url){
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
