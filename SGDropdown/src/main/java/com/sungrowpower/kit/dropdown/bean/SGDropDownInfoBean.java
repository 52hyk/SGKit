package com.sungrowpower.kit.dropdown.bean;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.lifecycle.Lifecycle;


import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback;
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOptionListener;
import com.sungrowpower.kit.dropdown.widget.SGDropDownColorMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: DropDown的属性封装
 * Create by hyk
 */
public class SGDropDownInfoBean {
    //按返回键是否消失
    private Boolean isDismissOnBackPressed = true;
    //点击外部消失
    private Boolean isDismissOnTouchOutside = true;
    //操作完毕后是否自动关闭
    private Boolean autoDismiss = true;
    // 是否有半透明的背景
    private Boolean hasShadowBg = true;
    // 依附于那个View
    private View atView = null;
    // 动画执行器，如果不指定，则会根据窗体类型dropDownType字段生成默认合适的动画执行器
    private SGDropDownAnimation sgDropDownAnimation = null;
    private DropDownAnimator customAnimator = null;

    // 最大宽度
    private int maxWidth;
    // 最大高度
    private int maxHeight;
    // 指定弹窗的宽，受max的宽高限制
    private int dropDownWidth;
    // 指定弹窗的高，受max的宽高限制
    private int dropDownHeight;
    //是否自动打开输入法
    private Boolean autoOpenSoftInput = false;
    private SGDropDownCallback sgDropDownCallback;
    //是否移动到软键盘上面，默认弹窗会移到软键盘上面
    private Boolean isMoveUpToKeyboard = true;
    //弹窗出现在目标的什么位置
    private DropDownPosition dropDownPosition = null;
    //弹窗是否强制抢占焦点
    private boolean isRequestFocus = true;
    //是否让输入框自动获取焦点
    private boolean autoFocusEditText = true;
    //是否点击透传，默认弹背景点击是拦截的
    private boolean isClickThrough = false;
    //是否关闭后进行资源释放
    private boolean isDestroyOnDismiss = false;
    //阴影背景的颜色
    private int shadowBgColor = SGDropDownColorMap.getInstance().shadowBgColor;
    //动画的时长
    private int animationDuration = SGDropDownColorMap.getInstance().animationDuration;
    //当触摸在这个区域时，不消失
    private ArrayList<Rect> notDismissWhenTouchInArea;
    //使用多列
    private int useColumn = 0;
    //数据集合
    private List<Object> options;
    //是否支持多选
    private boolean multiple = false;
    //数据监听回调
    private SGOnClickOptionListener sgOnClickOptionListener;
    //自定义的宿主生命周期
    private Lifecycle hostLifecycle;

    //设置Text文本，支持富文本
    private CharSequence sgKitText = "内容";
    //设置Title文本大小
    private float sgKitTextSize = SGDropDownColorMap.getInstance().textSize;
    //设置Title文本颜色
    private int sgKitTextColor = SGDropDownColorMap.getInstance().textColor;
    //设置Title文本Typeface(如“加粗”)
    private Typeface sgKitTypeface = Typeface.DEFAULT;
    //设置fontIcon文本大小
    private float sgFontIconTextSize = SGDropDownColorMap.getInstance().fontIconTextSize;
    //设置fontIcon文本颜色
    private int sgFontIconTextColor = SGDropDownColorMap.getInstance().fontIconTextColor;

    //设置fontIcon Text文本，支持富文本
    private CharSequence sgFontIconText = "&#xe98d;";

    //设置item选中文本颜色
    private int sgItemCheckedTextColor = SGDropDownColorMap.getInstance().itemCheckedTextColor;
    //设置item 未选中文本颜色
    private int sgItemUnCheckedTextColor = SGDropDownColorMap.getInstance().itemUnCheckedTextColor;
    //设置item 不能点击中文本颜色
    private int sgItemDisableTextColor = SGDropDownColorMap.getInstance().itemDisableTextColor;
    //设置item 选中背景颜色
    private Drawable sgItemCheckedTextBgColor = SGDropDownColorMap.getInstance().itemCheckedTextBgColor;
    //设置item 未选中背景颜色
    private Drawable sgItemUnCheckedTextBgColor = SGDropDownColorMap.getInstance().itemUnCheckedTextBgColor;


