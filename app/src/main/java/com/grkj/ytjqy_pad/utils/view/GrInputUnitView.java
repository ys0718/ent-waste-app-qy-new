package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;

import java.util.ArrayList;
import java.util.List;

/*
 * 可输入/选择下拉框
 * */
public class GrInputUnitView extends GrFormParentView {
    private LinearLayout mLayout;
    private AutoCompleteTextView actv;
    private ImageView iv_click;
    private View tem_click_view;
    private ImageView iv_clear;
    private String label = "";//显示内容

    //提示内容
    private String hint = HINT_TYPE_NONE;

    public static final String HINT_TYPE_NONE = "";
    public static final String HINT_TYPE_INSERT = "请输入";
    public static final String HINT_TYPE_INSERT_SELECT = "请输入/选择";
    public static final String HINT_TYPE_SELECT = "请选择";
    public static final String HINT_TYPE_SAVE = "保存后自动生成。";

    //是否可输入
    private boolean isEditable;
    //是否下拉选择
    private boolean selectable;
    //输入回调监听
    private OnTextChangedListener onTextChangedListener;
    //选择回调监听
    private OnItemChangedListener onItemChangedListener;

    //点击回调监听
    private OnclickListener onClickListener;

    //下拉数据列表
    private List<Select> datas = new ArrayList<>();
    //下拉名称列表
    private List<String> nameList = new ArrayList<>();
    //下拉值列表
    private List<String> valueList = new ArrayList<>();
    //已选中条目
    private Select selectItem;
    //下拉内容适配器
    private ArrayAdapter adapter;
    //是否隐藏
    private boolean isGone = false;


    public GrInputUnitView(Context context) {
        super(context);
        init(context, null);
    }

    public GrInputUnitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GrInputUnitView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        this.hint = array.getString(R.styleable.CommonAttrs_hint);
        this.isEditable = array.getBoolean(R.styleable.CommonAttrs_enabled, true);
        array.recycle();
        init(mContext);
    }


    /*
     * 初始化
     * */
    private void init(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gr_view_input, this, true);
        actv = view.findViewById(R.id.repeateMode);
        iv_clear = view.findViewById(R.id.iv_clear);
        iv_click = findViewById(R.id.iv_click);
        tem_click_view = findViewById(R.id.tem_click_view);
        mLayout = view.findViewById(R.id.mLayout);

        adapter = new ArrayAdapter<String>(mContext, R.layout.item_dropdown, nameList);
        actv.setAdapter(adapter);

//        监听布局长度/宽度
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = mLayout.getMeasuredWidth();
                //设置弹窗长度
                actv.setDropDownWidth(width);
            }
        });


        //设置下拉框横向偏移值
        actv.setDropDownHorizontalOffset(-20);
        //设置下拉框竖向偏移值
        actv.setDropDownVerticalOffset(10);
        //设置提示内容
        actv.setHint(this.hint);
        //设置是否可输入
        setContentBg(isEditable, selectable);

        actv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onclick();
                }
            }
        });

        //条目选择监听
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = datas.get(position);
                keyValue = selectItem.getValue();
                if (onItemChangedListener != null) {
                    onItemChangedListener.itemChange(selectItem);
                }
            }
        });

        //输入监听
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyValue = s.toString();
                //如果内容为空则隐藏清空按钮，否则则展示
                if (TextUtils.isEmpty(s.toString())) {
                    iv_clear.setVisibility(GONE);
                    tem_click_view.setClickable(true);
                } else {
                    if (isEditable) {
                        tem_click_view.setClickable(false);
                        iv_clear.setVisibility(VISIBLE);
//                        iv_clear.requestFocus();
                        iv_clear.setClickable(true);
                    }
                }

                //设置输入回调
                if (onTextChangedListener != null) {
                    onTextChangedListener.textChange(s.toString());
                }
            }
        });


        //清空
        iv_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actv.setText("");
            }
        });


