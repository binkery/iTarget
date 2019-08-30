package com.binkery.base.utils

import android.app.Activity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.binkery.base.R

class Dialogs {
    companion object {


        fun showDialog(activity: Activity, title: String, message: String, left: String, right: String, listener: OnConfromListener?) {

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
                dialog.dismiss()
            }
            vRight.setOnClickListener {
                dialog.dismiss()
                listener?.onConform()
            }
            dialog.show()
        }

        fun showDeleteDialog(activity: Activity, message: String, listener: OnConfromListener?) {
            showDialog(activity, "确认删除", message, "再想想", "删除", listener)
        }

    }

    interface OnConfromListener {
        fun onConform()
    }
}