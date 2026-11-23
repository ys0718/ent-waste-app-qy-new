package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by seny on 2023/11/1
 * Description: 社会源收集实体
 **/
public class CollectInfoBean {

    /**
     * 主键
     */
    //value="主键")
    public String id;

    //value="单位ID")
    public String dwid;
    /**
     * 收集日期
     */
    //value="收集日期")
    public String sjrq;
    /**
     * 许可证编号
     */
    //value="许可证编号")
    public String xkzbh;
    /**
     * 许可证id
     */
    //value="许可证编号")
    public String xkzId;
    /**
     * 收集对象（小微企业单位）
     */
    //value="收集对象（小微企业单位）")
    public String sjdx;
    /**
     * 收集点
     */
    //value="收集点ID")
    public String sjdxId;
    /**
     * 贮存设施ID（仓库）
     */
    //value="贮存设施ID（仓库）")
    public String zcssId;
    /**
     * 自定义流水号
     */
    //value="自定义流水号")
    public String zdylsh;
    /**
     * 驾驶员姓名
     */
    //value="驾驶员姓名")
    public String jsyName;
    /**
     * 车牌号
     */
    //value="车牌号")
    public String cph;
    /**
     * 产生单位
     */
    //value="产生单位")
    public String csdw;
    /**
     * 省代码
     */
    //value="省代码")
    public String sssheng;
    /**
     * 省名称
     */
    //value="省名称")
    public String ssshengValue;
    /**
     * 市代码
     */
    //value="市代码")
    public String ssshi;
    /**
     * 市名称
     */
    //value="市名称")
    public String ssshiValue;
    /**
     * 区代码
     */
    //value="区代码")
    public String ssqu;
    /**
     * 区名称
     */
    //value="区名称")
    public String ssquValue;
    /**
     * 产生责任人
     */
    //value="产生责任人")
    public String cszrr;
    /**
     * 联系人电话
     */
    //value="联系人电话")
    public String lxPhone;
    /**
     * 产生责任人证件类型
     */
    //value="产生责任人证件类型")
    public String cszrrZjlx;
    /**
     * 产生责任人证件号
     */
    //value="产生责任人证件号")
    public String cszrrZjh;
    /**
     * 产生单位地址
     */
    //value="产生单位地址")
    public String csdwdz;
    /**
     * 收集单位责任人
     */
    //value="收集单位责任人")
    public String sjdwzrr;
    /**
     * 收集责任人证件类型
     */
    //value="收集责任人证件类型")
    public String sjzrrZjlx;
    /**
     * 收集单位责任人证件号
     */
    //value="收集单位责任人证件号")
    public String sjdwzrrZjh;
    /**
     * 修改时间戳
     */
    //value="修改时间戳")
    public String modifyrq;
    /**
     * 创建人ID
     */
    //value="创建人ID")
    public String createUserId;
    /**
     * 修改人ID
     */
    //value="修改人ID")
    public String upStringUserId;
    /**
     * 创建时间
     */
    //value="创建时间")
    public String createTime;
    /**
     * 删除标识（1-删除，0-新增，2-修改）
     */
    //value="删除标识（1-删除，0-新增，2-修改）")
    public String deleteFlag;

    /**
     * 提交状态标识（1-提交 0-未提交）
     */
    public String commitStatus;

    public List<CollectInfoChildBean> fwxxes = new ArrayList<>();


}
