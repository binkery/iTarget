package com.binkery.itarget.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binkery.itarget.R
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.utils.Utils
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 09
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class DateGridView : RecyclerView {

    private val mDateAdapter: DateAdapter = DateAdapter()

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layoutManager = GridLayoutManager(context, 7)
        adapter = mDateAdapter
    }

    fun setOnDateViewUpdate(updater: OnDateViewUpdate) {
        (adapter as DateAdapter).mOnDateViewUpdater = updater
    }

    fun scrollToDateTime(ms: Long) {
        val targetMS = msclean(ms)
        Utils.log("end = " + TextFormater.dataTimeWithoutSecond(targetMS))
        super.scrollToPosition(Int.MAX_VALUE / 2 - 14)
        (adapter as DateAdapter).setSelectedPosition(targetMS)
        (adapter as DateAdapter).mOnDateViewUpdater?.onDateViewSelected(targetMS)
        adapter.notifyDataSetChanged()
    }


    companion object {
        fun position2Calendar(position: Int): Calendar {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            val offset = calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DAY_OF_MONTH, position - (Int.MAX_VALUE / 2) - offset + 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar
        }

        fun ms2Calendar(ms: Long): Calendar {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            calendar.timeInMillis = ms
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar
        }

        fun msclean(ms: Long): Long {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            calendar.timeInMillis = ms
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

        fun ms2Position(ms: Long): Int {
            val calendar = ms2Calendar(ms)
            return ((System.currentTimeMillis() - calendar.timeInMillis) / Const.ONE_DAY).toInt()
        }
    }

    class DateAdapter : RecyclerView.Adapter<ViewHolder>() {

        var mOnDateViewUpdater: OnDateViewUpdate? = null
        private var mSelectDateTimeMS = 0L

        fun setSelectedPosition(position: Long) {
            mSelectDateTimeMS = position

        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            val offset = calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DAY_OF_MONTH, position - (Int.MAX_VALUE / 2) - offset + 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val ms = calendar.timeInMillis
            val mon = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            when (day) {
                1 -> holder?.vDate?.text = (mon + 1).toString() + "/" + day.toString()
                else -> holder?.vDate?.text = day.toString()
            }
            mOnDateViewUpdater?.onDateViewUpdate(holder?.vCount!!, ms)
            holder?.itemView?.setOnClickListener({
                mOnDateViewUpdater?.onDateViewSelected(ms)
                mSelectDateTimeMS = ms
                notifyDataSetChanged()
            })

            if (mSelectDateTimeMS == ms) {
                holder?.vDate?.setTextColor(holder.vDate.context?.resources?.getColor(R.color.color_46A0F0)!!)
            } else {
                holder?.vDate?.setTextColor(holder.vDate.context?.resources?.getColor(R.color.color_black)!!)
            }
        }

        override fun getItemCount(): Int = Int.MAX_VALUE

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.layout_date_item, parent, false)
            return ViewHolder(view)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vDate: TextView
        val vCount: TextView

        init {
            vDate = itemView.findViewById(R.id.vDate)
            vCount = itemView.findViewById(R.id.vCount)
        }
    }

    interface OnDateViewUpdate {
        fun onDateViewUpdate(textView: TextView, ms: Long)
        fun onDateViewSelected(ms: Long)
    }

}