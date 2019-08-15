package com.binkery.itarget.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class Utils {

    companion object {

        fun dip2px(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density + 0.5f).toInt()
        }

        fun sp2px(context: Context, sp: Int): Int {
            val scaledDensity = context.resources.displayMetrics.scaledDensity
            return (sp * scaledDensity + 0.5f).toInt()
        }

        fun px2dip(context: Context, px: Int): Int {
            val scale = context.resources.displayMetrics.density
            return (px / scale + 0.5f).toInt()
        }

        fun px2sp(context: Context, px: Int): Int {
            val scale = context.resources.displayMetrics.scaledDensity
            return (px / scale + 0.5f).toInt()
        }

        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun log(message: String) {
            Log.i("iTarget", message)
        }

        fun log(tag: String, message: String) {
            Log.i("iTarget-" + tag, message)
        }

    }
}