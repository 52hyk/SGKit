package com.sungrowpower.kit.dropdown.bean;

/**
 * 创建日期：2023/8/24 on 8:37
 * 描述:
 * 作者:hyk
 */
public class SGSimpleDataBean {
    //选项名称
    private String label;
    //是否可用
    private boolean disabled;
    //是否被选中
    private boolean isChecked;

    @Override
    public String toString() {
        return "SGSimpleDataBean{" +
                "label='" + label + '\'' +
                ", disabled=" + disabled +
                ", isChecked=" + isChecked +
                '}';
    }

    public SGSimpleDataBean(String label, boolean disabled, boolean isChecked) {
        this.label = label;
        this.disabled = disabled;
        this.isChecked = isChecked;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
