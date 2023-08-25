package com.sungrowpower.kit.dropdown.bean;

import java.util.List;

/**
 * 创建日期：2023/8/24 on 8:37
 * 描述:
 * 作者:hyk
 */
public class SGGroupDataBean {
    private String title;
    private List<SGSimpleDataBean> childData;

    public SGGroupDataBean(String title, List<SGSimpleDataBean> childData) {
        this.title = title;
        this.childData = childData;
    }

    @Override
    public String toString() {
        return "SGGroupDataBean{" +
                "title='" + title + '\'' +
                ", childData=" + childData +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SGSimpleDataBean> getChildData() {
        return childData;
    }

    public void setChildData(List<SGSimpleDataBean> childData) {
        this.childData = childData;
    }







}