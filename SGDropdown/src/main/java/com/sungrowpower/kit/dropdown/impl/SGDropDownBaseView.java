package com.sungrowpower.kit.dropdown.impl;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.sungrowpower.kit.R;
import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.animator.TranslateAnimator;
import com.sungrowpower.kit.dropdown.base.SGBaseView;
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation;
import com.sungrowpower.kit.dropdown.enums.DropDownPosition;
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOutsideListener;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;
import com.sungrowpower.kit.dropdown.widget.SGDropDownContainer;


/**
 * Description: 下拉筛选弹窗
 * Create by hyk
 */
public abstract class SGDropDownBaseView extends SGBaseView {
    protected SGDropDownContainer attachPopupContainer;
    public SGDropDownBaseView(@NonNull Context context) {
        super(context);
        attachPopupContainer = findViewById(R.id.attachPopupContainer);
    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._sg_dropdown_base_view;
    }
    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), attachPopupContainer, false);
        attachPopupContainer.addView(contentView);
    }

    @Override
    protected void initPopupContent() {
        if (attachPopupContainer.getChildCount() == 0) {
            addInnerContent();
        }
        // 指定阴影动画的目标View
        if (SGDropDownInfo.hasShadowBg) {
            shadowBgAnimator.targetView = getPopupContentView();
        }
//        getPopupContentView().setTranslationY(SGDropDownInfo.offsetY);
//        getPopupImplView().setTranslationX(SGDropDownInfo.offsetX);
//        getPopupImplView().setTranslationY(0f);
//        getPopupImplView().setVisibility(INVISIBLE);
        SGDropDownUtils.applyDropDownSize((ViewGroup) getPopupContentView(), getMaxWidth(), getMaxHeight(),
                getPopupWidth(), getPopupHeight(), new Runnable() {
            @Override
            public void run() {
                doAttach();
            }
        });
    }

    private void initAndStartAnimation(){
        initAnimator();
        doShowAnimation();
        doAfterShow();
    }

    public boolean isShowUp;
    public void doAttach() {
        if (SGDropDownInfo.atView == null) {
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");
        }

        //1. apply width and height
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getPopupContentView().getLayoutParams();
        params.width = getMeasuredWidth();


        //1. 获取atView在屏幕上的位置
        Rect rect = SGDropDownInfo.getAtViewRect();
        rect.left -= getActivityContentLeft();
        rect.right -= getActivityContentLeft();

        //水平居中
      /*  if (SGDropDownInfo.isCenterHorizontal && getPopupImplView() != null) {

            //参考目标View居中，而不是屏幕居中
            int tx = (rect.left + rect.right)/2 - getPopupImplView().getMeasuredWidth()/2;
            getPopupImplView().setTranslationX(tx);
        }else {
            int tx = rect.left + SGDropDownInfo.offsetX;
            int realWidth = getActivityContentView().getMeasuredWidth();
            if(tx + getPopupImplView().getMeasuredWidth() > realWidth){
                tx -= (tx + getPopupImplView().getMeasuredWidth() - realWidth);
            }
            getPopupImplView().setTranslationX(tx);
        }*/

        int centerY = rect.top + rect.height() / 2;
        View implView = getPopupImplView();
        FrameLayout.LayoutParams implParams = (FrameLayout.LayoutParams) implView.getLayoutParams();
        if ((centerY > getMeasuredHeight() / 2 || SGDropDownInfo.dropDownPosition == DropDownPosition.Top) && SGDropDownInfo.dropDownPosition != DropDownPosition.Bottom) {
            // 说明atView在Window下半部分，dropdown应该显示在它上方，计算atView之上的高度
            params.height = rect.top;
            isShowUp = true;
            implParams.gravity = Gravity.BOTTOM;
            if (getMaxHeight() != 0) {
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
            }
        } else {
            // atView在上半部分，dropdown应该显示在它下方，计算atView之下的高度
            params.height = getMeasuredHeight() - rect.bottom;
            isShowUp = false;
            params.topMargin = rect.bottom;
            implParams.gravity = Gravity.TOP;
            if (getMaxHeight() != 0) {
                implParams.height = Math.min(implView.getMeasuredHeight(), getMaxHeight());
            }
        }
        getPopupContentView().setLayoutParams(params);
        implView.setLayoutParams(implParams);
        getPopupContentView().post(new Runnable() {
            @Override
            public void run() {
                initAndStartAnimation();
               // getPopupImplView().setVisibility(VISIBLE);
            }
        });
        attachPopupContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (SGDropDownInfo.isDismissOnTouchOutside) {
                    dismiss();
                }
                return false;
            }
        });
        attachPopupContainer.setOnClickOutsideListener(new SGOnClickOutsideListener() {
            @Override
            public void onClickOutside() {
                if (SGDropDownInfo.isDismissOnTouchOutside) {
                    dismiss();
                }
            }
        });
    }
    @Override
    protected DropDownAnimator getPopupAnimator() {
        return new TranslateAnimator(getPopupImplView(), getAnimationDuration(), isShowUp ?
                SGDropDownAnimation.TranslateFromBottom : SGDropDownAnimation.TranslateFromTop);
    }

    @Override
    protected int getMaxWidth() {
        return SGDropDownUtils.getAppWidth(getContext());
    }
}