package com.binkery.itarget.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.ui.RecordAdapter
import com.binkery.itarget.ui.TargetType
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.widgets.DateGridView
import kotlinx.android.synthetic.main.activity_target_many_time.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 12
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class ManyTimeActivity : BaseActivity() {


    private var mTargetId = -1
    private val mRecordAdapter = RecordAdapter(this)
    private var mItemList: MutableList<ItemEntity> = arrayListOf()
    private var mSelecteDateTimeMs = System.currentTimeMillis()

    override fun getContentLayoutId(): Int = R.layout.activity_target_many_time

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)

        mTargetId = intent.getIntExtra("target_id", -1)
        vDateGridView.setOnDateViewUpdate(object : DateGridView.OnDateViewUpdate {
            override fun onDateViewSelected(ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                mRecordAdapter.update(TargetType.MANY_TIME, result.toMutableList())
                mSelecteDateTimeMs = ms
            }

            override fun onDateViewUpdate(textView: TextView, ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                if (result.isEmpty()) {
                    textView.text = ""
                } else {
                    var sum = 0L
                    result.forEach({
                        if (it.endTime > 0) sum += it.endTime - it.startTime
                    })
                    textView.text = TextFormater.durationSum(sum)
                }
            }
        })

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = mRecordAdapter

        vAddItem.setOnClickListener({

            var item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(mTargetId)
            if (item == null) {
                item = ItemEntity()
                item.uuid = UUID.randomUUID().toString()
                item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                item.targetId = mTargetId
                DBHelper.getInstance().itemDao().insertItem(item)

            } else {
                item.endTime = System.currentTimeMillis() / 60_000 * 60_000
                DBHelper.getInstance().itemDao().updateItem(item)
            }
            updateView(System.currentTimeMillis())

        })
        updateView(mSelecteDateTimeMs)
    }

    private fun updateView(ms: Long) {
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        setTitle(targetEntity.name)
        vTargetLayout.setTargetType(TargetType.MANY_TIME.title)

        mItemList = DBHelper.getInstance().itemDao().queryItemByTargetId(mTargetId)
        vDateGridView.scrollToDateTime(ms)

        val item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(mTargetId)
        if (item == null) {
            vAddItem.text = "开始"
        } else {
            vAddItem.text = "结束"
        }
    }

}