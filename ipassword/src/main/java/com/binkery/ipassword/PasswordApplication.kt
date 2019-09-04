package com.binkery.ipassword

import android.app.Application
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class PasswordApplication : Application() {

    private val lifecycleCallbacks: PasswordActivityLifecycleCallbacks = PasswordActivityLifecycleCallbacks()

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(lifecycleCallbacks)

        val filter = IntentFilter()
        filter.addAction("com.binkery.ipassword.EXIT_APP")
        LocalBroadcastManager.getInstance(this).registerReceiver(ExitBroadcastReceiver(lifecycleCallbacks), filter)

    }

}