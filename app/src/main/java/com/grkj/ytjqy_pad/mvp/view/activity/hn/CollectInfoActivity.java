package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoBean;
import com.grkj.ytjqy_pad.mvp.model.CollectInfoChildBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxBean;
import com.grkj.ytjqy_pad.mvp.model.JyxwUserInfoBean;
import com.grkj.ytjqy_pad.mvp.model.LabelInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.GfYbqyXxbaXkzxxZcssBean;
import com.grkj.ytjqy_pad.mvp.presenter.CollectInfoPresenter;
import com.grkj.ytjqy_pad.mvp.view.activity.printer2.NewPrintLabelActivity;
import com.grkj.ytjqy_pad.utils.common.DateTimeUtil;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.view.BoldTextView;
import com.grkj.ytjqy_pad.utils.view.GrScrollView;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
import com.grkj.ytjqy_pad.utils.view.QCTJTextView;
import com.grkj.ytjqy_pad.utils.view.RoundCornerDialog;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yusam on 2022/3/21.
 * 社会收集源信息
 */

public class CollectInfoActivity extends BaseActivity<CollectInfoPresenter> implements CollectInfoPresenter.View {

    private CollectInfoBean mData = new CollectInfoBean();

    //批次号
    @BindView(R.id.et_item1)
    EditText et_item1;

    //产生时间
    @BindView(R.id.et_item2)
    EditText et_item2;

    //废物类别
    @BindView(R.id.et_item3)
    EditText et_item3;

    //贮存仓库
    @BindView(R.id.et_item4)
    EditText et_item4;

    //废物名称
    @BindView(R.id.et_item5)
    EditText et_item5;

    //物理特性
    @BindView(R.id.et_item6)
    EditText et_item6;

    //废物特性
    @BindView(R.id.et_item7)
    EditText et_item7;

    //产生量
    @BindView(R.id.et_item8)
    EditText et_item8;

    //容器/包装编码
    @BindView(R.id.et_item9)
    EditText et_item9;

    //产废去向
    @BindView(R.id.et_item10)
    EditText et_item10;

    //容器/包装类型
    @BindView(R.id.et_item11)
    EditText et_item11;

    //容器/包装数量
    @BindView(R.id.et_item12)
    EditText et_item12;

    //产生危险废物设施编码
    @BindView(R.id.et_item13)
    EditText et_item13;

    //产生部门经办人
    @BindView(R.id.et_item14)
    EditText et_item14;

    //包装类型
    @BindView(R.id.et_item15)
    EditText et_item15;

    //包装规模
    @BindView(R.id.et_item16)
    EditText et_item16;

    //计量单位
    @BindView(R.id.et_item17)
    EditText et_item17;


    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //提交
    @BindView(R.id.ll_commit)
    LinearLayout ll_commit;


    //新增子表
    @BindView(R.id.ll_add_child)
    LinearLayout ll_add_child;

    //删除
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //显示/隐藏详情按钮
    @BindView(R.id.btn_show_hide)
    TextView btn_show_hide;

    //显示/隐藏详情
    @BindView(R.id.ll_detail)
    MyLinearLayout ll_detail;

    //表单内容
    @BindView(R.id.ll_clickable)
    MyLinearLayout ll_clickable;


    private String typeName = "自行利用";
    private String typeValue = "R";
    private String dataStr;
    private boolean isShowDetail = false;
    private boolean isCommit = false;
    private boolean isDeleteMain = true;//是否为删除主表

    @Override
    protected int getLayout() {
        return R.layout.activity_collect_info;
    }

    @Override
    protected void initEventAndData() {
        dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);

        if (TextUtils.isEmpty(dataStr)) {
            mData = new CollectInfoBean();
            ll_delete.setVisibility(View.GONE);
            mData.sjrq = DateTimeUtil.getCurrentDate();
            et_item1.setText(mData.sjrq);
            isShowDetail = true;
        } else {
            mData = GsonUtil.GsonToBean(dataStr, CollectInfoBean.class);
            params.put(Constants.MAINID, mData.id);
            mPresenter.getChildList(params);
            showHideBtn();
            initView();
            isShowDetail = false;

            mPresenter.getZcssListByXkz(mData.xkzId);

        }
        showHideDetail();

