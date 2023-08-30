package com.sungrowpower.kit.dropdown.animator;

import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;


/**
 * Description: 平移动画
 * Create by hyk
 */
public class TranslateAlphaAnimator extends DropDownAnimator {
    //动画起始坐标
    private float startTranslationX, startTranslationY;
    private float defTranslationX, defTranslationY;

    public TranslateAlphaAnimator(View target, int animationDuration, SGDropDownAnimation SGDropDownAnimation) {
        super(target, animationDuration, SGDropDownAnimation);
    }

    @Override
    public void initAnimator() {
        defTranslationX = targetView.getTranslationX();
        defTranslationY = targetView.getTranslationY();

        targetView.setAlpha(0);
        // 设置移动坐标
        applyTranslation();
        startTranslationX = targetView.getTranslationX();
        startTranslationY = targetView.getTranslationY();
    }

    private void applyTranslation() {
        switch (SGDropDownAnimation) {
            case TranslateAlphaFromLeft:
                targetView.setTranslationX(-(targetView.getMeasuredWidth()/* + halfWidthOffset*/));
                break;
            case TranslateAlphaFromTop:
                targetView.setTranslationY(-(targetView.getMeasuredHeight() /*+ halfHeightOffset*/));
                break;
            case TranslateAlphaFromRight:
                targetView.setTranslationX(targetView.getMeasuredWidth() /*+ halfWidthOffset*/);
                break;
            case TranslateAlphaFromBottom:
                targetView.setTranslationY(targetView.getMeasuredHeight() /*+ halfHeightOffset*/);
                break;
        }
    }

    @Override
    public void animateShow() {
        targetView.animate().translationX(defTranslationX).translationY(defTranslationY).alpha(1f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animationDuration)
                .withLayer()
                .start();
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        observerAnimator(targetView.animate().translationX(startTranslationX).translationY(startTranslationY).alpha(0f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(animationDuration)
                .withLayer())
                .start();
    }
}
