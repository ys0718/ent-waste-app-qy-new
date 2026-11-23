package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.graphics.Canvas;
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
import com.grkj.ytjqy_pad.mvp.model.BaseBean;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.presenter.MulPrintListPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.BasePrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.view.InputSelectView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
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

/**
 * Created by yusam on 2023/2/15.
 * 批量打印标签列表
 */

public class MulPrintListActivity extends BaseActivity<MulPrintListPresenter> implements MulPrintListPresenter.View{

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

    //选中条数
    @BindView(R.id.tv_num)
    TextView tv_num;


    //废物详细名称
    @BindView(R.id.isv_fwxxmc)
    InputSelectView isv_fwxxmc;

    //登记类型
    @BindView(R.id.isv_djlx)
    InputSelectView isv_djlx;

    //时间选择
    @BindView(R.id.v_select_time)
    SelectTimeView v_select_time;

    //全选布局
    @BindView(R.id.ll_select_all)
    LinearLayout ll_select_all;

    //主布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    @BindView(R.id.iv_select_all)
    ImageView iv_select_all;

    @BindView(R.id.ll_search)
    LinearLayout ll_search;


    //全选
    private boolean isSelectAll = false;

    //类型下拉列表
    private List<CommonProductInfoBean> typeList = new ArrayList<>();


    private int totalPage;
    private int pageNumber = 1;
    private List<CommonProductInfoBean> mList;//标签列表
    private CommonAdapter<CommonProductInfoBean> adapter;//标签列表适配器
    private Map<String, String> params = new HashMap<>();
    private String tzType ="rk";

    private int maxNum = 80;
    private String qrCodeValues;
    private String typch;//同一批次号
    private String rkpch;//入库批次号


    /**
     * 地址选择字段结束
     **/


    @Override
    protected int getLayout(){
        return R.layout.activity_mul_print_list;
    }

    @Override
    protected void initEventAndData(){
        tv_title.setText("批量打印标签");
        initRecycleView();

        typch = getIntent().getStringExtra("TYPCH");
        rkpch = getIntent().getStringExtra("RKPCH");

        params.put("tzType",tzType);
        if (TextUtils.isEmpty(typch)){//正常菜单进来
            ll_search.setVisibility(View.VISIBLE);
            mPresenter.getTypeList();

            v_select_time.setOnTimeChangedListener(new SelectTimeView.OnTimeChangedListener(){
                @Override
                public void startTimeChanged(String startTime,String endTime){
                    params.put("startTime",startTime);
                    params.put("endTime",endTime);
                }

            });
        }else {//同一批次号进来
            params.put("typch",typch);
        }

        Map<String, String> wltxParam = new HashMap<>();
        wltxParam.put("types","物理特性");
        mPresenter.getParamList(wltxParam);

        refreshLayout.autoRefresh();

    }


