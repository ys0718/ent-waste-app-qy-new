package com.grkj.ytjqy_pad.mvp.model;

import java.util.ArrayList;
import java.util.List;

public class WasteTypeListBean {
    public String id;

    public String fwlb;

    public String pcode;

    public String fwmc;

    public String lev;

    public String mlnf;

    public List<ChildList> childList;

    public static class ChildList{

        public String id;

        public String fwlb;

        public String pcode;

        public String fwmc;

        public String lev;

        public String mlnf;
    }
}
