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
import com.grkj.ytjqy_pad.mvp.model.StockListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.JyStockListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.NumberUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/20.
 * 库存列表
 */

public class JyStockListActivity extends BaseActivity<JyStockListPresenter> implements JyStockListPresenter.View{

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

    //筛选框
    @BindView(R.id.et_item1)
    TextView et_item1;


    //筛选框
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    private int totalPage;
    private int pageNumber = 1;
    private List<StockListBean> mList;//企业列表
    private CommonAdapter<StockListBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout(){
        return R.layout.activity_stock_list;
    }

    @Override
    protected void initEventAndData(){
        tv_title.setText("库存统计");
        tv_left_bottom2.setText("库存总量(吨)");

        initRecycleView();
        mPresenter.getZcssList();
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
        findViewById(R.id.tv_title2).setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_back,R.id.ll_search})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 筛选
            case R.id.ll_search:
                showZcssPopWindow();

                break;

        }
    }


    @Override
    protected JyStockListPresenter createPresenter(){
        return new JyStockListPresenter();
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
        String url = ApiUrl.URL_STOCK_LIST_GZ;
        String environment = SPUtils.get(mContext, Constants.ENVIRONMENT, "").toString();
        if (environment.equals("hn")){
            url = ApiUrl.URL_STOCK_LIST_HN;
        }
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,url,params);
        adapter = new CommonAdapter<StockListBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final StockListBean bean,final int position){
                if (bean != null) {
                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),bean.fwdlbh,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwdlmc,R.string.str_record_list1_2);
                    setText(holder.getView(R.id.tv_item4),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item6),bean.storages,R.string.empty_replace);
//                    setText(holder.getView(R.id.tv_item7),bean.jldw,R.string.str_jldw_kcl);
                    setText(holder.getView(R.id.tv_item7),"吨",R.string.str_jldw_kcl);
                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                startActivity(new Intent(mContext, JyStockDetailListActivity.class)
                        .putExtra("ZCSSLX",mList.get(i).zcsslx)
                        .putExtra("ZCSSBM",mList.get(i).zcssbm)
                        .putExtra("FWXXMC",mList.get(i).fwxxmc)
                        .putExtra("FWXLBH",mList.get(i).fwxlbh)
                        .putExtra("FWDLBH",mList.get(i).fwdlbh));
            }

            @Override
            public boolean onItemLongClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                return false;
            }
        });


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
    private void fillRecycleView(PageBean<StockListBean> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<StockListBean> LabelInfoBeans = GsonUtil.JsonToList(json,StockListBean.class);
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
    public void getZcssListSuccess(List<ZcssBean> list){
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json,ZcssBean.class);
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }

    private SelectPopupWindow selectPopupWindow;

    private List<ZcssBean> zcssList = new ArrayList<>();

    //初始贮存设施下拉弹窗
    private void showZcssPopWindow(){
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        selectPopupWindowList.add(new SelectPopupWindowBean("","全部"));
        for (int i = 0; i < zcssList.size(); i++) {
            ZcssBean mData = zcssList.get(i);
            String name = getString(R.string.str_record_fwlb2,mData.no,mData.ssmc);
            selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).no,name));
        }

        selectPopupWindow = new SelectPopupWindow(mContext,selectPopupWindowList,new SelectPopupWindow.OnClickItemListener(){
            @Override
            public void onDo(SelectPopupWindowBean model,String mType){
                ZcssBean bean = new ZcssBean();
                for (int i = 0; i < zcssList.size(); i++) {
                    if (zcssList.get(i).no.equals(model.ID)){
                        bean = zcssList.get(i);
                    }
                }
                params.put("zcssmc",bean.ssmc);
                et_item1.setText(model.NAME);
                refreshLayout.autoRefresh();
                selectPopupWindow.dismiss();
            }
        });

//        selectPopupWindow.show(mLayout, "", ll_search, selectPopupWindowList.size() + 1);

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size()+1;
        }
        selectPopupWindow.show(mLayout,"",ll_search,sort);


    }


}
