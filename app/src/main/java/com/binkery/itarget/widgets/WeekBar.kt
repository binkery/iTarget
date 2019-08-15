package com.binkery.itarget.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.binkery.itarget.R

/**
 * Create by binkery@gmail.com
 * on 2019 08 09
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class WeekBar : LinearLayout {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_weekbar, this, true)
        orientation = LinearLayout.HORIZONTAL
    }
}