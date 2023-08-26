package com.sungrowpower.kit.dropdown.base;


import static com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation.NoAnimation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;

import com.sungrowpower.kit.dropdown.SGDropDown;
import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.animator.ShadowBgAnimator;
import com.sungrowpower.kit.dropdown.enums.DropDownStatus;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;
import com.sungrowpower.kit.dropdown.util.SGKeyboardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹窗基类
 * Create by hyk
 */
public abstract class SGBaseView extends FrameLayout implements LifecycleObserver, LifecycleOwner,
        ViewCompat.OnUnhandledKeyEventListenerCompat {
    public com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean SGDropDownInfoBean;
    protected DropDownAnimator dropDownAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    private final int touchSlop;
    public DropDownStatus dropDownStatus = DropDownStatus.Dismiss;
    protected boolean isCreated = false;
    private boolean hasModifySoftMode = false;
    private int preSoftMode = -1;
    private boolean hasMoveUp = false;
    protected Handler handler = new Handler(Looper.getMainLooper());
    protected LifecycleRegistry lifecycleRegistry;

    public SGBaseView(@NonNull Context context) {
        super(context);
        if (context instanceof Application) {
            throw new IllegalArgumentException("Dropdown的Context必须是Activity类型！");
        }
        lifecycleRegistry = new LifecycleRegistry(this);
        //它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setId(View.generateViewId());
        View contentView = LayoutInflater.from(context).inflate(getInnerLayoutId(), this, false);
        // 事先隐藏，等测量完毕恢复，避免影子跳动现象。
        contentView.setAlpha(0);
        addView(contentView);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public SGBaseView show() {
        Activity activity = SGDropDownUtils.context2Activity(this);
        if (activity == null || activity.isFinishing() || SGDropDownInfoBean == null) {
            return this;
        }
        if (dropDownStatus == DropDownStatus.Showing || dropDownStatus == DropDownStatus.Dismissing) {
            return this;
        }
        dropDownStatus = DropDownStatus.Showing;
        if (SGDropDownInfoBean.isRequestFocus) {
            SGKeyboardUtils.hideSoftInput(activity.getWindow());
        }

        getActivityContentView().post(attachTask);
        return this;
    }

    private final Runnable attachTask = new Runnable() {
        @Override
        public void run() {
            // 1. add dropDownView to its host.
            attachToHost();
            //2. 注册对话框监听器
            SGKeyboardUtils.registerSoftInputChangedListener(getHostWindow(), SGBaseView.this, new SGKeyboardUtils.OnSoftInputChangedListener() {
                @Override
                public void onSoftInputChanged(int height) {
                    onKeyboardHeightChange(height);
                    if (SGDropDownInfoBean != null && SGDropDownInfoBean.SGDropDownCallback != null) {
                        SGDropDownInfoBean.SGDropDownCallback.onKeyBoardStateChanged(SGBaseView.this, height);
                    }
                    if (height == 0) { // 说明输入法隐藏
                        SGDropDownUtils.moveDown(SGBaseView.this);
                        hasMoveUp = false;
                    } else {
//                        if (hasMoveUp) return;
                        //when show keyboard, move up
                        if (SGBaseView.this instanceof SGDropDownBaseView && dropDownStatus == DropDownStatus.Showing) {
                            return;
                        }
                        SGDropDownUtils.moveUpToKeyboard(height, SGBaseView.this);
                        hasMoveUp = true;
                    }
                }
            });

            // 3. do init，game start.
            init();
        }
    };


    private void attachToHost() {
        if (SGDropDownInfoBean == null) {
            throw new IllegalArgumentException("如果弹窗对象是复用的，则不要设置isDestroyOnDismiss(true)");
        }
        if (SGDropDownInfoBean.hostLifecycle != null) {
            SGDropDownInfoBean.hostLifecycle.addObserver(this);
        } else {
            if (getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().addObserver(this);
            }
        }


        //view实现
        ViewGroup decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        decorView.addView(this);

    }

    protected View getWindowDecorView() {
        if (getHostWindow() == null) {
            return null;
        }
        return (ViewGroup) getHostWindow().getDecorView();
    }

    public View getActivityContentView() {
        return ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    }

    protected int getActivityContentLeft() {
        if (!SGDropDownUtils.isLandscape(getContext())) {
            return 0;
        }
        //以Activity的content的left为准
        View decorView = ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        int[] loc = new int[2];
        //获取在当前窗口内的绝对坐标
        decorView.getLocationInWindow(loc);
        //x坐标
        return loc[0];
    }

    /**
     * 执行初始化
     */
    protected void init() {
        if (shadowBgAnimator == null) {
            shadowBgAnimator = new ShadowBgAnimator(this, getAnimationDuration(), getShadowBgColor());
        }

        //1. 初始化DropDown
        if (this instanceof SGDropDownBaseView) {
            initDropDownContent();
        } else if (!isCreated) {
            initDropDownContent();
        }
        if (!isCreated) {
            isCreated = true;
            onCreate();
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            if (SGDropDownInfoBean.SGDropDownCallback != null) {
                SGDropDownInfoBean.SGDropDownCallback.onCreated(this);
            }
        }
        handler.postDelayed(initTask, 10);
        //添加监听
        addOnUnhandledKeyListener(this);

    }
    protected void addOnUnhandledKeyListener(View view){
        ViewCompat.removeOnUnhandledKeyEventListener(view, this);
        ViewCompat.addOnUnhandledKeyEventListener(view, this);
    }
    private final Runnable initTask = new Runnable() {
        @Override
        public void run() {
            if (getHostWindow() == null) {
                return;
            }
            if (SGDropDownInfoBean.SGDropDownCallback != null) {
                SGDropDownInfoBean.SGDropDownCallback.beforeShow(SGBaseView.this);
            }
            beforeShow();
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);

        }
    };

    protected void initAnimator() {
        getDropDownContentView().setAlpha(1f);
        // 优先使用自定义的动画器
        if (SGDropDownInfoBean.customAnimator != null) {
            dropDownAnimator = SGDropDownInfoBean.customAnimator;
            dropDownAnimator.targetView = getDropDownContentView();
        } else {
            // 根据SGDropDownInfo的dropDownAnimation字段来生成对应的动画执行器，如果dropDownAnimation字段为null，则返回null
            dropDownAnimator = genAnimatorByDropDownType();
            if (dropDownAnimator == null) {
                dropDownAnimator = getPopupAnimator();
            }
        }

        //3. 初始化动画执行器
        if (SGDropDownInfoBean.hasShadowBg) {
            shadowBgAnimator.initAnimator();
        }

        if (dropDownAnimator != null) {
            dropDownAnimator.initAnimator();
        }
    }

    private void detachFromHost() {
        if (SGDropDownInfoBean != null) {
            ViewGroup decorView = (ViewGroup) getParent();
            if (decorView != null) {
                decorView.removeView(this);
            }
        }
    }

    public Window getHostWindow() {
        if (SGDropDownInfoBean != null) {
            return ((Activity) getContext()).getWindow();
        }
        return null;
    }

    protected void doAfterShow() {
        handler.removeCallbacks(doAfterShowTask);
        handler.postDelayed(doAfterShowTask, getAnimationDuration());
    }

    protected Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            dropDownStatus = DropDownStatus.Show;
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            onShow();


            if (SGDropDownInfoBean != null && SGDropDownInfoBean.SGDropDownCallback != null) {
                SGDropDownInfoBean.SGDropDownCallback.onShow(SGBaseView.this);
            }
            //再次检测移动距离
            if (getHostWindow() != null && SGDropDownUtils.getDecorViewInvisibleHeight(getHostWindow()) > 0 && !hasMoveUp) {
                SGDropDownUtils.moveUpToKeyboard(SGDropDownUtils.getDecorViewInvisibleHeight(getHostWindow()), SGBaseView.this);
            }
        }
    };


    @Override
    public boolean onUnhandledKeyEvent(View v, KeyEvent event) {
        return processKeyEvent(event.getKeyCode(), event);
    }


    public void dismissOrHideSoftInput() {
        if (SGKeyboardUtils.sDecorViewInvisibleHeightPre == 0) {
            dismiss();
        } else {
            SGKeyboardUtils.hideSoftInput(SGBaseView.this);
        }
    }


    protected boolean processKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && SGDropDownInfoBean != null) {
            if (SGDropDownInfoBean.isDismissOnBackPressed &&
                    (SGDropDownInfoBean.SGDropDownCallback == null || !SGDropDownInfoBean.SGDropDownCallback.onBackPressed(SGBaseView.this))) {
                dismissOrHideSoftInput();
            }
            return true;
        }
        return false;
    }

    class BackPressListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return processKeyEvent(keyCode, event);
        }
    }

    /**
     * 根据DropDownInfo的DropDownAnimation字段来生成对应的内置的动画执行器
     */
    protected DropDownAnimator genAnimatorByDropDownType() {
        if (SGDropDownInfoBean == null || SGDropDownInfoBean.SGDropDownAnimation == null) {
            return null;
        }
//        switch (SGDropDownInfo.SGDropDownAnimation) {
//            case ScaleAlphaFromCenter:
//            case ScaleAlphaFromLeftTop:
//            case ScaleAlphaFromRightTop:
//            case ScaleAlphaFromLeftBottom:
//            case ScaleAlphaFromRightBottom:
//                return new ScaleAlphaAnimator(getPopupContentView(), getAnimationDuration(), SGDropDownInfo.SGDropDownAnimation);
//
//            case TranslateAlphaFromLeft:
//            case TranslateAlphaFromTop:
//            case TranslateAlphaFromRight:
//            case TranslateAlphaFromBottom:
//                return new TranslateAlphaAnimator(getPopupContentView(), getAnimationDuration(), SGDropDownInfo.SGDropDownAnimation);
//
//            case TranslateFromLeft:
//            case TranslateFromTop:
//            case TranslateFromRight:
//            case TranslateFromBottom:
//                return new TranslateAnimator(getPopupContentView(), getAnimationDuration(), SGDropDownInfo.SGDropDownAnimation);
//
//            case ScrollAlphaFromLeft:
//            case ScrollAlphaFromLeftTop:
//            case ScrollAlphaFromTop:
//            case ScrollAlphaFromRightTop:
//            case ScrollAlphaFromRight:
//            case ScrollAlphaFromRightBottom:
//            case ScrollAlphaFromBottom:
//            case ScrollAlphaFromLeftBottom:
//                return new ScrollScaleAnimator(getPopupContentView(), getAnimationDuration(), SGDropDownInfo.SGDropDownAnimation);
//
//            case NoAnimation:
//                return new EmptyAnimator(getPopupContentView(), getAnimationDuration());
//        }
        return null;
    }

    /**
     * 内部使用，自定义弹窗的时候不要重新这个方法
     *
     * @return
     */
    protected abstract int getInnerLayoutId();

    /**
     * 如果你自己继承SGBaseView来做，这个不用实现
     *
     * @return
     */
    protected int getImplLayoutId() {
        return -1;
    }

    /**
     * 获取PopupAnimator，用于每种类型的DropDownView自定义自己的动画器
     *
     * @return
     */
    protected DropDownAnimator getPopupAnimator() {
        return null;
    }

    /**
     * 请使用onCreate，主要给弹窗内部用，不要去重写。
     */
    protected void initDropDownContent() {
    }

    /**
     * do init.
     */
    protected void onCreate() {
    }


    /**
     * 执行显示动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doShowAnimation() {
        if (SGDropDownInfoBean == null) {
            return;
        }
        if (SGDropDownInfoBean.hasShadowBg && shadowBgAnimator != null) {
            shadowBgAnimator.animateShow();
        }
        if (dropDownAnimator != null) {
            dropDownAnimator.animateShow();
        }
    }

    /**
     * 执行消失动画：动画由2部分组成，一个是背景渐变动画，一个是Content的动画；
     * 背景动画由父类实现，Content由子类实现
     */
    protected void doDismissAnimation() {
        if (SGDropDownInfoBean == null) {
            return;
        }
        if (SGDropDownInfoBean.hasShadowBg && shadowBgAnimator != null) {
            shadowBgAnimator.animateDismiss();
        }

        if (dropDownAnimator != null) {
            dropDownAnimator.animateDismiss();
        }
    }

    /**
     * 获取内容View，本质上DropDownView显示的内容都在这个View内部。
     * 而且我们对DropDownView执行的动画，也是对它执行的动画
     *
     * @return
     */
    public View getDropDownContentView() {
        return getChildAt(0);
    }

    public View getDropDownImplView() {
        return ((ViewGroup) getDropDownContentView()).getChildAt(0);
    }

    public int getAnimationDuration() {
        if (SGDropDownInfoBean == null) {
            return 0;
        }
        if (SGDropDownInfoBean.SGDropDownAnimation == NoAnimation) {
            return 1;
        }
        return SGDropDownInfoBean.animationDuration >= 0 ? SGDropDownInfoBean.animationDuration : SGDropDown.getAnimationDuration() + 1;
    }

    public int getShadowBgColor() {
        return SGDropDownInfoBean != null && SGDropDownInfoBean.shadowBgColor != 0 ? SGDropDownInfoBean.shadowBgColor : SGDropDown.getShadowBgColor();
    }


    /**
     * 弹窗的最大宽度，用来限制弹窗的最大宽度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxWidth() {
        return SGDropDownInfoBean.maxWidth;
    }

    /**
     * 弹窗的最大高度，用来限制弹窗的最大高度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxHeight() {
        return SGDropDownInfoBean.maxHeight;
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getDropDownWidth() {
        return SGDropDownInfoBean.dropDownWidth;
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getDropDownHeight() {
        return SGDropDownInfoBean.dropDownHeight;
    }

    /**
     * 消失
     */
    public void dismiss() {
        handler.removeCallbacks(attachTask);
        handler.removeCallbacks(initTask);
        if (dropDownStatus == DropDownStatus.Dismissing || dropDownStatus == DropDownStatus.Dismiss) {
            return;
        }
        dropDownStatus = DropDownStatus.Dismissing;
        clearFocus();
        if (SGDropDownInfoBean != null && SGDropDownInfoBean.SGDropDownCallback != null) {
            SGDropDownInfoBean.SGDropDownCallback.beforeDismiss(this);
        }
        beforeDismiss();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        doDismissAnimation();
        doAfterDismiss();
    }

    /**
     * 会等待弹窗show动画执行完毕再消失
     */
    public void smartDismiss() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                delayDismiss(getAnimationDuration() + 50);
            }
        });
    }

    public void delayDismiss(long delay) {
        if (delay < 0) {
            delay = 0;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, delay);
    }

    public void delayDismissWith(long delay, Runnable runnable) {
        this.dismissWithRunnable = runnable;
        delayDismiss(delay);
    }

    protected void doAfterDismiss() {
        //SGDropDownBaseView要等到完全关闭再关闭输入法，不然有问题
        if (SGDropDownInfoBean != null && SGDropDownInfoBean.autoOpenSoftInput && !(this instanceof SGDropDownBaseView)) {
            SGKeyboardUtils.hideSoftInput(this);
        }
        handler.removeCallbacks(doAfterDismissTask);
        handler.postDelayed(doAfterDismissTask, getAnimationDuration());
    }

    protected Runnable doAfterDismissTask = new Runnable() {
        @Override
        public void run() {
            dropDownStatus = DropDownStatus.Dismiss;
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            if (SGDropDownInfoBean == null) {
                return;
            }
            if (SGDropDownInfoBean.autoOpenSoftInput && SGBaseView.this instanceof SGDropDownBaseView) {
                SGKeyboardUtils.hideSoftInput(SGBaseView.this);
            }
            onDismiss();
            SGDropDown.longClickPoint = null;
            if (SGDropDownInfoBean.SGDropDownCallback != null) {
                SGDropDownInfoBean.SGDropDownCallback.onDismiss(SGBaseView.this);
            }
            if (dismissWithRunnable != null) {
                dismissWithRunnable.run();
                dismissWithRunnable = null;//no cache, avoid some bad edge effect.
            }
            if (SGDropDownInfoBean.isRequestFocus) {
                // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                if (getWindowDecorView() != null) {
                    View needFocusView = getWindowDecorView().findViewById(android.R.id.content);
                    if (needFocusView != null) {
                        needFocusView.setFocusable(true);
                        needFocusView.setFocusableInTouchMode(true);
                    }
                }
            }
            // 移除弹窗，GameOver
            detachFromHost();
        }
    };

    Runnable dismissWithRunnable;

    public void dismissWith(Runnable runnable) {
        this.dismissWithRunnable = runnable;
        dismiss();
    }

    public boolean isShow() {
        return dropDownStatus != DropDownStatus.Dismiss;
    }

    public boolean isDismiss() {
        return dropDownStatus == DropDownStatus.Dismiss;
    }

    public void toggle() {
        if (isShow()) {
            dismiss();
        } else {
            show();
        }
    }

    /**
     * 尝试移除弹窗内的Fragment，如果提供了Fragment的名字
     */
    protected void tryRemoveFragments() {
        if (getContext() instanceof FragmentActivity) {
            FragmentManager manager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            List<Fragment> fragments = manager.getFragments();
            List<String> internalFragmentNames = getInternalFragmentNames();
            if (fragments != null && fragments.size() > 0 && internalFragmentNames != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    String name = fragments.get(i).getClass().getSimpleName();
                    if (internalFragmentNames.contains(name)) {
                        manager.beginTransaction()
                                .remove(fragments.get(i))
                                .commitAllowingStateLoss();
                    }
                }
            }
        }
    }

    /**
     * 在弹窗内嵌入Fragment的场景中，当弹窗消失后，由于Fragment被Activity的FragmentManager缓存，
     * 会导致弹窗重新创建的时候，Fragment会命中缓存，生命周期不再执行。为了处理这种情况，只需重写：
     * getInternalFragmentNames() 方法，返回嵌入的Fragment名称，Dropdown会自动移除Fragment缓存。
     * 名字是: Fragment.getClass().getSimpleName()
     *
     * @return
     */
    protected List<String> getInternalFragmentNames() {
        return null;
    }

    /**
     * 消失动画执行完毕后执行
     */
    protected void onDismiss() {
    }

    /**
     * onDismiss之前执行一次
     */
    protected void beforeDismiss() {
    }

    /**
     * onCreated之后，onShow之前执行
     */
    protected void beforeShow() {
    }

    /**
     * 显示动画执行完毕后执行
     */
    protected void onShow() {
    }

    protected void onKeyboardHeightChange(int height) {
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        onDetachedFromWindow();
        detachFromHost();
        destroy();
    }

    public void destroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        if (SGDropDownInfoBean != null) {
            SGDropDownInfoBean.atView = null;
            SGDropDownInfoBean.SGDropDownCallback = null;
            SGDropDownInfoBean.hostLifecycle = null;
            if (SGDropDownInfoBean.customAnimator != null && SGDropDownInfoBean.customAnimator.targetView != null) {
                SGDropDownInfoBean.customAnimator.targetView.animate().cancel();
            }
            tryRemoveFragments();

            if (SGDropDownInfoBean.isDestroyOnDismiss) {
                SGDropDownInfoBean = null;
            }
        }

        if (shadowBgAnimator != null && shadowBgAnimator.targetView != null) {
            shadowBgAnimator.targetView.animate().cancel();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
        if (SGDropDownInfoBean != null) {
            if (getWindowDecorView() != null) {
                SGKeyboardUtils.removeLayoutChangeListener(getHostWindow(), SGBaseView.this);
            }
            if (hasModifySoftMode) {
                //还原WindowSoftMode
                getHostWindow().setSoftInputMode(preSoftMode);
                hasModifySoftMode = false;
            }
            if (SGDropDownInfoBean.isDestroyOnDismiss) {
                destroy();//如果开启isDestroyOnDismiss，强制释放资源
            }
        }
        if (SGDropDownInfoBean != null && SGDropDownInfoBean.hostLifecycle != null) {
            SGDropDownInfoBean.hostLifecycle.removeObserver(this);
        } else {
            if (getContext() != null && getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().removeObserver(this);
            }
        }
        dropDownStatus = DropDownStatus.Dismiss;
        hasMoveUp = false;
    }

    private void passClickThrough(MotionEvent event) {
        if (SGDropDownInfoBean != null && SGDropDownInfoBean.isClickThrough) {
                getActivityContentView().dispatchTouchEvent(event);
        }
    }

    private float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果自己接触到了点击，并且不在dropDownContentView范围内点击，则进行判断是否是点击事件,如果是，则dismiss
        Rect rect = new Rect();
        getDropDownImplView().getGlobalVisibleRect(rect);
        if (!SGDropDownUtils.isInRect(event.getX(), event.getY(), rect)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    passClickThrough(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (SGDropDownInfoBean != null && SGDropDownInfoBean.isDismissOnTouchOutside) {
                        dismiss();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    passClickThrough(event);
                    if (distance < touchSlop && SGDropDownInfoBean != null && SGDropDownInfoBean.isDismissOnTouchOutside) {
                        //查看是否在排除区域外
                        ArrayList<Rect> rects = SGDropDownInfoBean.notDismissWhenTouchInArea;
                        if (rects != null && rects.size() > 0) {
                            boolean inRect = false;
                            for (Rect r : rects) {
                                if (SGDropDownUtils.isInRect(event.getX(), event.getY(), r)) {
                                    inRect = true;
                                    break;
                                }
                            }
                            if (!inRect) {
                                dismiss();
                            }
                        } else {
                            dismiss();
                        }
                    }
                    x = 0;
                    y = 0;
                    break;
            }
        }
        return true;
    }

}
