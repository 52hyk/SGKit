package com.sungrowpower.kit.dropdown.base;


import static com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation.NoAnimation;
import static com.sungrowpower.kit.dropdown.util.SGKeyboardUtils.showSoftInput;

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
import android.view.WindowManager;
import android.widget.EditText;
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

import com.sungrowpower.kit.dropdown.animator.DropDownAnimator;
import com.sungrowpower.kit.dropdown.animator.EmptyAnimator;
import com.sungrowpower.kit.dropdown.animator.ScaleAlphaAnimator;
import com.sungrowpower.kit.dropdown.animator.ScrollScaleAnimator;
import com.sungrowpower.kit.dropdown.animator.ShadowBgAnimator;
import com.sungrowpower.kit.dropdown.animator.TranslateAlphaAnimator;
import com.sungrowpower.kit.dropdown.animator.TranslateAnimator;
import com.sungrowpower.kit.dropdown.enums.DropDownStatus;
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils;
import com.sungrowpower.kit.dropdown.util.SGKeyboardUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹窗基类，顶布局
 * Create by hyk
 */
public abstract class SGBaseView extends FrameLayout implements LifecycleObserver, LifecycleOwner,
        ViewCompat.OnUnhandledKeyEventListenerCompat {
    public com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean sgDropDownInfoBean;
    protected DropDownAnimator dropDownAnimator;
    protected ShadowBgAnimator shadowBgAnimator;
    private final int touchSlop;
    public DropDownStatus dropDownStatus = DropDownStatus.Dismiss;
    protected boolean isCreated = false;
    //是否更新软键盘弹出模式
    private boolean hasModifySoftMode = false;
    //软键盘弹出模式
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
        //把阴影层加进来
        addView(contentView);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }


    /**
     * 显示弹框的入口
     * 判断弹窗是否正在显示或者已经显示过
     * @return
     */
    public SGBaseView show() {

        Activity activity = SGDropDownUtils.context2Activity(this);
        if (activity == null || activity.isFinishing() || sgDropDownInfoBean == null) {
            return this;
        }
        if (dropDownStatus == DropDownStatus.Showing || dropDownStatus == DropDownStatus.Dismissing) {
            return this;
        }

        dropDownStatus = DropDownStatus.Showing;
        if (sgDropDownInfoBean.isRequestFocus()) {
            SGKeyboardUtils.hideSoftInput(activity.getWindow());
        }

        attachTask();
        return this;
    }

    /**
     * 把当前的FrameLayout添加到顶级视图中
     * 注册软键盘监听
     * 初始化操作
     */
    public void attachTask() {
        // 1. add dropDownView to its host.
        attachToHost();
        //2. 注册对话框监听器
        SGKeyboardUtils.registerSoftInputChangedListener(getHostWindow(), SGBaseView.this, new SGKeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                onKeyboardHeightChange(height);
                if (sgDropDownInfoBean != null && sgDropDownInfoBean.getSgDropDownCallback() != null) {
                    sgDropDownInfoBean.getSgDropDownCallback().onKeyBoardStateChanged(SGBaseView.this, height);
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

        // 3. do init
        init();
    }

    /**
     * 1：添加宿主生命周期的监听
     * 2：把当前的FrameLayout添加到顶级视图中
     */
    private void attachToHost() {
        if (sgDropDownInfoBean == null) {
            throw new IllegalArgumentException("如果弹窗对象是复用的，则不要设置isDestroyOnDismiss(true)");
        }
        if (sgDropDownInfoBean.getHostLifecycle() != null) {
            sgDropDownInfoBean.getHostLifecycle().addObserver(this);
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

    /**
     * 获取顶级视图
     *
     * @return
     */
    protected View getWindowDecorView() {
        if (getHostWindow() == null) {
            return null;
        }
        return (ViewGroup) getHostWindow().getDecorView();
    }

    /**
     * 获取根视图
     *
     * @return
     */
    public View getActivityContentView() {
        return ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * 获取在当前窗口的x坐标
     *
     * @return
     */
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
            if (sgDropDownInfoBean.getSgDropDownCallback() != null) {
                sgDropDownInfoBean.getSgDropDownCallback().onCreated(this);
            }
        }
        initTask();
        //添加点击返回键监听
        addOnUnhandledKeyListener(this);

    }

    protected void addOnUnhandledKeyListener(View view) {
        ViewCompat.removeOnUnhandledKeyEventListener(view, this);
        ViewCompat.addOnUnhandledKeyEventListener(view, this);
    }

    /**
     * 初始化任务
     */
    public void initTask() {
        if (getHostWindow() == null) {
            return;
        }
        if (sgDropDownInfoBean.getSgDropDownCallback() != null) {
            sgDropDownInfoBean.getSgDropDownCallback().beforeShow(SGBaseView.this);
        }
        beforeShow();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        setFocusAndProcessBackPress();

    }

    /**
     * 初始化动画
     */
    protected void initAnimator() {
        getDropDownContentView().setAlpha(1f);
        // 优先使用自定义的动画器
        if (sgDropDownInfoBean.getCustomAnimator() != null) {
            dropDownAnimator = sgDropDownInfoBean.getCustomAnimator();
            dropDownAnimator.targetView = getDropDownContentView();
        } else {
            // 根据SGDropDownInfo的dropDownAnimation字段来生成对应的动画执行器，如果dropDownAnimation字段为null，则返回null
            dropDownAnimator = genAnimatorByDropDownType();
            if (dropDownAnimator == null) {
                dropDownAnimator = getDropDownAnimator();
            }
        }

        //3. 初始化动画执行器
        if (sgDropDownInfoBean.getHasShadowBg()) {
            shadowBgAnimator.initAnimator();
        }

        if (dropDownAnimator != null) {
            dropDownAnimator.initAnimator();
        }
    }

    /**
     * 从顶级试图中移除当前的FrameLayout
     */
    private void detachFromHost() {
        if (sgDropDownInfoBean != null) {
            ViewGroup decorView = (ViewGroup) getParent();
            if (decorView != null) {
                decorView.removeView(this);
            }
        }
    }

    /**
     * 从活动中获取窗口
     *
     * @return
     */
    public Window getHostWindow() {
        if (sgDropDownInfoBean != null) {
            return ((Activity) getContext()).getWindow();
        }
        return null;
    }

    /**
     * 等动画执行完相关操作
     */
    protected void doAfterShow() {
        handler.removeCallbacks(doAfterShowTask);
        handler.postDelayed(doAfterShowTask, getAnimationDuration());
    }

    /**
     * 强制获取焦点
     * 如果设置弹窗获取焦点，就会循环遍历所有的EditText，
     * 并且让第一个获取焦点弹出软键盘
     */
    public void setFocusAndProcessBackPress() {
        if (sgDropDownInfoBean != null && sgDropDownInfoBean.isRequestFocus()) {
            setFocusableInTouchMode(true);
            setFocusable(true);

            addOnUnhandledKeyListener(this);

            //let all EditText can process back pressed.
            ArrayList<EditText> list = new ArrayList<>();
            SGDropDownUtils.findAllEditText(list, (ViewGroup) getDropDownContentView());
            if (list.size() > 0) {
                preSoftMode = getHostWindow().getAttributes().softInputMode;

                getHostWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                hasModifySoftMode = true;

                for (int i = 0; i < list.size(); i++) {
                    final EditText et = list.get(i);
                    addOnUnhandledKeyListener(et);
                    if (i == 0) {
                        if (sgDropDownInfoBean.isAutoFocusEditText()) {
                            et.setFocusable(true);
                            et.setFocusableInTouchMode(true);
                            et.requestFocus();
                            if (sgDropDownInfoBean.getAutoOpenSoftInput()) {
                                showSoftInput(et);
                            }
                        } else {
                            if (sgDropDownInfoBean.getAutoOpenSoftInput()) {
                                showSoftInput(this);
                            }
                        }
                    }
                }
            } else {
                if (sgDropDownInfoBean.getAutoOpenSoftInput()) {
                    showSoftInput(this);
                }
            }
        }
    }

    /**
     * 设置获取焦点的view
     * @param focusView
     */
    protected void showSoftInput(View focusView) {
        if (sgDropDownInfoBean != null) {
            if (showSoftInputTask == null) {
                showSoftInputTask = new ShowSoftInputTask(focusView);
            } else {
                handler.removeCallbacks(showSoftInputTask);
            }
            handler.postDelayed(showSoftInputTask, 10);
        }
    }
    private ShowSoftInputTask showSoftInputTask;

    /**
     * 弹出软键盘
     */
    static class ShowSoftInputTask implements Runnable {
        View focusView;

        public ShowSoftInputTask(View focusView) {
            this.focusView = focusView;
        }

        @Override
        public void run() {
            if (focusView != null) {
                SGKeyboardUtils.showSoftInput(focusView);
            }
        }
    }

    /**
     * 动画执行完毕后的任务
     */
    protected Runnable doAfterShowTask = new Runnable() {
        @Override
        public void run() {
            dropDownStatus = DropDownStatus.Show;
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            onShow();


            if (sgDropDownInfoBean != null && sgDropDownInfoBean.getSgDropDownCallback() != null) {
                sgDropDownInfoBean.getSgDropDownCallback().onShow(SGBaseView.this);
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

    /**
     * 弹框消失or隐藏输入框
     */
    public void dismissOrHideSoftInput() {
        if (SGKeyboardUtils.sDecorViewInvisibleHeightPre == 0) {
            dismiss();
        } else {
            SGKeyboardUtils.hideSoftInput(SGBaseView.this);
        }
    }

    /**
     * 点击返回键响应事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    protected boolean processKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && sgDropDownInfoBean != null) {
            if (sgDropDownInfoBean.getDismissOnBackPressed() &&
                    (sgDropDownInfoBean.getSgDropDownCallback() == null || !sgDropDownInfoBean.getSgDropDownCallback().onBackPressed(SGBaseView.this))) {
                dismissOrHideSoftInput();
            }
            return true;
        }
        return false;
    }


    /**
     * 根据DropDownInfo的DropDownAnimation字段来生成对应的内置的动画执行器
     */
    protected DropDownAnimator genAnimatorByDropDownType() {
        if (sgDropDownInfoBean == null || sgDropDownInfoBean.getSgDropDownAnimation() == null) {
            return null;
        }
        switch (sgDropDownInfoBean.getSgDropDownAnimation()) {
            case ScaleAlphaFromCenter:
            case ScaleAlphaFromLeftTop:
            case ScaleAlphaFromRightTop:
            case ScaleAlphaFromLeftBottom:
            case ScaleAlphaFromRightBottom:
                return new ScaleAlphaAnimator(getDropDownContentView(), getAnimationDuration(), sgDropDownInfoBean.getSgDropDownAnimation());

            case TranslateAlphaFromLeft:
            case TranslateAlphaFromTop:
            case TranslateAlphaFromRight:
            case TranslateAlphaFromBottom:
                return new TranslateAlphaAnimator(getDropDownContentView(), getAnimationDuration(), sgDropDownInfoBean.getSgDropDownAnimation());

            case TranslateFromLeft:
            case TranslateFromTop:
            case TranslateFromRight:
            case TranslateFromBottom:
                return new TranslateAnimator(getDropDownContentView(), getAnimationDuration(), sgDropDownInfoBean.getSgDropDownAnimation());

            case ScrollAlphaFromLeft:
            case ScrollAlphaFromLeftTop:
            case ScrollAlphaFromTop:
            case ScrollAlphaFromRightTop:
            case ScrollAlphaFromRight:
            case ScrollAlphaFromRightBottom:
            case ScrollAlphaFromBottom:
            case ScrollAlphaFromLeftBottom:
                return new ScrollScaleAnimator(getDropDownContentView(), getAnimationDuration(), sgDropDownInfoBean.getSgDropDownAnimation());

            case NoAnimation:
                return new EmptyAnimator(getDropDownContentView(), getAnimationDuration());
        }
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
     * 获取DropDownAnimator，用于每种类型的DropDownView自定义自己的动画器
     *
     * @return
     */
    protected DropDownAnimator getDropDownAnimator() {
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
        if (sgDropDownInfoBean == null) {
            return;
        }
        if (sgDropDownInfoBean.getHasShadowBg() && shadowBgAnimator != null) {
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
        if (sgDropDownInfoBean == null) {
            return;
        }
        if (sgDropDownInfoBean.getHasShadowBg() && shadowBgAnimator != null) {
            shadowBgAnimator.animateDismiss();
        }

        if (dropDownAnimator != null) {
            dropDownAnimator.animateDismiss();
        }
    }

    /**
     * 获取内容View，本质上DropDownView显示的内容都在这个View内部。
     * 而且我们对DropDownView执行的动画，也是对它执行的动画
     * 也可以理解为阴影层
     *
     * @return
     */
    public View getDropDownContentView() {
        return getChildAt(0);
    }

    /**
     * 可以理解为数据层View
     *
     * @return
     */
    public View getDropDownImplView() {
        return ((ViewGroup) getDropDownContentView()).getChildAt(0);
    }

    /**
     * 获取动画时长
     *
     * @return
     */
    public int getAnimationDuration() {
        if (sgDropDownInfoBean == null) {
            return 0;
        }
        if (sgDropDownInfoBean.getSgDropDownAnimation() == NoAnimation) {
            return 1;
        }
        return sgDropDownInfoBean.getAnimationDuration();

        //return sgDropDownInfoBean.getAnimationDuration() >= 0 ? sgDropDownInfoBean.getAnimationDuration() : GlobalBean.getAnimationDuration() + 1;
    }

    /***
     * 获取阴影层背景颜色
     * @return
     */
    public int getShadowBgColor() {
        return sgDropDownInfoBean.getShadowBgColor();

        //return sgDropDownInfoBean != null && sgDropDownInfoBean.getShadowBgColor() != 0 ? sgDropDownInfoBean.getShadowBgColor()  : GlobalBean.getShadowBgColor();
    }


    /**
     * 弹窗的最大宽度，用来限制弹窗的最大宽度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxWidth() {
        return sgDropDownInfoBean.getMaxWidth();
    }

    /**
     * 弹窗的最大高度，用来限制弹窗的最大高度
     * 返回0表示不限制，默认为0
     *
     * @return
     */
    protected int getMaxHeight() {
        return sgDropDownInfoBean.getMaxHeight();
    }

    /**
     * 弹窗的宽度，用来动态设定当前弹窗的宽度，受getMaxWidth()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getDropDownWidth() {
        return sgDropDownInfoBean.getDropDownWidth();
    }

    /**
     * 弹窗的高度，用来动态设定当前弹窗的高度，受getMaxHeight()限制
     * 返回0表示不设置，默认为0
     *
     * @return
     */
    protected int getDropDownHeight() {
        return sgDropDownInfoBean.getDropDownHeight();
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (dropDownStatus == DropDownStatus.Dismissing || dropDownStatus == DropDownStatus.Dismiss) {
            return;
        }
        dropDownStatus = DropDownStatus.Dismissing;
        clearFocus();
        if (sgDropDownInfoBean != null && sgDropDownInfoBean.getSgDropDownCallback() != null) {
            sgDropDownInfoBean.getSgDropDownCallback().beforeDismiss(this);
        }
        beforeDismiss();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        doDismissAnimation();
        doAfterDismiss();
    }


    protected void doAfterDismiss() {
        //SGDropDownBaseView要等到完全关闭再关闭输入法，不然有问题
        if (sgDropDownInfoBean != null && sgDropDownInfoBean.getAutoOpenSoftInput() && !(this instanceof SGDropDownBaseView)) {
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
            if (sgDropDownInfoBean == null) {
                return;
            }
            if (sgDropDownInfoBean.getAutoOpenSoftInput() && SGBaseView.this instanceof SGDropDownBaseView) {
                SGKeyboardUtils.hideSoftInput(SGBaseView.this);
            }
            onDismiss();
            // SGDropDown.longClickPoint = null;
            if (sgDropDownInfoBean.getSgDropDownCallback() != null) {
                sgDropDownInfoBean.getSgDropDownCallback().onDismiss(SGBaseView.this);
            }

            if (sgDropDownInfoBean.isRequestFocus()) {
                // 让根布局拿焦点，避免布局内RecyclerView类似布局获取焦点导致布局滚动
                if (getWindowDecorView() != null) {
                    View needFocusView = getWindowDecorView().findViewById(android.R.id.content);
                    if (needFocusView != null) {
                        needFocusView.setFocusable(true);
                        needFocusView.setFocusableInTouchMode(true);
                    }
                }
            }
            // 移除弹窗，Over
            detachFromHost();
        }
    };


    public boolean isShow() {
        return dropDownStatus != DropDownStatus.Dismiss;
    }

    public boolean isDismiss() {
        return dropDownStatus == DropDownStatus.Dismiss;
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

    /**
     * 释放资源
     */
    public void destroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        if (sgDropDownInfoBean != null) {
            sgDropDownInfoBean.setAtView(null);
            sgDropDownInfoBean.setSgDropDownCallback(null);
            sgDropDownInfoBean.setHostLifecycle(null);
            if (sgDropDownInfoBean.getCustomAnimator() != null && sgDropDownInfoBean.getCustomAnimator().targetView != null) {
                sgDropDownInfoBean.getCustomAnimator().targetView.animate().cancel();
            }
//            tryRemoveFragments();

            if (sgDropDownInfoBean.isDestroyOnDismiss()) {
                sgDropDownInfoBean = null;
            }
        }

        if (shadowBgAnimator != null && shadowBgAnimator.targetView != null) {
            shadowBgAnimator.targetView.animate().cancel();
        }

    }

    /**
     * View从Window上分离
     * 1：注销软键盘监听
     * 2：释放资源
     * 3：注销生命周期的监听
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
        if (sgDropDownInfoBean != null) {
            if (getWindowDecorView() != null) {
                SGKeyboardUtils.removeLayoutChangeListener(getHostWindow(), SGBaseView.this);
            }
            if (hasModifySoftMode) {
                //还原WindowSoftMode
                getHostWindow().setSoftInputMode(preSoftMode);
                hasModifySoftMode = false;
            }
            if (sgDropDownInfoBean.isDestroyOnDismiss()) {
                destroy();//如果开启isDestroyOnDismiss，强制释放资源
            }
        }
        if (sgDropDownInfoBean != null && sgDropDownInfoBean.getHostLifecycle() != null) {
            sgDropDownInfoBean.getHostLifecycle().removeObserver(this);
        } else {
            if (getContext() != null && getContext() instanceof FragmentActivity) {
                ((FragmentActivity) getContext()).getLifecycle().removeObserver(this);
            }
        }
        dropDownStatus = DropDownStatus.Dismiss;
        hasMoveUp = false;
    }

    /**
     * 透传
     *
     * @param event
     */
    private void passClickThrough(MotionEvent event) {
        if (sgDropDownInfoBean != null && sgDropDownInfoBean.isClickThrough()) {
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
                    if (sgDropDownInfoBean != null && sgDropDownInfoBean.getDismissOnTouchOutside()) {
                        dismiss();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    float dx = event.getX() - x;
                    float dy = event.getY() - y;
                    float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    passClickThrough(event);
                    if (distance < touchSlop && sgDropDownInfoBean != null && sgDropDownInfoBean.getDismissOnTouchOutside()) {
                        //查看是否在排除区域外
                        ArrayList<Rect> rects = sgDropDownInfoBean.getNotDismissWhenTouchInArea();
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
