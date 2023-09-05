package com.sungrowpower.kit.dropdown.widget;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.sungrowpower.kit.R;
import com.sungrowpower.kit.dropdown.SGKit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取颜色的工具类
 */
public class SGDropDownColorMap {

    /**
     * 字体颜色
     */
    public int textColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_title);
    /**
     * 字体大小
     */
    public int textSize = SGKit.getInstance().getContext().getResources().getDimensionPixelSize(R.dimen.sgkit_textSize_16);
    /**
     * FontIcon字体颜色
     */
    public int fontIconTextColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_brand_routine);
    /**
     * fontIcon字体大小
     */
    public int fontIconTextSize = SGKit.getInstance().getContext().getResources().getDimensionPixelSize(R.dimen.sgkit_textSize_16);


    /**
     * 动画时间
     */
    public int animationDuration = 300;

    /**
     * 70%的透明度
     */
    public int shadowBgColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_mask_color);

    /**
     * 设置item选中文本颜色
     */
    public int itemCheckedTextColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_brand_routine);

    /**
     * 设置item 未选中文本颜色
     */
    public int itemUnCheckedTextColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_title);

    /**
     * 设置item 不能点击中文本颜色
     */
    public int itemDisableTextColor = SGKit.getInstance().getContext().getResources().getColor(R.color.sgkit_text_disabled);

    /**
     * 设置item 选中背景颜色
     */
    public Drawable itemCheckedTextBgColor = SGKit.getInstance().getContext().getResources().getDrawable(R.drawable.selected_bg);

    /**
     * 设置item 未选中背景颜色
     */
    public Drawable itemUnCheckedTextBgColor = SGKit.getInstance().getContext().getResources().getDrawable(R.drawable.unselected_bg);

    private static class Holder {
        private static final SGDropDownColorMap INSTANCE = new SGDropDownColorMap();
    }

    public static SGDropDownColorMap getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 解析json设置颜色字体大小
     *
     * @param json json数据
     */
    public void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            textColor = Color.parseColor(jsonObject.getString("textColor"));
            textSize = jsonObject.getInt("textSize");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
