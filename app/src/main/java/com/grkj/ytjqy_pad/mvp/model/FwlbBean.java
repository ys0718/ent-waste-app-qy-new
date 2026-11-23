package com.grkj.ytjqy_pad.mvp.model;

import java.util.Date;

/**
 * Created by yusam on 2023/2/15.
 * 产废/入库/出库/废物信息公用页面
 */
public class FwlbBean extends BaseBean {
    public String id;			//主键
    public String mainid;		//GF_YBQY_XXBA_ZCSS表主键
    public String fwdlbh;		//废物类别
    public String fwdlmc;		//废物类别名称
    public String fwxlbh;		//废物代码
    public String fwxlmc;		//废物名称
    public String fwxxmc;		//废物详细名称
    public String isSheng;		//迁移状态(gdgf-旧固废,gfhjjg-新固废)
    public String mlnf;		//名录版本

    public String createUserId;		//创建人ID
    public String updateUserId;		//修改人ID
    public Date createTime;			//创建时间

    public String fwtx;	//废物特性
    public String wlxz;	//物理性状、形态
    public String bzfs;	//包装方式

    public String wxcf;    //危险成分

    public String cshjId;    //gf_ybqy_xxba_cshj表的主键

    public String clfs;	//处理方式及去向

    public String rqbzbm;      //容器包装编码
    public String rqbzlx;      //容器/包装类型
    public String rqbzcz;      //容器包装材质
    public String rqbzgg;  //容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）

    public String zywrcf;    //主要污染成分
    public String zysx;    //注意事项
    public String remark;	//备注
}
