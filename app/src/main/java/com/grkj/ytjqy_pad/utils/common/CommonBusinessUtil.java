package com.grkj.ytjqy_pad.utils.common;

import android.os.Bundle;
import android.text.TextUtils;


/**
 * Create by seny on 2023/12/7
 * Description:业务封装类
 **/
public class CommonBusinessUtil {

    private CommonBusinessUtil() {
    }

    public static class CommonBusinessUtilInstance {
        private static final CommonBusinessUtil INSTANCE = new CommonBusinessUtil();
    }

    public static CommonBusinessUtil getInstance() {
        return CommonBusinessUtil.CommonBusinessUtilInstance.INSTANCE;
    }


}
