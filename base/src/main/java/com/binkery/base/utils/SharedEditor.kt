package com.binkery.base.utils

import android.content.Context
import android.content.SharedPreferences

class SharedEditor(private val name: String) {

    private var shared : SharedPreferences ?= null

    fun get(context: Context, key: String, defValue: String): String {
        val shared = getShared(context, name)
        return shared.getString(key, defValue)
    }

    fun put(context: Context, key: String, value: String) {
        val shared = getShared(context, name)
        shared.edit().putString(key, value).apply()
    }

    fun getShared(context: Context, name: String): SharedPreferences {
        if (shared == null) {
            shared = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
        return shared!!
    }

}