package com.grkj.ytjqy_pad.mvp.model.ybgf;

import java.util.ArrayList;
import java.util.List;

/**
 * 车间信息
 * @author: ganghaip
 * @date: 2026/5/23 星期六
 */
public class GfMlsGygfScheduleGroup {

    /**
     * 主键
     */
    public String id;
    /**
     * 单位ID
     */
    public String dwid;
    /**
     * 车间名称
     */
    public String cjmc;

    public List<GfMlsGygfScheduleGroupFwxx> fwxxList = new ArrayList<>();

}
