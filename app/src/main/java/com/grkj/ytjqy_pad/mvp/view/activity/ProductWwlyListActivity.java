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
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.WwlyListBean;
import com.grkj.ytjqy_pad.mvp.presenter.ProductWwlyListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
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
 * 委外利用列表
 */

public class ProductWwlyListActivity extends BaseActivity<ProductWwlyListPresenter> implements ProductWwlyListPresenter.View {


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
    private List<WwlyListBean> mList;//企业列表
    private CommonAdapter<WwlyListBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();
    private String typeName = "委外利用";
    private String typeValue = "R";

    /**
     * 地址选择字段结束
     **/
    @Override
    protected int getLayout() {
        return R.layout.activity_wwly_list;
    }

    @Override
    protected void initEventAndData() {
        registerRxBus();
        tv_title.setText(typeName + "登记");


        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        tv_left_bottom2.setText(typeName + "量(吨)");
//        tv_right1.setText("手动登记");
//        tv_right2.setText("扫码登记");
//        iv_right1.setImageResource(R.mipmap.ic_hand_white);
//        iv_right2.setImageResource(R.mipmap.ic_output_scan);
//        ll_add2.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        tv_left_bottom2.setText(typeName + "量(吨)");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_add, R.id.ll_add2, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //新增
            case R.id.ll_add:
                startActivityForResult(new Intent(mContext, ProductWwlyInfoActivity.class)
                        .putExtra("typeValue",typeValue)
                        .putExtra("typeName",typeName), 10);
                break;

            //扫码入库
            case R.id.ll_add2:
                startActivityForResult(new Intent(mContext, WwlyLargeScanActivity.class)
                        .putExtra("typeValue",typeValue)
                        .putExtra("typeName",typeName), 10);
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected ProductWwlyListPresenter createPresenter() {
        return new ProductWwlyListPresenter();
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
        params.put("lyczfsdlbh", typeValue);
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_PRODUCT_WWLY_LIST, params);
        adapter = new CommonAdapter<WwlyListBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final WwlyListBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item4), bean.ccsj, R.string.str_record_list12);
                    setText(holder.getView(R.id.tv_item6), bean.wwlyczl, R.string.empty_replace);
                    holder.setText(R.id.tv_item7,getString(R.string.str_jldw_zxww,typeName.substring(typeName.length()-2),bean.jldw));

                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                startActivityForResult(new Intent(mContext, ProductWwlyInfoActivity.class)
                        .putExtra("typeValue",typeValue)
                        .putExtra("typeName",typeName)
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
    private void fillRecycleView(PageBean<WwlyListBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<WwlyListBean> LabelInfoBeans = GsonUtil.JsonToList(json, WwlyListBean.class);
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
        tv_left_bottom.setText(total);
    }

    @Override
    public void getOutputInfoSuccess(String bean) {
        if (!TextUtils.isEmpty(bean)) {
            finish();
            startActivity(new Intent(mContext, ProductWwlyInfoActivity.class)
                    .putExtra("IS_SCAN", true)
                    .putExtra(Constants.JUMP_KEY, bean));
        } else {
            ToastUtil.showToast(mContext, "请扫描正确的二维码！");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }

//        if (resultCode == Constants.SCAN_REQUEST_CODE) {
//            String scanCodeResult = data.getStringExtra("ScanCodeResult");
//            KLog.e("onActivityResult===" + scanCodeResult);
//            mPresenter.getOutputInfo(scanCodeResult);
//        }

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
