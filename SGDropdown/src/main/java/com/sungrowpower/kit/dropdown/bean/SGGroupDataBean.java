package com.sungrowpower.kit.dropdown.bean;

import java.util.List;

/**
 * 创建日期：2023/8/24 on 8:37
 * 描述:分组数据模型
 * 作者:hyk
 */
public class SGGroupDataBean {
    //分组的名称
    private String title;

    //分组的数据
    private List<SGSimpleDataBean> childData;

    //是否分组
    private boolean isGroup;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public SGGroupDataBean(List<SGSimpleDataBean> childData, boolean isGroup) {
        this.childData = childData;
        this.isGroup = isGroup;
    }

    public SGGroupDataBean(String title, List<SGSimpleDataBean> childData, boolean isGroup) {
        this.title = title;
        this.childData = childData;
        this.isGroup = isGroup;
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
