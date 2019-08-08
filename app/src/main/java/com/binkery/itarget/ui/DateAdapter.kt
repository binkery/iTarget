package com.binkery.itarget.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binkery.itarget.R
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class DateAdapter : RecyclerView.Adapter<DateViewHolder>() {

    private var mListItem: MutableList<ItemEntity>? = null
    private var mDateViewClickListener: DateViewClickListener? = null
    private var mTargetType: Int = TargetType.COUNTER

    fun updateDate(type: Int, list: MutableList<ItemEntity>) {
        mListItem = list
        mTargetType = type
        notifyDataSetChanged()
    }

    fun setDateViewClickListener(listener: DateViewClickListener) {
        mDateViewClickListener = listener
    }

    override fun onBindViewHolder(holder: DateViewHolder?, position: Int) {
        val calendar = Calendar.getInstance()
        val offset = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_MONTH, position + offset - 1 - (Int.MAX_VALUE / 2))

        val ms = calendar.timeInMillis - (calendar.timeInMillis % (1000 * 60 * 60 * 24))

        val mon = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        when (day) {
            1 -> holder?.vDate?.text = (mon + 1).toString() + "/" + day.toString()
            else -> holder?.vDate?.text = day.toString()
        }

        val result = mListItem?.filter { it.startTime > ms && it.startTime < ms + Const.ONE_DAY }
        val text = when {
            result ==  null || result.isEmpty() -> ""
            mTargetType == TargetType.COUNTER && result.size > 1 -> "打卡" + result.size + "次"
            mTargetType == TargetType.COUNTER -> TextFormater.hhmm(result[0].startTime)
            mTargetType == TargetType.DURATION -> {
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
        holder?.vCount?.text = text
        holder?.itemView?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mDateViewClickListener?.onClick(ms)
            }
        })

    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.layout_date_item, parent, false)
        return DateViewHolder(view)
    }

}