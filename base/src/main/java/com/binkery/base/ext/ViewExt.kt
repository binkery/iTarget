package com.binkery.base.ext

import android.view.View

/**
 * Create by binkery@gmail.com
 * on 2019 09 26
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}