package com.grkj.ytjqy_pad.mvp.view.activity.hn;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by  on 2023/11/2
 * Description: 地区实体
 **/
public class AreaBean implements IPickerViewData {
    public String id;
    public String code;
    public String pcode;
    public String name;
    public String lev;
    public String islast;
    public List<AreaBean> child = new ArrayList<>();

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
