package com.binkery.itarget.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.utils.Utils
import com.binkery.itarget.widgets.DateGridView
import kotlinx.android.synthetic.main.activity_target_detail_base.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 13
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetFlowActivity : BaseActivity() {

    private var mTargetId = -1
    private var mTargetType = TargetType.MANY_COUNT
    private var mRecordAdapter = DateRecordAdapter(this, mTargetId)
    private var mItemList: MutableList<ItemEntity> = arrayListOf()

    override fun getContentLayoutId(): Int = R.layout.activity_target_detail_base

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        vDateGridView.setOnDateViewUpdate(object : DateGridView.OnDateViewUpdate {
            override fun onDateViewSelected(ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                mRecordAdapter.update(mTargetType, result.toMutableList(), ms)
                onDateChanged(ms, result)
            }

            override fun onDateViewUpdate(textView: TextView, ms: Long) {
                val result = mItemList.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
                onDateViewUpdate(textView, ms, result)
            }
        })
        mRecordAdapter = DateRecordAdapter(this, mTargetId)
        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = mRecordAdapter
        updateView(System.currentTimeMillis())

        vTargetLayout.setSettingClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                SettingActivity.start(this@TargetFlowActivity, mTargetId)
            }
        })
    }


    private fun onDateViewUpdate(textView: TextView, ms: Long, list: List<ItemEntity>) {
        val text = when (mTargetType) {
            TargetType.MANY_COUNT -> {
                when {
                    list.isEmpty() -> ""
                    list.size == 1 -> TextFormater.hhmm(list[0].startTime)
                    else -> "打卡" + list.size.toString() + "次"
                }

            }
            TargetType.MANY_TIME -> {
                when {
                    list.isEmpty() -> ""
                    else -> {
                        var sum = 0L
                        list.forEach({
                            if (it.endTime > 0) sum += it.endTime - it.startTime
                        })
                        if (sum == 0L) {
                            ""
                        } else {
                            TextFormater.durationMins(sum)
                        }
                    }
                }
            }
        }
        textView.text = text
    }


    private fun onDateChanged(ms: Long, list: List<ItemEntity>) {
        when (mTargetType) {
            TargetType.MANY_COUNT -> onDateChangedManyCount(ms, list)
            TargetType.MANY_TIME -> onDateChangeManyTime(ms, list)
        }
    }

    private fun updateView(ms: Long) {
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        mTargetType = TargetType.find(targetEntity.type)
        setTitle(targetEntity.name)
        vTargetLayout.setTargetType(TargetType.find(targetEntity.type).title)

        mItemList = DBHelper.getInstance().itemDao().queryItemByTargetId(mTargetId)
        vDateGridView.scrollToDateTime(ms)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 101 && resultCode == Activity.RESULT_OK -> updateView(System.currentTimeMillis())
        }
    }

    private fun onDateChangeManyTime(ms: Long, list: List<ItemEntity>) {
    }

    private fun onDateChangedManyCount(ms: Long, list: List<ItemEntity>) {
    }

}

private class DateRecordAdapter(activity: TargetFlowActivity, val targetId: Int) : BaseAdapter<ItemEntity>(activity) {

    private val TYPE_EMPTY = 0
    private val TYPE_NORMAL = 2

    private var mItemList: MutableList<ItemEntity>? = null
    private var mTargetType: TargetType = TargetType.MANY_COUNT
    private var mBaseDate = 0L

    fun update(type: TargetType, list: MutableList<ItemEntity>, baseDate: Long) {
        mTargetType = type
        mItemList = list
        mBaseDate = baseDate
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): ItemEntity? {
        return when {
            mItemList?.isEmpty() ?: true -> {
                null
            }
            else -> mItemList!![position]
        }
    }

    override fun getItemCount(): Int {
        val count = when {
            mItemList?.isEmpty() ?: true -> 1
            else -> mItemList?.size ?: 0
        }
        Utils.log("detail", "count = " + count)
        return count
    }

    override fun getItemViewCardType(position: Int): Int {
        Utils.log("detail", "mTargetType = " + mTargetType.value + ", position = " + position)
        return when {
            mItemList?.isEmpty() ?: true -> TYPE_EMPTY
            else -> TYPE_NORMAL
        }
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<ItemEntity> {
        Utils.log("detail", "viewType = " + viewType)
        return when (viewType) {
            TYPE_EMPTY -> EmptyRecordViewCard(mTargetType)
            TYPE_NORMAL -> RecordViewCard(mTargetType)
            else -> RecordViewCard(mTargetType)
        }
    }

}

private class EmptyRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {

    override fun onCreateView(view: View) {

    }

    override fun getLayoutId(): Int = R.layout.layout_date_record_empty

    override fun onBindView(entity: ItemEntity?, view: View) {

    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
    }

}

//class DateRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {
//    private var vStartTime: TextView? = null
//    private var vEndTime: TextView? = null
//    private var vTitle: TextView? = null
//    private var vContent: TextView? = null
//
//    override fun onCreateView(view: View) {
//        vStartTime = view.findViewById(R.id.vStartTime)
//        vEndTime = view.findViewById(R.id.vEndTime)
//        vTitle = view.findViewById(R.id.vTitle)
//        vContent = view.findViewById(R.id.vContent)
//
//    }
//
//    override fun getLayoutId(): Int = R.layout.layout_record_card_count
//
//    override fun onBindView(entity: ItemEntity?, view: View) {
//        vStartTime?.text = TextFormater.hhmm(entity?.startTime!!)
//
//        when {
//            targetType == TargetType.MANY_COUNT -> {
//                vEndTime?.text = ""
//                vTitle?.text = "增加了一条打卡"
//                vContent?.text = if (entity.content == "") "没什么都没有写" else entity.content
//            }
//            targetType == TargetType.MANY_TIME && entity.endTime == 0L -> {
//                vEndTime?.text = ""
//                vTitle?.text = "打卡进行中..."
//                vContent?.text = ""
//
//            }
//            targetType == TargetType.MANY_TIME -> {
//                vEndTime?.text = TextFormater.hhmm(entity.endTime)
//                vTitle?.text = "增加了一条打卡，共积累 " + (TextFormater.durationMins(entity.endTime - entity.startTime))
//                vContent?.text = if (entity.content == "") "没什么都没有写" else entity.content
//            }
//        }
//    }
//
//    override fun onItemClick(entity: ItemEntity?, position: Int) {
//        when {
//            targetType == TargetType.MANY_TIME && entity?.endTime == 0L -> {
//
//            }
//            else -> {
//                AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, entity.id, 101)
//            }
//        }
//    }
//}