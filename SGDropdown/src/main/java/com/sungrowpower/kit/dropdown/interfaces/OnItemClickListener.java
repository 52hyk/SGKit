package com.sungrowpower.kit.dropdown.interfaces;

import android.view.View;

/**
 * 创建日期：2023/8/29 on 16:20
 * 描述:RecyclerView item点击事件接口
 * 作者:hyk
 */
public interface OnItemClickListener {
     /**
      * 点击item的时候的回调
      * @param view
      * @param position
      */
     void onItemClick(View view, int position);
}
