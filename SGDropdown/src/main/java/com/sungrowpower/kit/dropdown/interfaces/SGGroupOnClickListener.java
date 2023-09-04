package com.sungrowpower.kit.dropdown.interfaces;


/**
 * Description:分组item的点击接口
 * Create by hyk
 */
public interface SGGroupOnClickListener {

    /**
     * 当选项改变时触发的回调函数
     */
    void onGroupChange(int childPos,int parentPos);
}
