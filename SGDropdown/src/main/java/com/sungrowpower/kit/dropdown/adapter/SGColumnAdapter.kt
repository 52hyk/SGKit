package com.sungrowpower.kit.dropdown.adapter

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean

/**
 * 创建日期：2023/8/15 on 17:25
 * 描述:
 * 作者:hyk
 */
class SGColumnAdapter(mData:MutableList<SGSimpleDataBean>, var sgDropDownInfoBean:SGDropDownInfoBean):BaseQuickAdapter<SGSimpleDataBean, QuickViewHolder>(mData) {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SGSimpleDataBean?) {
       // holder.getView<TextView>(R.id.tv_name).text=sgDropDownInfoBean.sgKitText
        holder.getView<TextView>(R.id.tv_name).setTextSize(TypedValue.COMPLEX_UNIT_PX, sgDropDownInfoBean.sgKitTextSize);
        holder.getView<TextView>(R.id.tv_name).setTextColor(sgDropDownInfoBean.sgKitTextColor)
        holder.getView<TextView>(R.id.tv_name).typeface = sgDropDownInfoBean.sgKitTypeface

        if (sgDropDownInfoBean.sgKitText.isNullOrEmpty()){
            holder.setText(R.id.tv_name,sgDropDownInfoBean.sgKitText)
        }else{
            holder.setText(R.id.tv_name,item!!.label)
        }

        if (item!!.isChecked){
            holder.getView<TextView>(R.id.tv_name).background=sgDropDownInfoBean.sgItemCheckedTextBgColor
            holder.getView<TextView>(R.id.tv_name).setTextColor(sgDropDownInfoBean.sgItemCheckedTextColor)
        }else{
            holder.getView<TextView>(R.id.tv_name).background=sgDropDownInfoBean.sgItemUnCheckedTextBgColor
            if (!item.isDisabled){
                holder.getView<TextView>(R.id.tv_name).setTextColor(sgDropDownInfoBean.sgItemUnCheckedTextColor)
            }else{
                holder.getView<TextView>(R.id.tv_name).setTextColor(sgDropDownInfoBean.sgItemDisableTextColor)
            }
        }


    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout._sg_column_item, parent)
    }
}