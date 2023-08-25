package com.sungrowpower.kit.dropdown.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.base.SGDropDownInfoBean
import com.sungrowpower.kit.dropdown.bean.SGGroupDataBean
import com.sungrowpower.kit.dropdown.interfaces.SGGroupOnClickListener
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOutsideListener

/**
 * 创建日期：2023/8/25 on 9:28
 * 描述:
 * 作者:hyk
 */
class MyGroupAdapter(mData:MutableList<SGGroupDataBean>,var sgDropDownInfoBean: SGDropDownInfoBean):
    BaseQuickAdapter<SGGroupDataBean, QuickViewHolder>(mData) {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: SGGroupDataBean?) {
        holder.setText(R.id.tv_name,item!!.title)
        holder.getView<RecyclerView>(R.id.rv).layoutManager=GridLayoutManager(context,3)
        holder.getView<RecyclerView>(R.id.rv).adapter=MyColumnAdapter(item.childData).apply {
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