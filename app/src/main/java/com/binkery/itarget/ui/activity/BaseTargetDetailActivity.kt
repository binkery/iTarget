package com.binkery.itarget.ui.activity

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
import com.binkery.itarget.ui.AddItemActivity
import com.binkery.itarget.ui.TargetType
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
class BaseTargetDetailActivity : BaseActivity() {

    private var mTargetId = -1
    private var mTargetType = TargetType.ONE_COUNT
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
            TargetType.ONE_COUNT -> {
                when {
                    list.isEmpty() -> ""
                    else -> TextFormater.hhmm(list[0].startTime)
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
                        TextFormater.durationSum(sum)
                    }
                }
            }
            TargetType.ONE_TIME -> {
                when {
                    list.isEmpty() -> ""
                    else -> TextFormater.durationSum(list[0].endTime - list[0].startTime)
                }
            }
        }
        textView.text = text
    }


    private fun onDateChanged(ms: Long, list: List<ItemEntity>) {
        when (mTargetType) {
            TargetType.ONE_COUNT -> onDateChangedOneCount(ms, list)
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
//        if (ms < System.currentTimeMillis() && ms + Const.ONE_DAY > System.currentTimeMillis()) {
//            when {
//                list.isEmpty() -> {
//                    vAddItem.text = "开始"
//                }
//                list[0].endTime == 0L -> {
//                    vAddItem.text = "结束"
//                }
//                else -> {
//                    vAddItem.text = "开始"
//                }
//            }
//
//        } else {
//            vAddItem.text = "新增"
//        }
    }

    private fun onDateChangedOneCount(ms: Long, list: List<ItemEntity>) {
//        when {
//            list.isEmpty() -> {
//                vAddItem.text = "去打卡"
//                vAddItem.setOnClickListener({
//                    AddItemActivity.startResult(this, mTargetId, -1, ms, System.currentTimeMillis(), 101)
//
//                })
//            }
//            list.size == 1 -> {
//                vAddItem.text = "重新打卡"
//                vAddItem.setOnClickListener({
//                    AddItemActivity.startResult(this, mTargetId, list[0].id, ms, list[0].startTime, 101)
//                })
//            }
//            else -> {
//
//            }
//        }
    }

    private fun onDateChangedManyCount(ms: Long, list: List<ItemEntity>) {
//        vAddItem.text = "打卡"
//        vAddItem.setOnClickListener({
//            AddItemActivity.startResult(this, mTargetId, -1, ms, System.currentTimeMillis(), 101)
//        })
    }

}

class DateRecordAdapter(activity: BaseTargetDetailActivity, val targetId: Int) : BaseAdapter<ItemEntity>(activity) {

