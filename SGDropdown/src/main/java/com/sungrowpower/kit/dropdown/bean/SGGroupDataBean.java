package com.sungrowpower.kit.dropdown.bean;

import java.util.List;

/**
 * 创建日期：2023/8/24 on 8:37
 * 描述:
 * 作者:hyk
 */
public class SGGroupDataBean {
    private String title;
    private SGSimpleDataBean childData;


    public SGGroupDataBean(String title, SGSimpleDataBean childData) {
        this.title = title;
        this.childData = childData;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public SGSimpleDataBean getChildData() {
        return childData;
    }

    public void setChildData(SGSimpleDataBean childData) {
        this.childData = childData;
    }




}
