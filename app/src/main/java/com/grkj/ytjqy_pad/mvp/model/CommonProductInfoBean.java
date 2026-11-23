package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2023/2/15.
 * 产废/入库/出库/废物信息公用页面
 */
public class CommonProductInfoBean extends BaseBean {

    public String id;      //主键
    public String ckpch;   //出库批次号
    public String cstzbm;   //产生台账编号
    public String csSzsbm;   //产生数字识别码
    public String szsbm;   //数字识别码
    public String cksj;      //出库时间
    public String jsdwmc;  //接收单位名称（zyldbh为“越境转移”时必填）
    public String jsdwid;  //接收单位id
    public String ckjbr;   //出库部门经办人
    public String ysjbr;   //运送部分经办人
    public String dwid;    //单位id
    public String entername;//企业名称

    public String fwdlbh;      //废物大类编号
    public String fwdlmc;      //废物大类名称
    public String fwxlbh;      //废物小类编号
    public String fwxlmc;      //废物小类名称
    public String fwxxmc;      //废物详细名称
    public String wlxz;        //物理性状
    public String fwtx;        //废物特性
    public String ckl;     //出库量
    public String jldw;        //计量单位
    public String rqbzbm;      //容器包装编码
    public String rqbzlx;      //容器/包装类型
    public String rqbzcz;      //容器包装材质
    public String rqbzgg;      //容器/包装规模
    public String rqbzsl;  //容器/包装数量
    public String rkpch;   //入库批次号
    public String zcssbm;  //贮存存设施编码
    public String zcssQyxxId;  //贮存区域id
    public String zcssQyxxMc;  //贮存区域名称
    public String zcsslx;  //贮存设施类型
    public String zcsstybh; // 贮存设施统一编号
    public String qx;//去向

    public String cspch;      //产生批次号
    public String mlnf;      //mlnf
    public String wxcf;      //危害成分
    public String fwId;      //废物id
    public String rkl;      //入库量
    public String rksj;      //入库时间
    public String isck;      //是否已出库


    public String mainid;      //出库关联id
    public String lrfs;    // 录入方式 1-称重录入 2-扫码录入 为空则是手动

    public String qrcodeValue;//二维码识别号
    public String rkQrcodeValue;//入库识别号
    public String zywrcf;
    public String dataFrom;//手持终端sczd 一体机ytj
    public String versionFrom;   //版本号

    public String zyldid; //转移联单id
    public String zyldbh; //转移联单编号
    public String htid;    //合同id
    public String htbh; //合同编号
    public String lydwid; //来源单位id
    public String lydwmc; //来源单位名称
    public String isly; //是否已利用 1是 0否
    public String ckfwid;//子表id

    public String zcjbr;   //贮存经办人
    public String zcjbrsj;   //贮存经办人手机
    public String zcfwxxId;      //产废环节关联id
    public String fwxxId;      //产废环节关联id
    public String syl;//贮存量
    public String wxtx;      //危险特性
    public String zysx;         //注意事项
    public String bz;         //bz
    public String csl;         //产生量
    public String cssj;         //产生时间
    public String remark;		//备注

    public String jbr;     //产生部门经办人
    public String jbrsj;     //产生部门经办人手机
    public String csjbr;     //产生部门经办人
    public String iscl;    //是否被处理（0-否；1-是）
    public String scssbh;//产生设施编号
    public String scssid;//产生设施id
    public String scssmc;//产生设施名称
    public String scsslx;//产生设施类型

    public String bzfs;	//包装方式
    public String bzsl;	//包装数量
    public String no;		//编号
    public String cshjId;        //产生环节id

    public String bqlx;  //标签类型 n-新版 o-旧版
    public String deviceCode;  //设备编码

    public String hftx;  ////挥发特性 1-挥发性 0-不挥发

    public String qrcodeUrl; //链接二维码

    public String wxfwbqewm; //二维码内容

    public String rk_id; //入库id

//    public String num = "0"; //批量入库数量

    public String typch; //同一批次号

    public GfYbqyTzCkZyFwxx gfYbqyTzCkZyFwxx = new GfYbqyTzCkZyFwxx(); //子表

    public GfYbqyTzRkFwxx gfYbqyTzRkFwxx = new GfYbqyTzRkFwxx(); //子表

    public int jumpType = 0;  //列表数据跳转标识 0-其它 1-扫码出入库列表跳转

    public String xwqyDwid;//小微企业单位id

    /*经营单位*/

    public String ewmwb; //二维码

    public String jsl; //接收量

    public String ldbh; //联单编号

    public String rkId;  //入库id

    public String xkzbh;  //许可证编号

    public String xkzbid; //许可证id

    public String xkzid;

    public String ssbm; //利用处置设施编码

