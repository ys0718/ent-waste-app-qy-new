package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.grkj.ytjqy_pad.R;
import com.grkj.ytjqy_pad.gather.Constants;
import com.grkj.ytjqy_pad.utils.common.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by  on 2023/10/31
 * Description: 湖南环境相关工具类
 **/
public class HnUtils {



    //三级联动组件
    private static OptionsPickerView locationOptions;

    private static TextView tv_selected;

    //显示三级联动选择器
    public static void showPickerDialog(Context mContext, List<AreaBean> dataList, OnOptionsSelectListener onOptionsSelectListener) {// 弹出选择器
//        initJsonData();

        if (locationOptions == null) {
            locationOptions = new OptionsPickerBuilder(mContext, onOptionsSelectListener)
                    .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                        @Override
                        public void customLayout(View v) {
                            final LinearLayout tvSubmit = v.findViewById(R.id.ll_finish);
                            tv_selected = v.findViewById(R.id.tvTitle);
                            tvSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    locationOptions.returnData();
                                    locationOptions.dismiss();
                                }
                            });
                        }
                    })
                    .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                        @Override
                        public void onOptionsSelectChanged(int options1, int options2, int options3) {
                            tv_selected.setText(getResultName(dataList, options1, options2, options3));
                        }
                    })
//                .setLineSpacingMultiplier(2.4f)//设置距离
//                .setItemVisibleCount(3)//设置显示的数据数量
                    .isDialog(false)
                    .setBackgroundId(R.mipmap.bg_login)
                    .setTitleSize(24)
                    .setSubCalSize(24)
                    .setContentTextSize(24)//设置标题字号
                    .setTitleText("请选择")
                    .isRestoreItem(false)//是否每次从头开始显示数据
//                .isAlphaGradient(true)
                    .build();
            initJsonData(dataList);


//            if (itemList3.size() != 0) {
//                locationOptions.setPicker(itemList1, itemList2, itemList3);
//            } else {
//                locationOptions.setPicker(itemList1, itemList2);
//            }
            if (options3Items.size() != 0) {
                locationOptions.setPicker(options1Items, options2Items, options3Items);
            } else {
                locationOptions.setPicker(options1Items, options2Items);
            }

            for (int i = 0; i < dataList.size(); i++) {
                if (TextUtils.equals(dataList.get(i).name,"湖南省")){
                    locationOptions.setSelectOptions(i,0);
                }
            }
        }





        locationOptions.show();
    }


    public static void closeDialog() {
        if (locationOptions != null) {
            locationOptions.dismiss();
            locationOptions = null;
        }
    }


    private static List<AreaBean> options1Items = new ArrayList<>(); //省
    private static List<List<String>> options2Items = new ArrayList<>();//市
    private static List<List<List<String>>> options3Items = new ArrayList<>();//区

    //初始化地区数据
    private static void initJsonData(List<AreaBean> rawDataJsonList) {//解析数据


        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = rawDataJsonList;

        for (int i = 0; i < rawDataJsonList.size(); i++) {//遍历省份
            List<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            List<List<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < rawDataJsonList.get(i).child.size(); c++) {//遍历该省份的所有城市
                String CityName = rawDataJsonList.get(i).child.get(c).name;
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //rawDataJsonList，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (rawDataJsonList.get(i).child.get(c).child == null
                        || rawDataJsonList.get(i).child.get(c).child.size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int j = 0; j < rawDataJsonList.get(i).child.get(c).child.size(); j++) {
                        City_AreaList.add(rawDataJsonList.get(i).child.get(c).child.get(j).name);
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            options2Items.add(CityList);
            //添加地区数据
            options3Items.add(Province_AreaList);
        }
    }


//    //初始化地区数据
//    private static void initJsonData(List<AreaBean> rawDataJsonList) {//解析数据
//        /**
//         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
//         * 关键逻辑在于循环体
//         *
//         * */
//        dataJsonList = new ArrayList<>();
//        dataJsonList.addAll(rawDataJsonList);
//        itemList1 = new ArrayList<>();
//        itemList2 = new ArrayList<>();
//        itemList3 = new ArrayList<>();
//
//        //数据处理
//        for (int i = 0; i < dataJsonList.size(); i++) {//遍历第一级
//            itemList1.add(dataJsonList.get(i).name);
//            List<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
//            List<List<String>> province_AreaList2 = new ArrayList<List<String>>();//该省的所有地区列表（第三极）
//            AreaBean bean = dataJsonList.get(i);
//            if (bean.child != null && bean.child.size() != 0) {
//                for (int k = 0; k < bean.child.size(); k++) {//遍历该省份的所有城市
//                    AreaBean childBean = bean.child.get(k);
//                    String cityName = childBean.name;
//                    cityList.add(cityName);//添加城市
//
//                    List<String> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
//
//                    if (childBean.child != null && childBean.child.size() != 0) {
//                        for (int j = 0; j < childBean.child.size(); j++) {
//                            String provinceName = childBean.child.get(j).name;
//                            province_AreaList.add(provinceName);//添加区县
//                            province_AreaList2.add(province_AreaList);
//                        }
//                    } else {
//                        province_AreaList.add("");
//                        province_AreaList2.add(province_AreaList);
//                    }
//                }
//            } else {
//                cityList.add("");
//                province_AreaList2.add(new ArrayList<>());
//            }
//            /**
//             * 添加城市数据
//             */
//            itemList2.add(cityList);
//            itemList3.add(province_AreaList2);
//
//        }
//
//    }


    //选择回调名字封装
    public static String getResultName(List<AreaBean> rawDataJsonList, int options1, int options2, int options3) {
        List<AreaBean> childList = new ArrayList<>();
        List<AreaBean> sonList = new ArrayList<>();
        String result = rawDataJsonList.get(options1).name;
        // 处理选择结果
        childList = rawDataJsonList.get(options1).child;

        if (childList != null && childList.size() != 0) {
            result += "/" + childList.get(options2).name;
            sonList = childList.get(options2).child;
        }

        if (sonList != null && sonList.size() != 0) {
            result += "/" + sonList.get(options3).name;
        }
        return result;
    }


}
