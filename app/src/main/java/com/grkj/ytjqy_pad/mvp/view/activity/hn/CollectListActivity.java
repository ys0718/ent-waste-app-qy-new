package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.App;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoBean;
import com.grkj.ytjqy_pad.mvp.model.QyAccountInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.ZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.CollectListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
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
 * Created by yusam on 2022/3/20.
 * 产废登记列表
 */

public class CollectListActivity extends BaseActivity<CollectListPresenter> implements CollectListPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;


    //称重登记
    @BindView(R.id.ll_add)
    LinearLayout ll_add;

    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    private int totalPage;
    private int pageNumber = 1;
    private List<CollectInfoBean> mList;//企业列表
    private CommonAdapter<CollectInfoBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout() {
        return R.layout.activity_collect_list;
    }

    @Override
    protected void initEventAndData() {
        ll_add.setVisibility(View.VISIBLE);
        SPUtils.put(App.getContext(),Constants.IS_HN_MENU, true);
        mPresenter.getZcssList();
        registerRxBus();
        initRecycleView();

        v_select_time.setOnTimeChangedListener(new SelectTimeView.OnTimeChangedListener() {
            @Override
            public void startTimeChanged(String startTime, String endTime) {
                params.put("startTime", startTime);
                params.put("endTime", endTime);
            }

        });

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
        initTopToolbar("社会源收集");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_add, R.id.ll_back,R.id.ll_search_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //新增
            case R.id.ll_add:
                startActivityForResult(new Intent(mContext, CollectInfoActivity.class), 10);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 搜索
            case R.id.ll_search_click:
                refreshLayout.autoRefresh();
                break;

        }
    }


    @Override
    protected CollectListPresenter createPresenter() {
        return new CollectListPresenter();
    }


    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }


    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_COLLECT_LIST, params);
        adapter = new CommonAdapter<CollectInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CollectInfoBean bean, final int position) {
                if (bean != null) {
                    setText(holder.getView(R.id.tv_item1), bean.csdw, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.sjdx, R.string.str_collect_item2);
                    setText(holder.getView(R.id.tv_item3), bean.cszrr, R.string.str_collect_item3);
                    setText(holder.getView(R.id.tv_item4), bean.lxPhone, R.string.str_collect_item4);
                    setText(holder.getView(R.id.tv_item5), getZcssmc(position), R.string.str_collect_item5);

                    FrameLayout iv_cup = holder.getView(R.id.iv_cup);

                    if (TextUtils.equals(bean.commitStatus,"1")){
                        iv_cup.setBackgroundResource(R.drawable.shape_green_round);
                        setText(holder.getView(R.id.tv_item6),"已入库" , R.string.empty_replace);
                    }else {
                        iv_cup.setBackgroundResource(R.drawable.shape_orange_round);
                        setText(holder.getView(R.id.tv_item6),"未入库" , R.string.empty_replace);
                    }


                    setText(holder.getView(R.id.tv_item8), bean.sjrq, R.string.str_collect_item1);




                    holder.getView(R.id.tv_item7).setVisibility(View.GONE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                startActivityForResult(new Intent(mContext, CollectInfoActivity.class)
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
        ((TextView) tv).setText(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value));
    }


    /**
     * 获取列表数据成功
     */
    @Override
    public void getListSuccess(final PageBean model, String url) {
        totalPage = model.getTotal();
        refreshLayout.finishRefresh();

        if (model != null) {
            fillRecycleView(model);
        }

    }

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<CollectInfoBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CollectInfoBean> LabelInfoBeans = GsonUtil.JsonToList(json, CollectInfoBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多列表成功
     */
    @Override
    public void getMoreListSuccess(PageBean model, String url) {
        refreshLayout.finishLoadmore();
        if (totalPage == pageNumber) {
            refreshLayout.finishLoadmore();
        }
        if (model != null) {
            fillRecycleView(model);
        }
    }

    @Override
    public void getTotalWeightSuccess(String total) {
    }

    private List<ZcssBean> zcssList = new ArrayList<>();

    @Override
    public void getZcssListSuccess(List<ZcssBean> list) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, ZcssBean.class);
        refreshLayout.autoRefresh();
    }

    private String getZcssmc(int j) {
        String result = "";
        if (zcssList != null && zcssList.size() != 0) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (TextUtils.equals(zcssList.get(i).id, mList.get(j).zcssId)) {
                    result = zcssList.get(i).ssmc;
                }
            }
        }
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


    private Subscription rxSubscription;        //    RxBus

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
