package com.sungrowpower.kit.dropdown.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOutsideListener;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;


/**
 * Description:阴影层，监听点击区域
 * Create by hyk
 */
public class SGDropDownContainer extends FrameLayout {
    public boolean isDismissOnTouchOutside = true;

    public SGDropDownContainer(@NonNull Context context) {
        super(context);
    }

    public SGDropDownContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SGDropDownContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算implView的Rect
        View implView = getChildAt(0);//数据层
        int[] location = new int[2];
        implView.getLocationInWindow(location);
        Rect implViewRect = new Rect(location[0], location[1], location[0] + implView.getMeasuredWidth(),
                location[1] + implView.getMeasuredHeight());
        if (!SGDropDownUtils.isInRect(event.getRawX(), event.getRawY(), implViewRect)) {//不在矩形中
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (distance < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {//判断移动距离
                        if (isDismissOnTouchOutside) {
                            if (listener != null) {
                                listener.onClickOutside();
                            }
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        return true;
    }

    private SGOnClickOutsideListener listener;

    public void setOnClickOutsideListener(SGOnClickOutsideListener listener) {
        this.listener = listener;
    }
}
