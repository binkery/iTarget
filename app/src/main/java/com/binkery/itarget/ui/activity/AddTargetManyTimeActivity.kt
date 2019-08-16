package com.binkery.itarget.ui.activity

import android.app.Activity
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnNumberChangedListener
import com.binkery.itarget.dialog.OnSingleChoiceItemSelectedListener
import com.binkery.itarget.dialog.OnTextChangedListener
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetType
import kotlinx.android.synthetic.main.activity_add_target_many_time.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetManyTimeActivity : BaseActivity() {

    private var mStringTargetName = ""
    private var mIntTargetMatch = 30

    override fun getContentLayoutId(): Int = R.layout.activity_add_target_many_time

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("target name")

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener({
            Dialogs.showEditTextDialog(this, mStringTargetName, "title", object : OnTextChangedListener {
                override fun onTextChanged(text: String) {
                    vTargetName.setValue(text)
                    mStringTargetName = text
                    if (mStringTargetName == "") {
                        vSave.isEnabled = false
                        vSave.setBackgroundResource(R.color.color_gray)
                    } else {
                        vSave.isEnabled = true
                        vSave.setBackgroundResource(R.color.color_46A0F0)
                    }
                }
            })
        })

        vTargetMatch.setKey("每日累计计时")
        vTargetMatch.setValue(mIntTargetMatch.toString())
        vTargetMatch.setOnClickListener({
            Dialogs.showNumberEditTextDialog(this, mIntTargetMatch, "", object : OnNumberChangedListener {
                override fun onChanged(value: Int) {
                    if (value <= 0) {
                        mIntTargetMatch = 1
                    } else {
                        mIntTargetMatch = value
                    }
                }
            })
        })

        vSave.isEnabled = false
        vSave.setBackgroundResource(R.color.color_gray)

        vSave.setOnClickListener({
            val target = TargetEntity()
            target.uuid = UUID.randomUUID().toString()
            target.type = TargetType.MANY_TIME.value
            target.name = mStringTargetName
            target.data1 = mIntTargetMatch.toString()
            DBHelper.getInstance().targetDao().insertTarget(target)
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

}