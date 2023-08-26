package com.sungrowpower.kit.dropdown.bean;

import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

import androidx.lifecycle.Lifecycle;


import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback;
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOptionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: DropDown的属性封装
 * Create by hyk
 */
public class SGDropDownInfoBean {
    //按返回键是否消失
    public Boolean isDismissOnBackPressed = true;
    //点击外部消失
    public Boolean isDismissOnTouchOutside = true;
    //操作完毕后是否自动关闭
    public Boolean autoDismiss = true;
    // 是否有半透明的背景
    public Boolean hasShadowBg = true;
    // 依附于那个View
    public View atView = null;
    // 动画执行器，如果不指定，则会根据窗体类型dropDownType字段生成默认合适的动画执行器
    public SGDropDownAnimation SGDropDownAnimation = null;
    public DropDownAnimator customAnimator = null;
    // 触摸的点
    public PointF touchPoint = null;
    // 最大宽度
    public int maxWidth;
    // 最大高度
    public int maxHeight;
    // 指定弹窗的宽高，受max的宽高限制
    public int dropDownWidth, dropDownHeight;
    //是否自动打开输入法
    public Boolean autoOpenSoftInput = false;
    public SGDropDownCallback SGDropDownCallback;
    //是否移动到软键盘上面，默认弹窗会移到软键盘上面
    public Boolean isMoveUpToKeyboard = true;
    //弹窗出现在目标的什么位置
    public DropDownPosition dropDownPosition = null;
    //弹窗是否强制抢占焦点
    public boolean isRequestFocus = true;
    //是否让输入框自动获取焦点
    public boolean autoFocusEditText = true;
    //是否点击透传，默认弹背景点击是拦截的
    public boolean isClickThrough = false;
    //是否关闭后进行资源释放
    public boolean isDestroyOnDismiss = false;
    //是否已屏幕中心进行定位，默认根据Material范式进行定位
    public boolean positionByWindowCenter = false;
    //阴影背景的颜色
    public int shadowBgColor = 0;
    //动画的时长
    public int animationDuration = -1;
    //当触摸在这个区域时，不消失
    public ArrayList<Rect> notDismissWhenTouchInArea;
    //使用多列
    public int useColumn = 0;
    //数据集合
    public List<Object> options;
    //是否支持多选
    public boolean multiple = false;
    //数据监听回调
    public SGOnClickOptionListener sgOnClickOptionListener;
    //自定义的宿主生命周期
    public Lifecycle hostLifecycle;

    public Rect getAtViewRect() {
        int[] locations = new int[2];
        atView.getLocationInWindow(locations);
        return new Rect(locations[0], locations[1], locations[0] + atView.getMeasuredWidth(),
                locations[1] + atView.getMeasuredHeight());
    }
}
