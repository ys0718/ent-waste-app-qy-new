package com.grkj.ytjqy_pad.gather;

import android.text.TextUtils;

import com.grkj.ytjqy_pad.utils.common.SPUtils;

/**
 * Created by yusam on 2018/10/12.
 * 请求地址
 */
public class ApiUrl {


    //最新公告
    public static final String URL_LASTED_NOTICE = "/ent-waste-qy/sys/sysnotice/getlastedNotice";

    //产废/入库/出库统计
    public static final String URL_GET_TOTAL = "/ent-waste-qy/homePage/getCflAndRklAndCkl";

    //收集单位入库/出库统计
    public static final String URL_GET_TOTAL_SJDW = "/ent-waste-qy/homePage/getFsJyRklAndCkl";

    //检查更新
    public static final String URL_GET_NEW_VERSION = "/ent-waste-qy/sys/mobilePackage/getLastVersionByDwid";

    //获取打印机信息
    public static final String URL_GET_PRINTER_INFO = "/ent-waste-qy/baseinfo/ybqyDevice/getdefineSetup?deviceCode=";

    //保存标签类型
    public static final String URL_SAVE_LABEL_TYPE = "/ent-waste-qy/baseinfo/ybqyDevice/updateSetUp";

    //社会源收集列表
    public static final String URL_COLLECT_LIST = "/ent-waste-qy/jyqy/gfybqyjyshysj/listJqgrid";

    //保存-社会源收集
    public static final String URL_COLLECT_SAVE = "/ent-waste-qy/jyqy/gfybqyjyshysj/save";

    //删除-产社会源收集
    public static final String URL_COLLECT_DELETE = "/ent-waste-qy/jyqy/gfybqyjyshysj/delete/";

    //删除-产社会源收集-子表
    public static final String URL_COLLECT_DELETE_CHILD = "/ent-waste-qy/jyqy/gfybqyjyshysjfwxx/delete/";

    //保存-社会源收集-子表
    public static final String URL_COLLECT_SAVE_CHILD = "/ent-waste-qy/jyqy/gfybqyjyshysjfwxx/add";

    //子列表-社会源收集
    public static final String URL_COLLECT_CHILD_LIST = "/ent-waste-qy/jyqy/gfybqyjyshysjfwxx/listJqgrid";

    //许可证列表
    public static final String URL_XKZ_LIST = "/ent-waste-qy/baseinfo/gfybqyxxbaxkzxx/listJqgrid";

    //根据许可证获取贮存设施列表
    public static final String URL_ZCSS_LIST_BY_XKZID = "/ent-waste-qy/baseinfo/gfybqyxxbaxkzxxzcss/listJqgrid?";

    //根据许可证ID获取废物类别列表
    public static final String URL_WASTE_TYPE_LIST_BY_XKZID = "/ent-waste-qy/baseinfo/gfybqyxxbaxkzxxfwxx/listJqgrid";

    //根据许可证ID获取废物类别列表
    public static final String URL_WASTE_TYPE_LIST_BY_ZCSSID = "/ent-waste-qy/baseinfo/ybqy/zcssFwxx/jqgrid";

    //产废登记列表
    public static final String URL_PRODUCT_RECORD_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCs/getlistJqgrid";

    //保存-产废登记
    public static final String URL_PRODUCT_RECORD_SAVE = "/ent-waste-qy/ybqy/gfYbqyTzCs/save";

    //删除-产废登记
    public static final String URL_PRODUCT_RECORD_DELETE = "/ent-waste-qy/ybqy/gfYbqyTzCs/delete/";

    //数据上报列表
    public static final String URL_DATA_UPLOAD_LIST = "/ent-waste-data-report/dataReport/getlistJqgrid";

    //数据上报详情列表
    public static final String URL_DATA_UPLOAD_STATUS = "/ent-waste-data-report/dataReport/getReportDetailStatus";

    //数据上报提交
    public static final String URL_DATA_UPLOAD_COMMIT = "/ent-waste-data-report/dataReport/reportData";

    //数据上报类型
    public static final String URL_DATA_UPLOAD_TYPE = "/ent-waste-data-report/dataReport/getReportType";

    //数据上报状态
    public static final String URL_DATA_UPLOAD_DETAIL_LIST = "/ent-waste-data-report/dataReport/getReportDataDetail";

    //入库列表
    public static final String URL_PRODUCT_INPUT_LIST = "/ent-waste-qy/ybqy/tzRk/getlistJqgrid";

    //虚拟标签-入库列表
    public static final String URL_XNBQ_PRODUCT_INPUT_LIST = "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/getListGrid";

    //虚拟标签-立产立处置列表
    public static final String URL_XNBQ_LC_LCZ_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/ybqyTzCkZxlyczGrid";

    //虚拟标签-产生申报列表
    public static final String URL_XNBQ_CSSB_LIST = "/ent-waste-qy/ybqy/gfYbqyTzNewCsXnbq/getListGrid";

    //虚拟标签-出库自行利用处置列表
    public static final String URL_XNBQ_CKZXLICZ_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/ybqyTzCkZxlyczGrid";

