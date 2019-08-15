package com.binkery.itarget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
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
    private var mBaseDateMs = 0L
    private var mBaseTimeMs = 0L


    companion object {

        fun startResult(activity: Activity, targetId: Int, itemId: Int, dateMs: Long, timeMs: Long, requestCode: Int) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("target_id", targetId)
            intent.putExtra("item_id", itemId)
            intent.putExtra("base_date", dateMs)
            intent.putExtra("base_time", timeMs)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {


        mItemId = intent.getIntExtra("item_id", -1)
        mTargetId = intent.getIntExtra("target_id", -1)
        mBaseDateMs = intent.getLongExtra("base_date", 0L)
        mBaseTimeMs = intent.getLongExtra("base_time", 0L)

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
            TargetType.ONE_TIME -> onTimeType()
            TargetType.MANY_TIME -> onTimeType()
            TargetType.MANY_COUNT -> onCountTpye()
            TargetType.ONE_COUNT -> onCountTpye()
        }
    }

    private fun onTimeType() {
        vInputValue.visibility = View.GONE

        if (mItemId == -1) {
            vEndTime.visibility = View.GONE

            var selectedTimeMs = getBaseDateTime(mBaseDateMs, mBaseTimeMs)
            vStartTime.setKey("开始时间")
            vStartTime.setValue(TextFormater.dataTimeWithoutSecond(selectedTimeMs))
            vStartTime.setOnClickListener({
                Dialogs.showTimePicker(this, mBaseDateMs, selectedTimeMs, object : OnMilliSecondsChangeListener {
                    override fun onChanged(ms: Long) {
                        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                        selectedTimeMs = ms
                    }
                })
            })

            vAddItem.setOnClickListener({
                insertOrUpdateItem(mTargetId, -1, selectedTimeMs, 0, 0, null)
            })
        } else {
            val item = DBHelper.getInstance().itemDao().queryItemById(mItemId)

            var startTime = item.startTime
            vStartTime.setKey("开始时间")
            vStartTime.setValue(TextFormater.dataTimeWithoutSecond(startTime))
            vStartTime.setOnClickListener({
                Dialogs.showTimePicker(this, startTime, startTime, object : OnMilliSecondsChangeListener {
                    override fun onChanged(ms: Long) {
                        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                        startTime = ms
                    }
                })
            })

            var endTime = if (item.endTime == 0L) getBaseDateTime(mBaseDateMs, mBaseTimeMs) else item.endTime
            vEndTime.setKey("结束时间")
            vEndTime.setValue(TextFormater.dataTimeWithoutSecond(endTime))
            vEndTime.setOnClickListener({
                Dialogs.showTimePicker(this, endTime, endTime, object : OnMilliSecondsChangeListener {
                    override fun onChanged(ms: Long) {
                        vEndTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                        endTime = ms
                    }
                })
            })
            vAddItem.setOnClickListener({
                insertOrUpdateItem(mTargetId, mItemId, startTime, endTime, 0, null)
            })
        }
    }

    private fun onCountTpye() {
        vEndTime.visibility = View.GONE
        vInputValue.visibility = View.GONE

        vStartTime.setKey("打卡时间")
        var selectedTimeMs = getBaseDateTime(mBaseDateMs, mBaseTimeMs)
        if (mItemId != -1) {
            val item = DBHelper.getInstance().itemDao().queryItemById(mItemId)
            selectedTimeMs = item.startTime
        }

        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(selectedTimeMs))
        vStartTime.setOnClickListener({
            Dialogs.showTimePicker(this, mBaseDateMs, selectedTimeMs, object : OnMilliSecondsChangeListener {
                override fun onChanged(ms: Long) {
                    vStartTime.setValue(TextFormater.dataTimeWithoutSecond(ms))
                    selectedTimeMs = ms
                }
            })
        })
        vAddItem.setOnClickListener({
            insertOrUpdateItem(mTargetId, mItemId, selectedTimeMs, 0, 0, null)
        })
    }

    private fun getBaseDateTime(baseDate: Long, baseTime: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        calendar.timeInMillis = baseDate
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.timeInMillis = baseTime
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute, 0)
        return calendar.timeInMillis
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