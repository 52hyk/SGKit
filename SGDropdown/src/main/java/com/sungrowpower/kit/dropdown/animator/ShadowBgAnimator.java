package com.sungrowpower.kit.dropdown.animator;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Description: 背景Shadow动画器，负责执行半透明的渐入渐出动画
 * Create by hyk
 */
public class ShadowBgAnimator extends DropDownAnimator {
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    public int startColor = Color.TRANSPARENT;
    public boolean isZeroDuration = false;
    public int shadowColor;
    public ShadowBgAnimator(View target, int animationDuration, int shadowColor) {
        super(target, animationDuration);
        this.shadowColor = shadowColor;
    }
    public ShadowBgAnimator() {}
    @Override
    public void initAnimator() {
        targetView.setBackgroundColor(startColor);
    }

    @Override
    public void animateShow() {
        //属性动画
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, startColor, shadowColor);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration? 0: animationDuration).start();
    }

    @Override
    public void animateDismiss() {
        if(animating) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofObject(argbEvaluator, shadowColor, startColor);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                targetView.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
        observerAnimator(animator);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setDuration(isZeroDuration ? 0: animationDuration).start();
    }

    public void applyColorValue(float val){
        targetView.setBackgroundColor((Integer) calculateBgColor(val));
    }

    public int calculateBgColor(float fraction){
        return (int) argbEvaluator.evaluate(fraction, startColor, shadowColor);
    }

}
