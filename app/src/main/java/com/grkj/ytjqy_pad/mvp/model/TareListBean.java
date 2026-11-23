package com.grkj.ytjqy_pad.mvp.model;

import com.grkj.ytjqy_pad.utils.popwindow.SelectPopupWindowBean;

/**
 * Created by yusam on 2022/3/21.
 * 皮重列表
 */

public class TareListBean extends SelectPopupWindowBean {
    /**
     * 主键
     */
    public String id;
    /**
     * 单位ID
     */
    public String dwid;
    /**
     * 贮存设施ID
     */
    public String zcssId;
    /**
     * 贮存设施编码
     */
    public String zcssbm;
    /**
     * 贮存设施类型
     */
    public String zcsslx;

    /**
     * 贮存设施名称
     */
    public String zcssmc;
    /**
     * 废物大类ID
     */
    public String fwId;
    /**
     * 废物大类编号
     */
    public String fwdlbh;
    /**
     * 废物大类名称
     */
    public String fwdlmc;
    /**
     * 废物小类编号
     */
    public String fwxlbh;
    /**
     * 废物小类名称
     */
    public String fwxlmc;

    /**
     * 废物信息名称
     */
    public String fwxxmc;
    /**
     * 容器包装编码
     */
    public String rqbzbm;
    /**
     * 容器/包装类型
     */
    public String rqbzlx;
    /**
     * 容器/包装材质
     */
    public String rqbzcz;
    /**
     * 容器/包装规格（≤25L-1；50L-2；200L-3；1000L-4；其它-9）
     */
    public String rqbzgg;
    /**
     * 容器/包装重量
     */
    public String rqbzzl;
    /**
     * 其它重量
     */
    public String qtzl;

    /**
     * 产生环节ID
     */
    public String cshjId;

    /*
    皮重量
    * */
    public String pzl;
}
