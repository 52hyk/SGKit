package com.sungrowpower.kit.dropdown.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.bean.SGDropDownInfoBean
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean
import com.sungrowpower.kit.dropdown.interfaces.OnItemClickListener

/**
 * 创建日期：2023/8/29 on 15:52
 * 描述:
 * 作者:hyk
 */
class SGColumnAdapter(
    private val sgSimpleDataBeans: List<SGSimpleDataBean>,
    private val sgDropDownInfoBean: SGDropDownInfoBean,
) : RecyclerView.Adapter<SGColumnAdapter.ViewHolder>() {

    var mClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout._sg_column_item, parent, false)
        return ViewHolder(itemView, mClickListener!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = sgSimpleDataBeans[position]
        holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, sgDropDownInfoBean.sgKitTextSize);
        holder.tvName.setTextColor(sgDropDownInfoBean.sgKitTextColor)
        holder.tvName.typeface = sgDropDownInfoBean.sgKitTypeface
        if (sgDropDownInfoBean.sgKitText.isNullOrEmpty()) {
            holder.tvName.setText(sgDropDownInfoBean.sgKitText)
        } else {
            holder.tvName.setText(item!!.label)
        }

        if (item!!.isChecked) {
            holder.tvName.background=sgDropDownInfoBean.sgItemCheckedTextBgColor
            holder.tvName.setTextColor(sgDropDownInfoBean.sgItemCheckedTextColor)
        } else {
            holder.tvName.background=sgDropDownInfoBean.sgItemUnCheckedTextBgColor
            if (!item.isDisabled) {
                holder.tvName.setTextColor(sgDropDownInfoBean.sgItemUnCheckedTextColor)
            } else {
                holder.tvName.setTextColor(sgDropDownInfoBean.sgItemDisableTextColor)
            }
        }
    }

    override fun getItemCount(): Int {
        return sgSimpleDataBeans.size
    }

    class ViewHolder(view: View,private val mListener: OnItemClickListener) : RecyclerView.ViewHolder(view),View.OnClickListener {
        var tvName: TextView

        init {
            itemView.setOnClickListener(this)
            tvName = view.findViewById<View>(R.id.tv_name) as TextView
        }

        override fun onClick(v: View?) {
            mListener.onItemClick(v,position)
        }
    }
}