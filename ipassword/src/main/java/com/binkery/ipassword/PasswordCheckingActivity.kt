package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.binkery.base.activity.BaseActivity
import com.binkery.ipassword.widgets.KeyBoardView
import kotlinx.android.synthetic.main.activity_password_checking.*

class PasswordCheckingActivity : BaseActivity() {

    companion object {

        fun start(fragment: Fragment, requestCode: Int) {
            val intent = Intent(fragment.activity, PasswordCheckingActivity::class.java)
            fragment.startActivityForResult(intent, requestCode)
        }

        fun start(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, PasswordCheckingActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_password_checking

    override fun onContentCreate(savedInstanceState: Bundle?) {
        var password = ""
        vPasswordInput.setValue(password)
        vTips.text = "请输入隐私密码"
        setTitle("隐私密码校验")
        vKeyBoardView.setOnValueChangedListener(object : KeyBoardView.OnValueChangedListener {
            override fun onValueChanged(value: String) {
                if (password.length < 4) {
                    password += value
                    vPasswordInput.setValue(password)
                }
                if (password.length == 4) {
                    val intent = Intent()
                    intent.putExtra("password", password)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            override fun onValueDeleted() {
                if (password.length > 1) {
                    password = password.substring(0, password.length - 1)
                } else {
                    password = ""
                }
                vPasswordInput.setValue(password)
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