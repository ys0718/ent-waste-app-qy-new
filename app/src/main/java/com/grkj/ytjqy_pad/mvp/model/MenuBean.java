package com.grkj.ytjqy_pad.mvp.model;

import java.util.List;

/**
 * 菜单实体
 * */
public class MenuBean {
    public boolean SELECT;

    public String id;
    public String name;
    public String href;
    public String target;
    public String icon;
    public String iconUrl;
    public String sort;
    public String isShow;
    public String permission;
    public String controlMark;
    public String flag;
    public String scope;
    public String type;

    public List<MenuBean> childList; //子表

}
