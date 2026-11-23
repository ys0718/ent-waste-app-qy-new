package com.grkj.ytjqy_pad.mvp.model.zyld;
import com.grkj.ytjqy_pad.mvp.model.BaseBean;

import java.math.BigDecimal;

/**
 * 转移联单批次号
 * @author: yusam
 * @date: 2026/9/7
 */
public class GfWxfwZyldFwxxPch extends BaseBean {

    /**
     * 主键
     */
    //(value = "主键")
    public String id;

    /**
     * 联单主键
     */
    //(value = "联单主键")
    public String mainid;

    /**
     * 废物信息主键
     */
    //(value = "废物信息主键")
    public String childid;

    /**
     * 台账批次号
     */
    //(value = "台账批次号")
    public String tzpch;


    /**
     * 台账id
     */
    //(value = "台账id")
    public String tzid;

    /**
     * 数字识别码
     */
    //(value = "数字识别码")
    public String szsbm;

    /**
     * 数据来源（1-产生台账；2-出库台账）
     */
    //(value = "数据来源（1-产生台账；2-出库台账）")
    public String dataSource;

    //(value = "二维码url")
    public String qrcodeUrl;

    //(value = "转移量")
    public String zysl;

    //(value = "计量单位")
    public String jldw;

    //(value = "接受数量")
    public String jssl;

    //(value = "签收标识")
    public String sfczzdcy;

    //(value = "签收标识")
    public String qsbs;
}
