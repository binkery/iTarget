package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.ipassword.widgets.KeyBoardView
import kotlinx.android.synthetic.main.activity_export_password.*

class ExportPasswordActivity : BasePasswordActivity() {

    companion object {
        private const val STATUS_FIRST = 1
        private const val STATUS_SECOND = 2
        fun start(activity: Activity) {
            val intent = Intent(activity, ExportPasswordActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private var mPassword = ""
    private var mFirstPassword = ""

    override fun getContentLayoutId(): Int = R.layout.activity_export_password

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("设置密码")

        vPasswordInput.setValue(mPassword)
        vKeyBoardView.setOnValueChangedListener(object : KeyBoardView.OnValueChangedListener {
            override fun onValueChanged(value: String) {


            }

            override fun onValueDeleted() {
            }

            override fun onReturn() {
            }

        })

    }

}