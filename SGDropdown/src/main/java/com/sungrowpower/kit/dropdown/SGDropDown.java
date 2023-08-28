package com.sungrowpower.kit.dropdown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.base.SGBaseView;
import com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback;
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOptionListener;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;
import com.sungrowpower.kit.dropdown.view.SGBuiltDropDownView;

import java.util.ArrayList;
import java.util.List;


public class SGDropDown {
    private SGDropDown() {
    }

    /**
     * 全局弹窗的设置
     **/
    //动画时间
    private static int animationDuration = 300;
    //50%的透明度
    private static int shadowBgColor = Color.parseColor("#7F000000");

    /**
     * 设置全局的背景阴影颜色
     *
     * @param color
     */
    public static void setShadowBgColor(int color) {
        shadowBgColor = color;
    }

    public static int getShadowBgColor() {
        return shadowBgColor;
    }


    /**
     * 设置全局动画时长
     *
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
    private final SGDropDownInfoBean sgDropDownInfoBean = new SGDropDownInfoBean();


    public static class Builder {
        private  SGDropDownInfoBean sgDropDownInfoBean = new SGDropDownInfoBean();
        private Context context;

        public Builder(Context context) {
            this.context = context;
//            SGKit.getInstance().setContext(context);

        }


        /**
         * 设置按下返回键是否关闭弹窗，默认为true
         *
         * @param isDismissOnBackPressed
         * @return
         */
        public Builder dismissOnBackPressed(Boolean isDismissOnBackPressed) {
            this.sgDropDownInfoBean.setDismissOnBackPressed(isDismissOnBackPressed);
            return this;
        }

        /**
         * 设置点击弹窗外面是否关闭弹窗，默认为true
         *
         * @param isDismissOnTouchOutside
         * @return
         */
        public Builder dismissOnTouchOutside(Boolean isDismissOnTouchOutside) {
            this.sgDropDownInfoBean.setDismissOnTouchOutside(isDismissOnTouchOutside);
            return this;
        }

        /**
         * 设置当操作完毕后是否自动关闭弹窗，默认为true。比如：点击Confirm弹窗的确认按钮默认是关闭弹窗，如果为false，则不关闭
         *
         * @param autoDismiss
         * @return
         */
        public Builder autoDismiss(Boolean autoDismiss) {
            this.sgDropDownInfoBean.setAutoDismiss(autoDismiss) ;
            return this;
        }

        /**
         * 弹窗是否有半透明背景遮罩，默认是true
         *
         * @param hasShadowBg
         * @return
         */
        public Builder hasShadowBg(Boolean hasShadowBg) {
            this.sgDropDownInfoBean.setHasShadowBg(hasShadowBg) ;
            return this;
        }


