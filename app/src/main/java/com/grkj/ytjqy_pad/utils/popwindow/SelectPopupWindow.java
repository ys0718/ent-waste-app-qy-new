package com.grkj.ytjqy_pad.utils.popwindow;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.grkj.ytjqy_pad.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2018/12/28.
 */

public class SelectPopupWindow extends PopupWindow {
    private final static int SINGLE = 1; //单选
    private final static int CHECK = 2; //复选check
    public View mView;
    public LinearLayout mMainLayout;
    public RecyclerView mRecyclerView;
    public CommonAdapter<SelectPopupWindowBean> adapter;
    private List<SelectPopupWindowBean> mData;
    private int clickType = SINGLE;

    public String mType;

    private Context mContext;

    public SelectPopupWindow(Context mContext, final List<SelectPopupWindowBean> data, OnClickItemListener listener) {
        super(mContext);
        this.mData = new ArrayList<>();
//        mData.add(new SelectPopupWindowBean("","请选择"));
        mData.addAll(data);
        this.mContext = mContext;
        this.mClickItemListenerInterface = listener;
        clickType = SINGLE;
        initView();
    }

    public SelectPopupWindow(Context mContext, final List<SelectPopupWindowBean> data, OnCheckClickItemListener listener) {
        super(mContext);
        this.mData = data;
        this.mContext = mContext;
        this.mCheckClickListenerInterface = listener;
        clickType = CHECK;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popupwindow_select, null);
        mMainLayout = (LinearLayout) mView.findViewById(R.id.mMainLayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CommonAdapter<SelectPopupWindowBean>(mContext, R.layout.item_popupwindow_select, mData) {
            @Override
            protected void convert(ViewHolder holder, final SelectPopupWindowBean bean, final int i) {
                holder.setText(R.id.mText, bean.NAME);
                if (clickType == CHECK) {
                    ((ImageView) holder.getView(R.id.img_select)).setSelected(bean.SELECT);
//                    ((ImageView)holder.getView(R.id.img_select)).setImageResource(R.drawable.checkbox_style_2);
                    holder.getView(R.id.img_select).setVisibility(View.VISIBLE);
                }

                holder.setOnClickListener(R.id.mLayout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickType == SINGLE) { //单选
                            mClickItemListenerInterface.onDo(mData.get(i), mType);
                        } else if (clickType == CHECK) {  //复选
                            bean.SELECT = !bean.SELECT;
                            ((ImageView) holder.getView(R.id.img_select)).setSelected(bean.SELECT);
                            adapter.notifyDataSetChanged();
                            List<SelectPopupWindowBean> lies_result = new ArrayList<>();
                            boolean isAll = false;
                            for (SelectPopupWindowBean bean : mData) {
                                if (bean.NAME.equals("全部") && bean.SELECT) {
                                    isAll = true;
                                }
                                if (!bean.equals("全部") && isAll) {
                                    bean.SELECT = false;
                                }
                                if (bean.SELECT) {
                                    lies_result.add(bean);
                                }
                            }
                            mCheckClickListenerInterface.onDo(mType, lies_result);
                        }
                    }
                });
            }

        };

        mRecyclerView.setAdapter(adapter);

        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Select_Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    private OnClickItemListener mClickItemListenerInterface;

    public interface OnClickItemListener {
        void onDo(SelectPopupWindowBean model, String mType);
    }

    public void setClicklistener(OnClickItemListener mClickItemListenerInterface) {
        clickType = SINGLE;
        this.mClickItemListenerInterface = mClickItemListenerInterface;
    }


    private OnCheckClickItemListener mCheckClickListenerInterface;

    public interface OnCheckClickItemListener {
        void onDo(String mType, List<SelectPopupWindowBean> datas);
    }

    public void setCheckListener(OnCheckClickItemListener mCheckClickListenerInterface) {
        clickType = CHECK;
        this.mCheckClickListenerInterface = mCheckClickListenerInterface;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public List<SelectPopupWindowBean> getDatas() {
        return adapter.getDatas();
    }

    /**
     * 设置数据
     *
     * @param mBeans 列表数据
     * @param mType  数据类型
     */
    public void setData(List<SelectPopupWindowBean> mBeans, String mType) {
        this.mType = mType;
        mData = mBeans;
        adapter.notifyDataSetChanged();
    }

    /**
     * 显示popupwindow选择
     *
     * @param mView 位置视图
     * @param sort  倍数
     */
    public void show(View mMainView, String mDataType, View mView, int sort) {
        this.setData(mData, mDataType);
        if (isLandscape()) {//横屏：宽度与锚点控件保持一致
            this.setWidth(mView.getWidth());
            this.setHeight(mView.getHeight() * sort);
            if (mMainView.getHeight() - mView.getY() < mView.getHeight() * sort) {
                this.showAsDropDown(mView, 0, -mView.getHeight() * (sort - 1));
            } else {
                this.showAsDropDown(mView, 0, 0);
            }
        } else {
            //竖屏：下拉内容宽度设置为整个屏幕宽度，并向左偏移使弹窗铺满屏幕
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels - 100;
            int[] location = new int[2];
            mView.getLocationOnScreen(location);
            this.setWidth(screenWidth);
            this.setHeight(mView.getHeight() * sort);
            if (mMainView.getHeight() - mView.getY() < mView.getHeight() * sort) {
                this.showAsDropDown(mView, -location[0] + 50, -mView.getHeight() * (sort - 1));
            } else {
                this.showAsDropDown(mView, -location[0] + 50, 0);
            }
        }
    }

    public void setBackGround(int resid) {
        mMainLayout.setBackgroundResource(resid);
    }

    /**
     * 是否横屏
     */
    private boolean isLandscape() {
        return mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void show(List<SelectPopupWindowBean> mBeans, String mDataType, View mView, int sort) {
        this.setData(mBeans, mDataType);
        if (isLandscape()) {//横屏：宽度与锚点控件保持一致
            this.setWidth(mView.getWidth());
            this.setHeight(mView.getHeight() * sort);
            this.showAsDropDown(mView, 0, 0);
        } else {
            //竖屏：下拉内容宽度设置为整个屏幕宽度，并向左偏移使弹窗铺满屏幕
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels - 100;
            int[] location = new int[2];
            mView.getLocationOnScreen(location);
            this.setWidth(screenWidth);
            this.setHeight(mView.getHeight() * sort);
            this.showAsDropDown(mView, -location[0] + 50, 0);
        }
    }

}
