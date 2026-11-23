package com.grkj.ytjqy_pad.utils.popwindow.spinner;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.DeletePopupWindow;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author shu
 * @Version 1.0
 * @Date: 2022/07/27/ 16:21
 * @Description 可搜索的下拉框
 **/
public class NewSearchSpinner extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "SearchSpinner";
    private EditText editText;
    private ImageView mRightIv;
    private View mRightImageTopView;
    private Context mContext;
    private SelectPopupWindow popupWindow;
//    private ListPopupWindow popupWindow;
    private TypedArray tArray;
    private boolean isPopupWindowShowing;
    private List<SelectPopupWindowBean> datas = new ArrayList<>();
    private boolean isNotSearch = false;//是否不进行搜索
    private String beforeStr = "";
    private RelativeLayout mLayout;
    private ImageView iv_clear;
    private List<SelectPopupWindowBean> temList = new ArrayList<>();
    private List<SelectPopupWindowBean> popList = new ArrayList<>();
    
    
    private Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    private int ori = mConfiguration.orientation; //获取屏幕方向
    private boolean isCallback = false;//是否已回调

    public NewSearchSpinner(Context context,AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.disabled = false;
        this.isNotSearch = true;
        initView(attrs);
    }


    /**
     * 设置数据源
     *
     * @param data
     */
    public void setItemData(List<SelectPopupWindowBean> data) {
        temList = data;
        datas.clear();
        datas.addAll(temList);
        editText.setHint("请选择/输入");
        initPopupWindow();

    }

    private boolean disabled = false;

    //设置不可选
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        editText.setEnabled(!disabled);

        //如果是不可选，设置不可点击、隐藏下拉图标
        if (disabled) {
            mRightIv.setVisibility(GONE);
            iv_clear.setVisibility(GONE);
            mLayout.setClickable(false);
            isNotSearch = true;
        } else {
            mRightIv.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                iv_clear.setVisibility(VISIBLE);
            }
            isNotSearch = false;
            mLayout.setClickable(true);
        }

    }

    /**
     * 反显绑定数据
     *
     * @param text
     */
    public void setText(String text) {
        String insertTxt = text;
        editText.setText(insertTxt);
    }


    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        editText.setTextColor(color);
    }


    /**
     * 获取文本
     *
     * @return
     */
    public String getText() {
        return editText.getText().toString();
    }


    /**
     * 返回现在的数据
     *
     * @return
     */
    public SelectPopupWindowBean getSelect() {
        for (SelectPopupWindowBean data : datas) {
            if (data.NAME.equals(editText.getText().toString())) {
                return data;
            }
        }
        return null;
    }


    /**
     * 设置提示文本
     *
     * @param hint
     */
    public void setHint(String hint) {
        editText.setHint(hint);
    }


    public void setRightImageDrawable(Drawable drawable) {
        mRightIv.setImageDrawable(drawable);
    }

    public void setRightImageResource(@DrawableRes int res) {
        mRightIv.setImageResource(res);
    }



    /**
     * 初始化布局
     */
    private void initView(AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.edit_spinner, this);
        editText =  findViewById(R.id.edit_sipnner_edit);
        mLayout = findViewById(R.id.mLayout);
        iv_clear = findViewById(R.id.iv_clear);
        editText.setOnClickListener(this);
        mRightIv =  findViewById(R.id.edit_spinner_expand);
        mRightImageTopView = findViewById(R.id.edit_spinner_expand_above);
        mRightImageTopView.setOnClickListener(this);
        mRightImageTopView.setClickable(false);
        mRightIv.setOnClickListener(this);
        tArray = mContext.obtainStyledAttributes(attrs, R.styleable.CommonAttrs);


        //清空
        iv_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                isNotSearch = false;
                isCallback = true;
                if (onInsertListener != null) {
                    onInsertListener.insertChange("", "", false);
                }

                editText.setText("");

            }
        });

        //输入监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (disabled) {
                    return;
                }

                String key = s.toString();

                editText.setHint(R.string.str_loading);
                editText.setSelection(key.length());

                if (!TextUtils.isEmpty(key)) {
                    iv_clear.setVisibility(VISIBLE);
                } else {
                    iv_clear.setVisibility(GONE);
                }

                if (!TextUtils.isEmpty(beforeStr) && !TextUtils.isEmpty(key)) {
                    if (beforeStr.equals(key)) {
                        isNotSearch = true;
                    } else {
                        isNotSearch = false;
                    }
                } else if (TextUtils.isEmpty(beforeStr) && !TextUtils.isEmpty(key)) {
                    isNotSearch = false;
                }

                if (onInsertListener != null && !isCallback) {
                    onInsertListener.insertChange(s.toString(), getValueByKey(key), isNotSearch);
                }

            }
        });

        editText.setHint(tArray.getString(R.styleable.CommonAttrs_hint));
        int imageId = tArray.getResourceId(R.styleable.CommonAttrs_rightImage, 0);
        if (imageId != 0) {
            mRightIv.setImageResource(imageId);
        }
        int bg = tArray.getResourceId(R.styleable.CommonAttrs_Background, 0);
        if (bg != 0) {
            editText.setBackgroundResource(bg);
        }
        tArray.recycle();

    }

    private onInsertListener onInsertListener;

    public void setOnInsertListener(NewSearchSpinner.onInsertListener onInsertListener) {
        this.onInsertListener = onInsertListener;
    }

    public interface onInsertListener {
        void insertChange(String name, String value, boolean isFromSelect);
    }



    /**
     * 初始化PopupWindow
     */
    private void initPopupWindow() {

        popList = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            popList.add(new SelectPopupWindowBean(datas.get(i).ID,datas.get(i).NAME));
        }

        popupWindow = new SelectPopupWindow(mContext,popList,new SelectPopupWindow.OnClickItemListener(){
            @Override
            public void onDo(SelectPopupWindowBean model,String mType){
//                mRightImageTopView.setClickable(false);
                KLog.e("initPopupWindow:" + model.NAME);
                editText.setText(model.NAME);
                popupWindow.dismiss();
            }
        });

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.bg_select);
            popupWindow.setBackgroundDrawable(drawable);
        }

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setWidth(mLayout.getWidth());

        int maxHeight = 10;
        if (maxHeight > datas.size()) {
            maxHeight = datas.size() + 2;
        }

        if (!popupWindow.isShowing()){
            popupWindow.show(popList,"",mLayout,maxHeight);
        }else {
            popupWindow.setData(popList,"");
        }
        
        
        
