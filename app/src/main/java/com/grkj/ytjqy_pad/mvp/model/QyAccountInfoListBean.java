package com.grkj.ytjqy_pad.mvp.model;

/**
 * Created by yusam on 2022/3/21.
 * 台账信息
 */

public class QyAccountInfoListBean {
    public boolean isToday;//是否为当天
    public String sj;//台账日期
    public String rkl;//入库量
    public String zcl;//贮存量
    public String ckl;//出库量
    public String csl;//产生量
    public String fwdlmc;//废物大类名称
    public String fwxxmc;//废物详细名称
    public String fwdlbh;//废物大类编号
    public String fwxlbh;//废物小类编号
    /**
     * 以下为筛选条件
     * */
    public String startTime;//开始时间
    public String endTime;//结束时间
    public String day;//all为全部  空为当天
}

