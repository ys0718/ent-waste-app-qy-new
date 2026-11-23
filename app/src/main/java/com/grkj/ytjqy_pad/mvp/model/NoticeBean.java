package com.grkj.ytjqy_pad.mvp.model;

/**
 * 公告实体
 */
public class NoticeBean {
    public String isHasNewNotice;//0表示没有最新公告也不会有content返回
    public Content content;

    public static class Content {
        public String id;
        public String title;
        public String content;
        public String isPublish;
        public String creator;
        public String modifyrq;
        public String createUserId;
        public String updateUserId;
        public String createTime;
        public String deleteFlag;
    }
}
