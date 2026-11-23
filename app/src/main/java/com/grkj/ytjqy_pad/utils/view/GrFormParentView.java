package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;

import java.util.HashMap;
import java.util.Map;

/*
 * 可输入/选择下拉框
 * */
public class GrFormParentView extends LinearLayout {
    protected TextView tv_item;
    private ImageView iv_warming;
    protected Context mContext;

    //条目名称
    protected String itemName;
    //键名
    protected String keyName;
    //键值
    protected String keyValue;

    //是否必填
    protected boolean required;


    public String getItemName() {
        return itemName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public boolean isRequired() {
        return required;
    }

    //获取设置的配置信息
    protected Configuration mConfiguration = this.getResources().getConfiguration();
    //获取屏幕方向
    protected int ori = mConfiguration.orientation;


    public GrFormParentView(Context context) {
        super(context);
        init(context, null);
    }

    public GrFormParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GrFormParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /*
     * 初始化
     * */
    protected void init(Context mContext, AttributeSet attrs) {
        this.mContext = mContext;
        if (attrs == null) {
            init(mContext);
            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.CommonAttrs);
        this.keyName = array.getString(R.styleable.CommonAttrs_key);
        this.keyValue = array.getString(R.styleable.CommonAttrs_value);
        this.itemName = array.getString(R.styleable.CommonAttrs_itemName);
        this.required = array.getBoolean(R.styleable.CommonAttrs_required, false);
        array.recycle();
        init(mContext);
    }


    /*
     * 初始化
     * */
    private void init(Context mContext) {
        this.mContext = mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.gr_view_item, this, true);
        tv_item = view.findViewById(R.id.tv_item);
        iv_warming = view.findViewById(R.id.iv_warming);
        //设置是否必选
        setRequired(required);

        Spanned requireItemName = Html.fromHtml("<font color=\"#e51e5f\">" + "* " + "</font>" + itemName);
        tv_item.setText(isRequired() ? requireItemName : this.itemName);

    }

    protected GrFormParentView newInstance(String itemName, String keyName, String keyValue, boolean required) {
        this.itemName = itemName;
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.required = required;
        init(mContext,null);
        return this;
    }


    /*
     * 必填检查
     * */
    public boolean checkRequired() {
        boolean pass = false;
        if (TextUtils.isEmpty(keyValue)) {
            iv_warming.setVisibility(VISIBLE);
            pass = false;
        } else {
            iv_warming.setVisibility(ori == mConfiguration.ORIENTATION_LANDSCAPE ?INVISIBLE: GONE);
            pass = true;
        }
        return pass;
    }

    //获取绑定的map
    public Map<String, String> getModelMap() {
        Map<String, String> map = new HashMap<>();

        if (TextUtils.isEmpty(keyName)) {
            return null;
        }
        map.put(keyName, keyValue);
        return map;
    }


    /*
     * 设置条目名称
     * */
    public void setItemName(String itemName) {
        this.itemName = itemName;
        tv_item.setText(itemName);
    }

    /*
     * 是否必填
     * */
    public void setRequired(boolean required) {
        this.required = required;
    }


}