package com.binkery.itarget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.binkery.itarget.BuildConfig
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnNumberChangedListener
import com.binkery.itarget.dialog.OnTextChangedListener
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.utils.Utils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class SettingActivity : BaseActivity() {


    companion object {
        fun start(activity: Activity, targetId: Int) {
            val intent = Intent(activity, SettingActivity::class.java)
            intent.putExtra("target_id", targetId)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_setting

    override fun onContentCreate(savedInstanceState: Bundle?) {

        val targetId = intent.getIntExtra("target_id", -1)
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(targetId)


        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        vActionBarTitle.text = "设置"

        vTargetName.setValue(targetEntity.name)
        vTargetName.setKey("目标名称")
        vTargetName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                Dialogs.showEditTextDialog(this@SettingActivity, targetEntity.name, "", object : OnTextChangedListener {
                    override fun onTextChanged(text: String) {
                        vTargetName.setValue(text)
                        DBHelper.getInstance().targetDao().updateTarget(targetEntity)
                        Utils.toast(applicationContext, "保存成功")
                    }
                })

            }

        })

        vTargetType.setKey("类型")
        vTargetType.setValue(TargetType.title(targetEntity.type))
        vTargetType.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Utils.toast(applicationContext, "暂不支持修改类型")
            }
        })

        vTargetMatch.setKey("目标")
        when {
            targetEntity.type == TargetType.MANY_COUNT.value -> {
                vTargetMatch.setValue(targetEntity.data1 + "次")
            }
            targetEntity.type == TargetType.MANY_TIME.value -> {
                vTargetMatch.setValue(targetEntity.data1 + "分钟")
            }
        }

        vTargetMatch.setOnClickListener({
            Dialogs.showNumberEditTextDialog(this, targetEntity.data1.toInt(), "", object : OnNumberChangedListener {
                override fun onChanged(value: Int) {
                    targetEntity.data1 = value.toString()
                    when {
                        targetEntity.type == TargetType.MANY_COUNT.value -> {
                            vTargetMatch.setValue(targetEntity.data1 + "次")
                        }
                        targetEntity.type == TargetType.MANY_TIME.value -> {
                            vTargetMatch.setValue(targetEntity.data1 + "分钟")
                        }
                    }
                    DBHelper.getInstance().targetDao().updateTarget(targetEntity)
                    Utils.toast(applicationContext, "目标修改成功")
                }
            })
        })

        vTargetRecord.setKey("打卡记录")
        vTargetRecord.setValue("")
        vTargetRecord.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Router.startTargetRecordActivity(this@SettingActivity, targetId)
            }
        })

        vAppVersion.setKey("版本")
        vAppVersion.setValue(BuildConfig.VERSION_NAME)
    }

}