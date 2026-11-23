package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.event.ChangeEvent;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.gather.RxBus;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyld;
import com.grkj.ytjqy_pad.mvp.presenter.ZyldListPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
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
 * Created by yusam on 2026/11/17.
 * 待办转移联单列表
 */

public class TodoZyldListActivity extends BaseActivity<ZyldListPresenter> implements ZyldListPresenter.View{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    //接收单位名称
    @BindView(R.id.et_item_jsdwmc)
    EditText et_item_jsdwmc;


    //移出单位名称
    @BindView(R.id.et_item_ycdwmc)
    EditText et_item_ycdwmc;

    //联单节点状态
    @BindView(R.id.et_item_actid)
    EditText et_item_actid;


    //总页数总条数
    @BindView(R.id.tv_right_bottom2)
    TextView tv_right_bottom2;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //新增
    @BindView(R.id.ll_add)
    LinearLayout ll_add;


    private int totalPage;
    private int pageNumber = 1;
    private List<GfWxfwZyld> mList;//数据列表
    private CommonAdapter<GfWxfwZyld> adapter;//数据列表适配器
    private Map<String, String> params = new HashMap<>();


    @Override
    protected int getLayout(){
        return R.layout.activity_zyld_list;
    }

    @Override
    protected void initEventAndData(){
        registerRxBus();
        et_item_jsdwmc.setHint("输入国家联单编号");

        Map<String, String> map = new HashMap<>();
        map.put("types", "联单类型-国家系统,联单节点状态");
        ll_add.setVisibility(View.GONE);
        mPresenter.getParamList(map);
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
        initTopToolbar("待办转移联单");

        findViewById(R.id.tv_title2).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tv_left_bottom2)).setText("待办转移联单");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }


    @OnClick({R.id.ll_add,R.id.ll_back,R.id.et_item_actid,R.id.ll_search_click,R.id.ll_clear_click})
    public void onViewClicked(View view){
        switch (view.getId()) {

            //手动入库
            case R.id.ll_add:
                goToActivity(new Intent(mContext, ZyldInfoActivity.class),10);
                break;

            //选择联单节点状态
            case R.id.et_item_actid:
                showPopWindow("联单节点状态", et_item_actid);
                break;

            //搜索
            case R.id.ll_search_click:
                params.put("gjldbh", et_item_jsdwmc.getText().toString());
                params.put("ycdwmc", et_item_ycdwmc.getText().toString());
                refreshLayout.autoRefresh();
                break;

            //清空
            case R.id.ll_clear_click:
                et_item_jsdwmc.setText("");
                et_item_ycdwmc.setText("");
                et_item_actid.setText("");
                params.put("gjldbh", "");
                params.put("ycdwmc", "");
                params.put("actid", "");
                refreshLayout.autoRefresh();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;

        }
    }

    @Override
    protected ZyldListPresenter createPresenter(){
        return new ZyldListPresenter();
    }


    @Override
    public void showError(String msg){
        toastShow(msg);
        hideLoading();
        mList.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

    private SelectPopupWindow selectPopupWindow;
    protected List<ParamListBean> allParamList = new ArrayList<>();
    //初始化下拉弹窗
    private void showPopWindow(String type, EditText item) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();

        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
                }
            }
        }

        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                params.put("actid", model.ID);
                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }



    /**
     * 企业列表初始化
     */
    private void initRecycleView(){
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,ApiUrl.getHnUrl(ApiUrl.URL_TODO_ZYLD_LIST),params);
        adapter = new CommonAdapter<GfWxfwZyld>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final GfWxfwZyld bean,final int position){
                if (bean != null) {
                    holder.getView(R.id.rl_weight).setVisibility(View.GONE);
                    setText(holder.getView(R.id.tv_item1),bean.jxhldbh,R.string.str_zyld_item1);
                    setText(holder.getView(R.id.tv_item2),bean.gjldbh,R.string.str_zyld_item2);
                    setText(holder.getView(R.id.tv_item3),bean.ycdwmc,R.string.str_zyld_item35);
                    setText(holder.getView(R.id.tv_item4),commonTransfer(bean.ldlxgj,"联单类型-国家系统"),R.string.str_zyld_item4);

                    TextView tv_item2 =  holder.getView(R.id.tv_item2);
                    tv_item2.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                goToActivity(new Intent(mContext, ZyldInfoActivity.class)
                        .putExtra("isDbld",true)
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
    private void fillRecycleView(PageBean<GfWxfwZyld> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<GfWxfwZyld> LabelInfoBeans = GsonUtil.JsonToList(json,GfWxfwZyld.class);
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
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
        refreshLayout.autoRefresh();
        initRecycleView();
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