    //入库列表-经营台账
    public static final String URL_JYTZ_INPUT_LIST = "/ent-waste-qy/fs/jydw/rk/jqgrid";

    //入库删除-经营台账
    public static final String URL_JYTZ_INPUT_DELETE = "/ent-waste-qy/fs/jydw/rk/delete/";

    //标签信息-入库-经营台账
    public static final String URL_JYTZ_RK_LABEL_INFO = "/ent-waste-qy/fs/jydw/rk/getLabelData";

    //标签信息-出库-经营台账
    public static final String URL_JYTZ_CK_LABEL_INFO = "/ent-waste-qy/fs/jydw/ck/getLabelData";



    //经营台账-出库下拉废物类别列表
    public static final String URL_JYTZ_FWLB_LIST = "/ent-waste-qy/fs/jydw/rk/getNotClRkList";


    //联单-经营台账
    public static final String URL_JYTZ_ZYLD_LIST = "/ent-waste-qy/gz/zyld/jqgrid1";

    //数字识别列表-经营台账
    public static final String URL_JYTZ_SZSBM_LIST = "/ent-waste-qy/gz/zyld/getWrkjlList";

    //联单信息-经营台账
    public static final String URL_JYTZ_ZYLD_INFO = "/ent-waste-qy/gz/zyld/getByLdbh";


    //入库保存-经营台账
    public static final String URL_JYTZ_INPUT_SAVE = "/ent-waste-qy/fs/jydw/rk/save";

    //入库保存-经营台账
    public static final String URL_JYTZ_OUTPUT_SAVE = "/ent-waste-qy/fs/jydw/ck/save";


    //利用处置列表-经营台账
    public static final String URL_JYTZ_LYCZ_LIST = "/ent-waste-qy/jydw/tzCkLycz/jqgrid";

    //打印顺控标签
    public static final String URL_PRINT_SK_LABEL = "/ent-waste-qy/app/gfappprintrecords/printSkLabel";


//    //选择产废信息列表
//    public static final String URL_SELECT_WASTE_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCs/getNotClCSList";

    //选择产废信息列表
    public static final String URL_SELECT_WASTE_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCs/getToRkList";

    //选择产废/出库信息列表
    public static final String URL_SELECT_MULTI_WASTE_LIST = "/ent-waste-qy/ybqy/tzCkZxlyczFwxx/getNotClList";

    //去皮列表
    public static final String URL_TARE_LIST = "/ent-waste-qy/param/gfybqypzglpzxx/getlistJqgrid"; 

    //保存-去皮
    public static final String URL_TARE_SAVE = "/ent-waste-qy/param/gfybqypzglpzxx/save";

    //删除-去皮
    public static final String URL_TARE_DELETE = "/ent-waste-qy/param/gfybqypzglpzxx/delete/";

    //产废/入库/出库总数
    public static final String URL_PRODUCT_INPUT_TOTAL = "/ent-waste-qy/app/IntegratedMachine/getTotal";

    //虚拟标签产废入库列表-总量
    public static final String URL_XNBQ_PRODUCT_INPUT_TOTAL = "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/getSum";

    //虚拟标签产生申报列表-总量
    public static final String URL_XNBQ_CSSB_TOTAL = "/ent-waste-qy/ybqy/gfYbqyTzNewCsXnbq/getSum";

    //虚拟标签立产立处置列表-总量
    public static final String URL_XNBQ_LC_LCZ_TOTAL = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/getSum";


    //虚拟标签出库自行利用处置列表-总量
    public static final String URL_XNBQ_CK_ZYLYCZ_TOTAL = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/getSum";

    //虚拟标签-保存-出库自行利用处置
    public static final String URL_XNBQ_CKZXLICZ_SAVE = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/addYbqyTzCkZxlycz";

    //虚拟标签-删除-出库自行利用处置
    public static final String URL_XNBQ_CKZXLICZ_DELETE = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/delYbqyTzCkZxlyczById?id=";

    //虚拟标签-保存-立产立清产生申报
    public static final String URL_XNBQ_CSSB_SAVE = "/ent-waste-qy/ybqy/gfYbqyTzNewCsXnbq/addCsAndZxlycz";

    //虚拟标签-上报国家-立产立清产生申报
    public static final String URL_XNBQ_CSSB_SBGJ = "/ent-waste-qy/ybqy/gfYbqyTzNewCsXnbq/reportNation";

    //虚拟标签-保存-立产立处理
    public static final String URL_XNBQ_LC_LCZ_SAVE = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/updateYbqyTzCkZxlycz";

    //虚拟标签-删除-立产立清产生申报
    public static final String URL_XNBQ_CSSB_DELETE = "/ent-waste-qy/ybqy/gfYbqyTzNewCsXnbq/delCsXnbqById?id=";

    //入库总数-经营台账
    public static final String URL_JYTZ_INPUT_TOTAL = "/ent-waste-qy/fs/jydw/rk/getTotal";

    //出库总数-经营台账
    public static final String URL_JYTZ_OUTPUT_TOTAL = "/ent-waste-qy/fs/jydw/ck/getTotal";

