package com.grkj.ytjqy_pad.mvp.view.activity.czdw;

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
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.QyAccountInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.mvp.presenter.ProductOutputListPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductOutputInfoActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.socks.library.KLog;
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
 * 出库列表
 */

public class ProductOutputListActivity extends BaseActivity<ProductOutputListPresenter> implements ProductOutputListPresenter.View {

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

    //手动出库
    @BindView(R.id.tv_right1)
    TextView tv_right1;

    //手动出库
    @BindView(R.id.iv_right1)
    ImageView iv_right1;

    //扫码出库
    @BindView(R.id.tv_right2)
    TextView tv_right2;

    //扫码出库
    @BindView(R.id.iv_right2)
    ImageView iv_right2;

    //扫码出库
    @BindView(R.id.ll_add2)
    LinearLayout ll_add2;

    //合计出库
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;


    private int totalPage;
    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//企业列表
    private CommonAdapter<CommonProductInfoBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout() {
        return R.layout.activity_output_list;
    }

    @Override
    protected void initEventAndData() {
        registerRxBus();
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
        }
        tv_title.setText("出库登记");
        initRecycleView();
        refreshLayout.autoRefresh();

        mPresenter.getButtonList();

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
        tv_left_bottom2.setText("合计出库");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_add, R.id.ll_add2,R.id.ll_add3, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //新增
            case R.id.ll_add:
                startActivityForResult(new Intent(mContext, ProductOutputInfoActivity.class).putExtra("isCzdw",true), 10);
                break;

            //称重入库
            case R.id.ll_add3:
                startActivityForResult(new Intent(mContext, ProductOutputInfoActivity.class)
                        .putExtra("isCzdw",true)
                        .putExtra("isWeight", true), 10);
                break;

            //扫码出库
            case R.id.ll_add2:


//                if (isLandscape) {
//                    startActivityForResult(new Intent(mContext, OutputScanActivity.class), 10);
//                } else {
//                    PermissionUtils.requestCameraAndScan(mContext, new PermissionUtils.OnBooleanListener() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
//                }

                startActivityForResult(new Intent(mContext, OutputLargeScanActivity.class), 10);

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected ProductOutputListPresenter createPresenter() {
        return new ProductOutputListPresenter();
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
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl. URL_PRODUCT_OUTPUT_LIST), params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.cksj, R.string.str_record_list5);
                    setText(holder.getView(R.id.tv_item6), bean.ckl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7),bean.jldw,R.string.str_jldw_output);

                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                startActivityForResult(new Intent(mContext, ProductOutputInfoActivity.class)
                        .putExtra("isCzdw", true)
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
        ((TextView) tv).setText(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value));
    }


    /**
     * 获取列表数据成功
     */
    @Override
    public void getListSuccess(final PageBean model, String url) {
        totalPage = model.getTotal();
        refreshLayout.finishRefresh();
        tv_right_bottom2.setText(getString(R.string.str_page_total, model.getTotal(), model.getRecords()));

        if (model != null) {
            fillRecycleView(model);
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
    public void getTotalWeightSuccess(StatisticsInfoListBean model) {
        if (!TextUtils.isEmpty(model.ckl_day)) {
            tv_left_bottom.setText(model.ckl_day);
        } else {
            tv_left_bottom.setText(getResources().getText(R.string.common_zero));
        }

    }

    @Override
    public void getOutputInfoSuccess(String bean) {
        if (!TextUtils.isEmpty(bean)) {
            finish();
            startActivity(new Intent(mContext, ProductOutputInfoActivity.class)
                    .putExtra("IS_SCAN", true)
                    .putExtra(Constants.JUMP_KEY, bean));
        } else {
            ToastUtil.showToast(mContext, "请扫描正确的二维码！");
        }
    }

    @Override
    public void isShowPlck(boolean isShowPlck) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }

        if (resultCode == Constants.SCAN_REQUEST_CODE) {
            String scanCodeResult = data.getStringExtra("ScanCodeResult");
            KLog.e("onActivityResult===" + scanCodeResult);
            mPresenter.getOutputInfo(scanCodeResult);
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
