package com.sungrowpower.demo.activity

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.kuaimin.dropdown.R
import com.sungrowpower.kit.dropdown.impl.PartShadowPopupView

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
class CustomPartShadowPopupView(context: Context) : PartShadowPopupView(context) {
    private val mData= mutableListOf<String>("张三","李四")
    private val adapter by lazy {
        MyAdapter(mData)
    }
    override fun getImplLayoutId(): Int {
        return R.layout.custom_part_shadow_popup2
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<RecyclerView>(R.id.rv).layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        findViewById<RecyclerView>(R.id.rv).adapter=adapter
    }

    override fun onShow() {
        super.onShow()
    }

    override fun onDismiss() {
        super.onDismiss()
    }
}