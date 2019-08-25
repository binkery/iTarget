package com.binkery.itarget.ui

import android.view.ViewGroup
import com.binkery.base.activity.BaseActivity
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class DateAdapter(activity: BaseActivity) : BaseAdapter<Int>(activity) {

    override fun getItem(position: Int): Int? = position

    override fun getItemViewCardType(position: Int): Int = 0

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<Int> {
        return DateViewHolder()
    }

    private var mListItem: MutableList<ItemEntity>? = null
    private var mDateViewClickListener: DateViewClickListener? = null
    private var mTargetType: TargetType = TargetType.MANY_COUNT
    var mSelectedPosition: Int = Int.MAX_VALUE / 2

    fun updateDate(type: TargetType, list: MutableList<ItemEntity>) {
        mListItem = list
        mTargetType = type
        notifyDataSetChanged()
    }

    fun setDateViewClickListener(listener: DateViewClickListener) {
        mDateViewClickListener = listener
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun getDateText(ms: Long): String {
        val result = mListItem?.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
        return when {
            result == null || result.isEmpty() -> ""
            mTargetType == TargetType.MANY_COUNT && result.size > 1 -> "打卡" + result.size + "次"
            mTargetType == TargetType.MANY_COUNT -> TextFormater.hhmm(result[0].startTime)
            mTargetType == TargetType.MANY_TIME -> {
                var sum = 0L
                result.forEach {
                    if (it.endTime > 0) {
                        sum += (it.endTime - it.startTime)
                    }
                }
                TextFormater.durationSumChar(sum)
            }
            else -> ""
        }
    }

    fun position2ms(position: Int): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val offset = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_MONTH, position - (Int.MAX_VALUE / 2) - offset + 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun onDateViewClick(position: Int) {
        mSelectedPosition = position
        mDateViewClickListener?.onClick(position2ms(position))
        notifyDataSetChanged()
    }

}