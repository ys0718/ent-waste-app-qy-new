package com.grkj.ytjqy_pad.event;

/**
 * Created by yusam on 2022/3/11.
 */

public class ChangeEvent {
    private String msg;

    public ChangeEvent() {
    }

    public ChangeEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
