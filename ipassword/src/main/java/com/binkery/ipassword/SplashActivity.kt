package com.binkery.ipassword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.binkery.base.activity.BaseActivity

class SplashActivity : BaseActivity() {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun getContentLayoutId(): Int = R.layout.activity_splash

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setActionBarEnable(false)
        mHandler.postDelayed({
            MainActivity.start(this)
            finish()
        }, 1000)

    }

}