    //处置利用总数-经营台账
    public static final String URL_JYTZ_CZLY_TOTAL = "/ent-waste-qy/jydw/tzCkLycz/countLyczTotal";

    //统计总数
    public static final String URL_PRODUCT_STATISTICS_TOTAL = "/ent-waste-qy/homePage/getStatisticsNumber";

    //当前用户企业信息
    public static final String URL_NOW_ENTERPRISE_INFO = "/ent-waste-qy/baseinfo/enterprise/getNowEnterprise";


    //获取产废并入库信息
    public static final String URL_PRODUCT_AND_INPUT_INFO = "/ent-waste-qy/ybqy/gfYbqyTzCfRk/getById";

    //虚拟标签-根据id查询产生入库台账
    public static final String URL_XNBQ_PRODUCT_AND_INPUT_INFO = "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/getCsRkXnbqById";

    //虚拟标签-根据id查询产生申报台账
    public static final String URL_XNBQ_CSSB_INFO = "/ent-waste-qy/ybqy/gfYbqyTzCkZxlyczXnbq/getYbqyTzCkZxlyczById";

    //保存-入库
    public static final String URL_PRODUCT_INPUT_SAVE = "/ent-waste-qy/ybqy/tzRk/save";

    //子表保存-入库
    public static final String URL_PRODUCT_INPUT_SAVE_CHILD = "/ent-waste-qy/ybqy/tzRkFwxx/addYbqyTzRkFwxxForIntegrated";

    //保存-产废并入库
    public static final String URL_PRODUCT_AND_INPUT_SAVE= "/ent-waste-qy/ybqy/gfYbqyTzCfRk/saveCfAndRk";

    //虚拟标签-保存-产废并入库
    public static final String URL_XNBQ_PRODUCT_AND_INPUT_SAVE= "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/saveCsRkXnbq";

    //保存-产、入、出同时
    public static final String URL_CRC_SAVE= "/ent-waste-qy/jyxw/gfYbqyTzCsRkCkJyxw/saveCfRkCk";

    //根据二维码获取入库信息
    public static final String URL_PRODUCT_INPUT_INFO = "/ent-waste-qy/ybqy/tzRk/getByQrCodeData";

    //根据出库返回的fwid获取产生环节废物id
    public static final String URL_GET_CSHJ_FWID = "/ent-waste-qy/baseinfo/ybqy/cshj/getFwxxByParam";

    //根据批次号获取产废登记信息
    public static final String URL_PRODUCT_RECORD_INFO = "/ent-waste-qy/ybqy/gfYbqyTzCs/getByQrCodeData";

    //根据批次号获取废物信息列表
    public static final String URL_SCAN_INPUT_INFO_LIST = "/ent-waste-qy/ybqy/gfYbqyTzCs/getRkListByQrCodeLatest";

    //删除-入库
    public static final String URL_PRODUCT_INPUT_DELETE = "/ent-waste-qy/ybqy/tzRk/deleteById";

    //删除-产废并入库
    public static final String URL_PRODUCT_AND_INPUT_DELETE = "/ent-waste-qy/ybqy/gfYbqyTzCfRk/delCfAndRk";

    //删除-产废并入库-虚拟标签
    public static final String URL_XNBQ_PRODUCT_AND_INPUT_DELETE = "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/delCsRkXnbqById?id=";

    //出库列表
    public static final String URL_PRODUCT_OUTPUT_LIST = "/ent-waste-qy/ybqy/tzCkZy/getlistJqgrid";

    //出库列表-经营台账
    public static final String URL_JYTZ_OUTPUT_LIST = "/ent-waste-qy/fs/jydw/ck/jqgrid";

    //利用处置保存-经营台账
    public static final String URL_JYTZ_LYCZ_SAVE = "/ent-waste-qy/jydw/tzCkLycz/save";

    //利用处置删除-经营台账
    public static final String URL_JYTZ_LYCZ_DELETE = "/ent-waste-qy/jydw/tzCkLycz/delete/";

    //利用处置-关联废物列表-经营台账
    public static final String URL_JYTZ_LYCZ_GLFW_LIST = "/ent-waste-qy/jydw/tzCkLycz/fwxx/getNotClList";

    //利用处置-已关联废物子表-经营台账
    public static final String URL_JYTZ_LYCZ_CHILD_LIST = "/ent-waste-qy/jydw/tzCkLycz/fwxx/jqgrid";

    public static final String URL_JYTZ_LYCZ_CHILD_CONNECT = "/ent-waste-qy/jydw/tzCkLycz/fwxx/insertBatch";



    //出库列表-收集
    public static final String URL_SJ_OUTPUT_LIST = "/ent-waste-qy/fs/jydw/ck/jqgrid";

    //出库列表-总数
    public static final String URL_SJ_OUTPUT_LIST_TOTAL = "/ent-waste-qy/fs/jydw/ck/getTotal";

    //保存-出库
    public static final String URL_PRODUCT_OUTPUT_SAVE = "/ent-waste-qy/ybqy/tzCkZy/savaCkZy";

