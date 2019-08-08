package com.binkery.itarget.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.binkery.itarget.BuildConfig
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
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
                val view = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_edittext, null)
                val vOk = view.findViewById<TextView>(R.id.vOk)
                val vCancel = view.findViewById<TextView>(R.id.vCancel)
                val vEditText = view.findViewById<EditText>(R.id.vEditText)
                vEditText.setText(vTargetName.getValue())
                val dialog = AlertDialog.Builder(this@SettingActivity)
                        .setTitle("输入目标名称").setView(view).create()
                vOk.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        vTargetName.setValue(vEditText.text.toString())
                        dialog.dismiss()
                        targetEntity.name = vEditText.text.toString()
                        DBHelper.getInstance().targetDao().updateTarget(targetEntity)

                        Utils.toast(applicationContext, "保存成功")
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

        vTargetType.setKey("类型")
        vTargetType.setValue(TargetType.title(targetEntity.type))
        vTargetType.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Utils.toast(applicationContext, "暂不支持修改类型")
            }
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