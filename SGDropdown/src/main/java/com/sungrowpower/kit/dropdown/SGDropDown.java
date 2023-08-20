package com.sungrowpower.kit.dropdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.Lifecycle;


import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.base.SGBaseView;
import com.sungrowpower.kit.dropdown.base.SGDropDownInfo;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;

import java.util.ArrayList;


public class SGDropDown {
    private SGDropDown() { }

    /**
     * 全局弹窗的设置
     **/

    private static int animationDuration = 300;
    private static int navigationBarColor = 0;
    private static int shadowBgColor = Color.parseColor("#7F000000");
    public static int isLightStatusBar = 0; //大于0为true，小于0为false
    public static int isLightNavigationBar = 0; //大于0为true，小于0为false

    /**
     * 设置全局的背景阴影颜色
     * @param color
     */
    public static void setShadowBgColor(int color) {
        shadowBgColor = color;
    }
    public static int getShadowBgColor() {
        return shadowBgColor;
    }



    /**
     * 设置全局的导航栏栏背景颜色
     *
     * @param color
     */
    public static void setNavigationBarColor(int color) {
        navigationBarColor = color;
    }

    public static int getNavigationBarColor() {
        return navigationBarColor;
    }



    /**
     * 统一设置是否是亮色状态栏
     * @param isLight
     */
    public static void setIsLightStatusBar(boolean isLight) {
        isLightStatusBar = isLight ? 1 : -1;
    }

    /**
     * 统一设置是否是亮色导航栏
     * @param isLight
     */
    public static void setIsLightNavigationBar(boolean isLight) {
        isLightNavigationBar = isLight ? 1 : -1;
    }

    /**
     * 设置全局动画时长
     * @param duration
     */
    public static void setAnimationDuration(int duration) {
        if (duration >= 0) {
            animationDuration = duration;
        }
    }

    public static int getAnimationDuration() {
        return animationDuration;
    }

