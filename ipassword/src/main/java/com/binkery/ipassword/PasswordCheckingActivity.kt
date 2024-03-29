package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.SharedUtils
import com.binkery.ipassword.widgets.KeyBoardView
import kotlinx.android.synthetic.main.activity_password_checking.*

class PasswordCheckingActivity : BaseActivity() {

    companion object {

        fun start(activity: Activity, exit: Boolean) {
            val intent = Intent(activity, PasswordCheckingActivity::class.java)
            intent.putExtra("exit", exit)
            activity.startActivity(intent)
        }
    }

    private var mExit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = 0x000000
            window.navigationBarColor = 0x000000
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            window.navigationBarDividerColor = 0xff0000ff.toInt()
        }

//        setContentView(R.layout.activity_password_checking)
    }

    override fun getContentLayoutId(): Int = R.layout.activity_password_checking

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mExit = intent.getBooleanExtra("exit", true)

//        vAppbar.setTitle("隐私密码校验")
        vAppbar.hide()

        var password = ""
        vPasswordInput.setValue(password)
//        vTips.text = "请输入隐私密码"
        vKeyBoardView.setOnValueChangedListener(object : KeyBoardView.OnValueChangedListener {
            override fun onValueChanged(value: String) {
                if (password.length < 4) {
                    password += value
                    vPasswordInput.setValue(password)
                }
                if (password.length == 4) {
                    if (SharedUtils.checkPassword(this@PasswordCheckingActivity, password)) {
                        SharedUtils.updateToken(this@PasswordCheckingActivity)
                        finish()
                    } else {
                        Utils.toast(this@PasswordCheckingActivity, "密码错误")
                        password = ""
                        vPasswordInput.setValue(password)
                    }
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
        if (mExit) {
            (application as PasswordApplication).exit()
        } else {
            finish()
        }
    }

}