    public int getSgItemCheckedTextColor() {
        return sgItemCheckedTextColor;
    }

    public void setSgItemCheckedTextColor(int sgItemCheckedTextColor) {
        this.sgItemCheckedTextColor = sgItemCheckedTextColor;
    }

    public int getSgItemUnCheckedTextColor() {
        return sgItemUnCheckedTextColor;
    }

    public void setSgItemUnCheckedTextColor(int sgItemUnCheckedTextColor) {
        this.sgItemUnCheckedTextColor = sgItemUnCheckedTextColor;
    }

    public int getSgItemDisableTextColor() {
        return sgItemDisableTextColor;
    }

    public void setSgItemDisableTextColor(int sgItemDisableTextColor) {
        this.sgItemDisableTextColor = sgItemDisableTextColor;
    }

    public Drawable getSgItemCheckedTextBgColor() {
        return sgItemCheckedTextBgColor;
    }

    public void setSgItemCheckedTextBgColor(Drawable sgItemCheckedTextBgColor) {
        this.sgItemCheckedTextBgColor = sgItemCheckedTextBgColor;
    }

    public Drawable getSgItemUnCheckedTextBgColor() {
        return sgItemUnCheckedTextBgColor;
    }

    public void setSgItemUnCheckedTextBgColor(Drawable sgItemUnCheckedTextBgColor) {
        this.sgItemUnCheckedTextBgColor = sgItemUnCheckedTextBgColor;
    }

    public CharSequence getSgFontIconText() {
        return sgFontIconText;
    }

    public void setSgFontIconText(CharSequence sgFontIconText) {
        this.sgFontIconText = sgFontIconText;
    }

    public float getSgFontIconTextSize() {
        return sgFontIconTextSize;
    }

    public void setSgFontIconTextSize(float sgFontIconTextSize) {
        this.sgFontIconTextSize = sgFontIconTextSize;
    }

    public int getSgFontIconTextColor() {
        return sgFontIconTextColor;
    }

    public void setSgFontIconTextColor(int sgFontIconColor) {
        this.sgFontIconTextColor = sgFontIconColor;
    }

    public CharSequence getSgKitText() {
        return sgKitText;
    }

    public void setSgKitText(CharSequence sgKitText) {
        this.sgKitText = sgKitText;
    }

    public float getSgKitTextSize() {
        return sgKitTextSize;
    }

    public void setSgKitTextSize(float sgKitTextSize) {
        this.sgKitTextSize = sgKitTextSize;
    }

    public int getSgKitTextColor() {
        return sgKitTextColor;
    }

    public void setSgKitTextColor(int sgKitTextColor) {
        this.sgKitTextColor = sgKitTextColor;
    }

    public Typeface getSgKitTypeface() {
        return sgKitTypeface;
    }

    public void setSgKitTypeface(Typeface sgKitTitleTypeface) {
        this.sgKitTypeface = sgKitTitleTypeface;
    }

    public Rect getAtViewRect() {
        int[] locations = new int[2];
        atView.getLocationInWindow(locations);
        return new Rect(locations[0], locations[1], locations[0] + atView.getMeasuredWidth(),
                locations[1] + atView.getMeasuredHeight());
    }

    public Boolean getDismissOnBackPressed() {
        return isDismissOnBackPressed;
    }

    public void setDismissOnBackPressed(Boolean dismissOnBackPressed) {
        isDismissOnBackPressed = dismissOnBackPressed;
    }

    public Boolean getDismissOnTouchOutside() {
        return isDismissOnTouchOutside;
    }

    public void setDismissOnTouchOutside(Boolean dismissOnTouchOutside) {
        isDismissOnTouchOutside = dismissOnTouchOutside;
    }

    public Boolean getAutoDismiss() {
        return autoDismiss;
    }

