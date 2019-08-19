package com.binkery.itarget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity

/**
 * Create by binkery@gmail.com
 * on 2019 08 19
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AboutActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, AboutActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_about

    override fun onContentCreate(savedInstanceState: Bundle?) {
    }

}