        Map<String, String> map = new HashMap<>();
        map.put("types", "个人证件类型,危险废物利用处置方式代码,包装方式,包装材质,包装规模,计量单位,物理特性");
        mPresenter.getParamList(map);
        mPresenter.getDwList();
        initRecycleView();
        mPresenter.getXzqyTree();
        mPresenter.getXkzList();
    }

    /*
    * 显示/隐藏按钮
    * */
    private void showHideBtn() {
        if (TextUtils.equals("1",mData.commitStatus)){
            ll_delete.setVisibility(View.GONE);
            ll_save.setVisibility(View.GONE);
            ll_commit.setVisibility(View.GONE);
            ll_add_child.setVisibility(View.GONE);
            ll_clickable.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
        }else {
            ll_delete.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);
            ll_commit.setVisibility(View.VISIBLE);
            ll_add_child.setVisibility(View.VISIBLE);
            ll_clickable.setOnInterceptTouchEvent(false, getResources().getColor(R.color.gray));
        }
    }


    /*
     * 显示/隐藏详情
     * */
    private void showHideDetail() {
        if (isShowDetail) {
            btn_show_hide.setText("收起");
            ll_detail.setVisibility(View.VISIBLE);
        } else {
            btn_show_hide.setText("展开");
            ll_detail.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("社会源收集信息");
    }

    private void initView() {
        et_item1.setText(mData.sjrq);
        et_item2.setText(mData.xkzbh);
        et_item3.setText(mData.sjdx);

        et_item5.setText(mData.zdylsh);
        et_item6.setText(mData.jsyName);
        et_item7.setText(mData.cph);
        et_item8.setText(mData.csdw);
        et_item9.setText(getString(R.string.common_str_ssq, mData.ssshengValue, mData.ssshiValue, mData.ssquValue));
        et_item10.setText(mData.cszrr);
        et_item11.setText(mData.lxPhone);
        et_item12.setText(mData.cszrrZjlx);
        et_item13.setText(mData.cszrrZjh);
        et_item14.setText(mData.csdwdz);
        et_item15.setText(mData.sjdwzrr);
        et_item16.setText(mData.sjzrrZjlx);
        et_item17.setText(mData.sjdwzrrZjh);

    }


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_delete, R.id.et_item1,R.id.et_item2, R.id.et_item3, R.id.et_item4,R.id.ll_commit,
            R.id.et_item9, R.id.et_item12, R.id.et_item16, R.id.ll_add_child, R.id.btn_show_hide})
    public void onViewClicked(View view) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        switch (view.getId()) {
            case R.id.et_item1://时间
                DateTimeUtil.showTimePicker(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        et_item1.setText(DateTimeUtil.formatDateTime(date, DateTimeUtil.DF_YYYY_MM_DD_HH_MM_SS));
                    }
                });
                break;
            case R.id.et_item2://许可证
                selectPopupWindowList = new ArrayList<>();
                for (int i = 0; i < xkzList.size(); i++) {
                    SelectPopupWindowBean bean = new SelectPopupWindowBean(xkzList.get(i).id, xkzList.get(i).xkzbh);
                    selectPopupWindowList.add(bean);
                }
                showPopWindow(KEY_XKZ, et_item2, selectPopupWindowList);
                break;
            case R.id.et_item3://收集对象
                selectPopupWindowList = new ArrayList<>();
                for (int i = 0; i < userList.size(); i++) {
                    SelectPopupWindowBean bean = new SelectPopupWindowBean(userList.get(i).id, userList.get(i).entername);
                    selectPopupWindowList.add(bean);
                }
                showPopWindow(KEY_SJDX, et_item3, selectPopupWindowList);
                break;

            case R.id.et_item4://贮存设施
                selectPopupWindowList = new ArrayList<>();
                for (int i = 0; i < zcssList.size(); i++) {
                    GfYbqyXxbaXkzxxZcssBean mData = zcssList.get(i);
                    String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
                    selectPopupWindowList.add(new SelectPopupWindowBean(zcssList.get(i).zcssId, name));
                }
                showPopWindow(KEY_ZCSS, et_item4, selectPopupWindowList);
                break;

            case R.id.et_item9://行政区域
                HnUtils.showPickerDialog(mContext, xzqyList, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        mData.sssheng = xzqyList.get(options1).code;
                        mData.ssshengValue = xzqyList.get(options1).name;
                        mData.ssshi = xzqyList.get(options1).child.get(options2).code;
                        mData.ssshiValue = xzqyList.get(options1).child.get(options2).name;
                        if (xzqyList.get(options1).child.get(options2).child != null && xzqyList.get(options1).child.get(options2).child.size() != 0) {
                            mData.ssqu = xzqyList.get(options1).child.get(options2).child.get(options3).code;
                            mData.ssquValue = xzqyList.get(options1).child.get(options2).child.get(options3).name;
                        }

                        et_item9.setText(HnUtils.getResultName(xzqyList, options1, options2, options3));
                    }
                });
                break;

            case R.id.et_item12://证件类型
                showPopWindow(KEY_ZJLX, et_item12, groupParamList(KEY_ZJLX));
                break;
            case R.id.et_item16://证件类型
                showPopWindow(KEY_ZJLX, et_item16, groupParamList(KEY_ZJLX));
                break;

            case R.id.ll_add_child://新增子表

                if (TextUtils.isEmpty(mData.xkzId)) {
                    ToastUtil.showToast(mContext, "请选择许可证！");
                    return;
                }

                startActivityForResult(new Intent(mContext, CollectInfoChildActivity.class)
                        .putExtra(Constants.DATA_PARENT, GsonUtil.GsonToString(mData)), 9);

                break;

            case R.id.btn_show_hide://显示/隐藏
                isShowDetail = !isShowDetail;
                showHideDetail();
                break;

            case R.id.ll_save://保存
                isCommit = false;
                groupData();
                break;

            case R.id.ll_commit://提交
                isCommit = true;
                groupData();
                break;


            // 删除
            case R.id.ll_delete:
                isDeleteMain = true;
                showDeleteDialog(ApiUrl.URL_COLLECT_DELETE + mData.id);
                break;

        }
    }

    private void groupData() {

        mData.sjrq = et_item1.getText().toString();
        mData.xkzbh = et_item2.getText().toString();
        mData.sjdx = et_item3.getText().toString();
        mData.zdylsh = et_item5.getText().toString();
        mData.jsyName = et_item6.getText().toString();
        mData.cph = et_item7.getText().toString();
        mData.csdw = et_item8.getText().toString();
        mData.cszrr = et_item10.getText().toString();
        mData.lxPhone = et_item11.getText().toString();
        mData.cszrrZjlx = et_item12.getText().toString();
        mData.cszrrZjh = et_item13.getText().toString();
        mData.csdwdz = et_item14.getText().toString();
        mData.sjdwzrr = et_item15.getText().toString();
        mData.sjzrrZjlx = et_item16.getText().toString();
        mData.sjdwzrrZjh = et_item17.getText().toString();
        mData.fwxxes = new ArrayList<>();
        mData.fwxxes.addAll(mList);

        if (isCommit){
            mData.commitStatus = "1";
        }else {
            mData.commitStatus = "0";
        }

        if (TextUtils.isEmpty(checkEmpty())) {
            showSaveDialog();
        } else {
            ToastUtil.showToast(mContext, checkEmpty());
            showHideDetail();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HnUtils.closeDialog();
    }

    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.xkzbh)) {
            return "请选择许可证编号！";
        } else if (TextUtils.isEmpty(mData.sjdx)) {
            return "请选择收集对象！";
        } else if (TextUtils.isEmpty(mData.zcssId)) {
            return "请选择贮存仓库！";
        } else if (TextUtils.isEmpty(mData.csdw)) {
            isShowDetail = true;
            return "请输入产生单位！";
        } else if (TextUtils.isEmpty(mData.sssheng)) {
            isShowDetail = true;
            return "请输入行政区域！";
        } else if (TextUtils.isEmpty(mData.cszrr)) {
            isShowDetail = true;
            return "请输入产生责任人！";
        } else if (TextUtils.isEmpty(mData.lxPhone)) {
            isShowDetail = true;
            return "请输入联系人电话！";
        } else if (TextUtils.isEmpty(mData.cszrrZjlx)) {
            isShowDetail = true;
            return "请输入产生责任人证件类型！";
        } else if (TextUtils.isEmpty(mData.cszrrZjh)) {
            isShowDetail = true;
            return "请输入产生责任人证件号！";
        } else if (TextUtils.isEmpty(mData.csdwdz)) {
            isShowDetail = true;
            return "请输入产生单位地址！";
        } else if (TextUtils.isEmpty(mData.sjdwzrr)) {
            isShowDetail = true;
            return "请输入收集单位责任人！";
        } else if (TextUtils.isEmpty(mData.sjzrrZjlx)) {
            isShowDetail = true;
            return "请输入收集责任人证件类型！";
        } else if (TextUtils.isEmpty(mData.sjdwzrrZjh)) {
            isShowDetail = true;
            return "请输入收集单位责任人证件号！";
        } else {
            return "";
        }

    }


    //确认保存
    private void showSaveDialog() {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定保存？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                Map<String, String> map = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                mPresenter.saveData(map, ApiUrl.URL_COLLECT_SAVE);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {
            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    /*
     * 确认删除
     * */
    private void showDeleteDialog(String url) {
        RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext);
        //设置标题，描述，文本等参数
        roundCornerDialog.setMessage("是否确定删除？");
        //确定按钮的点击回调方法
        roundCornerDialog.setConfirmListener(new RoundCornerDialog.ConfirmListener() {
            @Override
            public void onConfirmClick() {
                mPresenter.deleteData(url);
                roundCornerDialog.dismiss();
            }

            @Override
            public void onCancelClick() {

            }
        });
        //显示对话框
        roundCornerDialog.show();
    }


    @Override
    protected CollectInfoPresenter createPresenter() {
        return new CollectInfoPresenter();
    }


    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    private List<ParamListBean> allParamList = new ArrayList<>();

    @Override
    public void getParamListSuccess(List<ParamListBean> list) {
        String json = GsonUtil.GsonToString(list);
        KLog.e(list.size());
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);
    }

    private List<AreaBean> xzqyList = new ArrayList<>();

    @Override
    public void getXzqyTreeSuccess(List<AreaBean> list) {
        String json = GsonUtil.GsonToString(list);
        xzqyList = GsonUtil.JsonToList(json, AreaBean.class);
    }

    @Override
    public void saveDataSuccess(Object bean) {
        if (isCommit){
            ToastUtil.showToast(mContext, "提交成功！");
        }else {
            ToastUtil.showToast(mContext, "保存成功！");
        }
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), CollectInfoBean.class);
        mList.clear();

        if (mData.fwxxes!=null && mData.fwxxes.size()!=0){
            mList.addAll(mData.fwxxes);
            adapter.notifyDataSetChanged();
        }

        ll_delete.setVisibility(View.VISIBLE);
        setResult(9);
        showHideBtn();
    }


    @Override
    public void deleteDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "删除成功！");
        if (isDeleteMain){
            setResult(9);
            finish();
        }else {
            mPresenter.getChildList(params);
        }

    }

    private List<JyxwUserInfoBean> userList = new ArrayList<>();

    @Override
    public void getDwListSuccess(Object rows) {
        userList = GsonUtil.JsonToList(GsonUtil.GsonToString(rows), JyxwUserInfoBean.class);

    }

    private List<GfYbqyXxbaXkzxxZcssBean> zcssList = new ArrayList<>();

    @Override
    public void getZcssListByXkzSuccess(List<GfYbqyXxbaXkzxxZcssBean> list) {
        String json = GsonUtil.GsonToString(list);
        zcssList = GsonUtil.JsonToList(json, GfYbqyXxbaXkzxxZcssBean.class);

        if (!TextUtils.isEmpty(mData.zcssId)) {
            for (int i = 0; i < zcssList.size(); i++) {
                if (mData.zcssId.equals(zcssList.get(i).zcssId)) {
                    GfYbqyXxbaXkzxxZcssBean mData = zcssList.get(i);
                    String name = getString(R.string.str_input_zcbm, mData.no, mData.sslx, mData.ssmc);
                    et_item4.setText(name);
                }
            }
        }

    }


    private SelectPopupWindow selectPopupWindow;

    private static final String KEY_SJDX = "收集对象";
    private static final String KEY_XKZ = "许可证";
    private static final String KEY_ZCSS = "贮存设施";
    private static final String KEY_ZJLX = "个人证件类型";
    private static final String KEY_LYZCFS = "危险废物利用处置方式代码";


    private List<SelectPopupWindowBean> groupParamList(String type) {
        List<SelectPopupWindowBean> selectPopupWindowList = new ArrayList<>();
        for (int i = 0; i < allParamList.size(); i++) {
            List<ParamListBean> childList = allParamList.get(i).childList;

            if (childList != null && childList.size() != 0 && type.equals(allParamList.get(i).type)) {
                for (int j = 0; j < childList.size(); j++) {
                    selectPopupWindowList.add(new SelectPopupWindowBean(childList.get(j).code, childList.get(j).value));
                }
            }
        }
        return selectPopupWindowList;
    }


    //初始化选择地址下拉弹窗
    private void showPopWindow(String type, EditText item, List<SelectPopupWindowBean> selectPopupWindowList) {
        selectPopupWindow = new SelectPopupWindow(mContext, selectPopupWindowList, new SelectPopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                item.setText(model.NAME);
                switch (mType) {
                    case KEY_XKZ://许可证
                        mData.xkzId = model.ID;
                        mData.zcssId = "";
                        et_item4.setText(mData.zcssId);
                        mPresenter.getZcssListByXkz(mData.xkzId);
                        break;
                    case KEY_SJDX://收集对象
                        mData.sjdxId = model.ID;
                        break;
                    case KEY_ZCSS://贮存设施
                        mData.zcssId = model.ID;
                        break;

                }

                selectPopupWindow.dismiss();
            }
        });

        int sort = 5;
        if (selectPopupWindowList.size() < 4) {
            sort = selectPopupWindowList.size() + 1;
        }

        selectPopupWindow.show(mLayout, type, (View) item.getParent(), sort);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 9) {
            isScrollToBottom = true;
            params.put(Constants.MAINID, mData.id);
            mPresenter.getChildList(params);
        } else if (resultCode == 10) {
            String selectBeanStr = data.getStringExtra("selectBean");
            CollectInfoChildBean childBean = GsonUtil.GsonToBean(selectBeanStr, CollectInfoChildBean.class);
            mList.add(childBean);
            adapter.notifyDataSetChanged();
            scrollToBottom(scroll_view, View.FOCUS_DOWN);
        }
    }

    /***************************************************************子表部分***************************************************************/


    private List<CollectInfoChildBean> mList;//企业列表
    private CommonAdapter<CollectInfoChildBean> adapter;//企业列表适配器
    private Map<String, String> params = new HashMap<>();


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    /**
     * 企业列表初始化
     */
    private void initRecycleView() {
        mList = new ArrayList<>();
        adapter = new CommonAdapter<CollectInfoChildBean>(mContext, R.layout.item_collect_waste_info, mList) {
            @Override
            protected void convert(ViewHolder holder, final CollectInfoChildBean bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwxxmc) ? getString(R.string.common_empty) : bean.fwxxmc);

                    String groupFwlb = getString(R.string.str_record_fwlb2, bean.fwdlbh, bean.fwdlmc);
                    setText(holder.getView(R.id.tv_item1), bean.qrcodeValue, R.string.empty_replace);
                    setText(holder.getView(R.id.tv_item2), bean.fwxxmc, R.string.str_record_list1_2);
                    setText(holder.getView(R.id.tv_item3), groupFwlb, R.string.str_record_list1);
                    setText(holder.getView(R.id.tv_item4), bean.fwxlbh, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item5), bean.rksj, R.string.str_record_list4);
                    setText(holder.getView(R.id.tv_item8), bean.rkl + bean.jldw, R.string.str_record_list13);
