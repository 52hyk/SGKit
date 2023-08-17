package com.sungrowpower.demo.activity

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.kuaimin.dropdown.R

/**
 * 创建日期：2023/8/15 on 17:25
 * 描述:
 * 作者:hyk
 */
class MyAdapter(mData:MutableList<String>):BaseQuickAdapter<String, QuickViewHolder>(mData) {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        holder.setText(R.id.tv_name,item)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int,
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_item, parent)
    }
}