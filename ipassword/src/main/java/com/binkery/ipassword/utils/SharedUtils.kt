package com.binkery.ipassword.utils

import android.content.Context
import com.binkery.base.utils.SharedEditor

class SharedUtils {

    companion object {
        private val sToken = SharedEditor("token")
        private const val KEY_PASSWORD = "password"
        private const val KEY_TOKEN = "token"

        fun hasPassword(context: Context): Boolean {
            val value = sToken.get(context, KEY_PASSWORD, "")
            return value != null && value != ""
        }

        fun checkPassword(context: Context, password: String): Boolean {
            val value = sToken.get(context, KEY_PASSWORD, "")
            if (value != null && value == password) {
                return true
            }
            return false
        }

        fun setPassword(context: Context, password: String) {
            sToken.put(context, KEY_PASSWORD, password)
        }

        fun getPassword(context: Context): String {
            return sToken.get(context, KEY_PASSWORD, "")
        }

        fun isTokenTimeout(context: Context): Boolean {
            val value = sToken.get(context, KEY_TOKEN, "0")
            if (value.toLong() > 0) {
                return true
            }
            return false
        }

        fun updateToken(context: Context) {
            sToken.put(context, KEY_TOKEN, System.currentTimeMillis().toString())
        }

        fun cleanToken(context: Context) {
            sToken.put(context, KEY_TOKEN, "0")
        }

    }

}