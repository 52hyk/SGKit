package com.sungrowpower.kit.dropdown.util;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.sungrowpower.kit.dropdown.base.SGBaseView;


/**
 * Description:
 * Create by hyk
 */
public final class SGKeyboardUtils {
    public static int sDecorViewInvisibleHeightPre;
    private static final SparseArray<ViewTreeObserver.OnGlobalLayoutListener> listenerArray = new SparseArray<>();
    private SGKeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static int sDecorViewDelta = 0;
    /**
     * 获取软键盘高度
     * @param window
     * @return
     */
    private static int getDecorViewInvisibleHeight(final Window window) {
        final View decorView = window.getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        Log.d("KeyboardUtils", "getDecorViewInvisibleHeight: "
                + (decorView.getBottom() - outRect.bottom));
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= SGDropDownUtils.getNavBarHeight() + SGDropDownUtils.getStatusBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    /**
     * Register soft input changed listener.
     *
     * @param window The activity.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final Window window, final SGBaseView dropDownView, final OnSoftInputChangedListener listener) {
        final int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final FrameLayout contentView = window.findViewById(android.R.id.content);
        final int[] decorViewInvisibleHeightPre = {getDecorViewInvisibleHeight(window)};
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getDecorViewInvisibleHeight(window);
                if (decorViewInvisibleHeightPre[0] != height) {
                    listener.onSoftInputChanged(height);
                    decorViewInvisibleHeightPre[0] = height;
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        listenerArray.append(dropDownView.getId(), onGlobalLayoutListener);
    }

    /**
     * 注销监听
     * @param window
     * @param dropDownView
     */
    public static void removeLayoutChangeListener(Window window, SGBaseView dropDownView){
        final View contentView = window.findViewById(android.R.id.content);
        if (contentView == null) {
            return;
        }
        ViewTreeObserver.OnGlobalLayoutListener tag = listenerArray.get(dropDownView.getId());
        if (tag != null) {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(tag);
            tag = null;
            listenerArray.remove(dropDownView.getId());
        }
    }

    /**
     * 显示软键盘
     * @param view
     */
    public static void showSoftInput(final View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, 0 ,new SoftInputReceiver(view.getContext()));
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

     //监听调用结果
    private static class SoftInputReceiver extends ResultReceiver{
        private Context context;
        public SoftInputReceiver(Context context) {
            super(new Handler());
            this.context = context;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    toggleSoftInput(context);
                }
            context = null;
        }
    }

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(0, 0);
    }

    /**
     * 隐藏输入法
     * @param view
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏输入法
     * @param window
     */
    public static void hideSoftInput(@NonNull final Window window) {
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    /**
     * 软键盘改变时的监听
     */
    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }
}