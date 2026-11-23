package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.zyld.JsdwxxBean;
import com.grkj.ytjqy_pad.mvp.presenter.SelectJsdwListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.view.GrRecyclerView;
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
 * Created by yusam on 2026/9/10.
 * 选择接收单位列表
 */

public class SelectJsdwListActivity extends BaseActivity<SelectJsdwListPresenter> implements SelectJsdwListPresenter.View{

    @BindView(R.id.mRecyclerView)
    GrRecyclerView mRecyclerView;

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

    //贮存设备名称-文字
    @BindView(R.id.et_item1)
    TextView et_item1;

    //贮存设备名称-布局
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //废物名称-文字
    @BindView(R.id.et_item2)
    TextView et_item2;

    //废物名称-布局
    @BindView(R.id.ll_search2)
    LinearLayout ll_search2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    // 入库批次搜索
    @BindView(R.id.et_search)
    EditText et_search;

    //入库批次搜索
    @BindView(R.id.ll_input)
    LinearLayout ll_input;



    private int pageNumber = 1;
    private List<JsdwxxBean> mList;//产废列表
    private CommonAdapter<JsdwxxBean> adapter;//产废列表适配器
    private Map<String, String> params = new HashMap<>();
    private JsdwxxBean selectBean = new JsdwxxBean();

    private String title = "";
    private String url = "";
    private boolean isFromInput = true;

    @Override
    protected int getLayout(){
        return R.layout.activity_select_jsdw_list;
    }

    @Override
    protected void initEventAndData(){
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        initRecycleView();
        String lastJsdwBeanStr = SPUtils.get(mContext, Constants.LAST_JSDW_BEAN, "").toString();
        if (!TextUtils.isEmpty(lastJsdwBeanStr)){
            JsdwxxBean lastBean = GsonUtil.GsonToBean(lastJsdwBeanStr, JsdwxxBean.class);
            lastBean.SELECT = false;
            mList.add(lastBean);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop(title);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        initTopToolbar(title);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_save,R.id.ll_back, R.id.ll_search_click})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 确定
            case R.id.ll_save:
                if (selectBean.SELECT) {
                    Intent result = new Intent();
                    result.putExtra("selectBean",GsonUtil.GsonToString(selectBean));
                    selectBean.isLastSelected = true;
                    selectBean.SELECT = false;
                    SPUtils.put(mContext, Constants.LAST_JSDW_BEAN,GsonUtil.GsonToString(selectBean));
                    setResult(11,result);
                    finish();
                } else {
                    ToastUtil.showToast(mContext,"请选择"+title);
                }

                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;


            // 搜索
            case R.id.ll_search_click:
                String searchStr = et_search.getText().toString();
                params.put("jydwmc",searchStr);
                refreshLayout.autoRefresh();

                break;


        }
    }


    @Override
    protected SelectJsdwListPresenter createPresenter(){
        return new SelectJsdwListPresenter();
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
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,url,params);
        adapter = new CommonAdapter<JsdwxxBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final JsdwxxBean bean,final int position){
                if (bean != null) {
                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            for (int i = 0; i < mList.size(); i++) {
                                mList.get(i).SELECT = false;
                            }
                            bean.SELECT = true;
                            selectBean = bean;
                            adapter.notifyDataSetChanged();
                        }
                    });

                    String jydwmc = bean.jydwmc;
                    TextView tvItem1 = holder.getView(R.id.tv_item1);
                    if (bean.isLastSelected) {
                        //“(上次选中)”后缀以红色显示
                        String suffix = "(上次选中)";
                        SpannableString spannableString = new SpannableString(jydwmc + suffix);
                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#e51e5f")),
                                spannableString.length() - suffix.length(), spannableString.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvItem1.setVisibility(View.VISIBLE);
                        tvItem1.setText(spannableString);
                    } else {
                        setText(tvItem1, jydwmc, R.string.empty_replace);
                    }
                    setText(holder.getView(R.id.tv_item2),bean.lxrsj,R.string.str_zyld_item25);
                    setText(holder.getView(R.id.tv_item3),bean.lxr,R.string.str_zyld_item28);
                    setText(holder.getView(R.id.tv_item4),bean.xkzbh,R.string.str_zyld_item27);

                    holder.getView(R.id.rl_weight).setVisibility(View.GONE);
                }
            }
        };


        mRecyclerView.setAdapter(adapter);
    }

    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value));
    }



    private List<JsdwxxBean> temList = new ArrayList<>();
    
    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(List<JsdwxxBean> model){
        mList.clear();
        mList.addAll(model);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }



    @Override
    public void getListSuccess(List<JsdwxxBean> list) {
        refreshLayout.finishRefresh();
        fillRecycleView(list);
    }

}