    @Override
    protected void initLandscapeView(){
        super.initLandscapeView();
        initTop();
        if (!TextUtils.isEmpty(typch)){
            ll_search.setVisibility( View.INVISIBLE);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

    }


    @Override
    protected void initPortraitView(){
        super.initPortraitView();
        if (!TextUtils.isEmpty(typch)){
            ll_search.setVisibility(View.GONE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @OnClick({R.id.ll_back,R.id.ll_search_click,R.id.ll_select_all,R.id.ll_print})
    public void onViewClicked(View view){
        switch (view.getId()) {

            // 全选
            case R.id.ll_select_all:
                isSelectAll = !isSelectAll;
                handleSelectAll();
                break;

            // 返回
            case R.id.ll_back:
                finish();
                break;


            // 搜索按钮
            case R.id.ll_search_click:
                refreshLayout.autoRefresh();
                break;

            // 打印按钮
            case R.id.ll_print:
                if (selectNum == 0){
                    ToastUtil.showToast(mContext,"请至少选择一条数据！");
                    return;
                }

                Map<String, String> param = new HashMap<>();
                param.put("qrCodeValues",qrCodeValues);
                param.put("type",params.get("tzType"));
                mPresenter.getLabelInfo(param);
                break;

        }
    }


    //处理全选/取消全选
    private void handleSelectAll(){
        iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
        if (mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).SELECT = isSelectAll;
            }
        }
        adapter.notifyDataSetChanged();
        updateTodoListNum();
    }


    @Override
    protected MulPrintListPresenter createPresenter(){
        return new MulPrintListPresenter();
    }


    @Override
    public void showError(String msg){
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
    private void initRecycleView(){
        mList = new ArrayList<>();
        pageNumber = mPresenter.initRefresh(refreshLayout,mContext,ApiUrl.URL_PRINT_LIST,params);
        adapter = new CommonAdapter<CommonProductInfoBean>(mContext,R.layout.item_common_waste_info,mList){
            @Override
            protected void convert(ViewHolder holder,final CommonProductInfoBean bean,final int position){
                if (bean != null) {
                    String time = "";
                    String pch = "入库批次号：" + bean.rkpch;
                    int unitResStr = R.string.str_jldw_record;
                    switch (tzType) {
                        case "rk"://入库
                            bean.csl = bean.rkl;
                            time = "入库时间："+bean.rksj;
                            pch = "入库批次号：" + bean.rkpch;
                            unitResStr = R.string.str_jldw_input;
                            break;
                        case "ck"://出库
                            bean.csl = bean.ckl;
                            time = "出库时间："+bean.cksj;
                            pch = "出库批次号：" + bean.ckpch;
                            unitResStr = R.string.str_jldw_output;
                            break;
                    }

                    if (!TextUtils.isEmpty(rkpch) && rkpch.equals(bean.rkpch)){
                        holder.getView(R.id.iv_current).setVisibility(View.VISIBLE);
                    }else{
                        holder.getView(R.id.iv_current).setVisibility(View.GONE);
                    }


                    setText(holder.getView(R.id.tv_item1),bean.fwxxmc,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2),bean.fwdlbh,R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item3),bean.fwdlmc,R.string.str_record_list1_2);
                    setText(holder.getView(R.id.tv_item4),bean.fwxlbh,R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item5),pch,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item8),time,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item6),bean.csl,R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item7),bean.jldw,unitResStr);


                    ImageView cb = holder.getView(R.id.btn_select);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    cb.setVisibility(View.VISIBLE);
                    cb.setImageResource(bean.SELECT?R.drawable.checkbox_theme:R.mipmap.ic_unselected);
                    item_layout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if (!bean.SELECT && selectNum >= maxNum) {
                                ToastUtil.showToast(mContext,"最多只能打印"+maxNum+"条！");
                            } else {
                                bean.SELECT = !bean.SELECT;
                                adapter.notifyItemChanged(position);
                                updateTodoListNum();
                            }

                        }
                    });

                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view,RecyclerView.ViewHolder viewHolder,int i){
//                mPresenter.getLabelInfo(mList.get(i).qrcodeValue, url);
            }

            @Override
            public boolean onItemLongClick(View view,RecyclerView.ViewHolder viewHolder,int i){
                return false;
            }
        });


        mRecyclerView.setAdapter(adapter);
    }

    private int selectNum = 0;

    //更新待处理条数
    private void updateTodoListNum(){
        selectNum = 0;
        qrCodeValues = "";
        if (mList != null && mList.size() != 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).SELECT && selectNum < maxNum) {
                    selectNum++;
                    qrCodeValues += mList.get(i).qrcodeValue+",";
                }
            }
            checkIsAll(adapter.getDatas());
        }

        if (selectNum > 0) {//如果已有选中的，则不可刷新
            refreshLayout.setEnableRefresh(false);
        } else {
            refreshLayout.setEnableRefresh(true);
        }

        tv_num.setText("已选中"+selectNum+"条");

    }


    protected <T extends BaseBean> void checkIsAll(List<T> newList){
        isSelectAll = true;
        if (newList != null && newList.size() != 0) {
            for (int j = 0; j < newList.size(); j++) {
                if (!newList.get(j).SELECT) {
                    isSelectAll = false;
                }
            }
        }
        iv_select_all.setImageResource(isSelectAll?R.mipmap.ic_selected:R.mipmap.ic_unselected);
    }


    private void setText(View tv,String value,int strRes){
        tv.setVisibility(View.VISIBLE);
        ((TextView) tv).setText(TextUtils.isEmpty(value)?getString(strRes,getString(R.string.common_empty_text)):getString(strRes,value));
    }


    @Override
    public void getTypeListSuccess(List<CommonProductInfoBean> list){
        String json = GsonUtil.GsonToString(list);
        typeList = GsonUtil.JsonToList(json,CommonProductInfoBean.class);
        List<Select> tempList = new ArrayList<>();
        for (int i = 0; i < typeList.size(); i++) {
            tempList.add(new Select(typeList.get(i).fwxxmc,typeList.get(i).fwxxmc));
        }

        isv_fwxxmc.setDatas(tempList);
//        isv_fwxxmc.setHint("废物详细名称");
        isv_fwxxmc.setOnTextChangedListener(new InputSelectView.OnTextChangedListener(){
            @Override
            public void textChange(String changeText){
                params.put("fwxxmc",changeText);
            }
        });

        List<Select> tempList2 = new ArrayList<>();
//        tempList2.add(new Select("产废登记", "cf"));
        tempList2.add(new Select("入库登记","rk"));
        tempList2.add(new Select("出库登记","ck"));
        isv_djlx.setDatas(tempList2);
//        isv_djlx.setEnabled(false);
        isv_djlx.setItem(tempList2.get(0));
        isv_djlx.setOnItemChangedListener(new InputSelectView.OnItemChangedListener(){
            @Override
            public void itemChange(Select item){
                tzType = item.getValue();
                params.put("tzType",item.getValue());
            }
        });

    }

    protected List<ParamListBean> allParamList = new ArrayList<>();

    @Override
    public void getParamListSuccess(List<ParamListBean> list){
        String json = GsonUtil.GsonToString(list);
        allParamList = GsonUtil.JsonToList(json,ParamListBean.class);
    }

    /*
     * 通用转义
     * */
    protected String commonTransfer(String code,String type){
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
        ll_select_all.setVisibility(View.VISIBLE);

        if (model != null) {
            fillRecycleView(model);
        }

    }

    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<CommonProductInfoBean> model){
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }

        String json = GsonUtil.GsonToString(model.getRows());
        List<CommonProductInfoBean> LabelInfoBeans = GsonUtil.JsonToList(json,CommonProductInfoBean.class);
        mList.addAll(LabelInfoBeans);
        if (!TextUtils.isEmpty(typch)){
            isSelectAll = true;
            handleSelectAll();
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 加载更多列表成功
     */
    @Override
    public void getMoreListSuccess(PageBean model,String url){
        refreshLayout.finishLoadmore();
        ll_select_all.setVisibility(View.GONE);

        if (totalPage == pageNumber) {
            refreshLayout.finishLoadmore();
        }
        if (model != null) {
            fillRecycleView(model);
        }
    }

    @Override
    public void getLabelInfoSuccess(List<LabelInfoBean> result){
        KLog.e("getLabelInfoSuccess:"+GsonUtil.GsonToString(result));
//        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(result),LabelInfoBean.class);
//        data.WLXZ = commonTransfer(data.WLXZ,"物理特性");
//        startActivityForResult(new Intent(mContext,NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(data)),10);

        List<LabelInfoBean> newList = new ArrayList<>();

        String str = GsonUtil.GsonToString(result);
        newList = GsonUtil.JsonToList(str,LabelInfoBean.class);


        if (newList!=null && newList.size()!=0){
            for (int i = 0; i < newList.size(); i++) {
                newList.get(i).WLXZ = commonTransfer(newList.get(i).WLXZ,"物理特性");
            }
            showMessageDialog("温馨提示：该功能需要连接广软多标签蓝牙打印机",newList);
        }

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == 9) {//刷新列表
            refreshLayout.autoRefresh();
        }
    }

    /**
     * 提示窗口
     */
    protected void showMessageDialog(String mMessage,List<LabelInfoBean> result) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage(mMessage);
        //设置为只显示内容
        roundCornerDialog.setDialogType(RoundCornerDialog.TYPE_SAVE);
        roundCornerDialog.setConfirmText("继续");
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                roundCornerDialog.dismiss();
                finish();
                startActivityForResult(new Intent(mContext,BasePrintLabelActivity.class).putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(result)),10);
            }

            @Override
            public void onCancelClick() {
                roundCornerDialog.dismiss();
            }
        });

        //显示对话框
        roundCornerDialog.show();
    }




}
