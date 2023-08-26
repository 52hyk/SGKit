package com.sungrowpower.kit.dropdown.base;

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
    protected SGDropDownContainer sgDropDownContainer;
    public SGDropDownBaseView(@NonNull Context context) {
        super(context);
        sgDropDownContainer = findViewById(R.id.attachPopupContainer);
        Log.i("content-->==-",findViewById(R.id.attachPopupContainer).getId()+"");

    }

    @Override
    final protected int getInnerLayoutId() {
        return R.layout._sg_dropdown_base_view;
    }
    protected void addInnerContent() {
        View contentView = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), sgDropDownContainer, false);
        sgDropDownContainer.addView(contentView);
    }

    @Override
    protected void initPopupContent() {
        if (sgDropDownContainer.getChildCount() == 0) {
            addInnerContent();
        }
        // 指定阴影动画的目标View
        if (SGDropDownInfoBean.hasShadowBg) {
            shadowBgAnimator.targetView = getDropDownContentView();
        }

//        getPopupImplView().setVisibility(INVISIBLE);
        Log.i("content-->",sgDropDownContainer.getChildCount()+"=="+getDropDownImplView().getId()+"=="+getDropDownContentView().getId());
        SGDropDownUtils.applyDropDownSize((ViewGroup) getDropDownContentView(), getMaxWidth(), getMaxHeight(),
                getDropDownWidth(), getDropDownHeight(), new Runnable() {
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
        if (SGDropDownInfoBean.atView == null) {
            throw new IllegalArgumentException("atView must not be null for PartShadowPopupView！");
        }

        //1. apply width and height
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getDropDownContentView().getLayoutParams();
        params.width = getMeasuredWidth();


        //1. 获取atView在屏幕上的位置
        Rect rect = SGDropDownInfoBean.getAtViewRect();
        rect.left -= getActivityContentLeft();
        rect.right -= getActivityContentLeft();


        int centerY = rect.top + rect.height() / 2;
        View implView = getDropDownImplView();
        FrameLayout.LayoutParams implParams = (FrameLayout.LayoutParams) implView.getLayoutParams();
        if ((centerY > getMeasuredHeight() / 2 || SGDropDownInfoBean.dropDownPosition == DropDownPosition.Top) && SGDropDownInfoBean.dropDownPosition != DropDownPosition.Bottom) {
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
        getDropDownContentView().setLayoutParams(params);
        implView.setLayoutParams(implParams);
        getDropDownContentView().post(new Runnable() {
            @Override
            public void run() {
                initAndStartAnimation();
               // getPopupImplView().setVisibility(VISIBLE);
            }
        });
        sgDropDownContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (SGDropDownInfoBean.isDismissOnTouchOutside) {
                    dismiss();
                }
                return false;
            }
        });
        sgDropDownContainer.setOnClickOutsideListener(new SGOnClickOutsideListener() {
            @Override
            public void onClickOutside() {
                if (SGDropDownInfoBean.isDismissOnTouchOutside) {
                    dismiss();
                }
            }
        });
    }
    @Override
    protected DropDownAnimator getPopupAnimator() {
        return new TranslateAnimator(getDropDownImplView(), getAnimationDuration(), isShowUp ?
                SGDropDownAnimation.TranslateFromBottom : SGDropDownAnimation.TranslateFromTop);
    }

    @Override
    protected int getMaxWidth() {
        return SGDropDownUtils.getAppWidth(getContext());
    }
}
