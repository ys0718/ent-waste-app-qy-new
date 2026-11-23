package com.grkj.ytjqy_pad.gather;

import com.google.gson.Gson;

import java.util.List;

/**
 * 公共分页封装类
 *
 * @anthor yusam
 * created on 2022/3/19
 */

public class PageBean<T> {
    private int page;
    private int records;
    private int total;
    private List<T> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
