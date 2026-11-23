package com.grkj.ytjqy_pad.utils.popwindow.model;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2018/12/28.
 * 可删除下拉框
 */

public class DeletePopupWindow extends PopupWindow{
    public View mView;
    public LinearLayout mMainLayout;
    public RecyclerView mRecyclerView;
    public CommonAdapter<SelectPopupWindowBean> adapter;
    private List<SelectPopupWindowBean> mData;

    public String mType;

    private Context mContext;

    public DeletePopupWindow(Context mContext,final List<SelectPopupWindowBean> data,OnClickItemListener listener){
        super(mContext);
        this.mData = new ArrayList<>();
        mData.addAll(data);
        this.mContext = mContext;
        this.mClickItemListenerInterface = listener;
        initView();
    }


    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popupwindow_select,null);
        mMainLayout = (LinearLayout) mView.findViewById(R.id.mMainLayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new CommonAdapter<SelectPopupWindowBean>(mContext,R.layout.item_popupwindow_select,mData){
            @Override
            protected void convert(ViewHolder holder,final SelectPopupWindowBean bean,final int i){
                holder.setText(R.id.mText,bean.NAME);
                ImageView img_select = holder.getView(R.id.img_select);
                img_select.setVisibility(View.VISIBLE);
                img_select.setImageResource(R.mipmap.ic_delete_colorful);
                holder.setOnClickListener(R.id.mLayout,new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        if(mClickItemListenerInterface!=null){
                            mClickItemListenerInterface.onDo(mData.get(i),mType);
                        }


                    }
                });

                holder.setOnClickListener(R.id.img_select,new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(mClickItemListenerInterface!=null){
                            mClickItemListenerInterface.onDelete(mData.get(i));
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

    public interface OnClickItemListener{
        void onDo(SelectPopupWindowBean model,String mType);
        void onDelete(SelectPopupWindowBean model);
    }

    public void setClicklistener(OnClickItemListener mClickItemListenerInterface){
        this.mClickItemListenerInterface = mClickItemListenerInterface;
    }



    /**
     * 获取数据
     *
     * @return
     */
    public List<SelectPopupWindowBean> getDatas(){
        return adapter.getDatas();
    }

    /**
     * 设置数据
     *
     * @param mBeans 列表数据
     * @param mType  数据类型
     */
    public void setData(List<SelectPopupWindowBean> mBeans,String mType){
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
    public void show(View mMainView,String mDataType,View mView,int sort){
        this.setData(mData,mDataType);
        this.setWidth(mView.getWidth());
        this.setHeight(mView.getHeight() * sort);
        if (mMainView.getHeight()-mView.getY() < mView.getHeight() * sort) {
            this.showAsDropDown(mView,100,-mView.getHeight() * (sort-1));
        } else {
            this.showAsDropDown(mView,100,0);
        }
    }

    public void setBackGround(int resid){
        mMainLayout.setBackgroundResource(resid);
    }

    public void show(List<SelectPopupWindowBean> mBeans,String mDataType,View mView,int sort){
        this.setData(mBeans,mDataType);
        this.setWidth(mView.getWidth());
        this.setHeight(mView.getHeight() * sort);
        this.showAsDropDown(mView,0,0);
    }

}