//        //下拉点击
//        iv_click.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // 显示下拉框
//                actv.showDropDown();
//            }
//        });
        //下拉点击
        tem_click_view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                actv.requestFocus();
                if (onClickListener != null) {
                    onClickListener.onclick();
                }
                // 显示下拉框
                actv.showDropDown();
            }
        });
    }


    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, boolean required, List<Select> data, boolean isEditable, boolean selectable) {
        super.newInstance(itemName, keyName, keyValue, required);
        this.itemName = itemName;
        this.keyName = keyName;
        this.keyValue = keyValue;
        this.required = required;
        //可选择时，默认不可输入
        this.isEditable = isEditable;
        this.selectable = selectable;

        //如果label为空则显示Value,优先显示label
        actv.setText(TextUtils.isEmpty(this.label) ? this.keyValue : this.label, false);

        setRequired(required);
        setDatas(data);
        setContentBg(isEditable, selectable);
        setSelectable(selectable);

        return this;
    }


    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, boolean required, List<Select> data) {
        return this.newInstance(itemName, keyName, keyValue, required, data, false, true);
    }


    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, boolean required, boolean isEditable, boolean isSelectable) {
        return this.newInstance(itemName, keyName, keyValue, required, new ArrayList<>(), isEditable, isSelectable);
    }

    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, String label, boolean required, List<Select> data) {
        this.label = label;
        return this.newInstance(itemName, keyName, keyValue, required, data, false, true);
    }

    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue) {
        return this.newInstance(itemName, keyName, keyValue, false, new ArrayList<>(), false, false);
    }

    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, String label) {
        this.label = label;
        return this.newInstance(itemName, keyName, keyValue, false, new ArrayList<>(), false, false);
    }

    public GrInputUnitView newInstance(String itemName, String keyName, String keyValue, boolean isGone) {
        this.setGone(isGone);
        return this.newInstance(itemName, keyName, keyValue, false, new ArrayList<>(), false, false);
    }

    /*
     * 设置数据并组装名称列表、值列表
     * */
    public void setDatas(List<Select> data) {
        datas.clear();
        datas.addAll(data);

        if (datas == null || datas.size() == 0) {
            return;
        }

        selectable = true;
        nameList = new ArrayList<>();
        valueList = new ArrayList<>();

        //处理异步
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectable = true;
                isEditable = true;
                for (int i = 0; i < datas.size(); i++) {
                    nameList.add(datas.get(i).getName());
                    valueList.add(datas.get(i).getValue());
                }

                int maxHeight = 5;
                if (maxHeight > datas.size()) {
                    maxHeight = datas.size();
                }

                actv.setDropDownHeight(mLayout.getHeight() * maxHeight);


                adapter.clear();
                adapter.addAll(nameList);
                adapter.notifyDataSetChanged();
            }
        }, 1000);

    }

    public void setGone(boolean gone) {
        isGone = gone;
        this.setVisibility(isGone ? GONE : VISIBLE);
    }

    /*
     * 设置默认选中条目
     * */
    public void setItem(Select selectBean) {
        this.selectItem = selectBean;
        actv.setText(selectBean.getName(), false);
    }


    /*
     * 设置提示信息
     * */
    public void setHint(String hint) {
        this.hint = hint;
        actv.setHint(this.hint);

        if (hint.equals(HINT_TYPE_SAVE)) {
            actv.setHintTextColor(mContext.getResources().getColor(R.color.color_font_red));
        }
    }

    /**
     * 设置是否可编辑
     * */
    public void setEditable(boolean editable) {
        isEditable = editable;
        setContentBg(isEditable, selectable);
    }

    /*
     * 是否必填
     * */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /*
     * 是否可下拉
     * */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        iv_click.setVisibility(selectable ? VISIBLE : GONE);
        tem_click_view.setVisibility(selectable ? VISIBLE : GONE);
    }

    /*
     * 是否可输入
     * */
    public void setContentBg(boolean isEditable, boolean selectable) {
        this.isEditable = isEditable;
//        actv.setCursorVisible(isEditable);
        setSelectable(selectable);
        actv.setFocusableInTouchMode(isEditable);


        //根据横/竖屏进行不同展示及操作
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            actv.setDropDownBackgroundResource(R.mipmap.bg_edittext_disable);
            mLayout.setBackgroundResource(isEditable || selectable ? R.mipmap.bg_edittext : R.mipmap.bg_edittext_disable);
        } else {
            actv.setTextColor(mContext.getResources().getColor(R.color.colorFontB));
            actv.setDropDownBackgroundResource(R.drawable.bg_edittext);
            mLayout.setBackgroundResource(isEditable || selectable ? R.drawable.bg_edittext : R.drawable.bg_edittext_disable);
        }

    }

    //输入类型
    public void setInputType(int inputType) {
        this.actv.setInputType(inputType);
    }

    public void setText(String text) {
        actv.setText(text);
    }

    public AutoCompleteTextView getTextView() {
        this.actv.setEnabled(true);
        this.actv.setClickable(true);
        return this.actv;
    }

    /*
     * 输入监听回调
     * */
    public interface OnTextChangedListener {
        void textChange(String changeText);
    }

    /*
     * 选择监听回调
     * */
    public interface OnItemChangedListener {
        void itemChange(Select changeText);

    }

    /*
     * 选择监听回调
     * */
    public interface OnclickListener {
        void onclick();

    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }


    public void setOnItemChangedListener(OnItemChangedListener onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}