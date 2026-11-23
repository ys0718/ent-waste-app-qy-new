package com.grkj.ytjqy_pad.utils.popwindow;

/**
 * Created by chenerte on 2017/11/28.
 */

public class SelectPopupWindowBean {
    public String ID;
    public String NAME;
    public String GROUP_NAME;
    public boolean SELECT;

    public SelectPopupWindowBean() {

    }

    public SelectPopupWindowBean(String ID, String NAME) {
        this.ID = ID;
        this.NAME = NAME;
    }

    public SelectPopupWindowBean(String ID, String NAME,boolean SELECT) {
        this.ID = ID;
        this.NAME = NAME;
        this.SELECT = SELECT;
    }

    public SelectPopupWindowBean(String ID, String NAME, String GROUP_NAME) {
        this.ID = ID;
        this.NAME = NAME;
        this.GROUP_NAME = GROUP_NAME;
    }
}
