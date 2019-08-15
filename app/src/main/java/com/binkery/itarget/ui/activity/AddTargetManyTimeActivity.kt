package com.binkery.itarget.ui.activity

import android.app.Activity
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
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
    private val mTargetMatches = arrayOf("最多", "最少")
    private var mIntMatchType: Int = 0
    private var mMinute = 0

    override fun getContentLayoutId(): Int = R.layout.activity_add_target_many_time

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("target name")

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener({
            Dialogs.showEditTextDialog(this, mStringTargetName, "title", object : OnTextChangedListener {
                override fun onTextChanged(text: String) {
                    vTargetName.setValue(text)
                    mStringTargetName = text
                }
            })
        })

        vMatchType.setValue(mTargetMatches[mIntMatchType])
        vMatchType.setOnClickListener({
            Dialogs.showSingleChoiceItems(this, mTargetMatches, mIntMatchType, object : OnSingleChoiceItemSelectedListener {
                override fun onSelected(which: Int, item: String) {
                    mIntMatchType = which
                    vMatchType.setValue(item)

                }
            })
        })

        vMatchCount.setValue(mMinute.toString() + "minutes")
        vMatchCount.setOnClickListener({
            Dialogs.showEditTextDialog(this, "", "title", object : OnTextChangedListener {
                override fun onTextChanged(text: String) {
                    vMatchCount.setValue(text + "minutes")
                    mMinute = text.toInt()
                }
            })
        })


        vSave.setOnClickListener({
            val target = TargetEntity()
            target.uuid = UUID.randomUUID().toString()
            target.type = TargetType.MANY_TIME.value
            target.name = mStringTargetName
            target.data1 = mIntMatchType.toString()
            target.data2 = mMinute.toString()
            DBHelper.getInstance().targetDao().insertTarget(target)
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

}