    //保存-多条入库
    public static final String URL_PRODUCT_INPUT_SAVE_LIST = "/ent-waste-qy/ybqy/tzRk/batchRk";

    //保存-多条出库
    public static final String URL_PRODUCT_OUTPUT_SAVE_LIST = "/ent-waste-qy/ybqy/tzCkZy/batchCk";

    //删除-出库
    public static final String URL_PRODUCT_OUTPUT_DELETE = "/ent-waste-qy/ybqy/tzCkZy/deleteById";

    //自行利用列表
    public static final String URL_PRODUCT_ZXLY_LIST = "/ent-waste-qy/ybqy/tzCkZxlycz/listPage";
//    public static final String URL_PRODUCT_ZXLY_LIST = "/ent-waste-qy/ybqy/tzCkZxlycz/listJqgrid";

    //自行利用-废物信息列表
    public static final String URL_PRODUCT_ZXLY_FWXX_LIST = "/ent-waste-qy/ybqy/tzCkZxlyczFwxx/listPage";

    //自行利用当日总量
    public static final String URL_PRODUCT_ZXLY_TOTAL = "/ent-waste-qy/ybqy/tzCkZxlycz/getTotal";

    //保存-自行利用
    public static final String URL_PRODUCT_ZXLY_SAVE = "/ent-waste-qy/ybqy/tzCkZxlycz/save";

    //保存-多条自行利用
    public static final String URL_PRODUCT_ZXLY_SAVE_LIST = "/ent-waste-qy/ybqy/tzCkZxlycz/batchSave";

    //删除-自行利用
    public static final String URL_PRODUCT_ZXLY_DELETE = "/ent-waste-qy/ybqy/tzCkZxlycz/delete/";

    //委外利用列表
    public static final String URL_PRODUCT_WWLY_LIST = "/ent-waste-qy/ybqy/tzCkWwlycz/listPage";

    //自行利用根据批次号获取产废/出库信息
    public static final String URL_ZXLY_CS_CK_INFO = "/ent-waste-qy/ybqy/tzCkZxlycz/getByQrCodeData";

    //委外利用根据批次号获取产废/出库信息
    public static final String URL_WWLY_CS_CK_INFO = "/ent-waste-qy/ybqy/tzCkWwlycz/getByQrCodeData";

    //委外利用-废物信息列表
    public static final String URL_PRODUCT_WWLY_FWXX_LIST = "/ent-waste-qy/ybqy/tzCkWwlyczFwxx/listPage";

    //委外利用当日总量
    public static final String URL_PRODUCT_WWLY_TOTAL = "/ent-waste-qy/ybqy/tzCkWwlycz/getTotal";

    //保存-委外利用
    public static final String URL_PRODUCT_WWLY_SAVE = "/ent-waste-qy/ybqy/tzCkWwlycz/save";

    //保存-多条委外利用
    public static final String URL_PRODUCT_WWLY_SAVE_LIST = "/ent-waste-qy/ybqy/tzCkWwlycz/batchSave";

    //删除-委外利用
    public static final String URL_PRODUCT_WWLY_DELETE = "/ent-waste-qy/ybqy/tzCkWwlycz/delete/";


    //标签信息-产生登记
    public static final String URL_LABEL_INFO_CS = "/ent-waste-qy/ybqy/gfYbqyTzCs/getCsLabelData";

    //标签信息-入库
    public static final String URL_LABEL_INFO = "/ent-waste-qy/ybqy/tzRk/getLabelData";

    //标签信息-产入出
    public static final String URL_LABEL_INFO_LIST = "/ent-waste-qy/app/IntegratedMachine/getLableInfo";

    //标签信息-社会产生源子表id
    public static final String URL_LABEL_INFO_BY_CHILD_ID = "/ent-waste-qy/jyqy/gfybqyjyshysj/getLableInfo";

    //检测产废登记是否全部上报成功并能打印
    public static final String URL_CHECK_PRINTABLE_CFDJ = "/ent-waste-qy/app/IntegratedMachine/verifyCfIsReport";

    //检测产废入库是否全部上报成功并能打印
    public static final String URL_CHECK_PRINTABLE = "/ent-waste-qy/app/IntegratedMachine/verifyIsReport";

    //通过产生环节ID获取一条信息
    public static final String URL_GET_CSHJ_INFO = "/ent-waste-qy/baseinfo/ybqy/cshj/getByCshjId";

    //标签信息-出库
    public static final String URL_LABEL_INFO_CK = "/ent-waste-qy/ybqy/tzCkZy/getCkLabelData";

    //出库主表信息
    public static final String URL_CK_INFO = "/ent-waste-qy/ybqy/tzCkZy/data/";

    //出库主表信息
    public static final String URL_RFID_CARD_INFO = "/ent-waste-qy/baseinfo/ybqy/card/getByCardId?cardId=";

    //废物类别列表
    public static final String URL_WASTE_TYPE_LIST = "/ent-waste-qy/baseinfo/ybqy/cshj/list";

    //单位列表
    public static final String URL_DW_LIST = "/ent-waste-qy/jyqy/gfenterbaseinforelationjyxw/listXwqy";

