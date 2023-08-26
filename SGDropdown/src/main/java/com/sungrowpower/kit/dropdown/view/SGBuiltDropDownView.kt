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
import com.sungrowpower.kit.dropdown.adapter.MyGroupAdapter
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
        MyAdapter(mData).apply {
            setOnItemClickListener { adapter, view, position ->
                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }

                if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
                    SGDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(position, -1)
                }

                if (SGDropDownInfoBean.multiple) {
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

                if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
                    SGDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(position, -1)
                }
            }
        }
    }
    private val adapterColumn by lazy {
        MyColumnAdapter(mData).apply {
            setOnItemClickListener { adapter, view, position ->
                if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
                    SGDropDownInfoBean.sgOnClickOptionListener!!.onOptionClick(position, -1)
                }

                if (mData[position].isDisabled) {
                    return@setOnItemClickListener
                }
                if (SGDropDownInfoBean.multiple) {
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

                if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
                    SGDropDownInfoBean.sgOnClickOptionListener!!.onOptionChange(position, -1)
                }
            }
        }
    }

    private val adapterGroup by lazy {
        MyGroupAdapter(mGroupData,SGDropDownInfoBean)
    }

    override fun getImplLayoutId(): Int {
        return R.layout._sg_simple_drop_down
    }

    override fun onCreate() {
        super.onCreate()

        if (SGDropDownInfoBean.options.toString().contains("title")) {
            mGroupData = SGDropDownInfoBean.options as MutableList<SGGroupDataBean>
        } else {
            mData = SGDropDownInfoBean.options as MutableList<SGSimpleDataBean>
        }


        if (mData.size != 0) {

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
        } else {
            val params = findViewById<RecyclerView>(R.id.rv).layoutParams as MarginLayoutParams
            params.leftMargin = SGDropDownUtils.dp2px(context, 6F)
            params.rightMargin = SGDropDownUtils.dp2px(context, 6F)
            params.topMargin = SGDropDownUtils.dp2px(context, 12F)
            findViewById<RecyclerView>(R.id.rv).layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            findViewById<RecyclerView>(R.id.rv).adapter = adapterGroup
            adapterGroup.setGroupOnClickListener(object :SGGroupOnClickListener{
                override fun onGroupChange(childPos: Int, parentPos: Int) {
                    var parentDataList= mutableListOf<SGGroupBackDataBean>()

                    for (i in mGroupData.indices){
                        var parentData= mutableListOf<SGGroupChildBackDataBean>()
                        var isSelect=false
                        for (j in mGroupData[i].childData.indices){
                            if (mGroupData[i].childData[j].isChecked){
                                var childBackDataBean=SGGroupChildBackDataBean()
                                childBackDataBean.childPos=j
                                parentData.add(childBackDataBean)
                                isSelect=true
                            }
                        }
                        if (isSelect){
                            parentDataList.add(SGGroupBackDataBean(i,parentData))
                        }
                    }
                    if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
                        SGDropDownInfoBean.sgOnClickOptionListener!!.selectedValue(null,parentDataList)
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
        if (SGDropDownInfoBean.sgOnClickOptionListener != null) {
            SGDropDownInfoBean.sgOnClickOptionListener!!.selectedValue(tempSelect,null)
        }
    }
}