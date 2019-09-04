package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.SharedUtils
import com.binkery.ipassword.widgets.KeyBoardView
import kotlinx.android.synthetic.main.activity_password_setting.*

/**
 * 密码设置和修改
 */
class PasswordSettingActivity : BaseActivity() {

    private var mStatus = STATUS_PASSWORD_FIRST

    private var mPassword = ""
    private var mFirstPassword = ""


    companion object {

        private const val STATUS_PASSWORD_FIRST = 2
        private const val STATUS_PASSWORD_SECOND = 3

        fun start(activity: Activity, title: String, requestCode: Int) {
            val intent = Intent(activity, PasswordSettingActivity::class.java)
            intent.putExtra("title", title)
            activity.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment, title: String, requestCode: Int) {
            val intent = Intent(fragment.activity, PasswordSettingActivity::class.java)
            intent.putExtra("title", title)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_password_setting

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle(intent.getStringExtra("title"))

        vTips.text = "请输入新密码"
        vPasswordInput.setValue(mPassword)
        vKeyBoardView.setOnValueChangedListener(object : KeyBoardView.OnValueChangedListener {
            override fun onValueChanged(value: String) {
                if (mPassword.length < 4) {
                    mPassword += value
                    vPasswordInput.setValue(mPassword)
                }
                if (mPassword.length == 4) {
                    when (mStatus) {
                        STATUS_PASSWORD_SECOND -> {
                            if (mPassword == mFirstPassword) {
                                val intent = Intent()
                                intent.putExtra("password", mPassword)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            } else {
                                Utils.toast(this@PasswordSettingActivity, "两次密码不一致")
                                mPassword = ""
                                mFirstPassword = ""
                                mStatus = STATUS_PASSWORD_FIRST
                                vPasswordInput.setValue(mPassword)
                                vTips.text = "请输入新密码"
                            }
                        }
                        STATUS_PASSWORD_FIRST -> {
                            mStatus = STATUS_PASSWORD_SECOND
                            mFirstPassword = mPassword
                            mPassword = ""
                            vPasswordInput.setValue(mPassword)
                            vTips.text = "请再次输入新密码"
                        }
                    }
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

    override fun onBackClick() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}