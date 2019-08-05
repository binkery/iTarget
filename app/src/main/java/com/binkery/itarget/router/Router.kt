package com.binkery.itarget.router

import android.app.Activity
import android.content.Intent
import com.binkery.itarget.ui.*

/**
 *
 *
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

        fun startAddItemActivity(activity: Activity, itemId: Int?) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivity(intent)
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


    }
}
