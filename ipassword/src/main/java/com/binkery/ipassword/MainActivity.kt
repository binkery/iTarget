package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.binkery.base.utils.Dialogs
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.utils.RequestCode
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BasePasswordActivity() {


    private val mAdapter = MainPagerAdapter(supportFragmentManager)

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

    inner class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MainItemFragment()
                1 -> MainSettingFragment()
                else -> MainItemFragment()
            }
        }

        override fun getCount(): Int = 2

        override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
            Log.i("bky", "setPrimaryItem " + position + ", obj = " + obj)
            when (position) {
                0 -> {
                    vMainPage.setTextColor(getColor(R.color.color_46A0F0))
                    vSettingPage.setTextColor(getColor(R.color.color_black))
                }
                1 -> {
                    vSettingPage.setTextColor(getColor(R.color.color_46A0F0))
                    vMainPage.setTextColor(getColor(R.color.color_black))
                }
            }
            super.setPrimaryItem(container, position, obj)
        }

    }
}
