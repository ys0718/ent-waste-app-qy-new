package com.grkj.ytjqy_pad.mvp.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.ApiUrl;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.mvp.model.CommonProductInfoBean;
import com.grkj.ytjqy_pad.utils.common.GsonUtil;
import com.grkj.ytjqy_pad.utils.common.ToastUtil;
import com.grkj.ytjqy_pad.utils.popwindow.model.Select;
import com.grkj.ytjqy_pad.utils.view.AddNumberView;
import com.grkj.ytjqy_pad.utils.view.GrAddNumberView;
import com.grkj.ytjqy_pad.utils.view.GrFormParentView;
import com.grkj.ytjqy_pad.utils.view.GrInputView;
import com.grkj.ytjqy_pad.utils.view.InputSelectView;
import com.socks.library.KLog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yusam on 2022/3/20.
 * 地磅测试页
 */
public class ViewTestActivity extends AppCompatActivity {

    private List<Select> selectList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);

        GrInputView giv_1 = findViewById(R.id.giv_1);
        GrInputView giv_2 = findViewById(R.id.giv_2);


        LinearLayout ll_left = findViewById(R.id.ll_left);
        LinearLayout ll_right = findViewById(R.id.ll_right);


        InputSelectView isv_fwxxmc = findViewById(R.id.isv_fwxxmc);

        List<Select> tempList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tempList.add(new Select("名称" + i, "" + i));
        }

        isv_fwxxmc.setDatas(tempList);
//        isv_fwxxmc.setHint("废物详细名称");
        isv_fwxxmc.setOnTextChangedListener(new InputSelectView.OnTextChangedListener() {
            @Override
            public void textChange(String changeText) {

            }
        });

        isv_fwxxmc.setOnItemChangedListener(new InputSelectView.OnItemChangedListener() {
            @Override
            public void itemChange(Select changeText) {

            }
        });


        for (int i = 0; i < 10; i++) {
            selectList.add(new Select("名称" + i, "" + i));
        }

//        giv_1.setDatas(selectList);
//        giv_1.setItemName("开始时间1111111111111111111");
//        giv_2.setItemName("结束时间");


        List<GrFormParentView> viewList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            GrInputView test1 = new GrInputView(this);
            GrInputView test2 = new GrInputView(this);
            GrAddNumberView test3 = new GrAddNumberView(this);
//            test1.newInstance("左条目" + i, "left_key" + i, "right_name" + i, new ArrayList<>());
            test1.newInstance("左条目" + i, "left_key" + i, "", true, selectList);
            test2.newInstance("右条目" + i, "right_key" + i, "", false, false,false);
            test3.newInstance("数量" + i, "right_key" + i, "", false);
//            ll_left.addView(test1);
            viewList.add(test1);
            viewList.add(test2);
            viewList.add(test3);
        }

        Configuration mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        for (int i = 0; i < viewList.size(); i++) {

            if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                if (i % 2 == 0) {
                    ll_left.addView(viewList.get(i));
                } else {
                    ll_right.addView(viewList.get(i));
                }
            } else {
                ll_left.addView(viewList.get(i));
            }
        }


//        for (int i = 0; i < 10; i++) {
//            GrInputView test1 = new GrInputView(this);
//            test1.newInstance("右条目" + i, "right_key" + i, "2", false, true);
//            ll_right.addView(test1);
//            viewList.add(test1);
//        }

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                int requireNum = 0;
                int passNum = 0;
                for (int i = 0; i < viewList.size(); i++) {
                    if (viewList.get(i).isRequired()) {
                        requireNum++;
                        if (viewList.get(i).checkRequired()) {
                            passNum++;
                        }
                    }

                    params.putAll(viewList.get(i).getModelMap());
                }

                KLog.e(requireNum);
                KLog.e(passNum);
                if (requireNum != passNum) {
                    ToastUtil.showToast(ViewTestActivity.this, "请检查参数！");
                }

                KLog.e(params);
//                Map<String, String> map1 = giv_1.checkRequired();
//
//                Map<String, String> map2 = giv_2.checkRequired();
//
//                map2.putAll(map1);
//
//                KLog.e(map2);
            }
        });

    }


}