    private val TYPE_EMPTY = 0
    private val TYPE_ADD = 1
    private val TYPE_NORMAL = 2
    private val TYPE_GOING_ON = 3

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
                val item = ItemEntity()
                item.targetId = targetId
                item.startTime = mBaseDate
                item
            }
            mTargetType == TargetType.MANY_COUNT && position == 0 -> {
                val item = ItemEntity()
                item.startTime = mBaseDate
                item.targetId = targetId
                item
            }

            (mTargetType == TargetType.ONE_TIME || mTargetType == TargetType.MANY_TIME) && position == 0 -> {
                if (mItemList!![0].endTime == 0L) {
                    mItemList!![0]
                } else {
                    val item = ItemEntity()
                    item.targetId = targetId
                    item.startTime = mBaseDate
                    item
                }
            }
            mTargetType == TargetType.MANY_COUNT || mTargetType == TargetType.MANY_TIME -> mItemList!![position - 1]
            else -> mItemList!![position]
        }
    }

    override fun getItemCount(): Int {
        val count = when {
            mItemList?.isEmpty() ?: true -> 1
            mTargetType == TargetType.MANY_COUNT -> (mItemList?.size ?: 0) + 1
            mTargetType == TargetType.MANY_TIME -> {
                if (mItemList?.get(0)?.endTime == 0L) {
                    mItemList?.size ?: 0
                } else {
                    (mItemList?.size ?: 0) + 1
                }
            }
            mTargetType == TargetType.ONE_TIME || mTargetType == TargetType.ONE_COUNT -> 1
            else -> mItemList?.size ?: 0
        }
        Utils.log("detail", "count = " + count)
        return count
    }

    override fun getItemViewCardType(position: Int): Int {
        Utils.log("detail", "mTargetType = " + mTargetType.value + ", position = " + position)
        return when {
            mItemList?.isEmpty() ?: true -> TYPE_EMPTY
            mTargetType == TargetType.MANY_COUNT && position == 0 -> TYPE_ADD
            (mTargetType == TargetType.MANY_TIME || mTargetType == TargetType.ONE_TIME) && position == 0 -> {
                if (mItemList?.get(0)?.endTime == 0L) {
                    TYPE_GOING_ON
                } else {
                    TYPE_ADD
                }
            }
            else -> TYPE_NORMAL
        }
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<ItemEntity> {
        Utils.log("detail", "viewType = " + viewType)
        return when (viewType) {
            TYPE_EMPTY -> EmptyRecordViewCard(mTargetType)
            TYPE_NORMAL -> DateRecordViewCard(mTargetType)
            TYPE_GOING_ON -> GoingOnRecordViewCard(mTargetType)
            TYPE_ADD -> AddRecordViewCard(mTargetType)
            else -> DateRecordViewCard(mTargetType)
        }
    }

}

class GoingOnRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {

    private var vStartTime:TextView ?= null

    override fun onCreateView(view: View) {
        vStartTime = view.findViewById(R.id.vStartTime)
    }

    override fun getLayoutId(): Int = R.layout.layout_date_record_going_on

    override fun onBindView(entity: ItemEntity?, view: View) {
        vStartTime?.text = TextFormater.dataTimeWithoutSecond(entity?.startTime ?: 0L)
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, entity.id, entity.startTime, System.currentTimeMillis(), 101)
    }

}

class EmptyRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {

    override fun onCreateView(view: View) {

    }

    override fun getLayoutId(): Int = R.layout.layout_date_record_empty

    override fun onBindView(entity: ItemEntity?, view: View) {

    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, -1, entity?.startTime, System.currentTimeMillis(), 101)
    }

}

class AddRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {
    override fun onCreateView(view: View) {
    }

    override fun getLayoutId(): Int = R.layout.layout_date_record_add

    override fun onBindView(entity: ItemEntity?, view: View) {
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, -1, entity?.startTime, System.currentTimeMillis(), 101)
    }

}

class DateRecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {
    private var vStartTime: TextView? = null
    private var vEndTime: TextView? = null
    private var vValue: TextView? = null

    override fun onCreateView(view: View) {
        vStartTime = view.findViewById(R.id.vStartTime)
        vEndTime = view.findViewById(R.id.vEndTime)
        vValue = view.findViewById(R.id.vValue)
    }

    override fun getLayoutId(): Int = R.layout.layout_record_card_count

    override fun onBindView(entity: ItemEntity?, view: View) {
        when {
            targetType == TargetType.MANY_COUNT || targetType == TargetType.ONE_COUNT -> {
                vStartTime?.text = "打卡时间："
                vEndTime?.text = TextFormater.dataTimeWithoutSecond(entity?.startTime!!)
            }
            targetType == TargetType.MANY_TIME || targetType == TargetType.ONE_TIME -> {
                vStartTime?.text = TextFormater.dataTimeWithoutSecond(entity?.startTime!!)
                if (entity?.endTime!! > 0) {
                    vEndTime?.text = TextFormater.dataTimeWithoutSecond(entity?.endTime)
                    vValue?.text = TextFormater.durationMins(entity?.endTime - entity?.startTime)
                } else {
                    vEndTime?.text = ""
                    vValue?.text = "进行中..."
                }
            }
        }
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, entity.id, entity.startTime, entity.startTime, 101)
    }
}