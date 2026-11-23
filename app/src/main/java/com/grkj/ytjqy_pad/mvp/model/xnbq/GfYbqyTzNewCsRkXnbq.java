package com.grkj.ytjqy_pad.mvp.model.xnbq;

import com.grkj.ytjqy_pad.mvp.model.BaseBean;


/**
 * 危废产生入库台账_虚拟标签（散装/槽罐）
 */
public class GfYbqyTzNewCsRkXnbq extends BaseBean {
    public String id;          //主键
    public String rkpch;       //收集台账编号（入库台账编号）
    public String dwid;        //单位ID
    public String rksj;          //入库时间
    public String djsj;          //对接时间
    public String djbs;        //对接标识（1-成功，其他未对接或对接失败）
    public String sfdj;        //首次对接标识
    public String zxbs;        //注销标识
    public String zxsj;          //注销时间
    public String fwdlbh;      //废物大类编号
    public String fwdlmc;      //废物大类名称
    public String fwxlbh;      //废物小类编号
    public String fwxlmc;      //废物小类名称
    public String fwxxmc;      //废物详细名称
    public String fwtx;        //废物特性
    public String wlxz;        //废物形态
    public String zycf;        //主要成分
    public String wxcf;        //危险成分
    public String zysx;        //注意事项
    public String szsbm;       //数字识别码
    public String rkl;     //入库量
    public String jldw;        //计量单位
    public String csssbm;      //生产设施编码
    public String scssmc;      //生产设施名称
    public String sstybh;      //统一生产设施编号
    public String zcsstybh;    //贮存设施统一编号
    public String zcssbm;      //贮存设施编码
    public String zcsslx;      //贮存设施类型
    public String csbmjbr;     //产生部门经办人
    public String zcjbr;       //贮存部门经办人
    public String ysjbr;       //运输部门经办人
    public String fwzt;        //废物状态 1-未出库
    public String xnbqlx;      //虚拟标签类型 1-罐车运输入库；2-管道运输；3-车辆运输
    public String mdhll;       //密度和流量（类型2时必填）
    public String clms;        //车辆描述（类型3时必填）
    public String ewmwb;       //二维码文本（对接国家生成）
    public String sbfhbs;      //返回标识（对接国家返回）
    public String cfxxtybh;    //产废信息统一编号
    public String lyczfs;      //利用处置方式大类
    public String lyczfsxl;    //利用处置方式小类
    public String fwly;        //废物来源
    public String sjlybs;      //数据来源标识
    public String sfyjznhtz;   //是否硬件智能化台账
    public String rqbzlx;      //容器/包装类型
    public String rqbzcz;      //容器/包装材质
    public String rqbzgg;      //容器/包装规格
    public String csid;        //关联产生ID

    public String tzyy;        //调整原因（非持久化字段）

}
