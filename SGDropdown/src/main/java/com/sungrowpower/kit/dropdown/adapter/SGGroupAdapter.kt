package com.sungrowpower.kit.dropdown.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean
import com.sungrowpower.kit.dropdown.bean.SGGroupDataBean
import com.sungrowpower.kit.dropdown.interfaces.SGGroupOnClickListener
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils

/**
 * 创建日期：2023/8/29 on 15:52
 * 描述:
 * 作者:hyk
 */
class SGGroupAdapter(
    private val sgGroupDataBean: List<SGGroupDataBean>,
    private val sgDropDownInfoBean: SGDropDownInfoBean,
    var context: Context
) : RecyclerView.Adapter<SGGroupAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sg_group_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sgGroupDataBean[position]
        holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, sgDropDownInfoBean.sgKitTextSize);
        holder.tvName.setTextColor(sgDropDownInfoBean.sgKitTextColor)
        holder.tvName.typeface = sgDropDownInfoBean.sgKitTypeface

        if (sgDropDownInfoBean.sgKitText.isNullOrEmpty()) {
            holder.tvName.text = sgDropDownInfoBean.sgKitText
        } else {
            holder.tvName.setText(item.title)
        }

        holder.recyclerView!!.layoutManager = GridLayoutManager(context, 3)
        val params = holder.recyclerView!!.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = SGDropDownUtils.dp2px(context, 4F)
        holder.recyclerView!!.adapter =
            SGColumnAdapter(item.childData, sgDropDownInfoBean).apply {
                setOnItemClickListener { view, position ->
                    if (item.childData[position].isDisabled) {
                        return@setOnItemClickListener
                    }
                    if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                        sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(
                            position,
                            holder.layoutPosition
                        )
                    }

                    item.childData[position].isChecked = !item.childData[position].isChecked
                    notifyItemChanged(position)
                    if (listener != null) {
                        listener!!.onGroupChange(position, holder.layoutPosition)
                    }
                    if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                        sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(
                            position,
                            holder.layoutPosition
                        )
                    }
                }
            }
    }

    private var listener: SGGroupOnClickListener? = null

    public fun setGroupOnClickListener(listener: SGGroupOnClickListener?) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return sgGroupDataBean.size
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var tvName: TextView
        var recyclerView: RecyclerView? = null

        init {
            tvName = view.findViewById<View>(R.id.tv_name) as TextView
            recyclerView = view.findViewById(R.id.rv)
        }

    }
}