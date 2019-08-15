package com.binkery.itarget.router

import android.app.Activity
import android.content.Intent
import com.binkery.itarget.ui.*
import com.binkery.itarget.ui.TargetViewActivity
import com.binkery.itarget.ui.activity.OneCountActivity

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class Router {

    companion object {

        fun startTargetHomeActivity() {

        }

        fun startTargetViewActivity(activity: Activity, targetId: Int) {
            val intent = Intent(activity, TargetViewActivity::class.java)
            intent.putExtra("target_id", targetId)
            activity.startActivity(intent)
        }

        fun startAddTargetActivity(activity: Activity) {
            val intent = Intent(activity, AddTargetActivity::class.java)
            activity.startActivity(intent)
        }

        fun startAddItemActivity(activity: Activity, targetId: Int, itemId: Int?) {
//            val intent = Intent(activity, AddItemActivity::class.java)
//            intent.putExtra("target_id", targetId)
//            intent.putExtra("item_id", itemId)
//            activity.startActivity(intent)
        }

        fun startTargetRecordActivity(activity: Activity, targetId: Int) {
            val intent = Intent(activity, RecordActivity::class.java)
            intent.putExtra("target_id", targetId)
            activity.startActivity(intent)
        }


        fun startSettingActivity(activity: Activity, targetId: Int) {
            val intent = Intent(activity, SettingActivity::class.java)
            intent.putExtra("target_id", targetId)
            activity.startActivity(intent)
        }


        fun startEveryDayOnTimeActivity(activity: Activity, targetId: Int) {
            val intent = Intent(activity, OneCountActivity::class.java)
            intent.putExtra("target_id", targetId)
            activity.startActivity(intent)
        }

    }
}
