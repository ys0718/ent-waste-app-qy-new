package com.grkj.ytjqy_pad.mvp.model;

/**
 * 数据上报实体
 */
public class DataUploadBean {
    public String id;      //主键
    public String dwid;    //单位id
    public String dataTime;  //数据登记时间，按天进行统计
    public Integer dataCount;     //数据量（条）
    public Integer reportType;      //数据上报类型：0产废登记数据；1入库登记数据；2出库自行利用处置数据；3出库转移数据；
    public String reportTime;    //数据上报时间
    public Integer reportStatus;     //数据上报状态：0.未上报；1.已上报；2.部分上报失败；3全部上报失败
    public String reportStatusName;     //数据上报状态：0.未上报；1.已上报；2.部分上报失败；3全部上报失败
}
