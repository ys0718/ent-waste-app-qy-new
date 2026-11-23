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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.HistoryInfoBean;
import com.grkj.ytjqy_pad.mvp.presenter.HistoryPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;
import com.grkj.ytjqy_pad.utils.popwindow.model.DeletePopupWindow;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author shu
 * @Version 1.0
 * @Date: 2022/07/27/ 16:21
 * @Description 可搜索的下拉框
 **/
public class HistorySearchSpinner extends RelativeLayout implements View.OnClickListener, HistoryPresenter.View, BaseView {

    private static final String TAG = "SearchSpinner";
    private EditText editText;
    private ImageView mRightIv;
    private View mRightImageTopView;
    private Context mContext;
    private DeletePopupWindow popupWindow;
    private TypedArray tArray;
    private List<HistoryInfoBean> datas = new ArrayList<>();
    private RelativeLayout mLayout;
    private ImageView iv_clear;
    private List<HistoryInfoBean> temList = new ArrayList<>();
    private Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    private int ori = mConfiguration.orientation; //获取屏幕方向

    private HistoryPresenter mPresenter;
    private String key = "";
    private String token;

    private String hint = "请输入/选择";
    private List<SelectPopupWindowBean> popList = new ArrayList<>();
    private String insertTxt;

    public HistorySearchSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(attrs);
    }

    /**
     * 反显绑定数据
     *
     * @param text
     */
    public void setText(String text) {
        this.insertTxt = text;
        editText.setText(this.insertTxt);
    }

    /**
     * 设置请求key值，并作为默认值缓存
     */
    public void setKey(String key) {
        this.key = key;
        String lastValue = SPUtils.get(mContext, key, "").toString();
        setHint(hint);

        if (!TextUtils.isEmpty(lastValue) && TextUtils.isEmpty(insertTxt)){
            setText(lastValue);
        }
    }


    /**
     * 设置请求key值，并作为默认值缓存
     */
    public void setKey(String key,boolean isSetFirst) {
        this.key = key;
        if (isSetFirst){
            String lastValue = SPUtils.get(mContext, key, "").toString();
            setText(lastValue);
        }
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
    public HistoryInfoBean getSelect() {
        for (HistoryInfoBean data : datas) {
            if (data.name.equals(editText.getText().toString())) {
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
        this.hint = hint;
        editText.setHint(hint);
    }


    /**
     * 初始化布局
     */
    private void initView(AttributeSet attrs) {
        mPresenter = new HistoryPresenter();
        mPresenter.attachView(this);
        token = SPUtils.get(mContext, "token", "").toString();

        LayoutInflater.from(mContext).inflate(R.layout.edit_spinner, this);
        editText = findViewById(R.id.edit_sipnner_edit);
        mLayout = findViewById(R.id.mLayout);
        iv_clear = findViewById(R.id.iv_clear);
        editText.setOnClickListener(this);
        mRightIv = findViewById(R.id.edit_spinner_expand);
        mRightImageTopView = findViewById(R.id.edit_spinner_expand_above);
        mRightImageTopView.setVisibility(GONE);
        mRightImageTopView.setOnClickListener(this);
        mRightImageTopView.setClickable(false);
        mRightIv.setOnClickListener(this);
        tArray = mContext.obtainStyledAttributes(attrs, R.styleable.CommonAttrs);
        editText.setText("");
        editText.setHint(hint);

        //清空
        iv_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (onInsertListener != null) {
                    onInsertListener.insertChange("", "", false);
                }

                editText.setText("");

            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    String inputValue = editText.getText().toString();
                    if (!TextUtils.isEmpty(inputValue)) {
                        mPresenter.saveHistory(key, inputValue, token, mContext);
                    }
                }
            }
        });


        //输入监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String key = s.toString().trim();

                KLog.e("输入内容为：" + key);

                editText.setSelection(key.length());

                if (!TextUtils.isEmpty(key)) {
                    iv_clear.setVisibility(VISIBLE);
                } else {
                    iv_clear.setVisibility(GONE);
                }


            }
        });


//        editText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
//                    editText.setText("");
//                    return true;
//                }
//                return false;
//            }
//        });


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

    public void setDefaultText() {
        if (datas != null && datas.size() != 0) {
            editText.setText(datas.get(0).name);
        }
    }

    private onInsertListener onInsertListener;

    public void setOnInsertListener(HistorySearchSpinner.onInsertListener onInsertListener) {
        this.onInsertListener = onInsertListener;
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    @Override
    public void getHistoryListSuccess(List<HistoryInfoBean> list) {
        KLog.e("getHistoryListSuccess:" + list);
        datas.clear();
        String json = GsonUtil.GsonToString(list);
        datas = GsonUtil.JsonToList(json, HistoryInfoBean.class);
        initPopupWindow();
    }

    @Override
    public void saveHistorySuccess(Object result) {
        popupWindow.dismiss();
    }

    @Override
    public void deleteHistorySuccess(String name) {
        popupWindow.dismiss();
        mPresenter.getHistoryList(key, token, mContext);

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
            popList.add(new SelectPopupWindowBean(datas.get(i).name, datas.get(i).name));
        }

        popupWindow = new DeletePopupWindow(mContext, popList, new DeletePopupWindow.OnClickItemListener() {
            @Override
            public void onDo(SelectPopupWindowBean model, String mType) {
                editText.setText(model.NAME);
                SPUtils.put(mContext, key, model.NAME);
                popupWindow.dismiss();
            }

            @Override
            public void onDelete(SelectPopupWindowBean model) {
                mPresenter.deleteHistory(key, model.NAME, token, mContext);
            }
        });


        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        int maxHeight = 10;
        if (maxHeight > datas.size()) {
            maxHeight = datas.size() + 2;
        }

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.bg_select);
            popupWindow.setBackgroundDrawable(drawable);
            popupWindow.setWidth(mLayout.getWidth());
            popupWindow.show(popList, "", mLayout, maxHeight);
        } else {
            //下拉内容宽度设置为整个屏幕宽度，并向左偏移使弹窗铺满屏幕
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels - 100;
            int[] location = new int[2];
            mLayout.getLocationOnScreen(location);
            popupWindow.setData(popList, "");
            popupWindow.setWidth(screenWidth);
            popupWindow.setHeight(mLayout.getHeight() * maxHeight);
            popupWindow.showAsDropDown(mLayout, -location[0] + 50, 0);
        }

    }


    public void clearFocus() {
        String inputValue = editText.getText().toString();
        if (!TextUtils.isEmpty(inputValue)) {
            mPresenter.saveHistory(key, inputValue, token, mContext);
        }

        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    /**
     * 监听点击事件
     *
     * @param v
     */
    @Override
    public final void onClick(View v) {
        if (v.getId() == R.id.edit_spinner_expand_above) {
//            v.setClickable(false);
            return;
        }

//        if (v.getId() == R.id.edit_sipnner_edit || v.getId() == R.id.mLayout || v.getId() == R.id.edit_spinner_expand) {
//            mPresenter.getHistoryList(key,token,mContext);
//        }
        if (v.getId() == R.id.edit_spinner_expand) {
            mPresenter.getHistoryList(key, token, mContext);
        }
    }

}
