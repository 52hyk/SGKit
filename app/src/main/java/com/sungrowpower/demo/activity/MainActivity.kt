package com.sungrowpower.demo.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
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
                .isViewMode(true)
                .hasShadowBg(false)
                .popupPosition(DropDownPosition.Bottom)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
                //.popupView()
                .show()
        }

        findViewById<View>(R.id.tv_click2).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .isViewMode(true)
                .popupPosition(DropDownPosition.Top)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
                //.popupView()
                .show()
        }

        findViewById<View>(R.id.tv_click3).setOnClickListener { v ->
            SGDropDown.Builder(this@MainActivity)
                .atView(v)
                .isViewMode(true)
                .popupAnimation(SGDropDownAnimation.ScaleAlphaFromRightTop)
                .popupPosition(DropDownPosition.Bottom)
                .asCustom(CustomSGDropDownBaseView(this@MainActivity))
                .show()
        }
    }
}