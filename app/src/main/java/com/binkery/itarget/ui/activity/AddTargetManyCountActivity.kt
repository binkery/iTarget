package com.binkery.itarget.ui.activity

import android.app.Activity
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnNumberChangedListener
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
    private var mIntMatchCount: Int = 1


    override fun getContentLayoutId(): Int = R.layout.activity_add_target_many_count

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("设置目标")

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener({
            Dialogs.showEditTextDialog(this@AddTargetManyCountActivity, mStringTargetName, "", object : OnTextChangedListener {

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

        vTargetMatch.setKey("每次打卡次数")
        vTargetMatch.setValue(mIntMatchCount.toString())
        vTargetMatch.setOnClickListener({
            Dialogs.showNumberEditTextDialog(this, mIntMatchCount, "", object : OnNumberChangedListener {
                override fun onChanged(value: Int) {
                    if (value <= 0) {
                        mIntMatchCount = 1
                    } else {
                        mIntMatchCount = value
                    }
                    vTargetMatch.setValue(mIntMatchCount.toString())
                }
            })
        })

        vSave.isEnabled = false
        vSave.setBackgroundResource(R.color.color_gray)
        vSave.setOnClickListener({
            val target = TargetEntity()
            target.uuid = UUID.randomUUID().toString()
            target.type = TargetType.MANY_COUNT.value
            target.name = mStringTargetName
            target.data1 = mIntMatchCount.toString()
            DBHelper.getInstance().targetDao().insertTarget(target)
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

}