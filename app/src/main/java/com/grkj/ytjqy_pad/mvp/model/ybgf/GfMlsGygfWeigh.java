package com.grkj.ytjqy_pad.mvp.model.ybgf;


import com.grkj.ytjqy_pad.mvp.model.BaseBean;


/**
 * 过磅称重
 * @author: ganghaip
 * @date: 2026/5/23 星期六
 */
public class GfMlsGygfWeigh extends BaseBean {

    /**
     * 主键
     */
    public String id;

    /**
     * 单位ID
     */
    public String dwid;

    /**
     * 批次号
     */
    public String serialNum;

    /**
     * 废物id
     */
    public String fwid;

    /**
     * 固废类别
     */
    public String fwdlbh;

    /**
     * 固废类别名称
     */
    public String fwdlmc;

    /**
     * 固废代码
     */
    public String fwxlbh;

    /**
     * 废物描述
     */
    public String fwxlmc;

    /**
     * 废品名称
     */
    public String fwxxmc;

    /**
     * 物料编码
     */
    public String itemCode;

    /**
     * 物料描述
     */
    public String itemDesc;

    /**
     * 车间id
     */
    public String cjid;

    /**
     * 车间名称
     */
    public String cjmc;

    /**
     * 地榜单(自动生成)
     */
    public String outNo;

    /**
     * 过磅类型
     */
    public String weighType;

    /**
     * 皮重（过磅重量）载具重量+容器重量
     */
    public String weighValue;

    /**
     * 净重量
     */
    public String netWeight;

    /**
     * 毛重
     */
    public String containerWeight;

    /**
     * 称重时间
     */
    public String carWeightTime;

//    /**
//     * 流水号
//     */
//    public String lsh;

    /**
     * 数据来源于 YTJ-一体机，SCZD-手持终端，PC-电脑端
     */
    public String dataFrom;

    /**
     * 计量单位
     */
    public String jldw;
}
