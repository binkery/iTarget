package com.binkery.itarget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.base.activity.BaseActivity
import com.binkery.itarget.R
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnDeleteListener
import com.binkery.itarget.dialog.OnMilliSecondsChangeListener
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.base_activity.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class AddItemActivity : BaseActivity() {

    private var mItemId: Int = -1
    private var mTargetId: Int = -1

    companion object {

        fun startResult(activity: Activity, targetId: Int, itemId: Int, requestCode: Int) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("target_id", targetId)
            intent.putExtra("item_id", itemId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mItemId = intent.getIntExtra("item_id", -1)
        mTargetId = intent.getIntExtra("target_id", -1)

        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        if (mTargetId == -1 || targetEntity == null) {
            finish()
            return
        }

        vActionBarBack.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        val targetType = TargetType.find(targetEntity.type)
        when (targetType) {
            TargetType.MANY_TIME -> onTimeType()
            TargetType.MANY_COUNT -> onCountTpye()
        }

        if (mItemId != -1) {
            setTitle("修改记录")
            vDelete.visibility = View.VISIBLE
            vDelete.setOnClickListener({
                Dialogs.showDeleteDialog(this,"您是否要删除该打卡记录？", object : OnDeleteListener {
                    override fun onDeleted() {
                        val item = DBHelper.getInstance().itemDao().queryItemById(mItemId)
                        DBHelper.getInstance().itemDao().deleteItem(item)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })
            })
        } else {
            setTitle("添加记录")
            vDelete.visibility = View.GONE
        }
    }

    private fun onTimeType() {
        vInputValue.visibility = View.GONE

        var startTime = System.currentTimeMillis()
        var endTime = System.currentTimeMillis()
        var content = ""
        if (mItemId != -1) {
            val item = DBHelper.getInstance().itemDao().queryItemById(mItemId)
            startTime = item.startTime
            endTime = item.endTime
            content = item.content
        }

        vStartTime.setKey("开始时间")
        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(startTime))
        vStartTime.setOnClickListener({
            Dialogs.showDateTimePicker(this, startTime, object : OnMilliSecondsChangeListener {
                override fun onChanged(ms: Long) {
                    vStartTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                    startTime = ms
                    if (startTime > endTime) {
                        vAddItem.isEnabled = false
                        vAddItem.setBackgroundResource(R.color.color_gray)
                    } else {
                        vAddItem.isEnabled = true
                        vAddItem.setBackgroundResource(R.color.color_46A0F0)
                    }
                }
            })
        })

        vEndTime.setKey("结束时间")
        vEndTime.setValue(TextFormater.dataTimeWithoutSecond(endTime))
        vEndTime.setOnClickListener({
            Dialogs.showDateTimePicker(this, endTime, object : OnMilliSecondsChangeListener {
                override fun onChanged(ms: Long) {
                    vEndTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                    endTime = ms
                    if (startTime > endTime) {
                        vAddItem.isEnabled = false
                        vAddItem.setBackgroundResource(R.color.color_gray)
                    } else {
                        vAddItem.isEnabled = true
                        vAddItem.setBackgroundResource(R.color.color_46A0F0)
                    }
                }
            })
        })



        vContent.setText(content)

        vAddItem.setOnClickListener({

            content = vContent.text.toString()
            insertOrUpdateItem(mTargetId, mItemId, startTime, endTime, 0, content)
        })

    }

    private fun onCountTpye() {
        vEndTime.visibility = View.GONE
        vInputValue.visibility = View.GONE

        vStartTime.setKey("打卡时间")
        var startTime = System.currentTimeMillis()
        if (mItemId != -1) {
            val item = DBHelper.getInstance().itemDao().queryItemById(mItemId)
            startTime = item.startTime
        }

        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(startTime))
        vStartTime.setOnClickListener({
            Dialogs.showDateTimePicker(this, startTime, object : OnMilliSecondsChangeListener {
                override fun onChanged(ms: Long) {
                    vStartTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                    startTime = ms
                }
            })
        })
        vAddItem.setOnClickListener({
            val content = vContent.text.toString()
            insertOrUpdateItem(mTargetId, mItemId, startTime, 0, 0, content)
        })
    }

    private fun insertOrUpdateItem(targetId: Int, itemId: Int, startTime: Long, endTime: Long, value: Long, content: String?) {
        if (itemId == -1) {
            val item = ItemEntity()
            item.startTime = startTime
            item.endTime = endTime
            item.value = value
            item.content = content
            item.targetId = targetId
            item.uuid = UUID.randomUUID().toString()
            DBHelper.getInstance().itemDao().insertItem(item)

        } else {
            val item = DBHelper.getInstance().itemDao().queryItemById(itemId)
            if (startTime != 0L) item.startTime = startTime
            if (endTime != 0L) item.endTime = endTime
            if (value != 0L) item.value = value
            if (content != null) item.content = content
            DBHelper.getInstance().itemDao().updateItem(item)
        }
        setResult(Activity.RESULT_OK)
        finish()
    }
}