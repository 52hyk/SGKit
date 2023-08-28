package com.sungrowpower.kit.dropdown.view

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sungrowpower.kit.R
import com.sungrowpower.kit.dropdown.adapter.SGAdapter
import com.sungrowpower.kit.dropdown.adapter.SGColumnAdapter
import com.sungrowpower.kit.dropdown.adapter.SGGroupAdapter
import com.sungrowpower.kit.dropdown.bean.SGGroupBackDataBean
import com.sungrowpower.kit.dropdown.bean.SGGroupBackDataBean.SGGroupChildBackDataBean
import com.sungrowpower.kit.dropdown.bean.SGGroupDataBean
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean
import com.sungrowpower.kit.dropdown.base.SGDropDownBaseView
import com.sungrowpower.kit.dropdown.interfaces.SGGroupOnClickListener
import com.sungrowpower.kit.dropdown.util.SGDropDownUtils

/**
 * Description:内置View显示
 * Create by dance, at 2018/12/21
 */
class SGBuiltDropDownView(context: Context) : SGDropDownBaseView(context) {
    private var mData = mutableListOf<SGSimpleDataBean>()
    private var mGroupData = mutableListOf<SGGroupDataBean>()

    private val adapter by lazy {
        SGAdapter(mData, sgDropDownInfoBean).apply {
            setOnItemClickListener { adapter, view, position ->
                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }

                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(position, -1)
                }

                if (sgDropDownInfoBean.isMultiple) {
                    mData[position].isChecked = !mData[position].isChecked
                    notifyItemChanged(position)

                    setSelect()
                } else {
                    for (i in mData.indices) {
                        mData[i].isChecked = false
                    }
                    mData[position].isChecked = true
                    notifyDataSetChanged()

                    setSelect()
                }

                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(position, -1)
                }
            }
        }
    }
    private val adapterColumn by lazy {
        SGColumnAdapter(mData, sgDropDownInfoBean).apply {
            setOnItemClickListener { adapter, view, position ->
                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(position, -1)
                }

                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }
                if (sgDropDownInfoBean.isMultiple) {
                    mData[position].isChecked = !mData[position].isChecked
                    notifyItemChanged(position)

                    setSelect()
                } else {
                    for (i in mData.indices) {
                        mData[i].isChecked = false
                    }
                    mData[position].isChecked = true
                    notifyDataSetChanged()

                    setSelect()
                }

                if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                    sgDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(position, -1)
                }
            }
        }
    }

    private val adapterGroup by lazy {
        SGGroupAdapter(mGroupData, sgDropDownInfoBean)
    }

    override fun getImplLayoutId(): Int {
        return R.layout._sg_simple_drop_down
    }

    override fun onCreate() {
        super.onCreate()


        mGroupData = sgDropDownInfoBean.options as MutableList<SGGroupDataBean>
        if (mGroupData.size != 0){
            if (!mGroupData[0].isGroup) {
                mData = mGroupData[0].childData
            }
        }
        if (mData.size != 0) {

            if (sgDropDownInfoBean.useColumn == 0) {
//                val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
//                params.bottomMargin = SGDropDownUtils.dp2px(context, 4F)
//                params.topMargin = SGDropDownUtils.dp2px(context, 4F)
                findViewById<RecyclerView>(R.id.rv).layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                findViewById<RecyclerView>(R.id.rv).adapter = adapter
            } else {
                val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
                params.leftMargin = SGDropDownUtils.dp2px(context, 5F)
                params.rightMargin = SGDropDownUtils.dp2px(context, 5F)
                params.topMargin = SGDropDownUtils.dp2px(context, 8F)

                findViewById<RecyclerView>(R.id.rv).layoutManager =
                    GridLayoutManager(context, sgDropDownInfoBean.useColumn)
                findViewById<RecyclerView>(R.id.rv).adapter = adapterColumn
            }
        } else {
            val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
            params.leftMargin = SGDropDownUtils.dp2px(context, 5F)
            params.rightMargin = SGDropDownUtils.dp2px(context, 5F)
            params.topMargin = SGDropDownUtils.dp2px(context, 8F)
            findViewById<RecyclerView>(R.id.rv).layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            findViewById<RecyclerView>(R.id.rv).adapter = adapterGroup
            adapterGroup.setGroupOnClickListener(object : SGGroupOnClickListener {
                override fun onGroupChange(childPos: Int, parentPos: Int) {
                    var parentDataList = mutableListOf<SGGroupBackDataBean>()

                    for (i in mGroupData.indices) {
                        var parentData = mutableListOf<SGGroupChildBackDataBean>()
                        var isSelect = false
                        for (j in mGroupData[i].childData.indices) {
                            if (mGroupData[i].childData[j].isChecked) {
                                var childBackDataBean = SGGroupChildBackDataBean()
                                childBackDataBean.childPos = j
                                parentData.add(childBackDataBean)
                                isSelect = true
                            }
                        }
                        if (isSelect) {
                            parentDataList.add(SGGroupBackDataBean(i, parentData))
                        }
                    }
                    if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
                        sgDropDownInfoBean.sgOnClickOptionListener!!.selectedValue(
                            null,
                            parentDataList
                        )
                    }

                }

            })
        }
        Log.i("content-->==", findViewById<LinearLayout>(R.id.layout).id.toString())
    }


    public fun setSelect() {
        var tempSelect = mutableListOf<Int>()
        for (i in mData.indices) {
            if (mData[i].isChecked) {
                tempSelect.add(i)
            }
        }
        if (sgDropDownInfoBean.sgOnClickOptionListener != null) {
            sgDropDownInfoBean.sgOnClickOptionListener!!.selectedValue(tempSelect, null)
        }
    }
}