package com.binkery.ipassword

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.binkery.base.utils.Utils
import com.binkery.ipassword.utils.SharedUtils

class PasswordApplication : Application() {

    private val activities: ArrayList<Activity> = ArrayList()

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {

                activity?.apply {
                    if (this is BasePasswordActivity && this.shouldCheckPassword()) {
                        if (SharedUtils.hasPassword(this) && SharedUtils.isTokenTimeout(this)) {
                            PasswordCheckingActivity.start(this, true)
                        }
                    }
                }
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                activities.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity?.apply {
                    activities.add(this)
                }
            }
        })

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Utils.log("onTrimMemory level = " + level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            SharedUtils.cleanToken(this)
        }
    }

    fun exit() {
        activities.forEach {
            it.finish()
        }
        System.exit(0)
    }

}