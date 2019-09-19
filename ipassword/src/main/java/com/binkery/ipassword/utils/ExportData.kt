package com.binkery.ipassword.utils

import android.content.Context
import android.os.Environment
import com.binkery.base.utils.Utils
import com.binkery.ipassword.code.CodeEntity
import com.binkery.ipassword.sqlite.DBHelper
import com.google.gson.Gson
import java.io.File

class ExportData {
    companion object {

        fun export(context: Context, password: String): String {
            val data = DBHelper.instance.itemDao().queryAll()
            val json = Gson().toJson(data)
            val path = getFilePath(context)
            CodeEntity.encode2File(path, json, password)
            return path
        }


        private fun getFilePath(context: Context): String {
            val fileName = Utils.datetimeFormat("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()) + ".pwd"
            val exportPath = File(getRootPath(context), fileName)
            return exportPath.absolutePath
        }

        fun getRootPath(context: Context): String {
            val dir = "ipassword"
            val directoryPath: String =
                    if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {//判断外部存储是否可用
                        Environment.getExternalStorageDirectory().absolutePath + File.separator + dir
                    } else {//没外部存储就使用内部存储
                        context.filesDir.toString() + File.separator + dir
                    }
            val file = File(directoryPath)
            if (!file.exists()) {//判断文件目录是否存在
                file.mkdirs()
            }
            return file.absolutePath
        }
    }
}