package com.binkery.ipassword.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.binkery.ipassword.R

class PasswordDialog {

    companion object {
        fun show(activity: Activity, title: String, password: String, listener: OnComfirmListener) {
            val view = LayoutInflater.from(activity).inflate(R.layout.dialog_password, null, false)
            val vOk = view.findViewById<TextView>(R.id.vOk)
            val vCancel = view.findViewById<TextView>(R.id.vCancel)

            val vPasswordInput = view.findViewById<EditText>(R.id.vPasswordInput)
            val vTitle = view.findViewById<TextView>(R.id.vTitle)

            vTitle.text = title
            vPasswordInput.setText(password)

            val dialog = AlertDialog.Builder(activity).setView(view).create()
            vOk.setOnClickListener {
                listener.onComfirm(vPasswordInput.text.toString())
                dialog.dismiss()
            }
            vCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

        }
    }


    interface OnComfirmListener {
        fun onComfirm(password:String)
    }
}