        /**
         * 设置弹窗依附的View，Attach弹窗必须设置这个
         *
         * @param atView
         * @return
         */
        public Builder atView(View atView) {
            sgDropDownInfoBean.setAtView(atView);
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
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        sgDropDownInfoBean.setTouchPoint(new PointF(event.getRawX(), event.getRawY())) ;
                    }
                    return false;
                }
            });
            return this;
        }

        /**
         * 为弹窗设置内置的动画器，默认情况下，已经为每种弹窗设置了效果最佳的动画器；如果你不喜欢，仍然可以修改。
         *
         * @param sgDropDownAnimation
         * @return
         */
        public Builder dropDownAnimation(SGDropDownAnimation sgDropDownAnimation) {
            this.sgDropDownInfoBean.setSgDropDownAnimation(sgDropDownAnimation);
            return this;
        }

        /**
         * 自定义弹窗动画器
         *
         * @param customAnimator
         * @return
         */
        public Builder customAnimator(DropDownAnimator customAnimator) {
            this.sgDropDownInfoBean.setCustomAnimator(customAnimator) ;
            return this;
        }

        /**
         * 设置高度，如果重写了弹窗的getDropDownHeight，则以重写的为准
         * 并且受最大高度限制
         *
         * @param height
         * @return
         */
        public Builder dropDownHeight(int height) {
            this.sgDropDownInfoBean.setDropDownHeight(height);
            return this;
        }

        /**
         * 设置宽度，如果重写了弹窗的getDropDownWidth，则以重写的为准
         * 并且受最大宽度限制
         *
         * @param width
         * @return
         */
        public Builder dropDownWidth(int width) {
            this.sgDropDownInfoBean.setDropDownWidth(width);
            return this;
        }

        /**
         * 设置最大宽度，如果重写了弹窗的getMaxWidth，则以重写的为准
         *
         * @param maxWidth
         * @return
         */
        public Builder maxWidth(int maxWidth) {
            this.sgDropDownInfoBean.setMaxWidth(maxWidth);
            return this;
        }

        /**
         * 设置最大高度，如果重写了弹窗的getMaxHeight，则以重写的为准
         *
         * @param maxHeight
         * @return
         */
        public Builder maxHeight(int maxHeight) {
            this.sgDropDownInfoBean.setMaxHeight(maxHeight);
            return this;
        }


        /**
         * 是否自动打开输入法，当弹窗包含输入框时很有用，默认为false
         *
         * @param autoOpenSoftInput
         * @return
         */
        public Builder autoOpenSoftInput(Boolean autoOpenSoftInput) {
            this.sgDropDownInfoBean.setAutoOpenSoftInput(autoOpenSoftInput);
            return this;
        }

        /**
         * 当弹出输入法时，弹窗是否要移动到输入法之上，默认为true。如果不移动，弹窗很有可能被输入法盖住
         *
         * @param isMoveUpToKeyboard
         * @return
         */
        public Builder moveUpToKeyboard(Boolean isMoveUpToKeyboard) {
            this.sgDropDownInfoBean.setMoveUpToKeyboard(isMoveUpToKeyboard);
            return this;
        }

        /**
         * 设置弹窗出现在目标的什么位置，有两种取值：Top，Bottom。这种手动设置位置的行为
         * @param dropDownPosition
         * @return
         */
        public Builder dropDownPosition(DropDownPosition dropDownPosition) {
            this.sgDropDownInfoBean.setDropDownPosition(dropDownPosition) ;
            return this;
        }


        /**
         * 是否抢占焦点，默认情况下弹窗会抢占焦点，目的是为了能处理返回按键事件。如果为false，则不在抢焦点，但也无法响应返回按键了
         *
         * @param isRequestFocus 默认为true
         * @return
         */
        public Builder isRequestFocus(boolean isRequestFocus) {
            this.sgDropDownInfoBean.setRequestFocus(isRequestFocus);
            return this;
        }

        /**
         * 是否让弹窗内的输入框自动获取焦点，默认是true。弹窗内有输入法的情况下该设置才有效
         *
         * @param autoFocusEditText
         * @return
         */
        public Builder autoFocusEditText(boolean autoFocusEditText) {
            this.sgDropDownInfoBean.setAutoFocusEditText(autoFocusEditText);
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
            this.sgDropDownInfoBean.setClickThrough(isClickThrough);
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
            this.sgDropDownInfoBean.setDestroyOnDismiss(isDestroyOnDismiss);
            return this;
        }


        /**
         * 半透明阴影的颜色
         *
         * @param shadowBgColor
         * @return
         */
        public Builder shadowBgColor(int shadowBgColor) {
            this.sgDropDownInfoBean.setShadowBgColor(shadowBgColor);
            return this;
        }

        /**
         * 动画时长
         *
         * @param animationDuration
         * @return
         */
        public Builder animationDuration(int animationDuration) {
            this.sgDropDownInfoBean.setAnimationDuration(animationDuration);
            return this;
        }

        /**
         * 开启dismissOnTouchOutside(true)时，即使触摸在指定View时也不消失；
         * 该方法可调用多次，每次可添加一个Rect区域
         *
         * @param view 触摸View
         * @return
         */
        public Builder notDismissWhenTouchInView(View view) {
            if (this.sgDropDownInfoBean.getNotDismissWhenTouchInArea() == null) {
                this.sgDropDownInfoBean.setNotDismissWhenTouchInArea(new ArrayList<>());
            }
            this.sgDropDownInfoBean.getNotDismissWhenTouchInArea().add(SGDropDownUtils.getViewRect(view));
            return this;
        }

        /**
         * 默认情况下Dropdown监视Activity的生命周期，对于Fragment(或其他任意拥有Lifecycle的组件)实现的UI，可以传入Fragment
         * 的Lifecycle，从而实现在Fragment销毁时弹窗也自动销毁，无需手动调用dismiss()和destroy()
         *
         * @param lifecycle 自定义UI的生命周期
         * @return
         */
        public Builder customHostLifecycle(Lifecycle lifecycle) {
            this.sgDropDownInfoBean.setHostLifecycle(lifecycle);
            return this;
        }

        /**
         * 设置弹窗显示和隐藏的回调监听
         *
         * @param sgDropDownCallback
         * @return
         */
        public Builder setDropDownViewCallback(SGDropDownCallback sgDropDownCallback) {
            this.sgDropDownInfoBean.setSgDropDownCallback(sgDropDownCallback);
            return this;
        }

        /**
         *置弹数据点击、变化等监听
         */
        public Builder setOnClickOptionListener( SGOnClickOptionListener sgOnClickOptionListener) {
            this.sgDropDownInfoBean.setSGOnClickOptionListener(sgOnClickOptionListener);
            return this;
        }


        /**
         * 设置多列
         *
         * @param useColumn
         * @return
         */
        public Builder setUseColumn(int useColumn) {
            this.sgDropDownInfoBean.setUseColumn(useColumn) ;
            return this;
        }

        /**
         * 设置数据集合
         * @param dataBean
         * @return
         */
        public Builder setOptions(List<Object> dataBean) {
            this.sgDropDownInfoBean.setOptions(dataBean);
            return this;
        }

        /**
         * 是否支持多选
         * @param multiple
         * @return
         */
        public Builder setMultiple(Boolean multiple){
            this.sgDropDownInfoBean.setMultiple(multiple);
            return this;
        }

        /**
         * 设置TextView文本内容
         * @param sgKitText
         * @return
         */
        public Builder setSgKitText(CharSequence sgKitText){
            this.sgDropDownInfoBean.setSgKitText(sgKitText);
            return this;
        }
        /**
         * 设置文TextView颜色
         * @param sgKitTextColor
         * @return
         */
        public Builder setSgKitTextColor(int sgKitTextColor){
            this.sgDropDownInfoBean.setSgKitTextColor(sgKitTextColor);
            return this;
        }
        /**
         * 设置文TextView的文字大小
         * @param sgKitTextSize
         * @return
         */
        public Builder setSgKitTextSize(float sgKitTextSize){
            this.sgDropDownInfoBean.setSgKitTextSize(sgKitTextSize);
            return this;
        }

        /**
         * 设置TextView的字体
         * @param sgKitTypeface
         * @return
         */
        public Builder setSgKitText(Typeface sgKitTypeface){
            this.sgDropDownInfoBean.setSgKitTypeface(sgKitTypeface);
            return this;
        }


        public SGBaseView customView(SGBaseView dropDownView) {

            dropDownView.sgDropDownInfoBean = this.sgDropDownInfoBean;
            return dropDownView;
        }

        public SGBaseView dropDownView() {
            SGBuiltDropDownView sgBuiltDropDownView=new SGBuiltDropDownView(context);
            sgBuiltDropDownView.sgDropDownInfoBean=this.sgDropDownInfoBean;
            return sgBuiltDropDownView;
        }

        public Builder setDropDownAttrs(SGDropDownInfoBean sgDropDownInfoBean) {
            this.sgDropDownInfoBean=sgDropDownInfoBean;
            return this;
        }
    }


}
