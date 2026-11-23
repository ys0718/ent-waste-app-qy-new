package com.grkj.ytjqy_pad.mvp.view.activity.zyld;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseActivity;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.EnterpriseInfoBean;
import com.grkj.ytjqy_pad.mvp.model.ParamListBean;
import com.grkj.ytjqy_pad.mvp.model.UserBean;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyld;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldFwxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.GfWxfwZyldYsxx;
import com.grkj.ytjqy_pad.mvp.model.zyld.JsdwxxBean;
import com.grkj.ytjqy_pad.mvp.presenter.ZyldInfoPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.spinner.HistorySearchSpinner;
import com.grkj.ytjqy_pad.utils.view.AdjustableView;
import com.grkj.ytjqy_pad.utils.view.MyLinearLayout;
import com.grkj.ytjqy_pad.utils.view.SelectTimeView;
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
 * Created by yusam on 2026/9/11.
 * 转移联单信息
 */
public class ZyldInfoActivity extends BaseActivity<ZyldInfoPresenter> implements ZyldInfoPresenter.View {


    //总条数
    @BindView(R.id.tv_num)
    TextView tv_num;

    //父布局
    @BindView(R.id.mLayout)
    LinearLayout mLayout;

    //全选
    @BindView(R.id.iv_select_all)
    ImageView iv_select_all;


    //搜索-入库时间
    @BindView(R.id.sv_rksj)
    SelectTimeView sv_rksj;

    //搜索-废物名称
    @BindView(R.id.et_scss)
    EditText et_scss;

    //转移类型
    @BindView(R.id.et_item_zylx)
    EditText et_item_zylx;

    //联单类型
    @BindView(R.id.et_item_ldlx)
    EditText et_item_ldlx;

    //国家联单编号
    @BindView(R.id.et_item_gjldbh)
    EditText et_item_gjldbh;

    //精细化联单编号
    @BindView(R.id.et_item_jxhldbh)
    EditText et_item_jxhldbh;

    //移出单位联系人
    @BindView(R.id.et_item_ycdwlxr)
    EditText et_item_ycdwlxr;

    //移出单位联系人手机
    @BindView(R.id.et_item_ycdwlxrsj)
    EditText et_item_ycdwlxrsj;

    //应急联系电话
    @BindView(R.id.et_item_yjlxdh)
    EditText et_item_yjlxdh;

    //运送部门经办人
    @BindView(R.id.et_item_ysbmjbr)
    HistorySearchSpinner et_item_ysbmjbr;

    //出库部门经办人
    @BindView(R.id.et_item_ckbmjbr)
    HistorySearchSpinner et_item_ckbmjbr;

    //备注
    @BindView(R.id.et_item_bz)
    EditText et_item_bz;

    //运输单位名称
    @BindView(R.id.et_item_ysdwmc)
    EditText et_item_ysdwmc;

    //运输单位联系人
    @BindView(R.id.et_item_ysdwlxr)
    EditText et_item_ysdwlxr;

    //运输单位搜索内容
    @BindView(R.id.et_ycdw_search)
    HistorySearchSpinner et_ycdw_search;


    //运输单位联系人手机
    @BindView(R.id.et_item_ysdwlxrsj)
    EditText et_item_ysdwlxrsj;

    //道路运输许可证号
    @BindView(R.id.et_item_ysdwxkz)
    EditText et_item_ysdwxkz;

    //统一社会信用代码
    @BindView(R.id.et_item_ysdwxydm)
    EditText et_item_ysdwxydm;

    //运输单位地址
    @BindView(R.id.et_item_ysdwdz)
    EditText et_item_ysdwdz;

    //接收单位名称
    @BindView(R.id.et_item_jsdwmc)
    EditText et_item_jsdwmc;

    //接收单位电话
    @BindView(R.id.et_item_jsdwdh)
    EditText et_item_jsdwdh;

    //接收单位经营许可证号
    @BindView(R.id.et_item_jsdwxkz)
    EditText et_item_jsdwxkz;

    //接收单位联系人
    @BindView(R.id.et_item_jsdwlxr)
    EditText et_item_jsdwlxr;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //主页
    @BindView(R.id.ll_home)
    LinearLayout ll_home;

    //搜索区域
    @BindView(R.id.cv_search)
    LinearLayout cv_search;

    //搜索按钮
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    //全选
    @BindView(R.id.ll_select_all)
    LinearLayout ll_select_all;

    //清空
    @BindView(R.id.ll_clear_all)
    LinearLayout ll_clear_all;

    //清空图标
    @BindView(R.id.iv_clear_all)
    ImageView iv_clear_all;

