package com.binkery.ipassword

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class PasswordActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val activities: ArrayList<Activity> = ArrayList()

    fun exit() {
        Log.i("bky", "stack size = " + activities.size)
        activities.forEach {
            Log.i("bky", "exit " + it.localClassName)
            it.finish()
        }
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.i("bky", "activity destory " + activity?.localClassName)
        activities.remove(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.i("bky", "activity create " + activity?.localClassName)
        activity?.apply {
            activities.add(activity)
        }
    }

}