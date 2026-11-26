package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.JyStockDetailListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.NumberUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
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
 * 库存明细列表
 */

public class JyStockDetailListActivity extends BaseActivity<JyStockDetailListPresenter> implements JyStockDetailListPresenter.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //废物总量
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    //当前页
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //标题2
    @BindView(R.id.tv_title2)
    TextView tv_title2;

    //手动入库
    @BindView(R.id.tv_right1)
    TextView tv_right1;

    //手动入库
    @BindView(R.id.ll_add)
    LinearLayout ll_add;

    //称重入库
    @BindView(R.id.tv_right2)
    TextView tv_right2;

    //合计入库
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;

    //称重入库
    @BindView(R.id.ll_add2)
    LinearLayout ll_add2;


    private int totalPage;
    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//企业列表
    private CommonAdapter<CommonProductInfoBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout(){
        return R.layout.activity_common_list;
    }


    @Override
    protected void initEventAndData(){
        tv_title.setText("库存明细");
        params.put("zcsslx",getIntent().getStringExtra("ZCSSLX"));
        params.put("zcssbm",getIntent().getStringExtra("ZCSSBM"));
        params.put("fwdlbh",getIntent().getStringExtra("FWDLBH"));
        params.put("fwxxmc",getIntent().getStringExtra("FWXXMC"));
        params.put("fwxlbh",getIntent().getStringExtra("FWXLBH"));
        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
        tv_title2.setVisibility(View.INVISIBLE);
        ll_add.setVisibility(View.INVISIBLE);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        tv_title2.setVisibility(View.GONE);
        ll_add.setVisibility(View.GONE);
        findViewById(R.id.ll_add2).setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_back})
    public void onViewClicked(View view){
        switch (view.getId()) {
            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }


    @Override
    protected JyStockDetailListPresenter createPresenter(){
        return new JyStockDetailListPresenter();
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
        String url = ApiUrl.URL_STOCK_DETAIL_LIST_GZ;
        String environment = SPUtils.get(mContext, Constants.ENVIRONMENT, "").toString();
        if (environment.equals("hn")){
            url = ApiUrl.URL_STOCK_DETAIL_LIST_HN;
        }
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,url,params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final CommonProductInfoBean bean,final int position){
                if (bean != null) {
                    holder.setText(R.id.tv_item1,TextUtils.isEmpty(bean.fwxxmc)?getString(R.string.common_empty):bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2,bean.fwdlbh,bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),groupFwlb,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4),bean.rksj,R.string.str_record_list4);
                    setText(holder.getView(R.id.tv_item5),bean.rkpch,R.string.str_record_list6);
                    setText(holder.getView(R.id.tv_item6),bean.syl,R.string.empty_replace);
//                    setText(holder.getView(R.id.tv_item7),bean.jldw,R.string.str_jldw_zcl);
                    setText(holder.getView(R.id.tv_item7),"吨",R.string.str_jldw_zcl);

                }
            }
        };
//        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
//                startActivityForResult(new Intent(mContext, JytzLyczInfoActivity.class)
//                        .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(i))), 9);
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
//                return false;
//            }
//        });


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
    public void getListSuccess(final PageBean model,String url){
        totalPage = model.getTotal();
        refreshLayout.finishRefresh();
        tv_right_bottom2.setText(getString(R.string.str_page_total,model.getTotal(),model.getRecords()));

        if (model != null) {
            fillRecycleView(model);
        }

    }

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<CommonProductInfoBean> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean> LabelInfoBeans = GsonUtil.JsonToList(json,CommonProductInfoBean.class);
        mList.addAll(LabelInfoBeans);
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
    public void getTotalWeightSuccess(String total){
        tv_left_bottom.setText(NumberUtils.formatNumber(total));

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }
}
