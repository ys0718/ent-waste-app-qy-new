package com.grkj.ytjqy_pad.mvp.model;

/*
 * 自行利用设施
 * */
public class ZxlyssBean {

    // 主键
    public String id;
    // 企业id
    public String dwid;
    // 设施编号
    public String ssbh;
    // 利用处置设施名称
    public String ssmc;
    //设施类型
    public String czsslx;
    // 利用处置方式大类
    public String lyczfsdl;
    // 设计年利用处置能力
    public String ncznl;
    // 利用处置方式小类
    public String lyczfsxl;
    // 修改时间
    public String modifyrq;
    // 是否产生产品
    public String isCscp;
    // 是否产生次生废物
    public String isCsfw;
    // 计量单位
    public String jldw;
    public String qylx;		//企业类型
    public String qybaid;		//企业信息完善id
    public String isSheng;		 //迁移状态(gdgf-旧固废,gfhjjg-新固废)
    public String sftghp;		//是否通过环评
    public String dwlx;		//单位类型，csdw-一般危废，jsdw-接收单位,yfdw-医废处置单位

    public String createUserId;		//创建人ID
    public String updateUserId;		//修改人ID
}