    //运输单位删除图标
    @BindView(R.id.iv_delete_ysdw)
    ImageView iv_delete_ysdw;

    //运输单位搜索区域
    @BindView(R.id.ll_search_ysdw)
    LinearLayout ll_search_ysdw;

    //选择接收单位
    @BindView(R.id.ll_select_jsdw)
    LinearLayout ll_select_jsdw;

    //新增废物
    @BindView(R.id.ll_select_fwxx)
    LinearLayout ll_select_fwxx;

    //保存
    @BindView(R.id.ll_save)
    LinearLayout ll_save;

    //接收单位经办人布局
    @BindView(R.id.ll_jsdwjbr)
    LinearLayout ll_jsdwjbr;

    //上报国家
    @BindView(R.id.ll_upload)
    LinearLayout ll_upload;

    //删除
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;

    //基本信息布局
    @BindView(R.id.ll_base_info)
    MyLinearLayout ll_base_info;

    //接收单位布局
    @BindView(R.id.ll_jsdw)
    MyLinearLayout ll_jsdw;

    //运输单位布局
    @BindView(R.id.ll_ysdw)
    MyLinearLayout ll_ysdw;

    //收缩/展开
    AdjustableView adjustableView;

    private GfWxfwZyld mData = null;

    private boolean editable = false;//是否可上报国家
    private boolean isDbld = false;//是否待办联单

    @Override
    protected int getLayout() {
        return R.layout.activity_zyld_info;
    }


    private Map<String, String> params = new HashMap<>();
    private boolean isNew = true;//是否新建

