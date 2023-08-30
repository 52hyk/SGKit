package com.sungrowpower.kit.dropdown.animator;

import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;


/**
 * Description: 缩放透明
 * Create by hyk
 */
public class ScaleAlphaAnimator extends DropDownAnimator {
    public ScaleAlphaAnimator(View target, int animationDuration, SGDropDownAnimation SGDropDownAnimation) {
        super(target, animationDuration, SGDropDownAnimation);
    }

    float startScale = .85f;

    @Override
    public void initAnimator() {
        targetView.setScaleX(startScale);
        targetView.setScaleY(startScale);
        targetView.setAlpha(0);

        // 设置动画参考点
        targetView.post(new Runnable() {
            @Override
            public void run() {
                applyPivot();
            }
        });
    }

    /**
     * 根据不同的DropDownAnimation来设定对应的pivot
     */
    private void applyPivot() {
        switch (SGDropDownAnimation) {
            case ScaleAlphaFromCenter:
                targetView.setPivotX(targetView.getMeasuredWidth() / 2f);
                targetView.setPivotY(targetView.getMeasuredHeight() / 2f);
                break;
            case ScaleAlphaFromLeftTop:
                targetView.setPivotX(0);
                targetView.setPivotY(0);
                break;
            case ScaleAlphaFromRightTop:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(0f);
                break;
            case ScaleAlphaFromLeftBottom:
                targetView.setPivotX(0f);
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
            case ScaleAlphaFromRightBottom:
                targetView.setPivotX(targetView.getMeasuredWidth());
                targetView.setPivotY(targetView.getMeasuredHeight());
                break;
        }

    }

    @Override
    public void animateShow() {
        targetView.post(new Runnable() {
            @Override
            public void run() {
                targetView.animate().scaleX(1f).scaleY(1f).alpha(1f)
                        .setDuration(animationDuration)
                        .setInterpolator(new OvershootInterpolator(1f))
//                .withLayer() 在部分6.0系统会引起crash
                        .start();
            }
        });
    }

    @Override
    public void animateDismiss() {
        if (animating) {
            return;
        }
        observerAnimator(targetView.animate().scaleX(startScale).scaleY(startScale).alpha(0f).setDuration(animationDuration)
                .setInterpolator(new FastOutSlowInInterpolator()))
//                .withLayer() 在部分6.0系统会引起crash
                .start();
    }

}
