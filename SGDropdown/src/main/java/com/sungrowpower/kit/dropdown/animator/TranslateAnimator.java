package com.sungrowpower.kit.dropdown.animator;

import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;


/**
 * Description: 平移动画，不带渐变
 * Create by hyk
 */
public class TranslateAnimator extends DropDownAnimator {
    private float startTranslationX, startTranslationY;
    private float endTranslationX, endTranslationY;

    public TranslateAnimator(View target, int animationDuration, SGDropDownAnimation SGDropDownAnimation) {
        super(target, animationDuration, SGDropDownAnimation);
    }

    @Override
    public void initAnimator() {
        endTranslationX = targetView.getTranslationX();
        endTranslationY = targetView.getTranslationY();
        // 设置起始坐标
        applyTranslation();
        startTranslationX = targetView.getTranslationX();
        startTranslationY = targetView.getTranslationY();
    }

    private void applyTranslation() {
        switch (SGDropDownAnimation) {
            case TranslateFromLeft:
                targetView.setTranslationX(-targetView.getRight());
                break;
            case TranslateFromTop:
                targetView.setTranslationY(-targetView.getBottom());
                break;
            case TranslateFromRight:
                targetView.setTranslationX(((View) targetView.getParent()).getMeasuredWidth() - targetView.getLeft());
                break;
            case TranslateFromBottom:
                targetView.setTranslationY(((View) targetView.getParent()).getMeasuredHeight() - targetView.getTop());
                break;
        }
    }

    @Override
    public void animateShow() {
        ViewPropertyAnimator animator = null;
        switch (SGDropDownAnimation) {
            case TranslateFromLeft:
            case TranslateFromRight:
                animator = targetView.animate().translationX(endTranslationX);
                break;
            case TranslateFromTop:
            case TranslateFromBottom:
                animator = targetView.animate().translationY(endTranslationY);
                break;
        }
        if(animator!=null) {
            animator.setInterpolator(new FastOutSlowInInterpolator())
                    .setDuration(animationDuration)
                    .withLayer()
                    .start();
        }
    }

    @Override
    public void animateDismiss() {
        if(animating) {
            return;
        }
        ViewPropertyAnimator animator = null;
        switch (SGDropDownAnimation) {
            case TranslateFromLeft:
                startTranslationX = -targetView.getRight();
                animator = targetView.animate().translationX(startTranslationX);
                break;
            case TranslateFromTop:
                startTranslationY = -targetView.getBottom();
                animator = targetView.animate().translationY(startTranslationY);
                break;
            case TranslateFromRight:
                startTranslationX = ((View) targetView.getParent()).getMeasuredWidth() - targetView.getLeft();
                animator = targetView.animate().translationX(startTranslationX);
                break;
            case TranslateFromBottom:
                startTranslationY = ((View) targetView.getParent()).getMeasuredHeight() - targetView.getTop();
                animator = targetView.animate().translationY(startTranslationY);
                break;
        }
        if(animator!=null) {
            observerAnimator(animator.setInterpolator(new FastOutSlowInInterpolator())
                    .setDuration((long) (animationDuration*.8))
                    .withLayer())
                    .start();
        }
    }
}
