package com.sungrowpower.demo.activity;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kuaimin.dropdown.R;
import com.sungrowpower.kit.dropdown.impl.SGDropDownBaseView;

/**
 * 创建日期：2023/8/23 on 15:56
 * 描述:
 * 作者:hyk
 */
public class TestEditView extends SGDropDownBaseView {
    public TestEditView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.test_edit;
    }
}
