package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2022/3/21.
 * 自行利用信息
 */


public class ZxlyListBean extends BaseBean {
    public String id;      //主键
    public String dwid;    //单位id
    public String zxlyczpcbm;//自行利用/处置批次编码
    public String zxlyczsj;      //自行利用/处置时间
    public String ssbm;    //利用/处置设施编码
    public String ssmc;    //利用/处置设施名称
    public String lyczfsdlbh;  //利用处置方式大类编号
    public String lyczfsdlmc;  //利用处置方式大类名称
    public String lyczfsxlbh;//利用处置方式小类编号
    public String lyczfsxlmc;//利用处置方式小类名称
    public String zxlyczjbr;//自行利用/处置部门经办人
    public String zxlyczwbsj;//自行利用/处置完毕时间（年月日时分秒），必须大于出库时间
    public Integer lsh;     //流水号
    public String qrcodeValue;  //二维码包含的值内容
    public String jldw;    //计量单位

    public String fwdlbh;      //废物大类编号
    public String fwdlmc;      //废物大类名称
    public String fwxlbh;      //废物小类编号
    public String fwxlmc;      //废物小类名称
    public String fwxxmc;      //废物详细名称
    public String wlxz;        //物理性状-改为废物形态
    public String fwtx;        //废物特性-改为危险特性
    public String rqbzbm;      //容器包装编码
    public String rqbzlx;      //容器/包装类型
    public String rqbzcz;      //容器包装材质
    public String rqbzgg;  //容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）
    public String rqbzsl;  //容器/包装数量
    public String fwId;       //废物ID

    public String ckpch;    //出库批次号
    public String cspch;   //产生批次号
    public String zxlyczl;     //自行利用/处置量
    public String wwlyczl;     //委外利用/处置量
    public String csl;     //产生量
    public String ckl;     //出库量
    public String ckjbr;       //出库部门经办人
    public String ysjbr;       //运送部门经办人
    public String lyczlx;      //利用处置类型；1-立产立清利用处置；2-出库利用处置
    public String ssid;      //设施id

    public List<ZxlyFwListBean> zxlyczFwxxes =  new ArrayList<>();

    public static class ZxlyFwListBean{
        public boolean SELECT;//是否选中
        public String id;      //主键
        public String mainid;  //GF_YBQY_TZ_CK_ZXLYCZ主键
        public String ckid;    //GF_YBQY_TZ_CK表主键
        public String ckfwid;  //GF_YBQY_TZ_CK_FWXX表主键
        public String csfwid;  //GF_YBQY_TZ_CS表主键

        public String zxlyczl;     //自行利用/处置量
        public String wwlyczl;     //委外利用/处置量
        public String jldw;    //计量单位
        public String ckpch;   //出库批次号
        public String fwdlbh;  //废物大类编号
        public String fwdlmc;  //废物大类名称
        public String fwxlbh;  //废物小类编号
        public String fwxlmc;  //废物小类名称
        public String fwxxmc;  //废物详细名称
        public String wlxz;    //物理性状
        public String fwtx;        //废物特性-改为危险特性
        public String rqbzbm;      //容器包装编码
        public String rqbzlx;      //容器/包装类型
        public String rqbzcz;      //容器包装材质
        public String rqbzgg;  //容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）
        public String rqbzsl;  //容器/包装数量
        public String cspch;       //产生批次号
        public String mlnf;        //名录年份
        public String lyczl;        //利用处置量

    }


}