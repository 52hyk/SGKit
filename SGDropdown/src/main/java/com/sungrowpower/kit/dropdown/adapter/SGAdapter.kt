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
import com.sungrowpower.kit.fonticon.SGFontIcon

/**
 * 创建日期：2023/8/29 on 15:52
 * 描述:单行数据适配器
 * 作者:hyk
 */
class SGAdapter(
    private val sgSimpleDataBeans: List<SGSimpleDataBean>,
    private val sgDropDownInfoBean: SGDropDownInfoBean,
) : RecyclerView.Adapter<SGAdapter.ViewHolder>() {

    var mClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sg_simple_item, parent, false)
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

        holder.sgFont.setTextSize(TypedValue.COMPLEX_UNIT_PX, sgDropDownInfoBean.sgFontIconTextSize)
        holder.sgFont.setTextColor(sgDropDownInfoBean.sgFontIconTextColor)
        holder.sgFont.setText(sgDropDownInfoBean.sgFontIconText)

        if (item!!.isChecked) {
            holder.sgFont.visibility = View.VISIBLE
            holder.tvName.setTextColor(sgDropDownInfoBean.sgItemCheckedTextColor)

        } else {
            holder.sgFont.visibility = View.GONE
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

    class ViewHolder(view: View, private val mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var tvName: TextView
        val sgFont: SGFontIcon

        init {
            itemView.setOnClickListener(this)
            tvName = view.findViewById<View>(R.id.tv_name) as TextView
            sgFont = view.findViewById<View>(R.id.sg_font) as SGFontIcon
        }

        override fun onClick(v: View?) {
            mListener.onItemClick(v, position)
        }
    }
}