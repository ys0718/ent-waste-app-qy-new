package com.grkj.ytjqy_pad.mvp.view.activity.xnbq;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.EnterpriseInfoBean;
import com.grkj.ytjqy_pad.mvp.model.QyAccountInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.xnbq.GfYbqyTzCkZxlyczXnbq;
import com.grkj.ytjqy_pad.mvp.view.activity.czdw.InputLargeScanActivity;
import com.grkj.ytjqy_pad.mvp.presenter.XnbqCkzxlyczListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.NumberUtils;
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
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by yusam on 2026/10/28.
 * 虚拟标签-立产立清-出库自行利用处置列表
 */

public class XnbqCkzxlyczListActivity extends BaseActivity<XnbqCkzxlyczListPresenter> implements XnbqCkzxlyczListPresenter.View{

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

    //手动入库
    @BindView(R.id.tv_right1)
    TextView tv_right1;

    //手动入库
    @BindView(R.id.iv_right1)
    ImageView iv_right1;


    //合计入库
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;




    private int totalPage;
    private int pageNumber = 1;
    private List<GfYbqyTzCkZxlyczXnbq> mList;//企业列表
    private CommonAdapter<GfYbqyTzCkZxlyczXnbq> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();


    @Override
    protected int getLayout(){
        return R.layout.activity_input_list;
    }

    @Override
    protected void initEventAndData(){
        registerRxBus();

        findViewById(R.id.ll_add2).setVisibility(View.GONE);
//        findViewById(R.id.ll_add3).setVisibility(View.GONE);
        tv_title.setText("出库自行利用处置");
        ((TextView)findViewById(R.id.tv_right1)).setText("新增");

        initRecycleView();
        refreshLayout.autoRefresh();
    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
        findViewById(R.id.tv_title2).setVisibility(View.INVISIBLE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        findViewById(R.id.tv_title2).setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_add,R.id.ll_back,R.id.ll_add2,R.id.ll_add3})
    public void onViewClicked(View view){
        switch (view.getId()) {

            //手动入库
            case R.id.ll_add:
//                startActivityForResult(new Intent(mContext,XnbqLclclInfoActivity.class),10);
                goToActivity(new Intent(mContext, XnbqCkzxlyczInfoActivity.class),10);
                break;

            //称重入库
            case R.id.ll_add3:
//                startActivityForResult(new Intent(mContext,XnbqLclclInfoActivity.class)
//                        .putExtra("isWeight",true),10);

                goToActivity(new Intent(mContext, XnbqCkzxlyczInfoActivity.class)
                        .putExtra("isWeight",true),10);
                break;

            //扫码入库
            case R.id.ll_add2:
//                startActivityForResult(new Intent(mContext,InputLargeScanActivity.class)
//                                .putExtra("isCzdw",isCzdw)//是否为处置单位
//                        ,10);
                goToActivity(new Intent(mContext, InputLargeScanActivity.class)
                        ,10);

                break;
            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }


    @Override
    protected XnbqCkzxlyczListPresenter createPresenter(){
        return new XnbqCkzxlyczListPresenter();
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
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,ApiUrl.getHnUrl(ApiUrl.URL_XNBQ_CKZXLICZ_LIST),params);
        adapter = new CommonAdapter<GfYbqyTzCkZxlyczXnbq>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final GfYbqyTzCkZxlyczXnbq bean,final int position){
                if (bean != null) {
                    holder.setText(R.id.tv_item1,TextUtils.isEmpty(bean.fwxxmc)?getString(R.string.common_empty):bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2,bean.fwdlbh,bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),groupFwlb,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4),bean.zxlyczsj,R.string.str_jytz_list7);
                    setText(holder.getView(R.id.tv_item5), bean.csSzsbm, R.string.str_zyld_item31);
                    setText(holder.getView(R.id.tv_item6),bean.zxlyczl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7),bean.jldw,R.string.str_jldw_zxlyygl);


                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int i){
//                startActivityForResult(new Intent(mContext,XnbqLclclInfoActivity.class)
//                        .putExtra("isWeight",isWeightByLrfs(mList.get(i).lrfs))
//                        .putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(mList.get(i))),9);
                goToActivity(new Intent(mContext, XnbqCkzxlyczInfoActivity.class)
                        .putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(mList.get(i))),9);
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

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value)));
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
    private void fillRecycleView(PageBean<GfYbqyTzCkZxlyczXnbq> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<GfYbqyTzCkZxlyczXnbq> LabelInfoBeans = GsonUtil.JsonToList(json,GfYbqyTzCkZxlyczXnbq.class);
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
    public void getTotalWeightSuccess(StatisticsInfoListBean model){
        tv_left_bottom.setText(NumberUtils.formatNumber(model.CSL));
    }

    @Override
    public void getEnterpriseInfoSuccess(EnterpriseInfoBean bean){

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


    private Subscription rxSubscription;        //    RxBus

    /**
     * 订阅
     */
    private void registerRxBus(){
        rxSubscription = RxBus.getDefault().toObservable(ChangeEvent.class).subscribe(new Action1<ChangeEvent>(){
            @Override
            public void call(ChangeEvent changeEvent){
                try {
                    refreshLayout.autoRefresh();
                    if (!TextUtils.isEmpty(changeEvent.getMsg())) {
                        toastShow(changeEvent.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregister();
    }

    /**
     * 取消订阅
     */
    private void unregister(){
        if (!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }

}
