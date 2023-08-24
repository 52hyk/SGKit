package com.sungrowpower.kit.dropdown.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean
import com.sungrowpower.kit.fonticon.SGFontIcon

/**
 * 创建日期：2023/8/15 on 17:25
 * 描述:
 * 作者:hyk
 */
class MyAdapter(mData:MutableList<SGSimpleDataBean>):BaseQuickAdapter<SGSimpleDataBean, QuickViewHolder>(mData) {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SGSimpleDataBean?) {
        holder.setText(R.id.tv_name,item!!.label)
        if (item.isChecked){
            holder.getView<SGFontIcon>(R.id.sg_font).visibility=View.VISIBLE
            holder.getView<TextView>(R.id.tv_name).setTextColor(context.resources.getColor(R.color.sgkit_brand_routine))
        }else{
            holder.getView<SGFontIcon>(R.id.sg_font).visibility=View.GONE
            if (!item.isDisabled){
                holder.getView<TextView>(R.id.tv_name).setTextColor(context.resources.getColor(R.color.sgkit_text_title))
            }else{
                holder.getView<TextView>(R.id.tv_name).setTextColor(context.resources.getColor(R.color.sgkit_text_disabled))
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout._sg_simple_item, parent)
    }
}