package com.sungrowpower.kit.dropdown.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;


/**
 * Description: 弹窗动画执行器
 * Create by hyk
 */
public abstract class DropDownAnimator {
    protected boolean animating = false;
    public View targetView;
    public int animationDuration = 0;
    public SGDropDownAnimation SGDropDownAnimation; // 内置的动画

    public DropDownAnimator() {
    }

    public DropDownAnimator(View target, int animationDuration) {
        this(target, animationDuration, null);
    }

    public DropDownAnimator(View target, int animationDuration, SGDropDownAnimation SGDropDownAnimation) {
        this.targetView = target;
        this.animationDuration = animationDuration;
        this.SGDropDownAnimation = SGDropDownAnimation;
    }

    public abstract void initAnimator();

    public abstract void animateShow();

    public abstract void animateDismiss();

    public int getDuration() {
        return animationDuration;
    }

    protected ValueAnimator observerAnimator(ValueAnimator animator) {
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
            }
        });
        return animator;
    }

    protected ViewPropertyAnimator observerAnimator(ViewPropertyAnimator animator) {
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
            }
        });
        return animator;
    }
}
