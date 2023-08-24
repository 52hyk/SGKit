package com.sungrowpower.demo.activity

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.kuaimin.dropdown.R
import com.sungrowpower.kit.dropdown.impl.SGDropDownBaseView

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
class CustomSGDropDownBaseView(context: Context) : SGDropDownBaseView(context) {
    private val mData= mutableListOf<String>("Options1","Options2","Options3","Options4")
    private val adapter by lazy {
        MyAdapter(mData)
    }
    override fun getImplLayoutId(): Int {
        return R.layout.dropdown
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<RecyclerView>(R.id.rv).layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        findViewById<RecyclerView>(R.id.rv).adapter=adapter
        Log.i("content-->==",findViewById<LinearLayout>(R.id.layout).id.toString())
    }

    override fun onShow() {
        super.onShow()
    }

    override fun onDismiss() {
        super.onDismiss()
    }
}