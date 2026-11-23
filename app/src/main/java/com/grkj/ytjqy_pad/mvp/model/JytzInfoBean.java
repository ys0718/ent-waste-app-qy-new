package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2023/2/15.
 * 产废/入库/出库/废物信息公用页面
 */
public class JytzInfoBean extends BaseBean {

    public List<WasteItem> fwsz;      // 废物收贮列表
    public String rksj;               // 入库时间
    public String ysjbr;              // 运输经办人
    public String zcjbr;              // 贮存经办人
    public String zcssbm;             // 贮存设施编号
    public String zcssid;             // 贮存设施ID
    public String zcsslx;             // 贮存设施类型
    public String zyldId;             // 联单ID
    public String zyldNumber1;        // 联单编号
    public String dataFrom;           // 数据来源
    public String rkpch;              // 入库批次号
    public String id;                 // 主键
    public String csdwmc;                 // 产生单位名称
    public String jldw;                 // 计量单位

    public static class WasteItem {
        public String fwdm;    // 废物代码
        public String fwmc;    // 废物名称
        public String jldw;    // 计量单位
        public String jsl;     // 接收量
        public String szsbm;   // 数字识别码
    }


}
