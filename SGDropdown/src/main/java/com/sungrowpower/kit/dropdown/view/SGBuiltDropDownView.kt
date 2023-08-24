package com.sungrowpower.kit.dropdown.view

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.adapter.MyAdapter
import com.sungrowpower.kit.dropdown.adapter.MyColumnAdapter
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean
import com.sungrowpower.kit.dropdown.impl.SGDropDownBaseView
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
class SGBuiltDropDownView(context: Context) : SGDropDownBaseView(context) {
    private var mData = mutableListOf<SGSimpleDataBean>()
    private val adapter by lazy {
        MyAdapter(mData).apply {
            setOnItemClickListener { adapter, view, position ->
                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }
                for (i in mData.indices) {
                    mData[i].isChecked = false
                }
                mData[position].isChecked = true
                notifyDataSetChanged()
            }
        }
    }
    private val adapterColumn by lazy {
        MyColumnAdapter(mData).apply {
            setOnItemClickListener { adapter, view, position ->
                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }
                mData[position].isChecked = !mData[position].isChecked
                notifyItemChanged(position)
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout._sg_simple_drop_down
    }

    override fun onCreate() {
        super.onCreate()
        mData = SGDropDownInfoBean.options as MutableList<SGSimpleDataBean>
        if (SGDropDownInfoBean.useColumn == 0) {
            val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
            params.bottomMargin = SGDropDownUtils.dp2px(context, 4F)
            params.topMargin = SGDropDownUtils.dp2px(context, 4F)
            findViewById<RecyclerView>(R.id.rv).layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            findViewById<RecyclerView>(R.id.rv).adapter = adapter
        } else {
            val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
            params.leftMargin = SGDropDownUtils.dp2px(context, 6F)
            params.rightMargin = SGDropDownUtils.dp2px(context, 6F)
            params.topMargin = SGDropDownUtils.dp2px(context, 12F)

            findViewById<RecyclerView>(R.id.rv).layoutManager =
                GridLayoutManager(context, SGDropDownInfoBean.useColumn)
            findViewById<RecyclerView>(R.id.rv).adapter = adapterColumn
        }

        Log.i("content-->==", findViewById<LinearLayout>(R.id.layout).id.toString())
    }

    override fun onShow() {
        super.onShow()
    }

    override fun onDismiss() {
        super.onDismiss()
    }
}