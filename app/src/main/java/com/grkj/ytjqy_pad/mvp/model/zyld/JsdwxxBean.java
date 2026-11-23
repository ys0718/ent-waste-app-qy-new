package com.grkj.ytjqy_pad.mvp.model.zyld;

import com.grkj.ytjqy_pad.mvp.model.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 转移联单-接收单位信息
 * @author: yusam
 * @date: 2026/9/7 星期一
 */
public class JsdwxxBean extends BaseBean {

    public String xkzbh;

    public boolean isLastSelected;

    public String jydwmc;
    public String lxr;
    public String lxrsj;
    public List<Wtlysz> wtlysz = new ArrayList<>();
    public String jyqyid;


    public static class Wtlysz {
        public String lyczfs;//D1
        public String fwmc;//废离子交换树脂
        public double yjzyl;//10
        public String cfxxtybh;//44530280000000000451CF0001
        public String fwdm;//302-001-36
    }

}
