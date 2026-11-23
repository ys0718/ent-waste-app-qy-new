package com.grkj.ytjqy_pad.mvp.model;


/**
 * Create by seny on 2023/12/11
 * Description: 产生环节实体
 **/
public class CshjBean {


    public String id;		//主键
    public String dwid;	//企业id
    public String zywrcf;	//主要污染成分
    public String fwtx;	//废物特性
    public String wlxz;	//物理性状、形态
    public String bzfs;	//包装方式
    public String clfs;	//处理方式及去向
    public String fwdlbh;	//废物类别
    public String fwdlmc;	//废物类别名称
    public String fwxlbh;	//废物代码
    public String fwxlmc;	//废物名称
    public String fwxxmc;	//废物详细名称
    public String no;		//编号
    public String csy;		//废物来源类别
    public String modifyrq;	//修改时间
    public String remark;		//备注
    
    public String sndsjcsl;   //上年产生量
    
    public String csljldw;		// 产生量计量单位
    public String isSheng;		 //迁移状态(gdgf-旧固废,gfhjjg-新固废)
    public String dwlx;		//单位类型，csdw-一般危废，jsdw-接收单位,yfdw-医废处置单位
    
    public String fwdldm;		// 添加月报废物用（废物类别）
    
    public String fwxldm;		// 添加月报废物用（废物代码）
    public String mlnf;		// 危废名录年份

    public String createUserId;		//创建人ID
    public String updateUserId;		//修改人ID
    public String createTime;			//创建时间
    public String cshj;               //产生环节

    public String wxcf;    //危害成分
    public String zysx;    //注意事项

    public String rqbzbm;      //容器包装编码
    public String rqbzlx;      //容器/包装类型
    public String rqbzcz;      //容器包装材质
    public String rqbzgg;  //容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）

    public String scssmc;   //生产设施名称

    public String scssbh;   //生产设施编号

    /*
     * 生产设施ID
     * */
    public String scssid;

    /**
     *
     */
    public String hftx; //挥发特性 1-挥发性 0-不挥发

    public String ssfc; //所属分厂
    public String kcsx; //库存上限
    public String czsx; //处置上限
    
}
