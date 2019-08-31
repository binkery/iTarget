package com.binkery.base.utils

import android.content.Context
import android.widget.Toast

class Utils {

    companion object {
        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun toast(context: Context, resId: Int) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
        }
    }
}