    //产生设备列表
    public static final String URL_PRODUCT_DEVICE_LIST = "/ent-waste-qy/baseinfo/gfybqyxxbascssxxfwxx/getSsxxjqgrid?page=1&rows=99999";

    //一般固废-车间信息列表
    public static final String URL_CAR_ROOM_LIST = "/ent-waste-qy/mls/ybgygf/getScheduleGroup";

    //一般固废-保存
    public static final String URL_YBGF_SAVE = "/ent-waste-qy/mls/ybgygf/save";

    //一般固废-列表
    public static final String URL_YBGF_LIST = "/ent-waste-qy/mls/ybgygf/getlistJqgrid";

    //一般固废-删除
    public static final String URL_YBGF_DELETE = "/ent-waste-qy/mls/ybgygf/delete/";


    //产生设备列表
    public static final String URL_ALL_PRODUCT_DEVICE_LIST = "/ent-waste-qy/baseinfo/ybqy/gfybqyxxbascssxx/listScssJqgrid?page=1&rows=99999";

    //用户列表
    public static final String URL_USER_LIST = "/ent-waste-qy/sys/user/getSamePartUser?page=1&rows=99999";

    //接收单位企业列表
    public static final String URL_ENTER_LIST = "/ent-waste-qy/param/jsqy/listJqgridLast?";

    //出库子列表
    public static final String URL_OUTPUT_CHILD_LIST = "/ent-waste-qy/ybqy/tzCkZyFwxx/getlistJqgrid?page=1&rows=999";

    //入库子列表
    public static final String URL_INPUT_CHILD_LIST = "/ent-waste-qy/ybqy/tzRkFwxx/getlistJqgrid?page=1&rows=999";

    //来源企业列表
    public static final String URL_FROM_LIST = "/ent-waste-qy/baseinfo/enterprise/getlistJqgrid";

    //合同列表
    public static final String URL_HTXX_LIST = "/ent-waste-qy/htgl/gfhtgl/getlistJqgrid";

    //转移联单列表
    public static final String URL_ZYLD_LIST = "/ent-waste-qy/ld/zyld/list";

    //同步转移联单列表
    public static final String URL_SYNC_ZYLD_LIST = "/ent-waste-qy/ld/zyld/syncZyld";

    //转移联单-运输信息保存
    public static final String URL_ZYLD_YSXX_SAVE = "/ent-waste-qy/single/saveZyldYsxx";

    //转移联单-废物信息保存
    public static final String URL_ZYLD_FWXX_SAVE = "/ent-waste-qy/single/saveZyldFwxx";

    //转移联单-转移批次号保存
    public static final String URL_ZYLD_ZYPCH_SAVE = "/ent-waste-qy/single/updateZyldFwxxPch";

    //转移联单-主表保存
    public static final String URL_ZYLD_MAIN_SAVE = "/ent-waste-qy/single/saveZyld";

    //转移联单-上报国家-新建
    public static final String URL_ZYLD_SBGJ = "/ent-waste-qy/single/reportSingleTl/";

    //转移联单-上报国家-代办联单
    public static final String URL_ZYLD_SBGJ_TODO = "/ent-waste-qy/single/reportSingleBj/";

    //转移联单-运输单位信息查询-根据精准单位名称查询
    public static final String URL_ZYLD_YSQY_INFO = "/ent-waste-qy/single/listTransport";

    //转移联单-运输单位信息查询-根据主表查询
    public static final String URL_ZYLD_YSQY_INFO_BY_MAINID = "/ent-waste-qy/single/getYsxxListjqgrid";

    //转移联单-运输单位信息-删除
    public static final String URL_ZYLD_YSQY_INFO_DELETE = "/ent-waste-qy/single/deleteZyldYsxx/";

    //转移联单-选择接收单位
    public static final String URL_ZYLD_JSDW_INFO_LIST = "/ent-waste-qy/single/listLicence";

    //转移联单-根据主表获取废物列表
    public static final String URL_ZYLD_FWXX_LIST_BY_MAINID = "/ent-waste-qy/single/getFwxxListjqgrid";

    //转移联单-删除
    public static final String URL_ZYLD_DELETE = "/ent-waste-qy/single/deleteZyld/";

    //转移联单-废物删除
    public static final String URL_ZYLD_FWXX_DELETE = "/ent-waste-qy/single/deleteZyldFwxx/";

    //转移联单-批次号删除
    public static final String URL_ZYLD_FWXX_PCH_DELETE = "/ent-waste-qy/single/deleteZyldFwxxPch/";

    //选择转移批次号列表
    public static final String URL_SELECT_MULTI_ZYPCH_LIST = "/ent-waste-qy/single/getNotZyTzList_ytj";

    //保存联单废物批次号信息数据接口（可添加多个）
    public static final String URL_SAVE_MULTI_ZYPCH_LIST = "/ent-waste-qy/single/saveZyldFwxxPch";

    //联单废物信息批次号列表分页查询-一体机
    public static final String URL_ZYPCH_LIST = "/ent-waste-qy/single/getFwxxPchListjqgrid";

