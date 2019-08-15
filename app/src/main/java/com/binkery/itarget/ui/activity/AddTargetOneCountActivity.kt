package com.binkery.itarget.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnDateTimeChangedListener
import com.binkery.itarget.dialog.OnTextChangedListener
import kotlinx.android.synthetic.main.activity_add_target_one_count.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetOneCountActivity : BaseActivity() {

    private var mStringTargetName: String = ""
    private var mIntTargetMatch: Int = 0
    private val mTargetMatches = arrayOf("早于", "晚于")
    private var mTargetMatchHour: Int = 0
    private var mTargetMatchMinute: Int = 0

    override fun getContentLayoutId(): Int = R.layout.activity_add_target_one_count

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("设置达成条件")

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                Dialogs.showEditTextDialog(this@AddTargetOneCountActivity, mStringTargetName, "输入目标名称", object : OnTextChangedListener {
                    override fun onTextChanged(text: String) {
                        vTargetName.setValue(text)
                        mStringTargetName = text
                    }
                })

            }

        })

        vTargetMatch.setValue("早于")
        vTargetMatch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val dialog = AlertDialog.Builder(this@AddTargetOneCountActivity)
                        .setTitle("select").setSingleChoiceItems(mTargetMatches, mIntTargetMatch, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        mIntTargetMatch = which
                        vTargetMatch.setValue(mTargetMatches[which])
                        dialog?.dismiss()
                    }

                }).create()
                dialog.show()

            }

        })


        vTargetTime.setOnClickListener({
            Dialogs.showTimePicker(this, mTargetMatchHour, mTargetMatchMinute, object : OnDateTimeChangedListener {
                override fun onChanged(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
                    mTargetMatchHour = hour
                    mTargetMatchMinute = minute
                }
            })
        })

        vSave.setOnClickListener({

            val target = TargetEntity()
            target.name = mStringTargetName
            target.type = TargetType.ONE_COUNT.value
            target.data1 = mIntTargetMatch.toString()
            target.data2 = mTargetMatchHour.toString()
            target.data3 = mTargetMatchMinute.toString()
            DBHelper.getInstance().targetDao().insertTarget(target)
            setResult(Activity.RESULT_OK)
            finish()
        })

    }

}