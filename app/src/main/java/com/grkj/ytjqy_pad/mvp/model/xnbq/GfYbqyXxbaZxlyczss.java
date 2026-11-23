package com.grkj.ytjqy_pad.mvp.model.xnbq;

import com.grkj.ytjqy_pad.mvp.model.BaseBean;


/**
 * 一般企业信息完善_自行利用处置设施（填埋、焚烧除外）
 * @author haigangp
 */
public class GfYbqyXxbaZxlyczss extends BaseBean {

	// 主键
	public String id;
	// 企业id
	public String dwid;
	// 设施编号
	public String ssbh;
	// 利用处置设施名称
	public String ssmc;
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
	public String upStringUserId;		//修改人ID

	public String createTime;			//创建时间
	public String zxlyczsstybh;		//自行利用处置设施统一编号（国家返回）
	public String sszt;		//设施状态（0：停用；1：启用）
	public String lyczlx;		//利用处置类型（jydwly-经营单位利用）
	/**
	 * 产废信息统一编号
	 */
	public String cfxxtybh;
	public String fwdlbh;	//废物类别
	public String fwdlmc;	//废物类别名称
	public String fwxlbh;	//废物代码
	public String fwxlmc;	//废物名称
	public String fwxxmc;	//废物详细名称

	/**
	 * 处置设施类型
	 */
	public String czsslx;
	public String entername;          //企业名称
	public String lxrname;            //联系人名称
	public String lxrphone;           //联系人手机号码

}