    public String ssbh; //利用处置设施编号

    public String lyczfsdl;

    public String lyczfs; //利用处置设施大类

    public String lyczfsxl; //利用处置设施小类

    public String sslx; //利用处置设施类型

    public String zcssId; //贮存设施id

    public String zcssid; //贮存设施id

    public String sgfId; //省固废id

    public String clfs;

    public String rklx;//入库类型

    public String zyldNumber1;//转移联单编号

    public String zyldId;//转移联单id

    public String czfs;//处置方式

    public String czfsxl;//处置方式小类

    public String zyldFwxxId;//转移联单废物信息id

    public String csdwid;//产生单位id

    public String csdwmc;//产生单位名称

    public String fwxt;//废物形态

    public String yjEventId;//应急事件id

    public String yjEventName;//应急事件名称

    public String cph;    //车牌号
    public String sjmc;    //司机名称
    public String sjdh;    //司机联系方式
    public String zxlyczsj;//自行利用处置时间

    public String zxlyczl;//自行利用处置量


    //出库子表
    public static class GfYbqyTzCkZyFwxx{
        public String rkid;
        public String id;
        public String ckl;
        public String mlnf;
        public String fwdlbh;
        public String fwdlmc;
        public String fwxlbh;
        public String fwxlmc;
        public String fwxxmc;
        public String rkpch;
        public String jldw;
        public String rqbzbm;
        public String rqbzlx;
        public String rqbzsl;
        public String rqbzgg;
        public String rqbzcz;
        public String wlxz;
        public String fwtx;
        public String zcssbm;
        public String zcsslx;
        public String bqlx;      //标签类型
    }


    //入库子表
    public static class GfYbqyTzRkFwxx{
        public String id;          //主键
        public String mainid;      //GF_YBQY_TZ_RK主键
        public String csfwid;      //GF_YBQY_TZ_CS_ID表主键
        public String cspch;       //产生批次号
        public String fwdlbh;      //废物大类编号
        public String fwdlmc;      //废物大类名称
        public String fwxlbh;      //废物小类编号
        public String fwxlmc;      //废物小类名称
        public String fwxxmc;      //废物详细名称
        public String wlxz;        //物理性状
        public String rkl;     //入库量
        public String jldw;        //计量单位
        public String rqbzbm;      //容器包装编码
        public String rqbzlx;      //容器/包装类型
        public String rqbzsl;  //容器/包装数量
        public String isck;        //是否出库（0-否；1-是）
        public String mlnf;        //名录版本
        public String fwtx;        //废物特性
        public String rqbzcz;      //容器包装材质
        public String rqbzgg;      //容器/包装规格
        public String qx;      //去向
        public String bqlx;      //标签类型
        public String qrcodeValue;//二维码识别号
        public String qrcodeUrl;//二维码识别号

    }

    /**小微企业出库废物信息列表*/
    public List<GFHnJyqyTzRkFwxxes> gfHnJyqyTzRkFwxxes = new ArrayList<>();

    //小微企业出库废物信息列表
    public static class GFHnJyqyTzRkFwxxes{

        public String id;          //主键
        public String mainid;      //GF_YBQY_TZ_RK主键
        public String dwid;     //单位id
        public String ckId;
        public String xwqyDwid;     //小微企业单位id
        public String entername;        //单位名称
        public String fwId;
        public String fwdlbh;      //废物大类编号
        public String fwdlmc;      //废物大类名称
        public String fwxlbh;      //废物小类编号
        public String fwxlmc;      //废物小类名称
        public String fwxxmc;      //废物详细名称
        public String wlxz;        //物理性状
        public String ckpch;        //出库批次号
        public String cksj;     //出库时间
        public String ckl;     //出库量
        public String jldw;        //计量单位
        public String zcssbm;       //贮存设施编码
        public String zcsslx;       //贮存设施类型
        public String rqbzbm;      //容器包装编码
        public String rqbzsl;      //容器包装数量
        public String rqbzlx;      //容器/包装类型
        public String fwtx;        //废物特性
        public String wxcf;        //危险成分
        public String zysx;        //注意事项j
        public String zywrcf;      //主要污染成分
        public String rqbzcz;      //容器包装材质
        public String rqbzgg;      //容器/包装规格
        public String jsdwmc;      //接收单位名称
        public String jsdwid;      //接收单位id
        public String qrcodeValue; //二维码识别号
        public String rkQrcodeValue; //出库二维码
        public String remark;      //备注
        public String isly;
        public String lsh;
        public String fwzt; //废物状态
        public String ckjbr;        //出库经办人
        public String ysjbr;        //运输经办人
        public String sl;
        public String csdwmc; //产生单位名称
        public String csdwid; //产生单位id
        public boolean SELECT;
    }



}
