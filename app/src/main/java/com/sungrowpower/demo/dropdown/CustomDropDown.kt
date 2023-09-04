package com.sungrowpower.demo.dropdown

import android.content.Context
import com.sungrowpower.demo.R
import com.sungrowpower.kit.dropdown.base.SGDropDownBaseView

/**
 * 创建日期：2023/8/25 on 10:55
 * 描述:
 * 作者:hyk
 */
public class CustomDropDown(context: Context) : SGDropDownBaseView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.sg_custom
    }
    override fun onCreate() {
        super.onCreate()
    }
}