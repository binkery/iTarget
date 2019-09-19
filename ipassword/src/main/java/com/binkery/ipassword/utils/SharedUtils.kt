package com.binkery.ipassword.utils

import android.content.Context
import com.binkery.base.utils.SharedEditor

class SharedUtils {

    companion object {
        private val sToken = SharedEditor("token")
        private const val KEY_PASSWORD = "password"
        private const val KEY_TOKEN = "token"
        private const val AUTO_BACKUP = "auto_backup"
        private const val KEY_EXPORT_KEY = "export_key"

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
                return false
            }
            return true
        }

        fun updateToken(context: Context) {
            sToken.put(context, KEY_TOKEN, System.currentTimeMillis().toString())
        }

        fun cleanToken(context: Context) {
            sToken.put(context, KEY_TOKEN, "0")
        }

        fun isOpenAutoBackup(context: Context): Boolean {
            val value = sToken.get(context, AUTO_BACKUP, "0")
            return value == "1"
        }

        fun openAutoBackup(context: Context, password: String) {
            sToken.put(context, AUTO_BACKUP, "1")
            sToken.put(context, KEY_EXPORT_KEY, password)
        }

        fun closeAutoBackup(context: Context) {
            sToken.put(context, AUTO_BACKUP, "0")
        }

        fun getExportKey(context: Context): String {
            return sToken.get(context, KEY_EXPORT_KEY, "")
        }

    }

}