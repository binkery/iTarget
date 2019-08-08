package com.binkery.itarget.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import kotlinx.android.synthetic.main.activity_add_target.*
import kotlinx.android.synthetic.main.base_activity.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetActivity : BaseActivity() {

    private var mIntTargetType = 0
    private var mStringTargetName: String? = null

    override fun getContentLayoutId(): Int = R.layout.activity_add_target

    override fun onContentCreate(savedInstanceState: Bundle?) {

        vActionBarBack.setOnClickListener({
            finish()
        })
        vActionBarTitle.text = "新增目标"

        vTargetName.setValue("")
        vTargetName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val view = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_edittext, null)
                val vOk = view.findViewById<TextView>(R.id.vOk)
                val vCancel = view.findViewById<TextView>(R.id.vCancel)
                val vEditText = view.findViewById<EditText>(R.id.vEditText)
                vEditText.setText(vTargetName.getValue())
                val dialog = AlertDialog.Builder(this@AddTargetActivity)
                        .setTitle("输入目标名称").setView(view).create()
                vOk.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        vTargetName.setValue(vEditText.text.toString())
                        mStringTargetName = vEditText.text.toString()
                        dialog.dismiss()
                    }
                })
                vCancel.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        dialog.dismiss()
                    }
                })
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()

            }

        })

        vTargetType.setValue(TargetType.title(TargetType.COUNTER))
        vTargetType.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val titles = TargetType.titles()
                val dialog = AlertDialog.Builder(this@AddTargetActivity)
                        .setTitle("select").setSingleChoiceItems(titles, mIntTargetType, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        vTargetType.setValue(TargetType.title(which))
                        mIntTargetType = which
                        dialog?.dismiss()
                    }

                }).create()
                dialog.show()

            }

        })

        vSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(mStringTargetName)) {
                    Toast.makeText(this@AddTargetActivity, "Name is null ", Toast.LENGTH_LONG).show()
                    return
                }
                val entity = TargetEntity()
                entity.name = mStringTargetName
                entity.type = mIntTargetType
                entity.uuid = UUID.randomUUID().toString()
                DBHelper.getInstance().targetDao().insertTarget(entity)
                finish()
            }

        })

    }

}