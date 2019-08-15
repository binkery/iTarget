package com.binkery.itarget.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.binkery.itarget.R
import kotlinx.android.synthetic.main.widget_target_static_item.view.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 13
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetStaticItem : RelativeLayout {
    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_target_static_item, this, true)
    }

    fun setItem(key: String, value: String, unit: String) {
        vKey.text = key
        vValue.text = value
        vUnit.text = unit
    }
}