//                    setText(holder.getView(R.id.tv_item6), bean.rkl, R.string.empty_replace);
//                    setText(holder.getView(R.id.tv_item7), bean.jldw, R.string.str_jldw_input);
                    RelativeLayout rl_weight = holder.getView(R.id.rl_weight);
                    BoldTextView tv_item1 = holder.getView(R.id.tv_item1);
                    tv_item1.setMaxLines(2);
                    if (!TextUtils.isEmpty(bean.id)){
                        rl_weight.setVisibility(View.VISIBLE);
                        holder.getView(R.id.rl_weight).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.getLabelInfo(bean.id);
                            }
                        });
                    }else {
                        rl_weight.setVisibility(View.GONE);
                    }

                    ImageView iv_delete = holder.getView(R.id.iv_delete);
                    if (TextUtils.equals("1",mData.commitStatus) || mList.size() == 1){
                        iv_delete.setVisibility(View.GONE);
                    }else {
                        iv_delete.setVisibility(View.VISIBLE);
                        iv_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(bean.id)){//有id
                                    mList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    isDeleteMain = false;
                                    showDeleteDialog(ApiUrl.URL_COLLECT_DELETE_CHILD + bean.id);
                                }
                            }
                        });
                    }
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {

                startActivityForResult(new Intent(mContext, CollectInfoChildActivity.class)
                        .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(mList.get(i)))
                        .putExtra(Constants.DATA_PARENT, GsonUtil.GsonToString(mData)), 9);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
    }

    private void setText(View tv, String value, int strRes) {
        tv.setVisibility(View.VISIBLE);

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }


    @Override
    public void getChildListSuccess(PageBean pageBean) {
        if (pageBean != null) {
            fillRecycleView(pageBean);
        }
    }

    @Override
    public void getLabelInfoSuccess(Object bean) {
        LabelInfoBean data = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean),LabelInfoBean.class);
        data.WLXZ = commonTransfer(data.WLXZ,"物理特性");
        startActivityForResult(new Intent(mContext, NewPrintLabelActivity.class).putExtra(Constants.JUMP_KEY,GsonUtil.GsonToString(data)),10);
    }

    private List<GfYbqyXxbaXkzxxBean> xkzList = new ArrayList<>();

    @Override
    public void getXkzListSuccess(Object bean) {
        xkzList = GsonUtil.JsonToList(GsonUtil.GsonToString(bean), GfYbqyXxbaXkzxxBean.class);
    }

    /*
     * 通用转义
     * */
    private String commonTransfer(String code, String type) {
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


    private boolean isScrollToBottom = false;


    /**
     * 向列表视图填充数据
     */
    private void fillRecycleView(PageBean<CollectInfoChildBean> model) {
        if (model.getPage() > 1) {
        } else {
            mList.clear();
        }
        String json = GsonUtil.GsonToString(model.getRows());
        List<CollectInfoChildBean> LabelInfoBeans = GsonUtil.JsonToList(json, CollectInfoChildBean.class);
        mList.addAll(LabelInfoBeans);
        adapter.notifyDataSetChanged();

        if (isScrollToBottom) {
            scrollToBottom(scroll_view, View.FOCUS_DOWN);
        }

    }


    @BindView(R.id.scroll_view)
    GrScrollView scroll_view;


    private boolean isRunning = true;

    public void scrollToBottom(ScrollView scroll_view, int position) {
        isRunning = true;
        scroll_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isRunning) {
                    scroll_view.post(new Runnable() {
                        public void run() {
                            scroll_view.fullScroll(position);
                            isRunning = false;
                        }
                    });
                }
            }
        });
    }


}
