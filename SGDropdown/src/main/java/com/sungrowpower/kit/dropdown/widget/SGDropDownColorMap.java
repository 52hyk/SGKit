package com.sungrowpower.kit.dropdown.widget;

import android.graphics.Color;

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
