package com.binkery.ipassword

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ExitBroadcastReceiver(private val lifecycle: PasswordActivityLifecycleCallbacks) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("bky", "onReceive " + intent?.action)
        lifecycle.exit()
    }

}