//        popupWindow = new ListPopupWindow(mContext) {
//            @Override
//            public boolean isShowing() {
//                return isPopupWindowShowing;
//            }
//
//            @Override
//            public void show() {
//                super.show();
//                isPopupWindowShowing = true;
//                mRightImageTopView.setClickable(true);
////                mRightIv.startAnimation(mAnimation);
//            }
//        };
//
//
//        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
//            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.bg_select);
//            popupWindow.setBackgroundDrawable(drawable);
//        }else{
//            Drawable drawable = ContextCompat.getDrawable(mContext, R.color.color_bg_cancel);
//            popupWindow.setBackgroundDrawable(drawable);
//        }
//
//        popupWindow.setOnItemClickListener(this);
//        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
////        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        popupWindow.setWidth(mLayout.getWidth());
//
////        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        int maxHeight = 10;
//        if (maxHeight > datas.size()) {
//            maxHeight = datas.size() + 1;
//        }
//
//        popupWindow.setHeight(mLayout.getHeight() * maxHeight);
//        popupWindow.setAnchorView(editText);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                isPopupWindowShowing = false;
//            }
//        });
    }


    /**
     * 监听点击事件
     *
     * @param v
     */
    @Override
    public final void onClick(View v) {
        Log.e(TAG, "onClick: ");
        if (v.getId() == R.id.edit_spinner_expand_above) {
            v.setClickable(false);
            return;
        }

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
        }

        if (v.getId() == R.id.edit_sipnner_edit || v.getId() == R.id.mLayout || v.getId() == R.id.edit_spinner_expand) {
            Log.e(TAG, "点击事件");

            int maxHeight = 10;
            if (maxHeight > datas.size()) {
                maxHeight = datas.size() + 2;
            }
            popupWindow.show(mLayout,"",mLayout,maxHeight);
//            if (!popupWindow.isShowing()) {
//                popupWindow.show();
//            }
        }
    }


    private String getValueByKey(String key) {
        String value = "";
        if (!TextUtils.isEmpty(key) && datas != null && datas.size() != 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (key.equals(datas.get(i).NAME)) {
                    value = datas.get(i).ID;
                }
            }
        }
        return value;
    }



}
