package com.binkery.itarget.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnDateTimeChangedListener
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.utils.*
import com.binkery.itarget.widgets.DateGridView
import kotlinx.android.synthetic.main.activity_target_ont_count.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 09
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class OneCountActivity : BaseActivity() {

    private var mTargetId: Int = -1
    private var mTargetType = TargetType.ONE_TIME
    private var mSelectedEntity: ItemEntity? = null
    private var mItemList: MutableList<ItemEntity>? = null
    private var mSelecteDateTimeMs = 0L
    private var mIntMatchType = 0
    private var mIntMatchHour = 0
    private var mIntMatchMinute = 0

    private fun findItem(ms: Long): ItemEntity? {
        if (mItemList?.isEmpty() ?: false) {
            return null
        }
        return mItemList?.firstOrNull {
            it.startTime > ms && it.startTime < ms + Const.ONE_DAY
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_target_ont_count

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)

        setTitle(targetEntity.name)

        mIntMatchHour = targetEntity.data2.toInt()
        mIntMatchMinute = targetEntity.data3.toInt()
        mIntMatchType = targetEntity.data1.toInt()

        vTargetLayout.setTargetTile("每日" + (if (mIntMatchType == 0) "早于 " else "晚于 ") + (if (mIntMatchHour > 9) mIntMatchHour else "0" + mIntMatchHour) + ":" + (if (mIntMatchMinute > 9) mIntMatchMinute else "0" + mIntMatchMinute) + " 打卡")

        vDateGridView.setOnDateViewUpdate(object : DateGridView.OnDateViewUpdate {
            override fun onDateViewSelected(ms: Long) {
                mSelecteDateTimeMs = ms
                mSelectedEntity = findItem(ms)
                if (mSelectedEntity == null) {
                    vCardStatus.text = "未打卡"
                    vCardTime.text = ""
                    vAddItem.text = "去打卡"
                } else {
                    vCardStatus.text = "已打卡"
                    vCardTime.text = TextFormater.dataTimeWithoutSecond(mSelectedEntity?.startTime!!)
                    vAddItem.text = "重新打卡"
                }

            }

            override fun onDateViewUpdate(textView: TextView, ms: Long) {
                val item = findItem(ms)
                if (item == null) {
                    textView.text = ""
                } else {
                    textView.text = TextFormater.hhmm(item.startTime)
                }
            }
        })
        vAddItem.setOnClickListener({
            if (mSelectedEntity == null) {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                calendar.timeInMillis = mSelecteDateTimeMs
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                Dialogs.showTimePicker(this, currentHour, currentMinute, object : OnDateTimeChangedListener {
                    override fun onChanged(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
                        calendar.set(currentYear, currentMonth, currentDay, hour, minute)
                        val ms = calendar.timeInMillis
                        val item = ItemEntity()
                        item.startTime = ms
                        item.uuid = UUID.randomUUID().toString()
                        item.targetId = mTargetId
                        DBHelper.getInstance().itemDao().insertItem(item)
                        updateView(ms)
                        Utils.toast(this@OneCountActivity, "insert")
                    }
                })
            } else {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
                calendar.timeInMillis = mSelectedEntity?.startTime!!

                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                Dialogs.showTimePicker(this, currentHour, currentMinute, object : OnDateTimeChangedListener {
                    override fun onChanged(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
                        calendar.set(currentYear, currentMonth, currentDay, hour, minute)
                        val ms = calendar.timeInMillis

                        mSelectedEntity?.startTime = ms
                        DBHelper.getInstance().itemDao().updateItem(mSelectedEntity)

                        updateView(ms)
                        Utils.toast(this@OneCountActivity, "updated.")
                    }
                })

            }
        })

        vTargetLayout.setSettingClickListener(View.OnClickListener {
            Router.startSettingActivity(this, mTargetId)
        })

    }

    private fun updateView(ms: Long) {
        mItemList = DBHelper.getInstance().itemDao().queryItemByTargetId(mTargetId)
        vDateGridView.scrollToDateTime(ms)

        vTargetLayout.setTargetType(TargetType.ONE_TIME.title)
        vTargetLayout.setItem1("累计", (mItemList?.size ?: 0).toString(), "天")

        var matchCount = 0
        if (mItemList != null) {
            for (item in mItemList!!) {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
                calendar.timeInMillis = item.startTime
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                if (mIntMatchType == 0) {
                    if (hour < mIntMatchHour || (hour == mIntMatchHour && minute <= mIntMatchMinute)) {
                        matchCount++
                    }
                } else {
                    if (hour > mIntMatchHour || (hour == mIntMatchHour && minute >= mIntMatchMinute)) {
                        matchCount++
                    }
                }
            }
            vTargetLayout.setItem2("达成", matchCount.toString(), "天")
            vTargetLayout.setItem3("未达成", (mItemList?.size!! - matchCount).toString(), "天")
        } else {
            vTargetLayout.setItem2("达成", "0", "天")
            vTargetLayout.setItem3("未达成", "0", "天")
        }

    }

    override fun onResume() {
        super.onResume()
        updateView(System.currentTimeMillis())
    }
}