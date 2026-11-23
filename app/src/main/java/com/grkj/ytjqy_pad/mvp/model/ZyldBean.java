package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yusam on 2022/3/21.
 * 产生设施
 */

public class ZyldBean {
    public boolean SELECT;//是否选中
    public String id; //主键
    public String number1; //省联单编号
    public String jsdwmc; //经营单位名称
    public String csdwid; //产生单位id
    public String csdwzym;//产生单位名称
    public String zyjhid; //管理计划id
    public String gjnumber; //国家联单编号
    public String jsdwid; //接收单位id
    public String xkzid; //许可证主键
    public String xkzbh; //许可证编号
    public String jhzyrq; //计划转移时间
    public String CSDWBLSJ; //产生单位办理时间
    public String fyr; //发运人
    public String ydd; //运达地
    public String ldsffq; //联单是否作废
    public String zfly; //联单作废理由说明
    public String isend; //是否结束
    public String jsdwblsj; //接收单位办理时间
    public String csdwthrq; //产生单位退回日期
    public String jsr; //接收人
    public String fyrsj; //发运人手机
    public String jsrq; //接收日期
    public String eczy; //二次转移
    public String yslx; //运输类型
    public String ysfs; //运输方式
    public String yssjSta; //运输开始时间
    public String yssjEnd; //运输结束时间
    public String modifyrq; //修改时间
    public String deleteFlag; //删除标识
    public String ycshi; //移出市
    public String yrshi; //移入市
    public String xkzbid; //许可证绑定id
    public String actid; //1新建2安排运输3运输4确认数量5产废确认5_5处置上报国家6完结7环保确认8-环保退回
    public String blld; //联单类型(0.正常;1.补录普通;2.补录应急;3.补录特殊;4.补录跨境;00.跨省转入)
    public String ycyrshi; //移出移入市
    public String ly; //来源 0-回流 1-本地新增 2-本地新增（废水联单）
    public String ysapqk; //运输安排情况
    public String fwrqsl; //废物容器数量
    public String rqjldw; //废物容器计量单位
    public String fsqrfwsl; //废水去人废物数量
    public String qrbz; //确认备注
    public List<CommonProductInfoBean> fwxxList = new ArrayList<>();
}