    //贮存设施列表
    public static final String URL_ZCSS_LIST = "/ent-waste-qy/baseinfo/ybqy/zcss/list";

    //根据废物信息查询贮存设施列表
    public static final String URL_ZCSS_LIST_BY_PARAM = "/ent-waste-qy/baseinfo/ybqy/zcss/getZcssByFwdlbhAndFwxlbh";

    //自行利用设施列表
    public static final String URL_ZXLYSS_LIST = "/ent-waste-qy/baseinfo/ybqy/zxly/list";

    //经营台账-利用处置设施列表
    public static final String URL_JYTZ_LYCZSS_LIST = "/ent-waste-qy/baseinfo/ybqy/zxly/list";

    //根据贮存设施获取废物类别列表
    public static final String URL_WASTE_TYPE_LIST_BY_ID = "/ent-waste-qy/baseinfo/ybqy/zcssFwxx/list";

    //产生环节废物列表
    public static final String URL_CSHJ_FWLX_LIST = "/ent-waste-qy/ybqy/gfYbqyTzNewCsRkXnbq/selectYbqyCshjFwxxList";

    //出库编辑时-根据贮存设施获取废物类别列表
    public static final String URL_WASTE_TYPE_LIST_BY_ID_CKBJ = "/ent-waste-qy/ybqy/tzRk/getNotClRKAndOwnList";

    //出库新增时-根据贮存设施获取废物类别列表
    public static final String URL_WASTE_TYPE_LIST_BY_ID_CKXZ = "/ent-waste-qy/ybqy/tzRk/getNotClRkList";


    //按钮列表
    public static final String URL_BUTTONS_LIST = "/ent-waste-qy/sys/role/button/getNowUsrBtnPermission";

    //参数列表
    public static final String URL_PARAM_LIST = "/ent-waste-qy/param/code/codeList";

    //行政区域树
    public static final String URL_XZQY_TREE = "/ent-waste-qy/param/xzqh/xzqhTree";

    //系统信息
    public static final String URL_SYSTEM_INFO = "/ent-waste-qy/sys/sysbaseconfig/getSysBaseInfo?type=";

    //获取菜单列表
    public static final String URL_MENU_LIST = "/ent-waste-qy/sys/user/getNewNowUserMenuTree?scope=";

    //企业台账列表
    public static final String URL_COMPANY_ACCOUNT_LIST = "/ent-waste-qy/app/IntegratedMachine/qytz";

    //企业台账入库总量、出库总量
    public static final String URL_COMPANY_ACCOUNT_TOTAL = "/ent-waste-qy/app/IntegratedMachine/getEnterpriseAccountTotal";

    //企业台账入库总量、出库总量-广州
    public static final String URL_COMPANY_ACCOUNT_TOTAL_GZ = "/ent-waste-qy/jyqy/common/getAccountTotal";

    //企业台账入库总量、出库总量-湖南
    public static final String URL_COMPANY_ACCOUNT_TOTAL_HN = "/ent-waste-qy/hnjyqy/common/getAccountTotal";

    //经营台账列表-广东
    public static final String URL_JY_ACCOUNT_LIST_GZ = "/ent-waste-qy/jyqy/common/jytz";

    //经营台账列表-湖南
    public static final String URL_JY_ACCOUNT_LIST_HN = "/ent-waste-qy/hnjyqy/common/jytz";


    //打印列表
    public static final String URL_PRINT_LIST = "/ent-waste-qy/app/IntegratedMachine/reprintlable";

    //库存列表
    public static final String URL_STOCK_LIST = "/ent-waste-qy/app/IntegratedMachine/getStockStatistics";

    //库存详情列表
    public static final String URL_STOCK_DETAIL_LIST = "/ent-waste-qy/app/IntegratedMachine/getStockStatisticsDetail";

    //库存总量
    public static final String URL_STOCK_TOTAL = "/ent-waste-qy/app/IntegratedMachine/getStockStatisticsTotal";

    //经营库存列表-广州
    public static final String URL_STOCK_LIST_GZ = "/ent-waste-qy/jyqy/common/getStockStatistics";

    //经营库存详情列表-广州
    public static final String URL_STOCK_DETAIL_LIST_GZ = "/ent-waste-qy/jyqy/common/getStockStatisticsDetail";

    //库存总量-广州
    public static final String URL_STOCK_TOTAL_GZ = "/ent-waste-qy/jyqy/common/getStockStatisticsTotal";

    //经营库存列表-湖南
    public static final String URL_STOCK_LIST_HN = "/ent-waste-qy/hnjyqy/common/getStockStatistics";

    //经营库存详情列表-湖南
    public static final String URL_STOCK_DETAIL_LIST_HN = "/ent-waste-qy/hnjyqy/common/getStockStatisticsDetail";

    //库存总量-湖南
    public static final String URL_STOCK_TOTAL_HN = "/ent-waste-qy/hnjyqy/common/getStockStatisticsTotal";


    //修改密码
    public static final String URL_UPDATE_PASSWORD = "/ent-waste-qy/mobile/common/updatePassword";

