package com.grkj.ytjqy_pad.mvp.model;

import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;

/**
 * Created by yusam on 2022/3/21.
 * 校准信息
 */

public class CheckInfoBean extends SelectPopupWindowBean {
    /**
     * 主键
     */
    public String id;
    /**
     * 主表ID
     */
    public String mainid;
    /**
     * 废物大类编号
     */
    public String fwdlbh;
    /**
     * 废物小类编号
     */
    public String fwxlbh;
    /**
     * 废物大类名称
     */
    public String fwdlmc;
    /**
     * 废物小类名称
     */
    public String fwxlmc;
    /**
     * 废物详细名称
     */
    public String fwxxmc;
    /**
     * 产生量
     */
    public String csl;
    /**
     * 入库量
     */
    public String rkl;

    /**
     * 出库量
     */
    public String ckl;


    /**
     * 校正时间
     */
    public String jzsj;
    /**
     * 校正人员ID
     */
    public String jzryId;
    /**
     * 校正人员
     */
    public String jzry;
    /**
     * 情况说明
     */
    public String qksm;

    /**
     * 产生量计量单位
     */
    public String cslJldw;
    /**
     * 入库量计量单位
     */
    public String rklJldw;
    /**
     * 出库量计量单位
     */
    public String cklJldw;
    /**
     * 计量单位
     */
    public String jldw;


}