    public void setAutoDismiss(Boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    public Boolean getHasShadowBg() {
        return hasShadowBg;
    }

    public void setHasShadowBg(Boolean hasShadowBg) {
        this.hasShadowBg = hasShadowBg;
    }

    public View getAtView() {
        return atView;
    }

    public void setAtView(View atView) {
        this.atView = atView;
    }

    public SGDropDownAnimation getSgDropDownAnimation() {
        return sgDropDownAnimation;
    }

    public void setSgDropDownAnimation(SGDropDownAnimation sgDropDownAnimation) {
        this.sgDropDownAnimation = sgDropDownAnimation;
    }

    public DropDownAnimator getCustomAnimator() {
        return customAnimator;
    }

    public void setCustomAnimator(DropDownAnimator customAnimator) {
        this.customAnimator = customAnimator;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getDropDownWidth() {
        return dropDownWidth;
    }

    public void setDropDownWidth(int dropDownWidth) {
        this.dropDownWidth = dropDownWidth;
    }

    public int getDropDownHeight() {
        return dropDownHeight;
    }

    public void setDropDownHeight(int dropDownHeight) {
        this.dropDownHeight = dropDownHeight;
    }

    public Boolean getAutoOpenSoftInput() {
        return autoOpenSoftInput;
    }

    public void setAutoOpenSoftInput(Boolean autoOpenSoftInput) {
        this.autoOpenSoftInput = autoOpenSoftInput;
    }

    public SGDropDownCallback getSgDropDownCallback() {
        return sgDropDownCallback;
    }

    public void setSgDropDownCallback(SGDropDownCallback sgDropDownCallback) {
        this.sgDropDownCallback = sgDropDownCallback;
    }

    public Boolean getMoveUpToKeyboard() {
        return isMoveUpToKeyboard;
    }

    public void setMoveUpToKeyboard(Boolean moveUpToKeyboard) {
        isMoveUpToKeyboard = moveUpToKeyboard;
    }

    public DropDownPosition getDropDownPosition() {
        return dropDownPosition;
    }

    public void setDropDownPosition(DropDownPosition dropDownPosition) {
        this.dropDownPosition = dropDownPosition;
    }

    public boolean isRequestFocus() {
        return isRequestFocus;
    }

    public void setRequestFocus(boolean requestFocus) {
        isRequestFocus = requestFocus;
    }

    public boolean isAutoFocusEditText() {
        return autoFocusEditText;
    }

    public void setAutoFocusEditText(boolean autoFocusEditText) {
        this.autoFocusEditText = autoFocusEditText;
    }

    public boolean isClickThrough() {
        return isClickThrough;
    }

    public void setClickThrough(boolean clickThrough) {
        isClickThrough = clickThrough;
    }

    public boolean isDestroyOnDismiss() {
        return isDestroyOnDismiss;
    }

    public void setDestroyOnDismiss(boolean destroyOnDismiss) {
        isDestroyOnDismiss = destroyOnDismiss;
    }

    public int getShadowBgColor() {
        return shadowBgColor;
    }

    public void setShadowBgColor(int shadowBgColor) {
        this.shadowBgColor = shadowBgColor;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public ArrayList<Rect> getNotDismissWhenTouchInArea() {
        return notDismissWhenTouchInArea;
    }

    public void setNotDismissWhenTouchInArea(ArrayList<Rect> notDismissWhenTouchInArea) {
        this.notDismissWhenTouchInArea = notDismissWhenTouchInArea;
    }

    public int getUseColumn() {
        return useColumn;
    }

    public void setUseColumn(int useColumn) {
        this.useColumn = useColumn;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public SGOnClickOptionListener getSGOnClickOptionListener() {
        return sgOnClickOptionListener;
    }

    public void setSGOnClickOptionListener(SGOnClickOptionListener sgOnClickOptionListener) {
        this.sgOnClickOptionListener = sgOnClickOptionListener;
    }

    public Lifecycle getHostLifecycle() {
        return hostLifecycle;
    }

    public void setHostLifecycle(Lifecycle hostLifecycle) {
        this.hostLifecycle = hostLifecycle;
    }
}
