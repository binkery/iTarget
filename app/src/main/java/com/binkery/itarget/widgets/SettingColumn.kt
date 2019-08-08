package com.binkery.itarget.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.binkery.itarget.R
import com.binkery.itarget.utils.Utils
import kotlinx.android.synthetic.main.widget_setting_column.view.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class SettingColumn : RelativeLayout {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_setting_column, this, true)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SettingColumn, defStyleAttr, 0)
        vKey.text = typedArray.getText(R.styleable.SettingColumn_key)
        vValue.text = typedArray.getText(R.styleable.SettingColumn_value)
        typedArray.recycle()

        setBackgroundResource(R.color.color_white)
        setPadding(0, Utils.dip2px(context, 8), 0, Utils.dip2px(context, 8))
    }

    fun setKey(text: String) {
        vKey.text = text
    }

    fun setValue(text: String) {
        vValue.text = text
    }

    fun getValue(): String {
        return vValue.text.toString()
    }

}