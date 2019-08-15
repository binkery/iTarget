package com.binkery.itarget.dialog

/**
 * Create by binkery@gmail.com
 * on 2019 08 13
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
interface OnDateTimeChangedListener {
    fun onChanged(year: Int, month: Int, day: Int, hour: Int, minute: Int)
}