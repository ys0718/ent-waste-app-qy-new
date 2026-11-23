package com.grkj.ytjqy_pad.utils.common;

import android.widget.EditText;

/**
 * 编辑文本工具类
 */

public class EditTextUtils {

    /**
     *      EditText父布局加入以下代码可夺取焦点
     *      android:focusable="true"
     *      android:focusableInTouchMode="true"
     * */

    /**
     * 改变EditText可编辑状态
     *
     * @param editable 是否可编辑
     */
    public static void setEditable(boolean editable, EditText... etUnitNames) {
        for (EditText etUnitName : etUnitNames) {
            //设置输入框中的光标可见/不可见
            etUnitName.setCursorVisible(editable);
            //有/无焦点
            etUnitName.setFocusable(editable);
            //触摸时也得到/得不到焦点
            etUnitName.setFocusableInTouchMode(editable);
        }
    }

}
