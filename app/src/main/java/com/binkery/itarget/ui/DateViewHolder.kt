package com.binkery.itarget.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.binkery.itarget.R

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val vDate: TextView
    val vCount: TextView

    init {
        vDate = view.findViewById(R.id.vDate)
        vCount = view.findViewById(R.id.vCount)
    }
}