    /**
     * 在长按弹出弹窗后，能保证下层View不能滑动
     * @param v
     */
    public static PointF longClickPoint = null;
    public static void fixLongClick(View v){
        v.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    longClickPoint = new PointF(event.getRawX(), event.getRawY());
                }
                if("dropdown".equals(v.getTag()) && event.getAction()==MotionEvent.ACTION_MOVE){
                    //长按发送，阻断父View拦截
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    //长按结束，恢复阻断
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    v.setTag(null);
                }
                return false;
            }
        });
        v.setTag("dropdown");
    }

    public static class Builder {
        private final SGDropDownInfo SGDropDownInfo = new SGDropDownInfo();
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }


        /**
         * 设置按下返回键是否关闭弹窗，默认为true
         *
         * @param isDismissOnBackPressed
         * @return
         */
        public Builder dismissOnBackPressed(Boolean isDismissOnBackPressed) {
            this.SGDropDownInfo.isDismissOnBackPressed = isDismissOnBackPressed;
            return this;
        }

        /**
         * 设置点击弹窗外面是否关闭弹窗，默认为true
         *
         * @param isDismissOnTouchOutside
         * @return
         */
        public Builder dismissOnTouchOutside(Boolean isDismissOnTouchOutside) {
            this.SGDropDownInfo.isDismissOnTouchOutside = isDismissOnTouchOutside;
            return this;
        }

        /**
         * 设置当操作完毕后是否自动关闭弹窗，默认为true。比如：点击Confirm弹窗的确认按钮默认是关闭弹窗，如果为false，则不关闭
         *
         * @param autoDismiss
         * @return
         */
        public Builder autoDismiss(Boolean autoDismiss) {
            this.SGDropDownInfo.autoDismiss = autoDismiss;
            return this;
        }

        /**
         * 弹窗是否有半透明背景遮罩，默认是true
         *
         * @param hasShadowBg
         * @return
         */
        public Builder hasShadowBg(Boolean hasShadowBg) {
            this.SGDropDownInfo.hasShadowBg = hasShadowBg;
            return this;
        }

        /**
         * 是否设置背景为高斯模糊背景。默认为false
         *
         * @param hasBlurBg
         * @return
         */
        public Builder hasBlurBg(boolean hasBlurBg) {
            this.SGDropDownInfo.hasBlurBg = hasBlurBg;
            return this;
        }

        /**
         * 设置弹窗依附的View，Attach弹窗必须设置这个
         *
         * @param atView
         * @return
         */
        public Builder atView(View atView) {
            SGDropDownInfo.atView = atView;
            return this;
        }

        /**
         * 设置弹窗监视的View
         *
         * @param watchView
         * @return
         */
        public Builder watchView(View watchView) {
            watchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        SGDropDownInfo.touchPoint = new PointF(event.getRawX(), event.getRawY());
                    }
                    return false;
                }
            });
            return this;
        }

        /**
         * 为弹窗设置内置的动画器，默认情况下，已经为每种弹窗设置了效果最佳的动画器；如果你不喜欢，仍然可以修改。
         *
         * @param SGDropDownAnimation
         * @return
         */
        public Builder popupAnimation(SGDropDownAnimation SGDropDownAnimation) {
            this.SGDropDownInfo.SGDropDownAnimation = SGDropDownAnimation;
            return this;
        }

        /**
         * 自定义弹窗动画器
         *
         * @param customAnimator
         * @return
         */
        public Builder customAnimator(DropDownAnimator customAnimator) {
            this.SGDropDownInfo.customAnimator = customAnimator;
            return this;
        }

        /**
         * 设置高度，如果重写了弹窗的getPopupHeight，则以重写的为准
         * 并且受最大高度限制
         * @param height
         * @return
         */
        public Builder popupHeight(int height) {
            this.SGDropDownInfo.popupHeight = height;
            return this;
        }

        /**
         * 设置宽度，如果重写了弹窗的getPopupWidth，则以重写的为准
         * 并且受最大宽度限制
         * @param width
         * @return
         */
        public Builder popupWidth(int width) {
            this.SGDropDownInfo.popupWidth = width;
            return this;
        }

        /**
         * 设置最大宽度，如果重写了弹窗的getMaxWidth，则以重写的为准
         *
         * @param maxWidth
         * @return
         */
        public Builder maxWidth(int maxWidth) {
            this.SGDropDownInfo.maxWidth = maxWidth;
            return this;
        }

        /**
         * 设置最大高度，如果重写了弹窗的getMaxHeight，则以重写的为准
         *
         * @param maxHeight
         * @return
         */
        public Builder maxHeight(int maxHeight) {
            this.SGDropDownInfo.maxHeight = maxHeight;
            return this;
        }


        /**
         * 是否自动打开输入法，当弹窗包含输入框时很有用，默认为false
         *
         * @param autoOpenSoftInput
         * @return
         */
        public Builder autoOpenSoftInput(Boolean autoOpenSoftInput) {
            this.SGDropDownInfo.autoOpenSoftInput = autoOpenSoftInput;
            return this;
        }

        /**
         * 当弹出输入法时，弹窗是否要移动到输入法之上，默认为true。如果不移动，弹窗很有可能被输入法盖住
         *
         * @param isMoveUpToKeyboard
         * @return
         */
        public Builder moveUpToKeyboard(Boolean isMoveUpToKeyboard) {
            this.SGDropDownInfo.isMoveUpToKeyboard = isMoveUpToKeyboard;
            return this;
        }

        /**
         * 设置弹窗出现在目标的什么位置，有四种取值：Left，Right，Top，Bottom。这种手动设置位置的行为
         * 只对Attach弹窗和Drawer弹窗生效。
         *
         * @param dropDownPosition
         * @return
         */
        public Builder popupPosition(DropDownPosition dropDownPosition) {
            this.SGDropDownInfo.dropDownPosition = dropDownPosition;
            return this;
        }

        /**
         * 设置是否给StatusBar添加阴影，目前对Drawer弹窗和全屏弹窗生效生效。
         *
         * @param hasStatusBarShadow
         * @return
         */
        public Builder hasStatusBarShadow(boolean hasStatusBarShadow) {
            this.SGDropDownInfo.hasStatusBarShadow = hasStatusBarShadow;
            return this;
        }

        /**
         * 设置是否显示状态栏，默认是显示的。如果你希望弹窗隐藏状态栏，就设置为true;
         * 只在dialog模式下有效
         * @param hasStatusBar
         * @return
         */
        public Builder hasStatusBar(boolean hasStatusBar) {
            this.SGDropDownInfo.hasStatusBar = hasStatusBar;
            return this;
        }

        /**
         * 设置是否显示导航栏，默认是显示的。如果你希望弹窗隐藏导航栏，就设置为true
         * 只在dialog模式下有效
         * @param hasNavigationBar
         * @return
         */
        public Builder hasNavigationBar(boolean hasNavigationBar) {
            this.SGDropDownInfo.hasNavigationBar = hasNavigationBar;
            return this;
        }

        /**
         * 设置导航栏的颜色，如果你希望弹窗修改导航栏的颜色的时候用；
         * 只在dialog模式下有效
         * @param navigationBarColor
         * @return
         */
        public Builder navigationBarColor(int navigationBarColor) {
            this.SGDropDownInfo.navigationBarColor = navigationBarColor;
            return this;
        }

        /**
         * 设置导航栏是否是亮色，默认false
         * 只在dialog模式下有效
         * @param isLightNavigationBar
         * @return
         */
        public Builder isLightNavigationBar(boolean isLightNavigationBar) {
            this.SGDropDownInfo.isLightNavigationBar = isLightNavigationBar ? 1 : -1;
            return this;
        }

        /**
         * 设置状态栏是否是亮色，默认false
         * 只在dialog模式下有效
         * @param isLightStatusBar
         * @return
         */
        public Builder isLightStatusBar(boolean isLightStatusBar) {
            this.SGDropDownInfo.isLightStatusBar = isLightStatusBar ? 1 : -1;
            return this;
        }

        /**
         * 设置状态栏的背景颜色，目前只对全屏弹窗和Drawer弹窗有效，其他弹窗
         * Dropdown强制将状态栏设置为透明
         * @param statusBarBgColor
         * @return
         */
        public Builder statusBarBgColor(int statusBarBgColor) {
            this.SGDropDownInfo.statusBarBgColor = statusBarBgColor;
            return this;
        }

        /**
         * 弹窗在x方向的偏移量，对所有弹窗生效，单位是px
         *
         * @param offsetX
         * @return
         */
        public Builder offsetX(int offsetX) {
            this.SGDropDownInfo.offsetX = offsetX;
            return this;
        }

        /**
         * 弹窗在y方向的偏移量，对所有弹窗生效，单位是px
         *
         * @param offsetY
         * @return
         */
        public Builder offsetY(int offsetY) {
            this.SGDropDownInfo.offsetY = offsetY;
            return this;
        }

        /**
         * 是否启用拖拽，比如：Bottom弹窗默认是带手势拖拽效果的，如果禁用则不能拖拽
         *
         * @param enableDrag
         * @return
         */
        public Builder enableDrag(boolean enableDrag) {
            this.SGDropDownInfo.enableDrag = enableDrag;
            return this;
        }

        /**
         * 是否与目标水平居中，比如：默认情况下Attach弹窗依靠着目标的左边或者右边，如果isCenterHorizontal为true，则与目标水平居中对齐
         *
         * @param isCenterHorizontal
         * @return
         */
        public Builder isCenterHorizontal(boolean isCenterHorizontal) {
            this.SGDropDownInfo.isCenterHorizontal = isCenterHorizontal;
            return this;
        }

        /**
         * 是否抢占焦点，默认情况下弹窗会抢占焦点，目的是为了能处理返回按键事件。如果为false，则不在抢焦点，但也无法响应返回按键了
         *
         * @param isRequestFocus 默认为true
         * @return
         */
        public Builder isRequestFocus(boolean isRequestFocus) {
            this.SGDropDownInfo.isRequestFocus = isRequestFocus;
            return this;
        }

        /**
         * 是否让弹窗内的输入框自动获取焦点，默认是true。弹窗内有输入法的情况下该设置才有效
         *
         * @param autoFocusEditText
         * @return
         */
        public Builder autoFocusEditText(boolean autoFocusEditText) {
            this.SGDropDownInfo.autoFocusEditText = autoFocusEditText;
            return this;
        }

        /**
         * 是否使用暗色主题，默认是false。对所有内置弹窗生效。
         *
         * @param isDarkTheme
         * @return
         */
        public Builder isDarkTheme(boolean isDarkTheme) {
            this.SGDropDownInfo.isDarkTheme = isDarkTheme;
            return this;
        }

        /**
         * 是否点击弹窗背景时将点击事件透传到Activity下，默认是false。目前对Center弹窗，Attach弹窗，
         * Position弹窗，PartShadow弹窗生效；对Drawer弹窗，FullScreen弹窗，Bottom弹窗不生效（未开放功能）
         *
         * @param isClickThrough
         * @return
         */
        public Builder isClickThrough(boolean isClickThrough) {
            this.SGDropDownInfo.isClickThrough = isClickThrough;
            return this;
        }

        /**
         * 是否允许应用在后台的时候也能弹出弹窗，默认是false。注意如果开启这个开关，需要申请悬浮窗权限才能生效。
         * 直接使用 Dropdown.requestOverlayPermission()即可申请
         * @param enableShowWhenAppBackground
         * @return
         */
        public Builder enableShowWhenAppBackground(boolean enableShowWhenAppBackground) {
            this.SGDropDownInfo.enableShowWhenAppBackground = enableShowWhenAppBackground;
            return this;
        }

        /**
         * 是否开启三阶拖拽效果，想高德地图上面的弹窗那样可以拖拽的效果
         *
         * @param isThreeDrag
         * @return
         */
        public Builder isThreeDrag(boolean isThreeDrag) {
            this.SGDropDownInfo.isThreeDrag = isThreeDrag;
            return this;
        }

        /**
         * 是否在弹窗消失后就立即释放资源，杜绝内存泄漏，仅仅适用于弹窗只用一次的场景，默认为false。
         * 如果你的弹窗对象需是复用的，千万不要开启这个设置
         *
         * @param isDestroyOnDismiss
         * @return
         */
        public Builder isDestroyOnDismiss(boolean isDestroyOnDismiss) {
            this.SGDropDownInfo.isDestroyOnDismiss = isDestroyOnDismiss;
            return this;
        }

        /**
         * 设置圆角，对所有内置弹窗有效
         *
         * @param borderRadius
         * @return
         */
        public Builder borderRadius(float borderRadius) {
            this.SGDropDownInfo.borderRadius = borderRadius;
            return this;
        }

        /**
         * 是否以屏幕中心进行定位，默认是false，为false时根据Material范式进行定位，主要影响Attach系列弹窗
         * Material范式下是：
         *      弹窗优先显示在目标下方，下方距离不够才显示在上方
         * 以屏幕中心进行定位：
         *      目标在屏幕上半方弹窗显示在目标下面，目标在屏幕下半方则弹窗显示在目标上面
         *
         * @param positionByWindowCenter
         * @return
         */
        public Builder positionByWindowCenter(boolean positionByWindowCenter) {
            this.SGDropDownInfo.positionByWindowCenter = positionByWindowCenter;
            return this;
        }

        /**
         * Dropdown的弹窗默认是Dialog实现，该方法设置为true则切换为View实现，两者区别如下：
         * 1. Dialog实现，独立Window渲染，性能是View实现的2倍以上，但部分与输入法交互效果无法做到，
         *    比如根据输入进行联想搜索的场景，因为输入法也是一个Dialog，Android中无法实现2个Dialog同时获取焦点，
         *    而设置为View模式即可轻松实现；
         *    但是Dialog实现有个缺陷是弹窗内部无法使用Fragment，这是Android的限制；
         *    Dialog的层级高，会覆盖View层
         * 2. View实现本质是把弹窗挂载到Activity的decorView上面，由于还是View，所以很多与输入法的交互都能实现；
         *    View实现内部完全可以使用Fragment；
         *    缺点是和Activity相同渲染线程，性能比Dialog低
         *
         * @param viewMode 是否是View实现，默认是false
         * @return
         */
        public Builder isViewMode(boolean viewMode) {
            this.SGDropDownInfo.isViewMode = viewMode;
            return this;
        }

        /**
         * 半透明阴影的颜色
         * @param shadowBgColor
         * @return
         */
        public Builder shadowBgColor(int shadowBgColor) {
            this.SGDropDownInfo.shadowBgColor = shadowBgColor;
            return this;
        }

        /**
         * 动画时长
         * @param animationDuration
         * @return
         */
        public Builder animationDuration(int animationDuration) {
            this.SGDropDownInfo.animationDuration = animationDuration;
            return this;
        }

        /**
         * 是否保持屏幕常亮，默认false
         * @param keepScreenOn
         * @return
         */
        public Builder keepScreenOn(boolean keepScreenOn) {
            this.SGDropDownInfo.keepScreenOn = keepScreenOn;
            return this;
        }

        /**
         * 开启dismissOnTouchOutside(true)时，即使触摸在指定View时也不消失；
         * 该方法可调用多次，每次可添加一个Rect区域
         * @param view 触摸View
         * @return
         */
        public Builder notDismissWhenTouchInView(View view) {
            if(this.SGDropDownInfo.notDismissWhenTouchInArea==null){
                this.SGDropDownInfo.notDismissWhenTouchInArea = new ArrayList<>();
            }
            this.SGDropDownInfo.notDismissWhenTouchInArea.add(SGDropDownUtils.getViewRect(view));
            return this;
        }

        /**
         * 默认情况下Dropdown监视Activity的生命周期，对于Fragment(或其他任意拥有Lifecycle的组件)实现的UI，可以传入Fragment
         * 的Lifecycle，从而实现在Fragment销毁时弹窗也自动销毁，无需手动调用dismiss()和destroy()
         * @param lifecycle 自定义UI的生命周期
         * @return
         */
        public Builder customHostLifecycle(Lifecycle lifecycle) {
            this.SGDropDownInfo.hostLifecycle = lifecycle;
            return this;
        }

        /**
         * 设置弹窗显示和隐藏的回调监听
         *
         * @param SGDropDownCallback
         * @return
         */
        public Builder setPopupCallback(SGDropDownCallback SGDropDownCallback) {
            this.SGDropDownInfo.SGDropDownCallback = SGDropDownCallback;
            return this;
        }

        /****************************************** 便捷方法 ****************************************/




        public SGBaseView asCustom(SGBaseView popupView) {
//            if (popupView instanceof CenterPopupView) {
//                popupType(PopupType.Center);
//            } else if (popupView instanceof BottomPopupView) {
//                popupType(PopupType.Bottom);
//            } else if (popupView instanceof AttachPopupView) {
//                popupType(PopupType.AttachView);
//            } else if (popupView instanceof ImageViewerPopupView) {
//                popupType(PopupType.ImageViewer);
//            } else if (popupView instanceof PositionPopupView) {
//                popupType(PopupType.Position);
//            }
            popupView.SGDropDownInfo = this.SGDropDownInfo;
            return popupView;
        }

//        public BasePopupView popupView() {
//            CustomPartShadowPopupView customPartShadowPopupView=new CustomPartShadowPopupView(context);
//            customPartShadowPopupView.popupInfo=this.popupInfo;
//            return customPartShadowPopupView;
//        }
    }


}
