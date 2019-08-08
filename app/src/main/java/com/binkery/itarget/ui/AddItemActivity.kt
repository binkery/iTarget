package com.binkery.itarget.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.datatimepicker.DateTimePicker
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddItemActivity : BaseActivity() {

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {

        vActionBarTitle.text = "修改"
        vActionBarBack.setOnClickListener {
            finish()
        }


        val itemId = intent.getIntExtra("item_id", -1)
        val itemEntity = DBHelper.getInstance().itemDao().queryItemById(itemId)
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(itemEntity.targetId)


        when (targetEntity.type) {
            TargetType.COUNTER -> {
                vStartTime.setKey("打卡时间")
                vStartTime.setValue(TextFormater.dataTimeWithoutSecond(itemEntity.startTime))
                vEndTime.visibility = View.GONE
                vInputValue.visibility = View.GONE
            }
            TargetType.DURATION -> {
                vStartTime.setKey("开始时间")
                vStartTime.setValue(TextFormater.dataTimeWithoutSecond(itemEntity.startTime))

                vEndTime.setKey("结束时间")
                vEndTime.setValue(TextFormater.dataTimeWithoutSecond(itemEntity.endTime))

                vInputValue.visibility = View.GONE
            }
        }

        vStartTime.setOnClickListener {
            val dateTimePicker = DateTimePicker(this@AddItemActivity, { timestamp ->
                itemEntity.startTime = (timestamp / Const.ONE_MINUTE) * Const.ONE_MINUTE
                vStartTime.setValue(TextFormater.dataTimeWithoutSecond(timestamp))
            }, itemEntity.startTime - Const.ONE_YEAR, itemEntity.startTime + Const.ONE_YEAR)
            dateTimePicker.show(itemEntity.startTime)
        }

        vEndTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dataTimePicker = DateTimePicker(this@AddItemActivity, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        itemEntity.endTime = (timestamp / Const.ONE_MINUTE) * Const.ONE_MINUTE
                        vEndTime.setValue(TextFormater.dataTimeWithoutSecond(timestamp))
                    }
                }, itemEntity.endTime - Const.ONE_YEAR, itemEntity.endTime + Const.ONE_YEAR)
                dataTimePicker.show(itemEntity.endTime)
            }
        })

        vAddItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (itemEntity.endTime != 0L && itemEntity.endTime < itemEntity.startTime) {
                    Toast.makeText(this@AddItemActivity, "结束时间早于开始时间", Toast.LENGTH_LONG).show()
                    return
                }
                DBHelper.getInstance().itemDao().updateItem(itemEntity)
                finish()
            }
        })

    }

}