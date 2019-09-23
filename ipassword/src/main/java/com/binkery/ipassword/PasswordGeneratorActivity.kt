package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_password_generator.*
import java.util.*

class PasswordGeneratorActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, PasswordGeneratorActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }

        private const val NUMBER = "1234567890"
        private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
        private const val SPECIAL = "!@#$%^&*()_+-="
    }


    override fun getContentLayoutId(): Int = R.layout.activity_password_generator

    override fun onContentCreate(savedInstanceState: Bundle?) {

        vAppbar.setTitle("随机密码")
        vAppbar.setRightItem("完成", -1, View.OnClickListener {

            val intent = Intent()
            intent.putExtra("password", vPassword.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

        vGenerator.setOnClickListener {
            val chars = ArrayList<String>()
            if (vCheckBoxNumber.isChecked) {
                chars.add(NUMBER)
            }
            if (vCheckBoxLower.isChecked) {
                chars.add(LOWER)
            }
            if (vCheckBoxUpper.isChecked) {
                chars.add(UPPER)
            }
            if (vCheckBoxSpecial.isChecked) {
                chars.add(SPECIAL)
            }
            if (chars.isEmpty()) {
                return@setOnClickListener
            }

            val count = vSeekBarLength.progress

            val builder = StringBuffer()
            val random = Random(System.currentTimeMillis())
            for (index in 0 until count) {
                val char = chars[index % chars.size]
                val next = random.nextInt(char.length)
                builder.append(char[next])
            }
            val charArray = builder.toString().toCharArray()
            for (index in 0 until charArray.size) {
                val position = random.nextInt(charArray.size)
                if (index == position) {
                    continue
                } else {
                    val temp = charArray[index]
                    charArray[index] = charArray[position]
                    charArray[position] = temp
                }
            }
            vPassword.text = String(charArray)
        }

        vSeekBarLength.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val targetProgress = if (progress < 6) {
                    6
                } else {
                    progress
                }
                if (fromUser) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar?.setProgress(targetProgress, true)
                    } else {
                        seekBar?.progress = targetProgress
                    }
                }
                vPasswordLength.text = "长度：$targetProgress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


    }


}