package com.sungrowpower.kit.dropdown.adapter

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean
import com.sungrowpower.kit.dropdown.bean.SGGroupDataBean
import com.sungrowpower.kit.dropdown.interfaces.SGGroupOnClickListener

/**
 * 创建日期：2023/8/25 on 9:28
 * 描述:
 * 作者:hyk
 */
class SGGroupAdapter(mData:MutableList<SGGroupDataBean>, var sgDropDownInfoBean: SGDropDownInfoBean):
    BaseQuickAdapter<SGGroupDataBean, QuickViewHolder>(mData) {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SGGroupDataBean?) {
        //holder.setText(R.id.tv_name,item!!.title)
        holder.getView<TextView>(R.id.tv_name).setTextSize(TypedValue.COMPLEX_UNIT_PX, sgDropDownInfoBean.sgKitTextSize);
        holder.getView<TextView>(R.id.tv_name).setTextColor(sgDropDownInfoBean.sgKitTextColor)
        holder.getView<TextView>(R.id.tv_name).typeface = sgDropDownInfoBean.sgKitTypeface

        if (sgDropDownInfoBean.sgKitText.isNullOrEmpty()){
            holder.setText(R.id.tv_name,sgDropDownInfoBean.sgKitText)
        }else{
            holder.setText(R.id.tv_name,item!!.title)
        }

        holder.getView<RecyclerView>(R.id.rv).layoutManager=GridLayoutManager(context,3)
        holder.getView<RecyclerView>(R.id.rv).adapter=SGColumnAdapter(item!!.childData,sgDropDownInfoBean).apply {
            setOnItemClickListener { adapter, view, position ->
                if (item.childData[position].isDisabled) {
                    return@setOnItemClickListener
                }
                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(position, holder.layoutPosition)
                }

                item.childData[position].isChecked = !item.childData[position].isChecked
                notifyItemChanged(position)
                if (listener!=null){
                    listener!!.onGroupChange(position,holder.layoutPosition)
                }

                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(position, holder.layoutPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout._sg_group_item, parent)
    }

    private var listener: SGGroupOnClickListener? = null

   public fun setGroupOnClickListener(listener: SGGroupOnClickListener?) {
        this.listener = listener
    }
}