package com.sungrowpower.kit.dropdown.base;

import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import androidx.lifecycle.Lifecycle;


import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback;

import java.util.ArrayList;

/**
 * Description: Popup的属性封装
 * Create by hyk
 */
public class SGDropDownInfoBean {

    public Boolean isDismissOnBackPressed = true;  //按返回键是否消失
    public Boolean isDismissOnTouchOutside = true; //点击外部消失
    public Boolean autoDismiss = true; //操作完毕后是否自动关闭
    public Boolean hasShadowBg = true; // 是否有半透明的背景
    public View atView = null; // 依附于那个View
    // 动画执行器，如果不指定，则会根据窗体类型popupType字段生成默认合适的动画执行器
    public SGDropDownAnimation SGDropDownAnimation = null;
    public DropDownAnimator customAnimator = null;
    public PointF touchPoint = null; // 触摸的点
    public int maxWidth; // 最大宽度
    public int maxHeight; // 最大高度
    public int dropDownWidth, dropDownHeight; // 指定弹窗的宽高，受max的宽高限制
    public Boolean autoOpenSoftInput = false;//是否自动打开输入法
    public SGDropDownCallback SGDropDownCallback;

    public Boolean isMoveUpToKeyboard = true; //是否移动到软键盘上面，默认弹窗会移到软键盘上面
    public DropDownPosition dropDownPosition = null; //弹窗出现在目标的什么位置
    public Boolean hasStatusBarShadow = false; //是否显示状态栏阴影

    public boolean isRequestFocus = true; //弹窗是否强制抢占焦点
    public boolean autoFocusEditText = true; //是否让输入框自动获取焦点
    public boolean isClickThrough = false;//是否点击透传，默认弹背景点击是拦截的
    public boolean isDestroyOnDismiss = false; //是否关闭后进行资源释放
    public boolean positionByWindowCenter = false; //是否已屏幕中心进行定位，默认根据Material范式进行定位
    public int shadowBgColor = 0; //阴影背景的颜色
    public int animationDuration = -1; //动画的时长
    public int statusBarBgColor = 0; //状态栏阴影颜色，对Drawer弹窗和全屏弹窗有效
    public ArrayList<Rect> notDismissWhenTouchInArea; //当触摸在这个区域时，不消失
    public Lifecycle hostLifecycle; //自定义的宿主生命周期

    public Rect getAtViewRect(){
        int[] locations = new int[2];
        atView.getLocationInWindow(locations);
        return new Rect(locations[0], locations[1], locations[0] + atView.getMeasuredWidth(),
                locations[1] + atView.getMeasuredHeight());
    }
}
