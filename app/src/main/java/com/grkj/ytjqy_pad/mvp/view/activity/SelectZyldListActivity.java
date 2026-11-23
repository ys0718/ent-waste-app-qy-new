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
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectWasteListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.GrEdittext;
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
 * 转移联单选择列表
 */

public class SelectZyldListActivity extends BaseActivity<SelectWasteListPresenter> implements SelectWasteListPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //搜索-联单编号
    @BindView(R.id.et_search)
    GrEdittext et_search;

    //搜索-产生单位名称
    @BindView(R.id.et_search2)
    GrEdittext et_search2;


    private int totalPage;
    private int pageNumber = 1;
    private List<ZyldBean> mList;//产废列表
    private CommonAdapter<ZyldBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();
    private ZyldBean selectBean = new ZyldBean();

    @Override
    protected int getLayout() {
        return R.layout.activity_select_zyld_list;
    }

    @Override
    protected void initEventAndData() {
        String csdwzym = getIntent().getStringExtra("csdwzym");
        if (!TextUtils.isEmpty(csdwzym)) {
            params.put("csdwzym", csdwzym);
            et_search2.setText(csdwzym);
        }

        initRecycleView();
        refreshLayout.autoRefresh();

        et_search2.setOnClearListener(new GrEdittext.OnClearListener() {
            @Override
            public void onClick() {
                params.put("csdwzym", "");
                refreshLayout.autoRefresh();
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
        initTopToolbar("选择转移联单");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }


    @OnClick({R.id.ll_save, R.id.ll_back, R.id.ll_search_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean", GsonUtil.GsonToString(selectBean));
                    setResult(9, result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "请选择转移联单！");
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 搜索
            case R.id.ll_search_click:
                params.put("number1", et_search.getText().toString());
                params.put("csdwzym", et_search2.getText().toString());
                refreshLayout.autoRefresh();
                break;


        }
    }


    @Override
    protected SelectWasteListPresenter createPresenter() {
        return new SelectWasteListPresenter();
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
        params.put(Constants.ROWS_KEY, Constants.ROWS_VALUE);
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.getHnUrl(ApiUrl.URL_SJ_INPUT_LD_LIST), params);
        adapter = new CommonAdapter<ZyldBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final ZyldBean bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).SELECT = false;
                            }
                            bean.SELECT = true;
                            selectBean = bean;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    holder.getView(R.id.rl_weight).setVisibility(View.GONE);
                    TextView tv_item2 = holder.getView(R.id.tv_item2);
                    tv_item2.setMaxLines(2);
                    setText(holder.getView(R.id.tv_item1), bean.number1, R.string.str_ldbh);
                    setText(holder.getView(R.id.tv_item2), bean.csdwzym, R.string.str_record_list19);
                    setText(holder.getView(R.id.tv_item3), bean.yssjSta, R.string.str_record_list17);
                    setText(holder.getView(R.id.tv_item4), bean.yssjEnd, R.string.str_record_list18);
                    setText(holder.getView(R.id.tv_item5), getFwlbmc(bean.fwxxList, true), R.string.str_input_item5_2);
                    setText(holder.getView(R.id.tv_item8), getFwlbmc(bean.fwxxList, false), R.string.str_record_list2);
                    TextView tv_item5 =  holder.getView(R.id.tv_item5);
                    tv_item5.setMaxLines(3);
                    TextView tv_item8 =  holder.getView(R.id.tv_item8);
                    tv_item8.setMaxLines(3);

                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }

    private String getFwlbmc(List<CommonProductInfoBean> fwxxList, boolean isName) {
        String result = "";
        for (int i = 0; i < fwxxList.size(); i++) {
            CommonProductInfoBean bean = fwxxList.get(i);

            if (isName) {
                if (TextUtils.isEmpty(result) || (!TextUtils.isEmpty(result) && !result.contains(bean.fwdlmc))) {
                    result += bean.fwxxmc + ",";
                }
            }

            if (!isName) {
                if (TextUtils.isEmpty(result) || (!TextUtils.isEmpty(result) && !result.contains(bean.fwdlbh))) {
                    result += bean.fwxlbh + ",";
                }
            }

        }
        // 去掉最后一位逗号
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);  // 去掉最后一个字符
        }
        return result;
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
    private void fillRecycleView(PageBean<ZyldBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<ZyldBean> LabelInfoBeans = GsonUtil.JsonToList(json, ZyldBean.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
