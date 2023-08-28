package com.sungrowpower.kit.dropdown.bean;

import android.graphics.Color;

/**
 * 创建日期：2023/8/28 on 18:02
 * 描述:
 * 作者:hyk
 */
public class GlobalBean {
    /**
     * 全局弹窗的设置
     **/
    //动画时间
    private static int animationDuration = 300;
    //50%的透明度
    private static int shadowBgColor = Color.parseColor("#7F000000");

    /**
     * 设置全局的背景阴影颜色
     *
     * @param color
     */
    public static void setShadowBgColor(int color) {
        shadowBgColor = color;
    }

    public static int getShadowBgColor() {
        return shadowBgColor;
    }


    /**
     * 设置全局动画时长
     *
     * @param duration
     */
    public static void setAnimationDuration(int duration) {
        if (duration >= 0) {
            animationDuration = duration;
        }
    }

    public static int getAnimationDuration() {
        return animationDuration;
    }
}
