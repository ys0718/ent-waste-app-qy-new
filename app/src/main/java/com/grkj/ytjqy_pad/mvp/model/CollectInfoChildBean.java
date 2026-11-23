package com.grkj.ytjqy_pad.mvp.model;

/**
 * Create by  on 2023/11/1
 * Description: 社会源收集子表实体
 **/
public class CollectInfoChildBean {

    /**
     * 主键ID
     */
    //(value="主键ID")
    public String id;
    /**
     * gf_ybqy_jy_shysj表主键ID
     */
    //(value="gf_ybqy_jy_shysj表主键ID")
    public String mainid;
    /**
     * 利用处置方式
     */
    //(value="利用处置方式")
    public String lyczfs;
    /**
     * 废物大类编号
     */
    //(value="废物大类编号")
    public String fwdlbh;
    /**
     * 废物大类名称
     */
    //(value="废物大类名称")
    public String fwdlmc;
    /**
     * 废物小类编号
     */
    //(value="废物小类编号")
    public String fwxlbh;
    /**
     * 废物小类名称
     */
    //(value="废物小类名称")
    public String fwxlmc;
    /**
     * 废物详细名称
     */
    //(value="废物详细名称")
    public String fwxxmc;
    /**
     * 物理性状-改为废物形态
     */
    //(value="物理性状-改为废物形态")
    public String wlxz;
    /**
     * 废物特性-改为危险特性
     */
    //(value="废物特性-改为危险特性")
    public String fwtx;
    /**
     * 入库量
     */
    //(value="入库量")
    public String rkl;
    /**
     * 计量单位
     */
    //(value="计量单位")
    public String jldw;
    /**
     * 容器包装编码
     */
    //(value="容器包装编码")
    public String rqbzbm;
    /**
     * 容器/包装类型
     */
    //(value="容器/包装类型")
    public String rqbzlx;
    /**
     * 容器/包装数量
     */
    //(value="容器/包装数量")
    public String rqbzsl;
    /**
     * 容器/包装材质
     */
    //(value="容器/包装材质")
    public String rqbzcz;
    /**
     * 容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）
     */
    //(value="容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）")
    public String rqbzgg;
    /**
     * 修改时间戳
     */
    //(value="修改时间戳")
    public String modifyrq;
    /**
     * 二维码包含的值内容
     */
    //(value="二维码包含的值内容")
    public String qrcodeValue;
    /**
     * 主要污染成分
     */
    //(value="主要污染成分")
    public String zywrcf;
    /**
     * 注意事项
     */
    //(value="注意事项")
    public String zysx;

    //(value="危险成分")
    public String wxcf;

    public String bzfs;


    /**
     * 创建人ID
     */
    //(value="创建人ID")
    public String createUserId;
    /**
     * 修改人ID
     */
    //(value="修改人ID")
    public String upStringUserId;
    /**
     * 创建时间
     */
    //(value="创建时间")
    public String createTime;
    /**
     * 删除标识（1-删除，0-新增，2-修改）
     */
    //(value="删除标识（1-删除，0-新增，2-修改）")
    public String deleteFlag;
    /**
     * 备注
     */
    //(value="备注")
    public String remark;

    //(value="入库时间")
    public String rksj;


    /**
     * 贮存设施ID
     */
    //(value="贮存设施ID")
    public String zcssId;
    public String no;			//编号
    public String ssmc;		//设施名称
    public String sslx;		//设施类型
    public String zcnl;	//设计贮存能力
    public String zdmj;	//占地面积（平米）

    //(value="利用处置方式大类")
    public String lyczfsdl;

    //(value="利用处置方式小类")
    public String lyczfsxl;
    
}