    //下载Apk
    public static final String URL_DOWNLOAD_APK = "/ent-waste-qy/pluploadFtp/download/";

    //缓存列表
    public static final String URL_HISTORY_LIST = "/ent-waste-qy/fillincache/getCacheList";

    //缓存保存
    public static final String URL_HISTORY_SAVE = "/ent-waste-qy/fillincache/sava";

    //缓存删除
    public static final String URL_HISTORY_DELETE = "/ent-waste-qy/fillincache/delOneCacheItem";

    //出库校正保存
    public static final String URL_CHECK_SAVE_CK = "/ent-waste-qy/ybqy/gfybqytzckcorrectionrecord/save";

    //入库校正保存
    public static final String URL_CHECK_SAVE_RK = "/ent-waste-qy/ybqy/gfybqytzrkcorrectionrecord/save";

    //转移联单-列表
    public static final String URL_NEW_ZYLD_LIST = "/ent-waste-qy/single/getlistJqgrid";

    //待办转移联单-列表
    public static final String URL_TODO_ZYLD_LIST = "/ent-waste-qy/single/getlistJqgrid_dqs";





    /**收集入库登记列表*/
    public static final String URL_SJ_INPUT_LIST = "/ent-waste-qy/fs/jydw/rk/jqgrid";
    /**收集入库总数*/
    public static final String URL_SJ_INPUT_TOTAL = "/ent-waste-qy/fs/jydw/rk/getTotal";
    /**根据二维码获取收集入库数据*/
    public static final String URL_SJ_INPUT_INFO = "/ent-waste-qy/fs/jydw/rk/getByQrCodeData";
    /**收集入库-合同列表*/
    public static final String URL_SJ_INPUT_HT_LIST = "/ent-waste-qy/fs/htgl/jqgrid";
    /**收集入库-联单列表*/
    public static final String URL_SJ_INPUT_LD_LIST = "/ent-waste-qy/fs/zyld/jqgrid";
    /**收集入库-应急事件列表*/
    public static final String URL_SJ_INPUT_YJSJ_LIST = "/ent-waste-qy/fs/yjEvent/jqgrid";
    /**收集入库-贮存设施列表*/
    public static final String URL_SJ_INPUT_ZCSS_LIST = "/ent-waste-qy/fs/jydw/zcss/jqgrid";
    /**收集入库-贮存区域列表*/
    public static final String URL_SJ_INPUT_ZCQY_LIST = "/ent-waste-qy/fs/jydw/zcss/qyxx/jqgrid";
    /**收集入库-废物类别列表*/
    public static final String URL_SJ_INPUT_FWLB_LIST = "/ent-waste-qy/fs/zyld/fwxx/jqgrid";
    /**收集入库-产生单位列表*/
    public static final String URL_SJ_INPUT_CSDW_LIST = "/ent-waste-qy/fs/jydw/rk/listCsdwmc";
    /**收集入库-保存*/
    public static final String URL_SJ_INPUT_SAVE = "/ent-waste-qy/fs/jydw/rk/save";
    /**收集入库-标签信息*/
    public static final String URL_SJ_INPUT_LABEL = "/ent-waste-qy/fs/jydw/rk/getLabelData";
    /**收集入库-删除*/
    public static final String URL_SJ_INPUT_DELETE = "/ent-waste-qy/fs/jydw/rk/delete/";

    /**收集出库-保存*/
    public static final String URL_SJ_OUTPUT_SAVE = "/ent-waste-qy/fs/jydw/ck/save";

    /**收集出库-批量保存*/
    public static final String URL_SJ_OUTPUT_SAVE_MUL = "/ent-waste-qy/fs/jydw/ck/batchCk";
    /**收集出库-标签信息*/
    public static final String URL_SJ_OUTPUT_LABEL = "/ent-waste-qy/fs/jydw/ck/getLabelData";
    /**收集出库-删除*/
    public static final String URL_SJ_OUTPUT_DELETE = "/ent-waste-qy/fs/jydw/ck/delete/";

