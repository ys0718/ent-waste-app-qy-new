package com.grkj.ytjqy_pad.mvp.model.xnbq;

import com.grkj.ytjqy_pad.mvp.model.BaseBean;


/**
 * 危废产生台账_虚拟标签（立产立清）
 */
public class GfYbqyTzNewCsXnbq extends BaseBean{
    public String id;          //主键
    public String cspch;       //收集台账编号
    public String dwid;        //单位ID
    public String cssj;        //产生时间
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
    public String csl;     //产生量
    public String jldw;        //计量单位
    public String csssbm;      //生产设施编码
    public String scssmc;      //生产设施名称
    public String sstybh;      //统一生产设施编号
    public String csbmjbr;     //产生部门经办人
    public String fwzt;        //废物状态 1-未出库
    public String qx;          //去向 2-立产立清转移；3-立产立清自处置
    public String xnbqlx;      //虚拟标签类型 1-罐车运输入库；2-管道运输；3-车辆运输
    public String mdhll;       //密度和流量（类型2时必填）
    public String clms;        //车辆描述（类型3时必填）
    public String ewmwb;       //二维码文本（对接国家生成）
    public String cfxxtybh;    //产废信息统一编号
    public String rqbzlx;      //容器/包装类型（0-槽罐；1-装袋；2-装桶；9-其他）
    public String rqbzcz;      //容器/包装材质（1-金属；2-塑料；3-复合材料；9-其他）
    public String rqbzgg;      //容器/包装规格（1-≤25L；2-50L；3-200L；4-1000L；9-其它）
    public String sjlybs;      //数据来源标识（0-省级系统创建产生）
    public String sfyjznhtz;   //是否硬件智能化台账（0-手工台账）

    //国家接口返回字段
    public String wxfwbqewm;//危险废物标签二维码
    public String cstzbm;//产生台账编码

    //去向为3-立产立清自处置时关联出库自行利用处置GF_YBQY_TZ_CK_ZXLYCZ_XNBQ.id
    public String ckZxlyczXnbqId;
}
