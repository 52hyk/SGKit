package com.sungrowpower.demo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.sungrowpower.demo.R
import com.sungrowpower.demo.dropdown.CustomDropDown
import com.sungrowpower.kit.dropdown.SGDropDown
import com.sungrowpower.kit.dropdown.base.SGBaseView
import com.sungrowpower.kit.dropdown.bean.SGGroupBackDataBean
import com.sungrowpower.kit.dropdown.bean.SGGroupDataBean
import com.sungrowpower.kit.dropdown.bean.SGSimpleDataBean
import com.sungrowpower.kit.dropdown.enums.DropDownPosition
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation
import com.sungrowpower.kit.dropdown.interfaces.SGDropDownCallback
import com.sungrowpower.kit.dropdown.interfaces.SGOnClickOptionListener
import com.sungrowpower.kit.dropdown.view.SGBuiltDropDownView

/**
 * 创建日期：2023/8/15 on 13:35
 * 描述:
 * 作者:hyk
 * @author Administrator
 */
class MainActivity : Activity() {

    private val data = mutableListOf(SGSimpleDataBean("option", false, true),
        SGSimpleDataBean("option", false, false),
        SGSimpleDataBean("option", false, false),
        SGSimpleDataBean("option", true, false),
        SGSimpleDataBean("option", true, false))
    private val groupData = mutableListOf(SGGroupDataBean("Group1", data))

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView: View = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        findViewById<View>(R.id.tv_click).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .setOptions(data as List<Any>?)
                .dropDownPosition(DropDownPosition.Bottom)
                .setOnClickOptionListener(object :SGOnClickOptionListener{

                    override fun selectedValue(columnData: MutableList<Int>?, groupBackDataBeanList: MutableList<SGGroupBackDataBean>?, ) {

                    }

                    override fun onOptionClick(childPos: Int, parentPos: Int) {
                        Log.i("changer-Click", "childPos:$childPos,parentPos:$parentPos")

                    }

                    override fun onOptionChange(childPos: Int, parentPos: Int) {
                        Log.i("changer-Change", "childPos:$childPos,parentPos:$parentPos")

                    }
                })
                .dropDownView()
                .show()
        }

        findViewById<View>(R.id.tv_click2).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .setUseColumn(3)
                .setOptions(data as List<Any>?)
                .setMultiple(false)
                .dropDownPosition(DropDownPosition.Bottom)
                .customView(SGBuiltDropDownView(this@MainActivity))
                .show()
        }

        findViewById<View>(R.id.tv_click3).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .setOptions(data as List<Any>?)
                .dropDownPosition(DropDownPosition.Top)
                .dropDownView()
                .show()

        }
        findViewById<View>(R.id.tv_click4).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .setOptions(groupData as List<Any>?)
                .dropDownHeight(300)
                .dropDownPosition(DropDownPosition.Bottom)
                .customView(CustomDropDown(this@MainActivity))
                .show()

        }
        findViewById<View>(R.id.tv_click5).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .setOptions(groupData as List<Any>?)
                .dropDownPosition(DropDownPosition.Bottom)
                .setOnClickOptionListener(object :SGOnClickOptionListener{

                    override fun selectedValue(columnData: MutableList<Int>?, groupBackDataBeanList: MutableList<SGGroupBackDataBean>?, ) {
                        Log.i("changer-value", groupBackDataBeanList.toString())
                    }

                    override fun onOptionClick(childPos: Int, parentPos: Int) {
                        Log.i("changer-Click", "childPos:$childPos,parentPos:$parentPos")

                    }

                    override fun onOptionChange(childPos: Int, parentPos: Int) {
                        Log.i("changer-Change", "childPos:$childPos,parentPos:$parentPos")

                    }
                })
                .dropDownView()
                .show()

        }
    }

    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}