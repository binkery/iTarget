package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class AboutActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, AboutActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_about

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle("关于")
    }

}