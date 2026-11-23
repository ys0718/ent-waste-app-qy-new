package com.grkj.ytjqy_pad.mvp.model.xnbq;

import com.grkj.ytjqy_pad.mvp.model.BaseBean;


/**
 * 虚拟标签_自行利用处置
 * 立产立清-立产立处置申报、出库自行利用处置 是用同一张表
 * 使用type来区分，private String type;// 1 出库  2 立产立清-立产立处置
 */
public class GfYbqyTzCkZxlyczXnbq extends BaseBean {

    public String id;      //主键
    public String sslx;    //利用处置设施类型
    public String lyczfs;  //利用处置方式
    public String lyczfsxl;//利用处置方式小类
    public String dwid;    //单位id
    public String ckjbr;//出库部门经办人
    public String ysjbr;//运送部分经办人
    public String zxlyczpcbm;//自行利用处置批次编码
    public String zxlyczsj;//自行利用/处置完毕时间（年月日时分秒），必须大于出库时间
    public String zxlyczjbr;//自行利用/处置部门经办人
    public String sjlybs;//数据来源标识（枚举：0-省级系统创建产生，地市6位代码-地市对接产生，企业dwid-企业直连对接产生）
    public String sfyjznhtz;//是否硬件智能化台账（0-手工台账，1-智能化台账）
    public String ssbh;    //设施编号
    public String ssmc;    //设施名称
    public String sjzcnl;    //设计贮存能力
    public String lyczlx;      //利用处置类型（1-立产立清利用处置；2-出库利用处置）
    public String fwdlbh;      //废物大类编号
    public String fwdlmc;      //废物大类名称
    public String fwxlbh;      //废物小类编号
    public String fwxlmc;      //废物小类名称
    public String fwxxmc;      //废物详细名称
    public String wlxz;        //物理性状
    public String fwtx;        //废物特性
    public String wxcf;        //危害成分
    public String zysx;        //注意事项
    public String zycf;         //主要成分
    public String jldw;        //计量单位
    public String szsbm;        //数字识别码
    public String rqbzlx;  //容器/包装类型（0：槽罐；1：装袋；2：装桶；9：其他）
    public String rqbzcz;  //容器/包装材质（金属-1；塑料-2；复合材料-3；其他材质-9）
    public String rqbzgg;  //容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）
    public String csid;        //产生id
    public String rkid;        //入库id
    public String ckid;        //出库id
    public String cspch;        //产生批次号
    public String rkpch;        //入库批次号
    public String ckpch;        //出库批次号
    public String ckl;  //出库量

    //新加
    public String zxlyczl; //自行利用预估量
    public String czl;  //实际处置量(国家接口返回)
    public String zxlycztzbm; //自行利用处置台账编码(国家接口返回)
    public String zxlyczsstybh;//自行利用处置设施统一编号
    public String cfxxtybh;//产废信息统一编号
    public String type;// 1 出库  2 立产立清-立产立处置
    public String csXnbqId; // 关联立产立清申报 GF_YBQY_TZ_NEW_CS_XNBQ.ID（仅 type=2 时使用）


    public String sbzt;//上报状态
    public String csSzsbm;//父级产生台账的数字识别码（用于前端判断产生申报是否已完成）


}
