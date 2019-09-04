package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.base.utils.Dialogs
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.utils.RequestCode
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BasePasswordActivity(), MainPagerAdapter.OnSetPrimaryItemListener {


    private val mAdapter = MainPagerAdapter(supportFragmentManager, this)

    override fun onSetPrimaryItem(position: Int) {

    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle(R.string.app_name)

        DBHelper.instance.init(this)

        vViewPager.adapter = mAdapter

        vAddItem.setOnClickListener {
            AddItemActivity.start(this@MainActivity, -1)
        }

        vMainPage.setOnClickListener {
            vViewPager.setCurrentItem(0, true)
        }

        vSettingPage.setOnClickListener {
            vViewPager.setCurrentItem(1, true)
        }

    }

    override fun shouldCheckPassword(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        val password = SharedUtils.getPassword(this)
        if (password == "") {
            PasswordSettingActivity.start(this, "设置隐私密码", true)
        } else {
            if (SharedUtils.isTokenTimeout(this)) {
                PasswordCheckingActivity.start(this, true)
            } else {
                SharedUtils.updateToken(this)
            }
        }

    }

}
