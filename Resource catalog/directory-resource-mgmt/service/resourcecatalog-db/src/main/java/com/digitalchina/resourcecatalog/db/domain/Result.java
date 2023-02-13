package com.digitalchina.resourcecatalog.db.domain;

/**
 * @author: create by wangmh
 * @projectName: resourcecatalog
 * @packageName: com.digitalchina.resourcecatalog.admin.dto
 * @description:
 * @date: 2020/5/19
 **/
public class Result {
    private String name;
    private String total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