    @Override
    protected void initEventAndData() {

        String dataStr = getIntent().getStringExtra(Constants.JUMP_KEY);
        isDbld = getIntent().getBooleanExtra("isDbld",false);
        if (TextUtils.isEmpty(dataStr)) {//新增
            isNew = true;
            mData = new GfWxfwZyld();
            mData.ldlx = "1";//默认省内转移
            et_item_ldlx.setText("省内转移");
            mPresenter.getEnterpriseInfo();
        } else {
            isNew = false;
            GfWxfwZyld parentData = GsonUtil.GsonToBean(dataStr, GfWxfwZyld.class);
            mData = parentData;
            mPresenter.getYsqyInfoById(mData.id);
            mPresenter.getJsdwList(mData.jsdwmc);
            initView();

            ll_base_info.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
            ll_jsdw.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));
            ll_ysdw.setOnInterceptTouchEvent(true, getResources().getColor(R.color.gray));

        }

        et_ycdw_search.setKey(Constants.KEY_YSDWMC,false);
        et_item_ysbmjbr.setKey(Constants.KEY_YSBMJBR);
        et_item_ckbmjbr.setKey(Constants.KEY_CKBMJBR);
        Map<String, String> map = new HashMap<>();
        map.put("types", "联单包装方式,联单转移类型,联单节点状态,联单运输类型,联单运输方式,联单类型");
        mPresenter.getParamList(map);

    }

    //视图回显
    private void initView() {
        if (TextUtils.isEmpty(mData.gjldbh)){//国家联单编号为空时，可上报国家
            ll_upload.setVisibility(View.VISIBLE);
            ll_select_jsdw.setVisibility(View.VISIBLE);
            ll_delete.setVisibility(View.VISIBLE);
            ll_select_fwxx.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);

            editable = true;//可编辑
        }else {
            ll_upload.setVisibility(View.GONE);
            ll_select_jsdw.setVisibility(View.GONE);
            ll_delete.setVisibility(View.GONE);
            ll_search_ysdw.setVisibility(View.GONE);
            iv_delete_ysdw.setVisibility(View.GONE);
            ll_select_fwxx.setVisibility(View.GONE);
            ll_save.setVisibility(View.GONE);

            editable = false;//不可编辑
        }



        if(isDbld){
            ll_jsdwjbr.setVisibility(View.VISIBLE);
            ll_save.setVisibility(View.VISIBLE);
            ll_upload.setVisibility(View.VISIBLE);
        }

        //国家联单编号
        et_item_gjldbh.setText(mData.gjldbh);
        //精细化联单编号
        et_item_jxhldbh.setText(mData.jxhldbh);
        //移出单位联系人
        et_item_ycdwlxr.setText(mData.ycdwlxr);
        //移出单位联系人手机
        et_item_ycdwlxrsj.setText(mData.ycdwlxrsj);
        //应急联系电话
        et_item_yjlxdh.setText(mData.yjlxdh);
        //运送部门经办人
        et_item_ysbmjbr.setText(mData.ysbmjbr);
        //出库部门经办人
        et_item_ckbmjbr.setText(mData.ckbmjbr);
        //备注
        et_item_bz.setText(mData.bz);
        //接收单位名称
        et_item_jsdwmc.setText(mData.jsdwmc);
        //接收单位经营许可证号
        et_item_jsdwxkz.setText(mData.jsdwxkzbh);
        //接收单位联系人
        et_item_jsdwlxr.setText(mData.jsdwlxr);
        //接收单位电话
        et_item_jsdwdh.setText(mData.jsdwlxrsj);

    }

    @Override
    protected void initLandscapeView() {
        super.initLandscapeView();
        initTop();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        initRecycleView();

    }

    @Override
    protected void initPortraitView() {
        super.initPortraitView();
        initTopToolbar("转移联单");
//        adjustableView = findViewById(R.id.adjustableView);
//        LinearLayout ll_top = findViewById(R.id.ll_top);
//
//        adjustableView.setOnClickLayoutListener(new AdjustableView.OnClickLayoutListener() {
//            @Override
//            public void onClick(boolean isOpen) {
//                ll_top.setVisibility(isOpen ? View.VISIBLE : View.GONE);
//            }
//        });
//        mRecyclerView.setLayoutManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        initRecycleView();

    }


    @OnClick({R.id.ll_back, R.id.ll_save, R.id.ll_delete, R.id.et_item_zylx, R.id.et_item_ldlx, R.id.ll_search, R.id.et_scss,
            R.id.ll_select_ysdw, R.id.iv_delete_ysdw, R.id.ll_select_jsdw, R.id.ll_select_fwxx, R.id.ll_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            // 返回
            case R.id.ll_back:
                finish();
                break;

            // 删除运输单位信息
            case R.id.iv_delete_ysdw:
                mPresenter.deleteYsdwxx(zyldYsxx.id);
                break;

            // 精准搜索运输单位
            case R.id.ll_select_ysdw:
                if (TextUtils.isEmpty(mData.id)) {
                    ToastUtil.showToast(mContext, "请先保存数据！");
                    return;
                }

                mPresenter.getYsqyInfo(et_ycdw_search.getText().toString());
                break;


            // 转移类型
            case R.id.et_item_zylx:
                showPopWindow("联单转移类型", et_item_zylx);
                break;

            // 联单类型
            case R.id.et_item_ldlx:
                showPopWindow("联单类型", et_item_ldlx);
                break;

            // 选择接收单位
            case R.id.ll_select_jsdw:

                goToActivity(new Intent(mContext, SelectJsdwListActivity.class)
                                .putExtra("title", "选择接收单位")
                                .putExtra("url", ApiUrl.URL_ZYLD_JSDW_INFO_LIST)
                        , 11);
                break;


            //保存
            case R.id.ll_save:
//                mData.ysjbr = et_item18.getText().toString();
//                mData.zyldbh = editSpinner_21.getText().toString();
//                mData.jsdwmc = editSpinner_22.getText().toString();
                String defaultUserName = SPUtils.get(mContext, Constants.USER_NAME, "").toString();
                String dataFrom = isLandscape ? Constants.TYPE_YTJ : Constants.TYPE_SCZD;
                String deviceCode = SPUtils.get(mContext, "DEVICE_CODE", "").toString();
                String dwid = SPUtils.get(mContext, Constants.SELECT_DWID, "").toString();

                mData.yjlxdh = et_item_yjlxdh.getText().toString();
                mData.ysbmjbr = et_item_ysbmjbr.getText().toString();
                mData.ckbmjbr = et_item_ckbmjbr.getText().toString();
                mData.bz = et_item_bz.getText().toString();


                if (TextUtils.isEmpty(checkEmpty())) {
                    et_ycdw_search.clearFocus();
                    et_item_ysbmjbr.clearFocus();
                    et_item_ckbmjbr.clearFocus();
                    Map<String, Object> params = GsonUtil.GsonToMaps(GsonUtil.GsonToString(mData));
                    mPresenter.showSaveDialog(params);
                } else {
                    ToastUtil.showToast(mContext, checkEmpty());
                }

                break;


            // 上报国家
            case R.id.ll_upload:
                String url = ApiUrl.URL_ZYLD_SBGJ;
                if (isDbld){
                    url = ApiUrl.URL_ZYLD_SBGJ_TODO;
                }

                mPresenter.showUploadDialog(mData.id, url);
                break;

            // 删除
            case R.id.ll_delete:
                mPresenter.showDeleteDialog(mData.id);
                break;


            //搜索
            case R.id.ll_search:
                params.put("startTime", sv_rksj.getStartTime());
                params.put("endTime", sv_rksj.getEndTime());
                params.put("fwxxmc", et_scss.getText().toString());
                break;

            //新增废物信息
            case R.id.ll_select_fwxx:

                if (TextUtils.isEmpty(mData.id)) {
                    ToastUtil.showToast(mContext, "请先保存数据！");
                    return;
                }

                if (selectFwList.size() != 0) {
                    goToActivity(new Intent(mContext, ZyldFwxxAddActivity.class)
                                    .putExtra(Constants.MAINID, mData.id)
                                    .putExtra("zylx", mData.zylx)
                                    .putExtra("fwList", GsonUtil.GsonToString(selectFwList))
                            , 12);
                } else {
                    ToastUtil.showToast(mContext, "该接收单位未添加废物信息！");
                }


                break;

        }
    }

    private List<JsdwxxBean.Wtlysz> selectFwList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 9://刷新废物列表
                mPresenter.getFwList(mData.id);
                break;
            case 10://运输单位信息
                mPresenter.getYsqyInfoById(mData.id);
                break;
            case 11://选择处置单位信息
                String selectBeanStr = data.getStringExtra("selectBean");
                JsdwxxBean jsdwxxBean = GsonUtil.GsonToBean(selectBeanStr, JsdwxxBean.class);
                et_item_jsdwmc.setText(jsdwxxBean.jydwmc);
                et_item_jsdwlxr.setText(jsdwxxBean.lxr);
                et_item_jsdwdh.setText(jsdwxxBean.lxrsj);
                et_item_jsdwxkz.setText(jsdwxxBean.xkzbh);

                mData.jsdwlxr = jsdwxxBean.lxr;
                mData.jsdwmc = jsdwxxBean.jydwmc;
                mData.jsdwlxrsj = jsdwxxBean.lxrsj;
                mData.jsdwqyid = jsdwxxBean.jyqyid;
                mData.jsdwxkzbh = jsdwxxBean.xkzbh;
                selectFwList = jsdwxxBean.wtlysz;

                break;
        }
    }

    //运输单位信息
    private GfWxfwZyldYsxx zyldYsxx = null;

    //运输单位信息回显
    private void initYsdwView(GfWxfwZyldYsxx ysdwInfo) {
        zyldYsxx = ysdwInfo;
        ll_search_ysdw.setVisibility(View.GONE);
        if (editable){
            iv_delete_ysdw.setVisibility(View.VISIBLE);
        }
        et_item_ysdwmc.setText(ysdwInfo.ysdwmc);
        et_item_ysdwlxr.setText(ysdwInfo.ysdwlxr);
        et_item_ysdwlxrsj.setText(ysdwInfo.ysdwlxrsj);
        et_item_ysdwdz.setText(ysdwInfo.ysdwdz);
        et_item_ysdwxkz.setText(ysdwInfo.dlysjyxkzh);
        et_item_ysdwxydm.setText(ysdwInfo.tyshxydm);
    }


    /*
     * 判空处理
     * */
    private String checkEmpty() {
        if (TextUtils.isEmpty(mData.jsdwqyid)) {
            return "请选择接收单位！";
        } else {
            return "";
        }
    }


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<GfWxfwZyldFwxx> mFwList = new ArrayList<>();
    ;//数据列表
    private CommonAdapter<GfWxfwZyldFwxx> adapter;//适配器

    /**
     * 废物列表初始化
     */
    private void initRecycleView() {
        adapter = new CommonAdapter<GfWxfwZyldFwxx>(mContext, R.layout.item_common_waste_info, mFwList) {
            @Override
            protected void convert(ViewHolder holder, final GfWxfwZyldFwxx bean, final int position) {
                if (bean != null) {
                    holder.setText(R.id.tv_item1, TextUtils.isEmpty(bean.fwmc) ? getString(R.string.common_empty) : bean.fwmc);

                    setText(holder.getView(R.id.tv_item2), bean.fwdm, R.string.str_record_list2);
                    setText(holder.getView(R.id.tv_item3), bean.lyczfs, R.string.str_jytz_list6);
                    setText(holder.getView(R.id.tv_item4), bean.cfxxtybh, R.string.str_zyld_item29);
                    setText(holder.getView(R.id.tv_item5), commonTransfer(bean.bzfs, "联单包装方式"), R.string.str_zyld_item30);

                    TextView tv_item4 = holder.getView(R.id.tv_item4);
                    tv_item4.setEllipsize(TextUtils.TruncateAt.MIDDLE);

                    holder.getView(R.id.rl_weight).setVisibility(View.GONE);
                    LinearLayout item_layout = holder.getView(R.id.item_layout);
                    item_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bean.zylx = mData.zylx;
                            goToActivity(new Intent(mContext, ZyldFwxxAddActivity.class)
                                            .putExtra("zylx", mData.zylx)
                                            .putExtra("editable", editable)
                                            .putExtra("isDbld", isDbld)
                                            .putExtra("fwList", GsonUtil.GsonToString(selectFwList))
                                            .putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(bean))
                                    , 12);
                        }
                    });

                    ImageView iv_delete = holder.getView(R.id.iv_delete);
                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 2026/9/15 调用废物删除
                        }
                    });


                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                mFwList.get(i).SELECT = !mFwList.get(i).SELECT;
                adapter.notifyDataSetChanged();
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

        ((TextView) tv).setText(Html.fromHtml(TextUtils.isEmpty(value) ? getString(strRes, getString(R.string.common_empty_text)) : getString(strRes, value)));
    }


    @Override
    protected ZyldInfoPresenter createPresenter() {
        return new ZyldInfoPresenter();
    }

    @Override
    public void showError(String msg) {
        toastShow(msg);
    }


    @Override
    public void saveDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "保存成功！");
        setResult(9);
        mData = GsonUtil.GsonToBean(GsonUtil.GsonToString(bean), GfWxfwZyld.class);
        ll_upload.setVisibility(View.VISIBLE);
