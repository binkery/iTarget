package com.binkery.base.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun toast(context: Context, resId: Int) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
        }

        fun log(message: String) {
            Log.i("bky-ipw", message)
        }

        fun datetimeFormat(template: String, time: Long): String {
            val format = SimpleDateFormat(template)
            return format.format(Date(time))
        }
    }
}