package com.grkj.ytjqy_pad.mvp.view.activity;

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
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.presenter.PrintListPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.jydw.WasteInputInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.jydw.WasteOutputInfoActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.view.InputSelectView;
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

/**
 * Created by yusam on 2023/2/15.
 * 打印列表
 */

public class PrintListActivity extends BaseActivity<PrintListPresenter> implements PrintListPresenter.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;


    //接收单位名称
    @BindView(R.id.isv_fwxxmc)
    InputSelectView isv_fwxxmc;

    //登记类型
    @BindView(R.id.isv_djlx)
    InputSelectView isv_djlx;

    //时间选择
    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;


    private int totalPage;
    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//标签列表
    private CommonAdapter<CommonProductInfoBean> adapter;//标签列表适配器
    private Map<String, String> params = new HashMap<>();
    private String tzType;

    private String environment;

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout() {
        return R.layout.activity_print_list;
    }

    @Override
    protected void initEventAndData() {
        tv_title.setText("补打标签");
        environment = String.valueOf(SPUtils.get(mContext, Constants.ENVIRONMENT, ""));

        tzType = "cf";
        params.put("tzType", tzType);
        initRecycleView();
        Map<String, String> wltxParam = new HashMap<>();
        wltxParam.put("types", "物理特性");

//        List<Select> tempList = new ArrayList<>();
//        tempList.add(new Select("产废登记", "cf"));
//        tempList.add(new Select("入库登记", "rk"));
//        tempList.add(new Select("出库登记", "ck"));
//
//        isv_djlx.setDatas(tempList);
////        isv_djlx.setEnabled(false);
//        isv_djlx.setItem(tempList.get(0));
//        isv_djlx.setOnItemChangedListener(new InputSelectView.OnItemChangedListener() {
//            @Override
//            public void itemChange(Select item) {
//                params.put("tzType", item.getValue());
//            }
//        });

        mPresenter.getParamList(wltxParam);
        mPresenter.getTypeList();
        refreshLayout.autoRefresh();
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_back, R.id.ll_search_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 返回
            case R.id.ll_back:
                finish();
                break;


            // 搜索按钮
            case R.id.ll_search_click:
                refreshLayout.autoRefresh();
                break;

        }
    }


    @Override
    protected PrintListPresenter createPresenter() {
        return new PrintListPresenter();
    }


    @Override
    public void showError(String msg) {
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

    private String url = "";

    /**
     * 标签列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        String URL = getSelectUrl();
        pageNumber = mPresenter.initRefresh(refreshLayout, mContext, URL, params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext, R.layout.item_common_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CommonProductInfoBean bean, final int position) {
                if (bean != null) {
//                    url = ApiUrl.getHnUrl(ApiUrl. URL_LABEL_INFO_CS);
                    String time = "产废时间：" + bean.cssj;
                    int unitResStr = R.string.str_jldw_record;
                    String pch = bean.cspch;
                    switch (tzType) {
                        case "rk"://入库
                        case "gzRk":
                        case "hnRk":
                            bean.csl = bean.rkl;
//                            url = ApiUrl.getHnUrl(ApiUrl. URL_LABEL_INFO);
                            time = "入库时间：" + bean.rksj;
                            unitResStr = R.string.str_jldw_input;
                            pch = bean.rkpch;
                            break;
                        case "ck"://出库
                        case "gzCk":
                        case "hnCk":
                            bean.csl = bean.ckl;
//                            url = ApiUrl.getHnUrl(ApiUrl. URL_LABEL_INFO_CK);
                            time = "出库时间：" + bean.cksj;
                            unitResStr = R.string.str_jldw_output;
                            pch = bean.ckpch;
                            break;
                    }

                    setText(holder.getView(R.id.tv_item1), bean.fwxxmc, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.fwdlbh, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3), bean.fwdlmc, R.string.str_record_list1_2);
                    setText(holder.getView(R.id.tv_item4), pch, R.string.str_bdbq_pch);
                    setText(holder.getView(R.id.tv_item5), time, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item6), bean.csl, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7), bean.jldw, unitResStr);
                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                /*mPresenter.getLabelInfo(mList.get(i).qrcodeValue, url);*/
                goToActivity(new Intent(mContext, getTargetActivity())
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
     * 获取请求接口
     */
    private String getSelectUrl() {
        String url;//默认

        switch (tzType) {
            case "gzRk":
                url = ApiUrl.URL_WASTE_INPUT_LIST;
                break;
            case "gzCk":
                url = ApiUrl.URL_WASTE_OUTPUT_LIST;
                break;
            case "hnRk":
                url = ApiUrl.URL_HN_WASTE_INPUT_LIST;
                break;
            case "hnCk":
                url = ApiUrl.URL_HN_WASTE_OUTPUT_LIST;
                break;
            default:
                url = ApiUrl.URL_PRINT_LIST;
                break;
        }

        return url;
    }

    /**
     * 获取列表跳转目标页面
     */
    private Class getTargetActivity() {
        Class targetActivity = ProductRecordInfoActivity.class;
        switch (tzType) {
            case "cf":
                targetActivity = ProductRecordInfoActivity.class;
                break;
            case "rk":
                targetActivity = ProductInputInfoActivity.class;
                break;
            case "ck":
                targetActivity = ProductOutputInfoActivity.class;
                break;
            case "gzRk":
                targetActivity = WasteInputInfoActivity.class;
                break;
            case "gzCk":
                targetActivity = WasteOutputInfoActivity.class;
                break;
            case "hnRk":
                targetActivity = com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw.WasteInputInfoActivity.class;
                break;
            case "hnCk":
                targetActivity = com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw.WasteOutputInfoActivity.class;
                break;
            default:
                break;
        }

        return targetActivity;
    }


    private List<CommonProductInfoBean> receiverList = new ArrayList<>();
    private List<CommonProductInfoBean> temFromList = new ArrayList<>();

    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list) {
        String json = GsonUtil.GsonToString(list);
        receiverList = GsonUtil.JsonToList(json, CommonProductInfoBean.class);
        List<Select> tempList = new ArrayList<>();
        for (int i = 0; i < receiverList.size(); i++) {
            tempList.add(new Select(receiverList.get(i).fwxxmc, receiverList.get(i).fwxxmc));
        }

        isv_fwxxmc.setDatas(tempList);
//        isv_fwxxmc.setHint("废物详细名称");
        isv_fwxxmc.setOnTextChangedListener(new InputSelectView.OnTextChangedListener() {
            @Override
            public void textChange(String changeText) {
                params.put("fwxxmc", changeText);
            }
        });

        isv_djlx.setDatas(setSelectTypeList());
//        isv_djlx.setEnabled(false);
        isv_djlx.setItem(setSelectTypeList().get(0));
        isv_djlx.setOnItemChangedListener(new InputSelectView.OnItemChangedListener() {
            @Override
            public void itemChange(Select item) {
                tzType = item.getValue();
                //经营台账出入库列表无需参数
                if (!tzType.contains("Rk") && !tzType.contains("Ck")) {
                    params.put("tzType", item.getValue());
                }
                initRecycleView();
                refreshLayout.autoRefresh();
            }
        });

    }

    private List<Select> setSelectTypeList() {
        List<Select> selectList = new ArrayList<>();
        selectList.add(new Select("产废登记", "cf"));
        selectList.add(new Select("入库登记", "rk"));
        selectList.add(new Select("出库登记", "ck"));

        /*switch (environment) {
            case "gz":
                selectList.add(new Select("经营入库", "gzRk"));
                selectList.add(new Select("经营出库", "gzCk"));
                break;
            case "hn":
                selectList.add(new Select("经营入库", "hnRk"));
                selectList.add(new Select("经营出库", "hnCk"));
                break;
            //测试
            case "dev":
                selectList.add(new Select("广东入登", "gzRk"));
                selectList.add(new Select("广东出登", "gzCk"));
                selectList.add(new Select("湖南入登", "hnRk"));
                selectList.add(new Select("湖南出登", "hnCk"));
                break;
            default:
                break;
        }*/

        return selectList;
    }

    protected List<ParamListBean> allParamList = new ArrayList<>();

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
    }

    /*
     * 通用转义
     * */
    protected String commonTransfer(String code, String type) {
        String result = "";
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;
            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    if (!TextUtils.isEmpty(code) && code.equals(childList.get(j).code)) {
                        result = childList.get(j).value;
                    }
                }
            }
        }
        return result;
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
    public void getLabelInfoSuccess(Object result) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(result), LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ, "物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
//        startActivityForResult(new Intent(mContext, BasePrintLabelActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(data)), 10);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
