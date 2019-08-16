package com.binkery.itarget.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnTextChangedListener
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.ui.activity.BaseTargetDetailActivity
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.activity_check_in.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 15
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class CheckInActivity : BaseActivity() {

    private var mTargetId: Int = 0
    private var mTargetType = TargetType.MANY_TIME

    override fun getContentLayoutId(): Int = R.layout.activity_check_in

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        mTargetType = TargetType.find(targetEntity.type)

        vAddItem.setOnClickListener({
            AddItemActivity.startResult(this, mTargetId, -1, 101)
        })

        vTargetFlow.setOnClickListener({
            val intent = Intent(this, BaseTargetDetailActivity::class.java)
            intent.putExtra("target_id", mTargetId)
            startActivity(intent)
        })

        updateView()
    }


    private fun updateView() {
        when (mTargetType) {
            TargetType.MANY_TIME -> {
                val itemEntity = DBHelper.getInstance().itemDao().queryItemEndTimeNull(mTargetId)
                if (itemEntity == null) {
                    vStartTarget.text = "开始打卡"
                    vStartTime.visibility = View.GONE
                    vStartTarget.setOnClickListener({
                        val item = ItemEntity()
                        item.targetId = mTargetId
                        item.uuid = UUID.randomUUID().toString()
                        item.startTime = System.currentTimeMillis() / Const.ONE_MINUTE * Const.ONE_MINUTE
                        DBHelper.getInstance().itemDao().insertItem(item)
                        updateView()
                    })
                } else {
                    vStartTime.visibility = View.VISIBLE
                    vStartTime.text = "开始时间：" + TextFormater.dataTimeWithoutSecond(itemEntity.startTime)
                    vStartTarget.text = "结束打卡"
                    vAddItem.visibility = View.GONE
                    vStartTarget.setOnClickListener({
                        Dialogs.showContentInputDialog(this, object : OnTextChangedListener {
                            override fun onTextChanged(text: String) {
                                itemEntity.endTime = System.currentTimeMillis()
                                itemEntity.content = text
                                DBHelper.getInstance().itemDao().updateItem(itemEntity)
                                finish()
                            }
                        })


                    })
                }

            }
            TargetType.MANY_COUNT -> {
                vStartTarget.text = "现在打卡"
                vStartTime.visibility = View.GONE
                vStartTarget.setOnClickListener({

                    Dialogs.showContentInputDialog(this, object : OnTextChangedListener {
                        override fun onTextChanged(text: String) {
                            val item = ItemEntity()
                            item.targetId = mTargetId
                            item.uuid = UUID.randomUUID().toString()
                            item.content = text
                            item.startTime = System.currentTimeMillis() / Const.ONE_MINUTE * Const.ONE_MINUTE
                            DBHelper.getInstance().itemDao().insertItem(item)
                            finish()
                        }
                    })


                })
            }
        }
    }
}