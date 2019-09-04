package com.binkery.base.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.binkery.base.R

class Dialogs {
    companion object {


        fun showDialog(activity: Activity, title: String, message: String, left: String, right: String, leftListener: View.OnClickListener?, rightListener: View.OnClickListener?) {

            val view = LayoutInflater.from(activity).inflate(R.layout.base_dialog_conform, null, false)
            val vLeft = view.findViewById<TextView>(R.id.vLeft)
            vLeft.text = left
            val vRight = view.findViewById<TextView>(R.id.vRight)
            vRight.text = right
            val vTitle = view.findViewById<TextView>(R.id.vTitle)
            vTitle.text = title
            val vMessage = view.findViewById<TextView>(R.id.vMessage)
            vMessage.text = message

            val dialog = AlertDialog.Builder(activity).setView(view).create()

            vLeft.setOnClickListener {
                leftListener?.onClick(vLeft)
                dialog.dismiss()
            }
            vRight.setOnClickListener {
                rightListener?.onClick(vRight)
                dialog.dismiss()
            }
            dialog.show()
        }

        fun showDeleteDialog(activity: Activity, message: String, listener: OnConfromListener?) {
            showDialog(activity, "确认删除", message, "再想想", "删除", null, View.OnClickListener {
                listener?.onConform()
            })
        }

        fun showExitDialog(activity: Activity, message: String, listener: OnConfromListener?) {
            showDialog(activity, "确认退出", message, "再想想", "退出", null, View.OnClickListener {
                listener?.onConform()
            })
        }

//        fun showSelectedDialog(activity: Activity, message: String, left: String, right: String, listener: OnSelectedListener) {
//            showDialog(activity,)
//        }

    }

    interface OnConfromListener {
        fun onConform()
    }

    interface OnSelectedListener {
        fun onLeftSelected()
        fun onRightSelected()
    }
}