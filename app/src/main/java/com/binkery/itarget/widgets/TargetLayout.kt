package com.binkery.itarget.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.binkery.itarget.R
import kotlinx.android.synthetic.main.widget_target_layout.view.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 13
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetLayout : RelativeLayout {
    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_target_layout, this, true)
    }


    fun setTargetType(text: String) {
        vTargetType.text = text
    }

//    fun setTargetTile(text: String) {
//        vTargetTitle.text = text
//    }

    fun setSettingClickListener(listener: View.OnClickListener) {
        vTargetSetting.setOnClickListener(listener)
    }


    fun setItem1(key: String, value: String, unit: String) {
        vItem1.setItem(key, value, unit)
    }

    fun setItem2(key: String, value: String, unit: String) {
        vItem2.setItem(key, value, unit)
    }

    fun setItem3(key: String, value: String, unit: String) {
        vItem3.setItem(key, value, unit)
    }
}