    /**危废入库登记列表---广东*/
    public static final String URL_WASTE_INPUT_LIST = "/ent-waste-qy/jyqy/gfjyqytzrk/listJqgrid";
    /**危废入库总数*/
    public static final String URL_WASTE_INPUT_TOTAL = "/ent-waste-qy/jyqy/gfjyqytzrk/countRkTotal";
    /**废物名录树数据*/
    public static final String URL_ALL_WASTE_TYPE = "/ent-waste-qy/param/wxfw/getDataTree";
    /**省固废待入库数据列表*/
    public static final String URL_WAIT_INPUT_LIST = "/ent-waste-qy/jyqy/gfjyqytzrk/getgdSgfToRkList";
    /**保存危废入库数据*/
    public static final String URL_WASTE_INPUT_SAVE = "/ent-waste-qy/jyqy/gfjyqytzrk/save";
    /**删除危废入库数据*/
    public static final String URL_WASTE_INPUT_DELETE = "/ent-waste-qy/jyqy/gfjyqytzrk/delete/";
    /**危废出库登记列表*/
    public static final String URL_WASTE_OUTPUT_LIST = "/ent-waste-qy/jyqy/gfjyqytzck/listJqgrid";
    /**危废出库总数*/
    public static final String URL_WASTE_OUTPUT_TOTAL = "/ent-waste-qy/jyqy/gfjyqytzck/countCkTotal";
    /**待出库数据列表*/
    public static final String URL_WAIT_OUTPUT_LIST = "/ent-waste-qy/jyqy/gfjyqytzck/getNotClRkFwxx";
    /**许可证列表*/
    public static final String URL_LICENSE_LIST = "/ent-waste-qy/jyqy/common/getXkzList";
    /**自行利用处置设施列表*/
    public static final String URL_ZXLY_DEVICE_LIST = "/ent-waste-qy/baseinfo/ybqy/zxly/listJqgrid";
    /**保存危废出库数据*/
    public static final String URL_WASTE_OUTPUT_SAVE = "/ent-waste-qy/jyqy/gfjyqytzck/save";
    /**删除危废出库数据*/
    public static final String URL_WASTE_OUTPUT_DELETE = "/ent-waste-qy/jyqy/gfjyqytzck/deleteById";
    /**获取标签数据*/
    public static final String URL_GET_LABEL_INFO = "/ent-waste-qy/jyqy/common/getLabelData";
    /**根据二维码获取入库信息*/
    public static final String URL_GET_INPUT_INFO_BY_QRCODE = "/ent-waste-qy/jyqy/gfjyqytzrk/getRkByQrcode";
    /**根据二维码获取出库信息*/
    public static final String URL_GET_OUTPUT_INFO_BY_QRCODE = "/ent-waste-qy/jyqy/gfjyqytzck/getCkByQrCode";

    /**危废入库登记列表---湖南*/
    public static final String URL_HN_WASTE_INPUT_LIST = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/listJqgrid";
    /**危废入库总数*/
    public static final String URL_HN_WASTE_INPUT_TOTAL = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/countRkTotal";
    /**根据入库数据id查询出库详细信息*/
    public static final String URL_HN_WASTE_INPUT_INFO_BY_ID = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/getDetailById";
    /**小微企业待入库废物信息列表*/
    public static final String URL_HN_XWQY_WASTE_INPUT_LIST = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/getToRkList";
    /**扫码添加小微企业待入库废物*/
    public static final String URL_HN_SCAN_XWQY_WASTE_INPUT = "/ent-waste-qy/hnjyqy/common/getRkFwxxByQrcode";
    /**保存小微企业出库废物信息*/
    public static final String URL_SAVE_HN_XWQY_WASTE_OUTPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzrkfwxx/saveRkFwxx";
    /**删除小微企业出库废物信息*/
    public static final String URL_DELETE_HN_XWQY_WASTE_OUTPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzrkfwxx/deleteById";
    /**保存危废入库数据*/
    public static final String URL_SAVE_HN_WASTE_INPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/save";
    /**删除入库数据*/
    public static final String URL_DELETE_HN_WASTE_INPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzrk/deleteRkById";
    /**危废出库登记列表*/
    public static final String URL_HN_WASTE_OUTPUT_LIST = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/listJqgrid";
    /**危废出库总数*/
    public static final String URL_HN_WASTE_OUTPUT_TOTAL = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/countCkTotal";
    /**根据出库数据id查询出库详细信息*/
    public static final String URL_HN_WASTE_OUTPUT_INFO_BY_ID = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/data/";
    /**根据贮存设施id查询废物信息*/
    public static final String URL_HN_WASTE_INFO_BY_ZCSSID = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/getNotClRkFwxx";
    /**删除出库数据*/
    public static final String URL_DELETE_HN_WASTE_OUTPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/deleteById";
    /**保存危废出库数据*/
    public static final String URL_SAVE_HN_WASTE_OUTPUT = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/save";
    /**获取湖南标签数据*/
    public static final String URL_GET_HN_LABEL_INFO = "/ent-waste-qy/hnjyqy/common/getLabelData";
    /**根据二维码获取出库信息*/
    public static final String URL_GET_HN_OUTPUT_INFO_BY_QRCODE = "/ent-waste-qy/hnjyqy/gfhnjyqytzck/getCkByQrCode";



    public static String getHnUrl(String rawUrl) {
        boolean isHn = (boolean) SPUtils.get(App.getContext(), Constants.IS_HN_MENU, false);
        String newUrl = rawUrl;
        if (isHn && !TextUtils.isEmpty(rawUrl) && rawUrl.contains("/ent-waste-qy/ybqy")) {
            newUrl = rawUrl.replace("/ent-waste-qy/ybqy", "/ent-waste-qy/jyxw");
        }

        if (isHn && !TextUtils.isEmpty(rawUrl) && rawUrl.contains("/ent-waste-qy/app/IntegratedMachine")) {
            newUrl = rawUrl.replace("/ent-waste-qy/app/IntegratedMachine", "/ent-waste-qy/app/jyxw/IntegratedMachine");
        }

        return newUrl;
    }


}
