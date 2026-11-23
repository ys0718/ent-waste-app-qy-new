package com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.EnterpriseInfoBean;
import com.grkj.ytjqy_pad.mvp.model.QyAccountInfoListBean;
import com.grkj.ytjqy_pad.mvp.presenter.WasteInputListPresenter;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
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
 * 湖南---危废入库登记列表
 *
 * @author JueHongP
 */
public class WasteInputListActivity extends BaseActivity<WasteInputListPresenter> implements WasteInputListPresenter.View {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    /**
     * 废物总量
     */
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    /**
     * 当前页
     */
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    /**
     * 总页数总条数
     */
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 合计入库
     */
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;


    private int totalPage;
    private int pageNumber = 1;
    /**
     * 企业列表
     */
    private List<CommonProductInfoBean> mList;
    /**
     * 企业列表适配器
     */
    private CommonAdapter<CommonProductInfoBean> adapter;
    private Map<String, String> params = new HashMap<>();

    @Override
    protected WasteInputListPresenter createPresenter() {
        return new WasteInputListPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_input_list;
    }

    @Override
    protected void initEventAndData() {
        registerRxBus();

        /*台账跳转实体*/
        String tzDataStr = getIntent().getStringExtra(Constants.JUMP_KEY);//台账跳转实体
        if (!TextUtils.isEmpty(tzDataStr)) {
            QyAccountInfoListBean tzData = GsonUtil.GsonToBean(tzDataStr,QyAccountInfoListBean.class);
            ((TextView) findViewById(R.id.tv_title2)).setText(tzData.fwxxmc);
            if (!TextUtils.isEmpty(tzData.sj)) {
                params.put("date",tzData.sj);
                ((TextView) findViewById(R.id.tv_title2)).setText(tzData.sj);
            } else {
                params.put("day",tzData.day);
                if(!TextUtils.isEmpty(tzData.startTime)){
                    params.put("startTime",tzData.startTime);
                }

                if (!TextUtils.isEmpty(tzData.endTime)){
                    params.put("endTime",tzData.endTime);
                }
            }
            params.put("fwxxmc",tzData.fwxxmc);
            findViewById(R.id.ll_add).setVisibility(View.GONE);
            findViewById(R.id.ll_add2).setVisibility(View.GONE);
            findViewById(R.id.ll_add3).setVisibility(View.GONE);
        }else {
            params.put("startTime", DateTimeUtil.getCurrentTime());
            params.put("endTime",DateTimeUtil.getCurrentTime());
        }
        findViewById(R.id.ll_add2).setVisibility(View.GONE);
        findViewById(R.id.ll_add3).setVisibility(View.GONE);
        tv_title.setText("入库登记");
        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        tv_left_bottom2.setText("合计入库");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ll_add, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //手动入库
            case R.id.ll_add:
                goToActivity(new Intent(mContext, WasteInputInfoActivity.class), 10);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl.URL_HN_WASTE_INPUT_LIST), params, Constants.EV_HUNAN);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.rksj, R.string.str_record_list4);
                    setText(holder.getView(R.id.tv_item6), bean.rkl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), bean.jldw, R.string.str_jldw_input);


                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                goToActivity(new Intent(mContext, com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw.WasteInputInfoActivity.class)
                        .putExtra("isWeight", isWeightByLrfs(mList.get(i).lrfs))
                        .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(i))), 9);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });


        mRecyclerView.setAdapter(adapter);
    }

    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }

    @Override
    public void getListSuccess(PageBean pageBean, String url) {
        totalPage = pageBean.getTotal();
        refreshLayout.finishRefresh();
        tv_right_bottom2.setText(getString(R.string.str_page_total, pageBean.getTotal(), pageBean.getRecords()));

        if (pageBean != null) {
            fillRecycleView(pageBean);
        }
    }

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<CommonProductInfoBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean> LabelInfoBeans = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getMoreListSuccess(PageBean pageBean, String url) {
        refreshLayout.finishLoadmore();
        if (totalPage == pageNumber) {
            refreshLayout.finishLoadmore();
        }
        if (pageBean != null) {
            fillRecycleView(pageBean);
        }
    }

    @Override
    public void getTotalWeightSuccess(String total) {
        if (!TextUtils.isEmpty(total)) {
            tv_left_bottom.setText(total);
        } else {
            tv_left_bottom.setText(getResources().getText(R.string.common_zero));
        }
    }

    @Override
    public void getEnterpriseInfoSuccess(EnterpriseInfoBean bean) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //刷新列表
        if (resultCode == 9) {
            refreshLayout.autoRefresh();
        }
    }

    private Subscription rxSubscription;

    /**
     * 订阅
     */
    private void registerRxBus() {
        rxSubscription = RxBus.getDefault().toObservable(ChangeEvent.class).subscribe(new Action1<ChangeEvent>() {
            @Override
            public void call(ChangeEvent changeEvent) {
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
    public void onDestroy() {
        super.onDestroy();
        unregister();
    }

    /**
     * 取消订阅
     */
    private void unregister() {
        if (!rxSubscription.isUnsubscribed()) {
            rxSubscription.unsubscribe();
        }
    }
}
