package com.grkj.ytjqy_pad.mvp.view.activity.jydw;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.QyAccountInfoListBean;
import com.grkj.ytjqy_pad.mvp.model.StatisticsInfoListBean;
import com.grkj.ytjqy_pad.mvp.presenter.JyAccountListPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductInputListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductOutputListActivity;
import com.grkj.ytjqy_pad.mvp.view.activity.ProductRecordListActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.NumberUtils;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.view.InputSelectView;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xujiaji.happybubble.BubbleDialog;
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
 * 经营台账列表
 */

public class JyAccountListActivity extends BaseActivity<JyAccountListPresenter> implements JyAccountListPresenter.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //入库量
    @BindView(R.id.tv_left_bottom)
    TextView tv_left_bottom;

    //开始结束时间
    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    //入库量文字
    @BindView(R.id.tv_left_bottom2)
    TextView tv_left_bottom2;

    //出库量
    @BindView(R.id.tv_left_bottom3)
    TextView tv_left_bottom3;

    //出库量文字
    @BindView(R.id.tv_left_bottom4)
    TextView tv_left_bottom4;

    //当前页
    @BindView(R.id.tv_right_bottom1)
    TextView tv_right_bottom1;

    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //总页数总条数
    @BindView(R.id.ll_left_bottom2)
    LinearLayout ll_left_bottom2;


    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //筛选列表
    @BindView(R.id.rv_btn)
    RecyclerView rv_btn;

    //类型筛选
    @BindView(R.id.isv_type)
    InputSelectView isv_type;

    //废物名称筛选
    @BindView(R.id.item_fwmc)
    EditText item_fwmc;


    private int totalPage;
    private int pageNumber = 1;
    private List<QyAccountInfoListBean> mList;//企业列表
    private List<SelectPopupWindowBean> mBtnList = new ArrayList<>();//筛选列表
    private CommonAdapter<QyAccountInfoListBean> adapter;//企业列表适配器
    private CommonAdapter<SelectPopupWindowBean> btnAdapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();


    private String selectType;
    private String tjType;

    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout(){
        return R.layout.activity_qy_account_list;
    }

    @Override
    protected void initEventAndData(){
        List<Select> typeList = new ArrayList<>();
        typeList.add(new Select("按天统计","1"));
        typeList.add(new Select("按汇总统计","2"));
        isv_type.setDatas(typeList);
        isv_type.setItem(typeList.get(0));
        tjType = typeList.get(0).getValue();
        params.put("tjType",tjType);
        isv_type.setOnItemChangedListener(new InputSelectView.OnItemChangedListener(){
            @Override
            public void itemChange(Select changeText){
                tjType = changeText.getValue();
                params.put("tjType",changeText.getValue());
                refreshLayout.autoRefresh();
            }
        });


        item_fwmc.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){

            }

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                params.put("fwxxmc",s.toString());
                refreshLayout.autoRefresh();
            }
        });


        initBtnRecycleView();
        initRecycleView();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();

        ((TextView)findViewById(R.id.tv_title)).setText("经营台账");

        ll_left_bottom2.setVisibility(View.VISIBLE);
        tv_left_bottom2.setText("入库(吨)");
        tv_left_bottom4.setText("出库(吨)");
        tv_left_bottom.setTextColor(getResources().getColor(R.color.color_young_blue));
        tv_left_bottom3.setTextColor(getResources().getColor(R.color.color_young_green));
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
        rv_btn.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar("经营台账");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        rv_btn.setLayoutManager(new GridLayoutManager(mContext,4));
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
    protected JyAccountListPresenter createPresenter(){
        return new JyAccountListPresenter();
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
        String url = ApiUrl.URL_JY_ACCOUNT_LIST_GZ;
        String environment = SPUtils.get(mContext, Constants.ENVIRONMENT, "").toString();
        if (environment.equals("hn")){
            url = ApiUrl.URL_JY_ACCOUNT_LIST_HN;;
        }
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,url,params);
        adapter = new CommonAdapter<QyAccountInfoListBean>(mContext,R.layout.item_common_account_info,mList){
            @Override
            protected void convert(ViewHolder holder,final QyAccountInfoListBean bean,final int position){
                if (bean != null) {

//                    if (!TextUtils.isEmpty(selectType) && "day".equals(selectType)) {
//                        setText(holder.getView(R.id.tv_item1),bean.fwdlmc,R.string.empty_replace);
//                        bean.isToday = true;
//                        bean.sj = DateTimeUtil.getCurrentTime();
//                    } else {
//                        setText(holder.getView(R.id.tv_item1),bean.sj,R.string.empty_replace);
//                        bean.isToday = false;
//                    }

                    holder.getView(R.id.ll_cf).setVisibility(View.GONE);
                    holder.getView(R.id.line1).setVisibility(View.GONE);

                    setText(holder.getView(R.id.tv_item2),bean.csl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item3),bean.rkl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item4),bean.ckl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item5),bean.zcl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item11),getString(R.string.str_record_fwlb2,bean.fwdlbh,bean.fwdlmc),R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item12),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item13),bean.fwxxmc,R.string.empty_replace);

                    if ("1".equals(isv_type.getSelectItem().getValue())) {
                        holder.getView(R.id.tv_item1).setVisibility(View.VISIBLE);
                        setText(holder.getView(R.id.tv_item1),bean.sj,R.string.empty_replace);
                    } else {
                        holder.getView(R.id.tv_item1).setVisibility(View.GONE);
                        bean.startTime = params.get("startTime");
                        bean.endTime = params.get("endTime");
                        bean.day = "all";
                    }
                    String jumpStr = GsonUtil.GsonToString(bean);

                    //产生
                    holder.setOnClickListener(R.id.tv_item2,new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            startActivity(new Intent(mContext, ProductRecordListActivity.class)
                                    .putExtra(Constants.JUMP_KEY,jumpStr));
                        }
                    });

                    String environment = String.valueOf(SPUtils.get(mContext, Constants.ENVIRONMENT, ""));


                    //入库
                    holder.setOnClickListener(R.id.tv_item3,new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Class jumClass = WasteInputListActivity.class;
                            if (environment.equals("hn")){
                                jumClass = com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw.WasteInputListActivity.class;
                            }
                            startActivity(new Intent(mContext,jumClass)
                                    .putExtra(Constants.JUMP_KEY,jumpStr));
                        }
                    });
                    //出库
                    holder.setOnClickListener(R.id.tv_item4,new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            Class jumClass = WasteOutputListActivity.class;
                            if (environment.equals("hn")){
                                jumClass = com.grkj.ytjqy_pad.mvp.view.activity.hn.jydw.WasteOutputListActivity.class;
                            }
                            startActivity(new Intent(mContext,jumClass)
                                    .putExtra(Constants.JUMP_KEY,jumpStr));
                        }
                    });

                    showTipsCallback(holder.getView(R.id.tv_item5));

                }
            }
        };

        mRecyclerView.setAdapter(adapter);
    }



    /**
     * 筛选按钮列表初始化
     */
    private void initBtnRecycleView(){
        mBtnList = new ArrayList<>();
        mBtnList.add(new SelectPopupWindowBean("all","全部",true));
        mBtnList.add(new SelectPopupWindowBean("month","本月",false));
        mBtnList.add(new SelectPopupWindowBean("week","本周",false));
        mBtnList.add(new SelectPopupWindowBean("day","当天",false));
        addTimeCallback(0);
        btnAdapter = new CommonAdapter<SelectPopupWindowBean>(mContext,R.layout.item_select_btn,mBtnList){
            @Override
            protected void convert(ViewHolder holder,final SelectPopupWindowBean bean,final int position){
                if (bean != null) {
                    TextView tv_value = holder.getView(R.id.tv_value);
                    tv_value.setText(bean.NAME);
                    tv_value.setSelected(bean.SELECT);
                }
            }
        };
        btnAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int position){
                for (int i = 0; i < mBtnList.size(); i++) {
                    mBtnList.get(i).SELECT = false;
                }
                selectType = mBtnList.get(position).ID;
                mBtnList.get(position).SELECT = true;
                btnAdapter.notifyDataSetChanged();

                params.put("type",selectType);
                refreshLayout.autoRefresh();

                addTimeCallback(position);

            }

            @Override
            public boolean onItemLongClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                return false;
            }
        });

        rv_btn.setAdapter(btnAdapter);
    }

    private void addTimeCallback(int position){
        if ("all".equals(mBtnList.get(position).ID)) {
            v_select_time.setVisibility(View.VISIBLE);
            v_select_time.setOnTimeChangedListener(new SelectTimeView.OnTimeChangedListener(){
                @Override
                public void startTimeChanged(String startTime,String endTime){
                    params.put("startTime",startTime);
                    params.put("endTime",endTime);

                    if (TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime) || !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                        refreshLayout.autoRefresh();
                    }
                }
            });
        } else {
            v_select_time.setVisibility(isLandscape?View.INVISIBLE:View.GONE);
            params.put("startTime","");
            params.put("endTime","");
        }
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
    private void fillRecycleView(PageBean<QyAccountInfoListBean> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<QyAccountInfoListBean> LabelInfoBeans = GsonUtil.JsonToList(json,QyAccountInfoListBean.class);
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
        tv_left_bottom.setText(NumberUtils.formatNumber(model.rkl));
        tv_left_bottom3.setText(NumberUtils.formatNumber(model.ckl));
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }


}
