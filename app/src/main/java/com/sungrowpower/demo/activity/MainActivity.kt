package com.sungrowpower.demo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.kuaimin.dropdown.R
import com.sungrowpower.kit.dropdown.SGDropDown
import com.sungrowpower.kit.dropdown.enums.DropDownPosition
import com.sungrowpower.kit.dropdown.enums.SGDropDownAnimation

/**
 * 创建日期：2023/8/15 on 13:35
 * 描述:
 * 作者:hyk
 * @author Administrator
 */
class MainActivity : Activity() {
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
                .hasShadowBg(false)
                .dropDownPosition(DropDownPosition.Bottom)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
                .show()
        }

        findViewById<View>(R.id.tv_click2).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .dropDownPosition(DropDownPosition.Top)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
                .show()
        }

        findViewById<View>(R.id.tv_click3).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                .dropDownAnimation(SGDropDownAnimation.ScaleAlphaFromRightTop)
                .dropDownPosition(DropDownPosition.Bottom)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
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