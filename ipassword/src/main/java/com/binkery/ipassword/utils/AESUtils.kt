package com.binkery.ipassword.utils

import com.binkery.base.utils.MD5
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESUtils {

    companion object {

        private const val IV = "1234567890123456"

        fun encrypt(content: String, key: String): ByteArray {
            val keySpec = SecretKeySpec(MD5.md5(key).toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val paramSpec = IvParameterSpec(IV.toByteArray(Charsets.UTF_8))
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec)
            val encrypted = cipher.doFinal(content.toByteArray(Charsets.UTF_8))
            return encrypted
        }

        fun decode(content: ByteArray, key: String): ByteArray {
            val keySpec = SecretKeySpec(MD5.md5(key).toByteArray(), "AES")
            val paramSpec = IvParameterSpec(IV.toByteArray(Charsets.UTF_8))
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec)
            val result = cipher.doFinal(content)
            return result
        }

    }

}