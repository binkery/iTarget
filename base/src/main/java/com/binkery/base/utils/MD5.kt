package com.binkery.base.utils

import java.security.MessageDigest

/**
 * Create by binkery@gmail.com
 * on 2019 09 05
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class MD5 {

    companion object {

        fun md5(string: String): String {

            val digest = MessageDigest.getInstance("MD5")
            val byteArray = digest.digest(string.toByteArray(Charsets.UTF_8))
            val builder = StringBuffer()
            for (b in byteArray) {
                val value = b.toInt() and 0xFF
                var hexStr = Integer.toHexString(value)
                if (hexStr.length < 2) {
                    hexStr = "0" + hexStr
                }
                builder.append(hexStr)
            }
            return builder.toString()
        }

    }
}