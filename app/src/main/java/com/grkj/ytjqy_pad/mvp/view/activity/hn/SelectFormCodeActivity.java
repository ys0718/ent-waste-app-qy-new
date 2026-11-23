package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectFormCodePresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
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
 * 选择转移联单编号
 *
 * @author seny
 */
public class SelectFormCodeActivity extends BaseActivity<SelectFormCodePresenter> implements SelectFormCodePresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

//    /**
//     * 暂无数据
//     */
//    @BindView(R.id.tv_no_data)
//    TextView tv_no_data;

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
     * 主布局
     */
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    /**
     * 搜索布局
     */
    @BindView(R.id.ll_input)
    LinearLayout ll_input;

    /**
     * 联单编号
     */
    @BindView(R.id.et_search)
    EditText et_search;


    private int totalPage;
    private int pageNumber = 1;
    /**
     * 废物信息列表
     */
    private List<ZyldBean> mList;
    /**
     * 废物信息列表
     */
    private List<ZyldBean> allList;
    /**
     * 废物信息列表适配器
     */
    private CommonAdapter<ZyldBean> adapter;
    private  Map<String, String> params = new HashMap<>();
    private ZyldBean selectBean = new ZyldBean();

    private String zcssId = "";
    private String url = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_select_list;
    }

    @Override
    protected void initEventAndData() {
        findViewById(R.id.ll_home).setVisibility(View.GONE);
        String mDataStr = getIntent().getStringExtra("mData");
        CommonProductInfoBean mData = GsonUtil.GsonToBean(mDataStr, CommonProductInfoBean.class);
        String dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();
        params.put("page", "1");
        params.put("rows", "999");
        params.put("dwid", dwid);
        params.put("actids", "1,2,3,4,5,5_5");//字典参数
        params.put("fwdlbh", mData.fwdlbh);
        params.put("fwxlbh", mData.fwxlbh);
        params.put("fwxxmc", mData.fwxxmc);
        initRecycleView();
        refreshLayout.autoRefresh();
        ll_input.setVisibility(View.VISIBLE);

    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop("选择转移联单");
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

    }


    @Override
    protected void initPortraitView() {
        super.initPortraitView();

        initTopToolbar("选择转移联单");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ll_save, R.id.ll_back,R.id.ll_search_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean", GsonUtil.GsonToString(selectBean));
                    setResult(20, result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext, "请选择转移联单！");
                }

                break;

            // 搜索
            case R.id.ll_search_click:
                String searchStr = et_search.getText().toString();
                if (allList.size()!=0 && !TextUtils.isEmpty(searchStr)){
                    List<ZyldBean> newList =new ArrayList<>();
                    for (int i = 0; i < allList.size() ; i++) {
                        allList.get(i).SELECT = false;
                        if (allList.get(i).number1.contains(searchStr)){
                            newList.add(allList.get(i));
                        }
                    }
                    mList.clear();
                    mList.addAll(newList);
                }else {
                    mList.clear();
                    mList.addAll(allList);
                }

                adapter.notifyDataSetChanged();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

            default:
                break;

        }
    }


    @Override
    protected SelectFormCodePresenter createPresenter() {
        return new SelectFormCodePresenter();
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
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, ApiUrl.URL_ZYLD_LIST, params);
        adapter = new CommonAdapter<ZyldBean>(mContext, R.layout.item_warming_info, mList) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void convert(ViewHolder holder, final ZyldBean bean, final int position) {
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout itemLayout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT ? R.drawable.checkbox_theme : R.mipmap.ic_unselected);

                    TextView tv_item1 = holder.getView(R.id.tv_item1);
//                    tv_item1.setTextColor(getResources().getColor(R.color.colorTextBlack));
                    tv_item1.setTextAppearance(R.style.EditTextStyle3);

                    setText(holder.getView(R.id.tv_item1), bean.number1, R.string.empty_replace);
                    itemLayout.setOnClickListener(v -> {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).SELECT = false;
                        }
                        bean.SELECT = true;
                        selectBean = bean;
                        adapter.notifyDataSetChanged();
                    });


                }
            }
        };


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
    private void fillRecycleView(PageBean<ZyldBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<ZyldBean> LabelInfoBeans = GsonUtil.JsonToList(json, ZyldBean.class);
        mList.addAll(LabelInfoBeans);
        allList = new ArrayList<>();
        allList.addAll(LabelInfoBeans);

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

}
