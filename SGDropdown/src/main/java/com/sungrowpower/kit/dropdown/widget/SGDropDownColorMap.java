package com.sungrowpower.kit.dropdown.widget;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.sungrowpower.kit.R;
import com.sungrowpower.kit.dropdown.SGKit;

import org.json.JSONException;
import org.json.JSONObject;

public class SGDropDownColorMap {

    /**
     * 字体颜色
     */
    public int textColor =  SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_title);
    /**
     * 字体大小
     */
    public int textSize =SGKit.getInstance().getContext().getResources().getDimensionPixelSize(R.dimen.sgkit_textSize_16);
    /**
     * FontIcon字体颜色
     */
    public int fontIconTextColor =  SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_brand_routine);
    /**
     * fontIcon字体大小
     */
    public int fontIconTextSize =SGKit.getInstance().getContext().getResources().getDimensionPixelSize(R.dimen.sgkit_textSize_16);

    //动画时间
    public static int animationDuration = 300;
    //50%的透明度
    public static int shadowBgColor = Color.parseColor("#7F000000");

    public static void setShadowBgColor(int color) {
        shadowBgColor = color;
    }

    public static int getShadowBgColor() {
        return shadowBgColor;
    }

    public int itemCheckedTextColor =  SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_brand_routine);
    public int itemUnCheckedTextColor =  SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_title);
    public int itemDisableTextColor =  SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_disabled);

    public Drawable itemCheckedTextBgColor=SGKit.getInstance().getContext().getResources().getDrawable(R.drawable.selected_bg);
    public Drawable itemUnCheckedTextBgColor=SGKit.getInstance().getContext().getResources().getDrawable(R.drawable.unselected_bg);

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

    private static class Holder {
        private static final SGDropDownColorMap INSTANCE = new SGDropDownColorMap();
    }

    public static SGDropDownColorMap getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 解析json设置颜色字体大小
     * @param json json数据
     */
    public void parseJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            textColor = Color.parseColor(jsonObject.getString("textColor"));
            textSize = jsonObject.getInt("textSize");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
