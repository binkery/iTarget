package com.binkery.ipassword.code

import com.binkery.base.utils.MD5
import com.binkery.base.utils.Utils
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Create by binkery@gmail.com
 * on 2019 09 05
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class CodeEntity {

    companion object {
        fun encode2File(path: String, json: String, password: String) {
            val file = File(path)
            file.writeText("ipw", Charsets.UTF_8)
            file.appendBytes(kotlin.ByteArray(1, { 1 }))
            file.appendText(MD5.md5(password))
//            file.appendText(encode(password, json))
            file.appendBytes(encode(password, json))
        }

        fun decodeFromFile(path: String, password: String): String? {
            val file = File(path)
            val fis = file.inputStream()
            val headArray = ByteArray(3)
            fis.read(headArray)
            Utils.log("head = " + String(headArray))
            val version = fis.read()//version
            Utils.log("version = " + version)
            val md5Array = ByteArray(32)
            fis.read(md5Array)// md5
            val md5 = String(md5Array)
            Utils.log("md5 = " + md5)
            if (MD5.md5(password) != md5) {
                return null
            }

            val baos = ByteArrayOutputStream()
            fis.copyTo(baos, 1024)
//            val data = String(baos.toByteArray(), Charsets.UTF_8)
            val result = decode(password, baos.toByteArray())
            Utils.log("result = " + result)
            return result
        }

        private fun encode(password: String, data: String): ByteArray {

            val byteArray = password.toByteArray(Charsets.UTF_8)
            var key: Int = 0
            for (b in byteArray) {
                key += (b.toInt() and 0xFF)
            }
            Utils.log("encode key = " + key)
            val dataArray = data.toByteArray(Charsets.UTF_8)
            val resultArray = ByteArray(dataArray.size)
            dataArray.forEachIndexed { index, byte ->
                resultArray[index] = ((byte + key) and 0xFF).toByte()
//                Utils.log("encode byte " + byte + " to " + resultArray[index])
            }
            return resultArray
        }

        private fun decode(password: String, dataArray: ByteArray): String {
            val byteArray = password.toByteArray(Charsets.UTF_8)
            var key = 0
            for (b in byteArray) {
                key += (b.toInt() and 0xFF)
            }
            Utils.log("decode key = " + key)
//            val dataArray = data.toByteArray(Charsets.UTF_8)
            val resultArray = ByteArray(dataArray.size)
            dataArray.forEachIndexed { index, byte ->
                resultArray[index] = ((byte - key) and 0xFF).toByte()
//                Utils.log("decode byte " + byte + " to " + resultArray[index])
            }
            return String(resultArray, Charsets.UTF_8)
        }
    }


}