package com.sungrowpower.kit.dropdown.bean;

import java.util.List;

/**
 * 创建日期：2023/8/25 on 16:34
 * 描述:
 * 作者:hyk
 */
public class SGGroupBackDataBean {
    private int parentPos;
    private List<SGGroupChildBackDataBean> childList;

    @Override
    public String toString() {
        return "SGGroupBackDataBean{" +
                "parentPos=" + parentPos +
                ", childList=" + childList +
                '}';
    }

    public SGGroupBackDataBean(int parentPos, List<SGGroupChildBackDataBean> childList) {
        this.parentPos = parentPos;
        this.childList = childList;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public List<SGGroupChildBackDataBean> getChildList() {
        return childList;
    }

    public void setChildList(List<SGGroupChildBackDataBean> childList) {
        this.childList = childList;
    }

    public static class SGGroupChildBackDataBean{
        private int childPos;

        @Override
        public String toString() {
            return "SGGroupChildBackDataBean{" +
                    "childPos=" + childPos +
                    '}';
        }

        public int getChildPos() {
            return childPos;
        }

        public void setChildPos(int childPos) {
            this.childPos = childPos;
        }
    }
}