//        ll_save.setVisibility(View.GONE);

        initView();

    }

    @Override
    public void uploadDataSuccess(Object bean) {
        ToastUtil.showToast(mContext, "上报成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getEnterpriseInfoSuccess(EnterpriseInfoBean bean) {
        mData.ycdwlxr = bean.lxrname;
        mData.ycdwlxrsj = bean.lxrphone;
        mData.yjlxdh = bean.lxrphone;

        //移出单位联系人
        et_item_ycdwlxr.setText(mData.ycdwlxr);
        //移出单位联系人手机
        et_item_ycdwlxrsj.setText(mData.ycdwlxrsj);
        //应急联系电话
        et_item_yjlxdh.setText(mData.yjlxdh);

    }

    @Override
    public void getYsqyInfoSuccess(List<GfWxfwZyldYsxx> bean) {

        if (bean != null && bean.size() != 0) {
            GfWxfwZyldYsxx zyldYsxx = bean.get(0);
            zyldYsxx.mainid = mData.id;
            goToActivity(new Intent(mContext, YsdwAddActivity.class).putExtra(Constants.JUMP_KEY, GsonUtil.GsonToString(zyldYsxx)), 11);
        } else {
            ToastUtil.showToast(mContext, "暂无数据，请输入精准企业名称后重新搜索！");
        }

    }

    @Override
    public void getYsqyInfoByIdSuccess(List<GfWxfwZyldYsxx> list) {

        if (list != null && list.size() != 0) {
            initYsdwView(list.get(0));
        }
    }

    @Override
    public void deleteYsdwxxSuccess(String msg) {
        ToastUtil.showToast(mContext, msg);
        initYsdwView(new GfWxfwZyldYsxx());
        ll_search_ysdw.setVisibility(View.VISIBLE);
        iv_delete_ysdw.setVisibility(View.GONE);
    }

    @Override
    public void getJsdwListSuccess(JsdwxxBean bean) {

        if (!TextUtils.isEmpty(bean.jyqyid)) {
            selectFwList = bean.wtlysz;
        }
    }

    @Override
    public void getFwListSuccess(List<GfWxfwZyldFwxx> list) {
        mFwList.clear();
        mFwList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteDataSuccess(String msg) {
        ToastUtil.showToast(mContext, "删除成功！");
        setResult(9);
        finish();
    }

    @Override
    public void getParamListSuccess(List<ParamListBean> bean) {
        String json = GsonUtil.GsonToString(bean);
        allParamList = GsonUtil.JsonToList(json, ParamListBean.class);

        if (!TextUtils.isEmpty(mData.id)) {
            //转移类型
            et_item_zylx.setText(commonTransfer(mData.zylx, "联单转移类型"));
            //联单类型
            et_item_ldlx.setText(commonTransfer(mData.ldlx, "联单类型"));

            mPresenter.getFwList(mData.id);
        }

    }

    private SelectPopupWindow selectPopupWindow;
    protected List<ParamListBean> allParamList = new ArrayList<>();
    protected List<UserBean> receiverList = new ArrayList<>();


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
                //废物特性,物理特性,包装方式,计量单位,处理方式及去向,包装材质,包装规模
                switch (mType) {
                    case "联单转移类型":
                        mData.zylx = model.ID;
                        break;
                    case "联单类型":
                        mData.ldlx = model.ID;
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


}
