package com.grkj.ytjqy_pad.utils.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/*
 * 可输入/选择下拉框
 * */
public class InputSelectView extends LinearLayout{
    private Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    private int ori = mConfiguration.orientation; //获取屏幕方向
    private LinearLayout mLayout;
    private String hint = "请输入/选择";

    public InputSelectView(Context context){
        super(context);
        init(context,null);
    }

    public InputSelectView(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public InputSelectView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }


    private List<Select> datas = new ArrayList<>();
    private AutoCompleteTextView text;
    private boolean enabled;

    public List<Select> getDatas(){
        return datas;
    }

    private List<String> nameList = new ArrayList<>();
    private List<String> valueList = new ArrayList<>();
    private Select selectItem;

    public void setDatas(List<Select> data){
        datas.clear();
        datas.addAll(data);
        nameList = new ArrayList<>();
        valueList = new ArrayList<>();
        if (datas != null && datas.size() != 0) {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){

                    for (int i = 0; i < datas.size(); i++) {
                        nameList.add(datas.get(i).getName());
                        valueList.add(datas.get(i).getValue());
                    }

                    int maxHeight = 10;
                    if (maxHeight > datas.size()) {
                        maxHeight = datas.size();
                    }

                    text.setDropDownHeight(mLayout.getHeight() * maxHeight);

                    adapter.clear();
                    adapter.addAll(nameList);
                    adapter.notifyDataSetChanged();
                }
            },500);

            text.dismissDropDown();

        }

    }

    public void setItem(Select selectBean){
        this.selectItem = selectBean;
        text.setText(selectBean.getName(),false);
    }

    public Select getSelectItem(){
        return selectItem;
    }

    public void setHint(String hint){
        this.hint = hint;
        text.setHint(this.hint);
    }

    //是否可输入
    public void setEnabled(boolean isEnabled){
        this.enabled = isEnabled;
        text.setCursorVisible(isEnabled);
    }

    private ArrayAdapter adapter;

    private void init(Context mContext,@NonNull AttributeSet attrs){
        if (attrs == null) {
            init(mContext);
            return;
        }
        TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.CommonAttrs);
        this.hint = array.getString(R.styleable.CommonAttrs_hint);
        this.enabled = array.getBoolean(R.styleable.CommonAttrs_enabled,true);
        array.recycle();
        init(mContext);
    }


    //初始化
    private void init(Context mContext){
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_input_select,this,true);
        adapter = new ArrayAdapter<String>(mContext,R.layout.item_dropdown,nameList);
        text = (AutoCompleteTextView) view.findViewById(R.id.repeateMode);
        View tem_click_view = findViewById(R.id.tem_click_view);
        mLayout = view.findViewById(R.id.mLayout);
        text.setAdapter(adapter);

        //监听布局长度/宽度
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                int width = view.getMeasuredWidth();
                //设置弹窗长度
                text.setDropDownWidth(width);
            }
        });

        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
            text.setDropDownBackgroundResource(R.mipmap.bg_select);
        } else {
            text.setTextColor(mContext.getResources().getColor(R.color.colorFontB));
            text.setDropDownBackgroundResource(R.drawable.bg_edittext);
        }


        text.setDropDownHorizontalOffset(-20);
        text.setDropDownVerticalOffset(10);
        text.setHint(this.hint);
        text.setCursorVisible(enabled);
        ImageView iv_clear = view.findViewById(R.id.iv_clear);
        text.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                selectItem = datas.get(position);
                if (onItemChangedListener != null) {
                    onItemChangedListener.itemChange(selectItem);
                }
            }
        });

        text.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){

            }

            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count){

            }

            @Override
            public void afterTextChanged(Editable s){

                if (TextUtils.isEmpty(s.toString())) {
                    iv_clear.setVisibility(GONE);
                    tem_click_view.setClickable(true);
                } else {
                    if (enabled) {
                        tem_click_view.setClickable(false);
                        iv_clear.setVisibility(VISIBLE);
                        iv_clear.requestFocus();
                        iv_clear.setClickable(true);
                    }
                }

                if (onTextChangedListener != null) {
                    onTextChangedListener.textChange(s.toString());
                }
            }
        });


        //清空
        iv_clear.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                text.setText("");
            }
        });


        tem_click_view.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v){
                KLog.e("0000"+nameList.size());
                // 显示下拉框
                text.showDropDown();
            }
        });
    }


    public interface OnTextChangedListener{
        void textChange(String changeText);
    }

    public interface OnItemChangedListener{
        void itemChange(Select changeText);
    }

    private OnTextChangedListener onTextChangedListener;

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
        this.onTextChangedListener = onTextChangedListener;
    }

    private OnItemChangedListener onItemChangedListener;

    public void setOnItemChangedListener(OnItemChangedListener onItemChangedListener){
        this.onItemChangedListener = onItemChangedListener;
    }
}