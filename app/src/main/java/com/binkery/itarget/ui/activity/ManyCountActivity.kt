package com.binkery.itarget.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.dialog.Dialogs
import com.binkery.itarget.dialog.OnDateTimeChangedListener
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.ui.RecordAdapter
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.utils.Utils
import com.binkery.itarget.widgets.DateGridView
import kotlinx.android.synthetic.main.activity_target_many_count.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class ManyCountActivity : BaseActivity() {

    private var mTargetId: Int = -1
    private val mRecordAdapter = RecordAdapter(this)
    private var mItemList: MutableList<ItemEntity> = arrayListOf()
    private var mSelecteDateTimeMs = System.currentTimeMillis()

    override fun getContentLayoutId(): Int = R.layout.activity_target_many_count

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        vDateGridView.setOnDateViewUpdate(object : DateGridView.OnDateViewUpdate {
            override fun onDateViewSelected(ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                mRecordAdapter.update(TargetType.MANY_COUNT, result.toMutableList())
                mSelecteDateTimeMs = ms
            }

            override fun onDateViewUpdate(textView: TextView, ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                if (result.isEmpty()) {
                    textView.text = ""
                } else if (result.size == 1) {
                    textView.text = TextFormater.hhmm(result[0].startTime)
                } else {
                    textView.text = "打卡" + result.size.toString() + "次"
                }
            }
        })

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = mRecordAdapter

        vAddItem.setOnClickListener({

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
                    item.targetId = mTargetId
                    item.startTime = ms
                    DBHelper.getInstance().itemDao().insertItem(item)
                    Utils.toast(this@ManyCountActivity, "打卡成功")
                    updateView(mSelecteDateTimeMs)
                }
            })
        })

        updateView(mSelecteDateTimeMs)
    }

    private fun updateView(ms: Long) {
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        setTitle(targetEntity.name)
        vTargetLayout.setTargetType(TargetType.MANY_COUNT.title)

        mItemList = DBHelper.getInstance().itemDao().queryItemByTargetId(mTargetId)
        vDateGridView.scrollToDateTime(ms)

        vTargetLayout.setItem1("累计打卡", mItemList.size.toString(), "次")
    }

}