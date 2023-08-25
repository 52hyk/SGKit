package com.sungrowpower.kit.dropdown.interfaces;

import com.sungrowpower.kit.dropdown.bean.SGGroupBackDataBean;

import java.util.List;

/**
 * Description:
 * Create by hyk
 */
public interface SGOnClickOptionListener {


    /**
     * 当前选择的选项标识
     */
    void selectedValue(List<Integer> columnData, List<SGGroupBackDataBean> groupBackDataBeanList);

    /**
     * 点击选项时触发的回调函数
     * @param childPos 子列表的postion
     * @param parentPos 父列表的postion
     */
    void onOptionClick(int childPos,int parentPos);
    /**
     * 当选项改变时触发的回调函数
     */
    void onOptionChange(int childPos,int parentPos);
}
