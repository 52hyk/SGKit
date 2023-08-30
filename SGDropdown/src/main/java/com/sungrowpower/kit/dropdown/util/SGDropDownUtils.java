package com.sungrowpower.kit.dropdown.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;


import com.sungrowpower.kit.dropdown.base.SGBaseView;
import com.sungrowpower.kit.dropdown.base.SGDropDownBaseView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Description:
 * Create by hyk
 */
public class SGDropDownUtils {

    //应用界面可见高度，可能不包含导航和状态栏，看Rom实现
    public static int getAppHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static int getAppWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    //屏幕的高度，包含状态栏，导航栏，看Rom实现
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    public static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static void setWidthHeight(View target, int width, int height) {
        if (width <= 0 && height <= 0) {
            return;
        }
        ViewGroup.LayoutParams params = target.getLayoutParams();
        if (width > 0) {
            params.width = width;
        }
        if (height > 0) {
            params.height = height;
        }
        target.setLayoutParams(params);
    }

    public static void applyDropDownSize(final ViewGroup content, final int maxWidth, final int maxHeight,
                                         final int dropDownWidth, final int dropDownHeight, final Runnable afterApplySize) {
        content.post(() -> {
            ViewGroup.LayoutParams params = content.getLayoutParams();
            View implView = content.getChildAt(0);
            ViewGroup.LayoutParams implParams = implView.getLayoutParams();
            // 假设默认Content宽是match，高是wrap
            int w = content.getMeasuredWidth();
            // response impl view wrap_content params.
            if (maxWidth > 0) {
                //指定了最大宽度，就限制最大宽度
                params.width = Math.min(w, maxWidth);
                if (implParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    implParams.width = Math.min(w, maxWidth);
                }
                if (dropDownWidth > 0) {
                    params.width = Math.min(dropDownWidth, maxWidth);
                    implParams.width = Math.min(dropDownWidth, maxWidth);
                }
            } else if (dropDownWidth > 0) {
                params.width = dropDownWidth;
                implParams.width = dropDownWidth;
            }

            int h = content.getMeasuredHeight();
            if (maxHeight > 0) {
                params.height = Math.min(h, maxHeight);
                if (dropDownHeight > 0) {
                    params.height = Math.min(dropDownHeight, maxHeight);
                    implParams.height = Math.min(dropDownHeight, maxHeight);
                }
            } else if (dropDownHeight > 0) {
                params.height = dropDownHeight;
                implParams.height = dropDownHeight;
            }
            implView.setLayoutParams(implParams);
            content.setLayoutParams(params);
            content.post(() -> {
                if (afterApplySize != null) {
                    afterApplySize.run();
                }
            });

        });
    }


    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }

    private static int sDecorViewDelta = 0;

    public static int getDecorViewInvisibleHeight(final Window window) {
        final View decorView = window.getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    //监听到的keyboardHeight有一定几率是错误的，比如在同时显示导航栏和弹出输入法的时候，有一定几率会算上导航栏的高度，
    //这个不是必现的，暂时无解
    private static int preKeyboardHeight = 0;

    public static void moveUpToKeyboard(final int keyboardHeight, final SGBaseView pv) {
        preKeyboardHeight = keyboardHeight;
        pv.post(new Runnable() {
            @Override
            public void run() {
                moveUpToKeyboardInternal(preKeyboardHeight, pv);
            }
        });
    }

    private static void moveUpToKeyboardInternal(int keyboardHeight, SGBaseView pv) {
        if (pv.sgDropDownInfoBean == null || !pv.sgDropDownInfoBean.getMoveUpToKeyboard()) {
            return;
        }

        //判断是否盖住输入框
        ArrayList<EditText> allEts = new ArrayList<>();
        findAllEditText(allEts, pv);
        EditText focusEt = null;
        for (EditText et : allEts) {
            if (et.isFocused()) {
                focusEt = et;
                break;
            }
        }

        int dy = 0;
        int dropDownHeight = pv.getDropDownContentView().getHeight();
        int dropDownWidth = pv.getDropDownContentView().getWidth();
        if (pv.getDropDownImplView() != null) {
            dropDownHeight = Math.min(dropDownHeight, pv.getDropDownImplView().getMeasuredHeight());
            dropDownWidth = Math.min(dropDownWidth, pv.getDropDownImplView().getMeasuredWidth());
        }

        int screenHeight = pv.getMeasuredHeight();
        int focusEtTop = 0;
        int focusBottom = 0;
        if (focusEt != null) {
            int[] locations = new int[2];
            focusEt.getLocationInWindow(locations);
            focusEtTop = locations[1];
            focusBottom = focusEtTop + focusEt.getMeasuredHeight();
        }
        int animDuration = 100;
        //执行上移的逻辑
        if (pv instanceof SGDropDownBaseView) {
            int overflowHeight = (int) ((focusBottom + keyboardHeight) - screenHeight
                    - pv.getDropDownContentView().getTranslationY());
            if (focusEt != null && overflowHeight > 0) {
                dy = overflowHeight;
            }
        }
        pv.getDropDownContentView().animate().translationY(-dy)
                .setDuration(animDuration)
                .setInterpolator(new OvershootInterpolator(0))
                .start();
    }

    public static void moveDown(SGBaseView pv) {

        pv.getDropDownContentView().animate().translationY(0)
                .setDuration(100).start();
    }


    public static void findAllEditText(ArrayList<EditText> list, ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v instanceof EditText && v.getVisibility() == View.VISIBLE) {
                list.add((EditText) v);
            } else if (v instanceof ViewGroup) {
                findAllEditText(list, (ViewGroup) v);
            }
        }
    }


    private static void showToast(final Context context, final String text) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (context != null) {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static Activity context2Activity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context);
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }


    public static boolean hasSetKeyListener(View view) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObj = listenerInfoMethod.invoke(view);
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
            Field mOnKeyListenerField = listenerInfoClazz.getDeclaredField("mOnKeyListener");
            if (!mOnKeyListenerField.isAccessible()) {
                mOnKeyListenerField.setAccessible(true);
            }
            Object keyListener = mOnKeyListenerField.get(listenerInfoObj);
            return keyListener != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static int calculateInSampleSize(final BitmapFactory.Options options,
                                            final int maxWidth,
                                            final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    public static Rect getViewRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }

    /**
     * 判断是否横屏显示
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是平板
     *
     * @return
     */
    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
