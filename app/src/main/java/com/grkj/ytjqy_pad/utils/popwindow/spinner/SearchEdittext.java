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
import com.grkj.ytjqy_pad.base.BaseView;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.PageBean;
import com.grkj.ytjqy_pad.mvp.model.HtxxBean;
import com.grkj.ytjqy_pad.mvp.model.ZyldBean;
import com.grkj.ytjqy_pad.mvp.presenter.SearchEdittextPresenter;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.SPUtils;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author shu
 * @Version 1.0
 * @Date: 2022/07/27/ 16:21
 * @Description 可搜索的下拉框
 **/
public class SearchEdittext extends RelativeLayout implements View.OnClickListener, SearchEdittextPresenter.View, BaseView, AdapterView.OnItemClickListener {

    private static final String TAG = "SearchSpinner";
    private EditText editText;
    private ImageView mRightIv;
    private View mRightImageTopView;
    private Context mContext;
    private ListPopupWindow popupWindow;
    BaseEditSpinnerAdapter adapter;
    private TypedArray tArray;
    private boolean isPopupWindowShowing;
    private List<Select> datas = new ArrayList<>();
    private boolean isNotSearch = false;//是否不进行搜索
    private String beforeStr = "";
    private RelativeLayout mLayout;
    private ImageView iv_clear;
    private List<Select> temList = new ArrayList<>();
    private List rawList = new ArrayList();
    private Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    private int ori = mConfiguration.orientation; //获取屏幕方向
    private boolean isCallback = false;//是否已回调
    private String url;
    private SearchEdittextPresenter mPresenter;
    private String token;
    private String keyName;
    private HashMap<String,String> params;

    public SearchEdittext(Context context, AttributeSet attrs) {
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
    public void setItemData(List<Select> data) {
        temList = data;
        datas.clear();
        datas.addAll(temList);
        adapter = new SimpleAdapter(mContext, datas);
        setAdapter(adapter);
        editText.setHint("请选择/输入");
    }

    public void setUrl(String url, HashMap<String, String> params,String keyName) {
        this.url = url;
        mPresenter.getList(token, mContext, url, params);
        this.keyName = keyName;
        this.params = params;
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

    public void setKey() {

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
    public Select getSelect() {
        for (Select data : datas) {
            if (data.getName().equals(editText.getText().toString())) {
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
     * 设置设配器
     *
     * @param adapter
     */
    public void setAdapter(BaseEditSpinnerAdapter adapter) {
        this.adapter = adapter;
        setBaseAdapter(this.adapter);
    }


    /**
     * 初始化布局
     */
    private void initView(AttributeSet attrs) {
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
        token = SPUtils.get(mContext, "token", "").toString();
        mPresenter = new SearchEdittextPresenter();
        mPresenter.attachView(this);

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
                    onInsertListener.insertChange("", "", "",false);
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
                params.put(keyName,key);
                setUrl(url,params,keyName);

                editText.setHint(R.string.str_loading);
                editText.setSelection(key.length());

                if (!TextUtils.isEmpty(key)) {
                    iv_clear.setVisibility(VISIBLE);
                } else {
                    iv_clear.setVisibility(GONE);
                }
//
//                if (!TextUtils.isEmpty(beforeStr) && !TextUtils.isEmpty(key)) {
//                    if (beforeStr.equals(key)) {
//                        isNotSearch = true;
//                    } else {
//                        isNotSearch = false;
//                    }
//                } else if (TextUtils.isEmpty(beforeStr) && !TextUtils.isEmpty(key)) {
//                    isNotSearch = false;
//                }
//
//                if (onInsertListener != null && !isCallback) {
//                    onInsertListener.insertChange(s.toString(), getValueByKey(key), isNotSearch);
//                }

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

    public void setOnInsertListener(SearchEdittext.onInsertListener onInsertListener) {
        this.onInsertListener = onInsertListener;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void getListSuccess(Object bean, String url) {
        PageBean pageBean = GsonUtil.GsonToBean(bean, PageBean.class);
        List<ZyldBean> ldList = new ArrayList<>();
        List<HtxxBean> htList = new ArrayList<>();
        List<Select> selectList = new ArrayList<>();

        switch (url) {
            case ApiUrl.URL_SJ_INPUT_LD_LIST:
                ldList = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), ZyldBean.class);
                selectList = new ArrayList<>();
                rawList = ldList;
                for (int i = 0; i < ldList.size(); i++) {
                    selectList.add(new Select(ldList.get(i).number1, ldList.get(i).id));
                }
                break;
            case ApiUrl.URL_SJ_INPUT_HT_LIST:
                htList = GsonUtil.JsonToList(GsonUtil.GsonToString(pageBean.getRows()), HtxxBean.class);
                selectList = new ArrayList<>();
                rawList = htList;
                for (int i = 0; i < htList.size(); i++) {
                    selectList.add(new Select(htList.get(i).htbh, htList.get(i).id));
                }
                break;

        }

        if (selectList.size()!=0){
            setItemData(selectList);
        }

    }

    public interface onInsertListener {
        void insertChange(String name, String value,String selectStr, boolean isFromSelect);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    private final void setBaseAdapter(BaseAdapter adapter) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        popupWindow.setAdapter(adapter);
    }


    /**
     * 初始化PopupWindow
     */
    private void initPopupWindow() {
        popupWindow = new ListPopupWindow(mContext) {
            @Override
            public boolean isShowing() {
                return isPopupWindowShowing;
            }

            @Override
            public void show() {
                super.show();
                isPopupWindowShowing = true;
                mRightImageTopView.setClickable(true);
            }
        };


        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.bg_select);
            popupWindow.setBackgroundDrawable(drawable);
        } else {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.color.color_bg_cancel);
            popupWindow.setBackgroundDrawable(drawable);
        }

        popupWindow.setOnItemClickListener(this);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        popupWindow.setWidth(mLayout.getWidth());


        int maxHeight = 10;
        if (maxHeight > datas.size()) {
            maxHeight = datas.size() + 1;
        }

        popupWindow.setHeight(mLayout.getHeight() * maxHeight);
        popupWindow.setAnchorView(editText);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopupWindowShowing = false;
            }
        });
    }


    /**
     * 监听点击事件
     *
     * @param v
     */
    @Override
    public final void onClick(View v) {
        Log.e(TAG, "onClick: ");

        if (v.getId() == R.id.edit_spinner_expand) {

            if (adapter == null || popupWindow == null) {
                temList.add(new Select("暂无数据", ""));
                setItemData(temList);
            }

            Log.e(TAG, "点击事件");
            popupWindow.show();
        }
    }


    private String getValueByKey(String key) {
        String value = "";
        if (!TextUtils.isEmpty(key) && datas != null && datas.size() != 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (key.equals(datas.get(i).getName())) {
                    value = datas.get(i).getValue();
                }
            }
        }
        return value;
    }

    /**
     * 显示筛选数据
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        editText.setText(((BaseEditSpinnerAdapter) parent.getAdapter()).getItemString(position));
        isNotSearch = true;
        isCallback = true;
        if (onInsertListener != null) {
            onInsertListener.insertChange(datas.get(position).getName(), datas.get(position).getValue(),GsonUtil.GsonToString(rawList.get(position)), true);
        }

        if (!"暂无数据".equals(datas.get(position).getName())) {
            editText.setText(datas.get(position).getName());
            mRightImageTopView.setClickable(false);
        }
        popupWindow.dismiss();
    }


}
