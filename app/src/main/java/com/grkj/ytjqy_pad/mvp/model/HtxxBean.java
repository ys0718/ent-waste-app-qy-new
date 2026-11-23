package com.grkj.ytjqy_pad.mvp.model;

/**
 * Created by yusam on 2022/12/20.
 * 合同信息
 */

public class HtxxBean extends BaseBean {

    /**
     * 主键
     */
    //"主键")
    public String id;

    /**
     * 合同编号
     */
    //"合同编号")
    public String htbh;


    /**
     * 甲方单位
     */
    //"甲方单位")
    public String jfdwmc;

    /**
     * 甲方单位ID
     */
    //"甲方单位ID")
    public String jfdwid;

    /**
     * 乙方单位
     */
    //"乙方单位")
    public String yfdwmc;

    /**
     * 乙方单位ID
     */
    //"乙方单位ID")
    public String yfdwid;

    /**
     * 合同开始时间
     */
    public String htstatime;

    /**
     * 合同结束时间
     */
    public String htendtime;

//
//    /**
//     * 单位ID
//     */
//    //"单位ID")
//    public String dwid;
//
//

//    /**
//     * 甲方单位ID
//     */
//    //"甲方单位ID")
//    public String jfdwid;
//
//    //"甲方单位地址")
//    public String jfdwdz;


//    /**
//     * 开户名称
//     */
//    //"开户名称")
//    public String khmc;
//    /**
//     * 合同金额
//     */
//    //"合同金额")
//    public String htje;
//    /**
//     * 甲方联系人
//     */
//    //"甲方联系人")
//    public String jflxr;
//    /**
//     * 乙方联系人
//     */
//    //"乙方联系人")
//    public String yflxr;
//    /**
//     * 合同类型
//     */
//    //"合同类型")
//    public String htlx;
//    /**
//     * 合同类型代码
//     */
//    //"合同类型代码，1，2包年，3,4非包年")
//    public String htlxCode;
//    /**
//     * 财务类型
//     */
//    //"财务类型")
//    public String cwlx;
//    /**
//     * 财务类型代码
//     */
//    //"财务类型代码")
//    public String cwlxCode;
//    /**
//     * 税号
//     */
//    //"税号")
//    public String sh;
//    /**
//     * 开户银行
//     */
//    //"开户银行")
//    public String khBank;
//    /**
//     * 行业类型
//     */
//    //"行业类型")
//    public String hylx;
//    /**
//     * 行业类型code
//     */
//    //"行业类型code")
//    public String hylxCode;
//    /**
//     * 甲方联系电话
//     */
//    //"甲方联系电话")
//    public String jfphone;
//    /**
//     * 乙方联系电话
//     */
//    //"乙方联系电话")
//    public String yfphone;
//    /**
//     * 合同开始时间
//     */
//    //"合同开始时间")
//    public String htStartTime;
//    /**
//     * 合同结束时间
//     */
//    //"合同结束时间")
//    public String htEndTime;
//    /**
//     * 银行账号
//     */
//    //"银行账号")
//    public String bankNum;
//    /**
//     * 合同归属人ID
//     */
//    //"合同归属人ID")
//    public String htgsrId;
//    /**
//     * 合同归属
//     */
//    //"合同归属")
//    public String htgs;
//    /**
//     * 特殊约定
//     */
//    //"特殊约定")
//    public String tsyd;
//    /**
//     * 当前审核人姓名
//     */
//    //"当前审核人姓名")
//    public String shrxm;
//    /**
//     * 当前审核时间
//     */
//    //"当前审核时间")
//    public String shsj;
//    /**
//     * 审核意见
//     */
//    //"审核意见")
//    public String shyj;
//    /**
//     * 提交状态（0-未提交 1-已提交）
//     */
//    //"提交状态（0-未提交 1-已提交）")
//    public String tjzt;
//    /**
//     * 审核状态（0-未审核 1-审核不通过 2-审核通过）
//     */
//    //"审核状态（0-未审核 1-审核不通过 2-审核通过）")
//    public String shzt;
//    /**
//     * 合同状态（0-生效中 1-预警（到期前一个月） 2-已到期 3-已废除）
//     */
//    //"合同状态（0-生效中 1-预警（到期前一个月） 2-已到期 3-已废除 4-未生效）")
//    public String htzt;
//    /**
//     * 付款状态 1-已付款，0-未付款
//     */
//    //"付款状态 1-已付款，0-未付款")
//    public String paymentStatus;
//    /**
//     * 确认时间-供财务管理模块使用
//     */
//    //"确认时间-供财务管理模块使用")
//    public String confirmationTime;
//    /**
//     * 财务签名人
//     */
//    //"财务签名人")
//    public String cwer;
//    /**
//     * 创建者ID
//     */
//    //"创建者ID")
//    public String createUserId;
//    /**
//     * 更新者ID
//     */
//    //"更新者ID")
//    public String upStringUserId;
//    /**
//     * 创建时间
//     */
//    //"创建时间")
//    public String createTime;
//    /**
//     * 更新时间
//     */
//    //"更新时间")
//    public String modifyrq;
//    /**
//     * 删除标志位 1-删除，0新增，2，修改
//     */
//    //"删除标志位 1-删除，0新增，2，修改")
//    public String deleteFlag;
//
//    //"备注")
//    public String remark;
//
//    /**
//     * 附加条款（0-无 1-是）
//     */
//    //"附加条款（0-无 1-是）")
//    public String fjtk;
//
//    //"附加条款内容")
//    public String fjtknr;
//    //"附加条款选项")
//    public String fjtkxx;
//
//    /**
//     * 运输费用
//     */
//    //"运输费用")
//    public String ysfy;
//
//    /**
//     * 其他费用
//     */
//    //"其他费用")
//    public String qtfy;
//
//    /**
//     * 佣金备注
//     */
//    //"佣金备注")
//    public String yjbz;
//
//    /**
//     * 实际收款金额
//     */
//    //"实际收款金额")
//    public String sjskje;
//
//    //"佣金支付状态 1-已支付，0-未支付")
//    public String yjPayStatus;
//
//    //"佣金支付时间")
//    public String yjConfirmationTime;
//
//    /**
//     * 是否上传盖章合同（0-否 1-是）
//     */
//    //"是否上传盖章合同（0-否 1-是）")
//    public String uploadContract;
//
//    /**
//     * 上传合同时间
//     */
//    //"上传合同时间")
//    public String uploadContractTime;
//
//    /**
//     * 上传人姓名
//     */
//    //"上传人姓名")
//    public String uploadUserName;
//
//    /**
//     * 上传人ID
//     */
//    //"上传人ID")
//    public String uploadUserId;
//
//    //"付款方")
//    public String fkf;
//
//    //"乙方单位地址")
//    public String yfdwdz;
//
//    //"乙方开户名称")
//    public String yfkhmc;
//
//    //"乙方开户银行")
//    public String yfkhyh;
//
//    //"乙方银行账号")
//    public String yfyhzh;
//
//    //"乙方税号")
//    public String yfsh;
//
//    //"合同性质，在佛山的无废系统里面是合同类型")
//    public String htxz;
//
//    //"合同性质代码")
//    public String htxzCode;
//
//    //"费用类型")
//    public String fylx;
//
//    //"费用类型code")
//    public String fylxCode;
//
//    //"合同地址")
//    public String htdz;
//
//    //"是否提供规范化服务 1-提供 2-不提供")
//    public String isGfh;
//
//    //"服务项目，多个服务用逗号隔开,直接存中文")
//    public String fwxm;
//
//    //"业务单位ID")
//    public String ywdwid;
//
//    //"合同年月")
//    public String htny;
//
//    //"合同有效期-开始年月")
//    public String htyxqStart;
//
//    //"合同有效期-结束年月")
//    public String htyxqEnd;
//
//    //"用来保存yj json字符串,不用管")
//    public String yj;
//    //"佣金实际支付")
//    public String yjActualPay;
//    //"佣金应支付")
//    public String yjShouldPay;
//
//    public Integer lsh;
//    //"是否赋有补充协议 1-是 0-否")
//    public String hasBcxy; //是否赋有补充协议 1-是 0-否
//
//    //"回款日期")
//    public String hkrq;
//
//    //"包运次数")
//    public Integer bycs;
//
//    //"超出次数收费")
//    public String cccssf;


}
