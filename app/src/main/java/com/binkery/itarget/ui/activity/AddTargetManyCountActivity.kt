package com.binkery.itarget.ui.activity

import android.app.Activity
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnSingleChoiceItemSelectedListener
import com.binkery.itarget.dialog.OnTextChangedListener
import kotlinx.android.synthetic.main.activity_add_target_many_count.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetManyCountActivity : BaseActivity() {

    private var mStringTargetName: String = ""
    private val mTargetMatches = arrayOf("最多", "最少")
    private var mIntMatchType: Int = 0

    private val mMatchValues = arrayOf("1", "2", "3", "4", "5")
    private var mIntMatchValue: Int = 0


    override fun getContentLayoutId(): Int = R.layout.activity_add_target_many_count

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("设置目标")

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener({
            Dialogs.showEditTextDialog(this@AddTargetManyCountActivity, mStringTargetName, "", object : OnTextChangedListener {

                override fun onTextChanged(text: String) {
                    vTargetName.setValue(text)
                    mStringTargetName = text

                }
            })
        })

        vMatchType.setValue(mTargetMatches[mIntMatchType])
        vMatchType.setOnClickListener({
            Dialogs.showSingleChoiceItems(this@AddTargetManyCountActivity, mTargetMatches, mIntMatchType, object : OnSingleChoiceItemSelectedListener {
                override fun onSelected(which: Int, item: String) {
                    mIntMatchType = which
                    vMatchType.setValue(item)

                }
            })
        })

        vMatchCount.setValue(mMatchValues[mIntMatchValue])
        vMatchCount.setOnClickListener({
            Dialogs.showSingleChoiceItems(this@AddTargetManyCountActivity, mMatchValues, mIntMatchValue, object : OnSingleChoiceItemSelectedListener {
                override fun onSelected(which: Int, item: String) {
                    mIntMatchValue = which
                    vMatchCount.setValue(item)
                }
            })
        })


        vSave.setOnClickListener({
            val target = TargetEntity()
            target.uuid = UUID.randomUUID().toString()
            target.type = TargetType.MANY_COUNT.value
            target.name = mStringTargetName
            target.data1 = mIntMatchType.toString()
            target.data2 = mIntMatchValue.toString()
            DBHelper.getInstance().targetDao().insertTarget(target)
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

}