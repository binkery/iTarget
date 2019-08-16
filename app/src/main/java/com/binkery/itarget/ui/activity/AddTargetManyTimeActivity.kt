package com.binkery.itarget.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnNumberChangedListener
import com.binkery.itarget.dialog.OnTextChangedListener
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.utils.Utils
import kotlinx.android.synthetic.main.activity_add_target_many_time.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddTargetManyTimeActivity : BaseActivity() {


    companion object {
        fun start(activity: Activity, targetType: Int, requestCode: Int) {
            val intent = Intent(activity, AddTargetManyTimeActivity::class.java)
            intent.putExtra("target_type", targetType)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var mStringTargetName = ""
    private var mIntTargetMatch = 30
    private var mTargetType = TargetType.MANY_COUNT

    override fun getContentLayoutId(): Int = R.layout.activity_add_target_many_time

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("添加目标")

        mTargetType = TargetType.find(intent.getIntExtra("target_type", TargetType.MANY_COUNT.value))

        vTargetName.setValue(mStringTargetName)
        vTargetName.setOnClickListener({
            Dialogs.showEditTextDialog(this, mStringTargetName, "title", object : OnTextChangedListener {
                override fun onTextChanged(text: String) {
                    vTargetName.setValue(text)
                    mStringTargetName = text
                }
            })
        })

        when (mTargetType) {
            TargetType.MANY_COUNT -> {
                mIntTargetMatch = 3
                vTargetMatch.setKey("每次打卡次数")
                vTargetMatch.setValue(mIntTargetMatch.toString() + "次")
            }
            TargetType.MANY_TIME -> {
                mIntTargetMatch = 30
                vTargetMatch.setKey("每日累计计时")
                vTargetMatch.setValue(mIntTargetMatch.toString() + "分钟")
            }
        }


        vTargetMatch.setOnClickListener({
            Dialogs.showNumberEditTextDialog(this, mIntTargetMatch, "", object : OnNumberChangedListener {
                override fun onChanged(value: Int) {
                    if (value <= 0) {
                        mIntTargetMatch = 1
                    } else {
                        mIntTargetMatch = value
                    }
                    when (mTargetType) {
                        TargetType.MANY_COUNT -> {
                            vTargetMatch.setValue(mIntTargetMatch.toString() + "次")
                        }
                        TargetType.MANY_TIME -> {
                            vTargetMatch.setValue(mIntTargetMatch.toString() + "分钟")
                        }
                    }
                }
            })
        })

        vSave.setOnClickListener({

            if (mStringTargetName.isBlank()) {
                Utils.toast(applicationContext, "未输入目标名称！")
                return@setOnClickListener
            }

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