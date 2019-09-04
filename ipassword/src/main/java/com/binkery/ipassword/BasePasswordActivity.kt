package com.binkery.ipassword

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.RequestCode
import com.binkery.ipassword.utils.SharedUtils

abstract class BasePasswordActivity : BaseActivity() {

    companion object {
        private const val ACTION_EXIT_APP = "com.binkery.ipassword.EXIT_APP"
    }


    open fun shouldCheckPassword(): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter()
        filter.addAction(ACTION_EXIT_APP)
        LocalBroadcastManager.getInstance(this).registerReceiver(mExitAppReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mExitAppReceiver)
    }

    override fun onResume() {
        super.onResume()
        if (shouldCheckPassword()) {
            if (SharedUtils.hasPassword(this) && SharedUtils.isTokenTimeout(this)) {
                PasswordCheckingActivity.start(this, RequestCode.CHECK_PASSWORD)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.CHECK_PASSWORD && resultCode == Activity.RESULT_OK) {
            val password = data?.getStringExtra("password") ?: ""
            if (SharedUtils.checkPassword(this, password)) {
                SharedUtils.updateToken(this)
            } else {
                Utils.toast(this, "密码错误，请重新输入")
                PasswordCheckingActivity.start(this, 101)
            }

        } else if (requestCode == RequestCode.CHECK_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
            val intent = Intent(ACTION_EXIT_APP)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private val mExitAppReceiver:BroadcastReceiver  = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }

}