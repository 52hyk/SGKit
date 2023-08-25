package com.sungrowpower.kit.dropdown.interfaces;


import com.sungrowpower.kit.dropdown.base.SGBaseView;

/**
 * Description: dropdown显示和隐藏的回调接口，如果你不想重写3个方法，则可以使用SimpleCallback，
 * 它是一个默认实现类
 *  Create by hyk
 */
public interface SGDropDownCallback {
    /**
     * 弹窗的onCreate方法执行完调用
     */
    void onCreated(SGBaseView popupView);

    /**
     * 在show之前执行，由于onCreated只执行一次，如果想多次更新数据可以在该方法中
     */
    void beforeShow(SGBaseView popupView);

    /**
     * 完全显示的时候执行
     */
    void onShow(SGBaseView popupView);

    /**
     * 完全消失的时候执行
     */
    void onDismiss(SGBaseView popupView);

    /**
     * 准备消失的时候执行
     */
    void beforeDismiss(SGBaseView popupView);

    /**
     * 暴漏返回按键的处理，如果返回true，Dropdown不会处理；如果返回false，Dropdown会处理，
     * @return
     */
    boolean onBackPressed(SGBaseView popupView);

    /**
     * 当软键盘高度改变时调用，比如打开和关闭软键盘
     * @param height
     * @return
     */
    void onKeyBoardStateChanged(SGBaseView popupView, int height);

}
