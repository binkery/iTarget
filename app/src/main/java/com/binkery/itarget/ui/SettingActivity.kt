package com.binkery.itarget.ui

import android.os.Bundle
import android.view.View
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 *
 *
 */
class SettingActivity : BaseActivity() {

    var mTargetId:Int ?= null

    override fun getContentLayoutId(): Int = R.layout.activity_setting

    override fun onContentCreate(savedInstanceState: Bundle?) {

        mTargetId = intent.getIntExtra("target_id", -1)

        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        vActionBarTitle.text = "设置"

        vTargetName.setKey("名称")
        vTargetName.setValue("xxx")
        vTargetName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
            }
        })

        vTargetRecord.setKey("打卡记录")
        vTargetRecord.setValue("")
        vTargetRecord.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Router.startTargetRecordActivity(this@SettingActivity, mTargetId!!)
            }
        })
    }

}