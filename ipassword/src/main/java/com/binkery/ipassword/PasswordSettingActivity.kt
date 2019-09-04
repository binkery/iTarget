package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.SharedUtils
import com.binkery.ipassword.widgets.KeyBoardView
import kotlinx.android.synthetic.main.activity_password_setting.*

/**
 * 密码设置和修改
 */
class PasswordSettingActivity : BaseActivity() {

    private var mStatus = STATUS_PRE

    private var mPassword = ""
    private var mFirstPassword = ""
    private var mExit = true


    companion object {

        private const val STATUS_PRE = 1
        private const val STATUS__FIRST = 2
        private const val STATUS__SECOND = 3

        fun start(activity: Activity, title: String, exit: Boolean) {
            val intent = Intent(activity, PasswordSettingActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("exit", exit)
            activity.startActivity(intent)
        }

    }

    override fun getContentLayoutId(): Int = R.layout.activity_password_setting

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mExit = intent.getBooleanExtra("exit", true)
        setTitle(intent.getStringExtra("title"))

        mStatus = if (SharedUtils.hasPassword(this)) STATUS_PRE else STATUS__FIRST
        if (mStatus == STATUS_PRE) {
            vTips.text = "请输入原密码"
        } else if (mStatus == STATUS__FIRST) {
            vTips.text = "请输入新密码"
        }
        vPasswordInput.setValue(mPassword)


        vKeyBoardView.setOnValueChangedListener(object : KeyBoardView.OnValueChangedListener {
            override fun onValueChanged(value: String) {
                if (mPassword.length < 4) {
                    mPassword += value
                    vPasswordInput.setValue(mPassword)
                }
                if (mPassword.length == 4) {
                    checkAndUpdateStatus()
                }
            }

            override fun onValueDeleted() {
                if (mPassword.length > 1) {
                    mPassword = mPassword.substring(0, mPassword.length - 1)
                } else {
                    mPassword = ""
                }
                vPasswordInput.setValue(mPassword)
            }

            override fun onReturn() {
                onBackClick()
            }

        })
    }

    private fun checkAndUpdateStatus() {
        when (mStatus) {
            STATUS_PRE -> {
                if (SharedUtils.checkPassword(this, mPassword)) {
                    mStatus = STATUS__FIRST
                    vTips.text = "请输入新密码"
                } else {
                    Utils.toast(this, "密码错误")
                }
            }
            STATUS__FIRST -> {
                mFirstPassword = mPassword
                mStatus = STATUS__SECOND
                vTips.text = "请再次输入新密码"
            }
            STATUS__SECOND -> {
                if (mPassword == mFirstPassword) {
                    SharedUtils.setPassword(this, mPassword)
                    SharedUtils.updateToken(this)
                    Utils.toast(this, "密码设置成功！")
                    finish()
                } else {
                    Utils.toast(this@PasswordSettingActivity, "两次密码不一致")
                    mFirstPassword = ""
                    mStatus = STATUS__FIRST
                    vTips.text = "请输入新密码"
                }
            }
        }
        mPassword = ""
        vPasswordInput.setValue(mPassword)
    }

    override fun onBackClick() {
        if (mExit) {
            (application as PasswordApplication).exit()
        } else {
            finish()
        }
    }
}