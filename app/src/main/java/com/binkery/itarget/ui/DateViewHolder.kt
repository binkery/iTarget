package com.binkery.itarget.ui

import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseViewCard
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class DateViewHolder : BaseViewCard<Int>() {

    private var vDate: TextView? = null
    private var vCount: TextView? = null

    override fun onCreateView(view: View) {
        vDate = view.findViewById(R.id.vDate)
        vCount = view.findViewById(R.id.vCount)
    }

    override fun getLayoutId(): Int = R.layout.layout_date_item

    override fun onBindView(entity: Int?, view: View) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val offset = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.add(Calendar.DAY_OF_MONTH, entity!! - (Int.MAX_VALUE / 2) - offset + 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val ms = calendar.timeInMillis
        val mon = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        when (day) {
            1 -> vDate?.text = (mon + 1).toString() + "/" + day.toString()
            else -> vDate?.text = day.toString()
        }

        vCount?.text = (getAdapter() as DateAdapter).getDateText(ms)
        if(entity == (getAdapter() as DateAdapter).mSelectedPosition){
            vDate?.setTextColor(getActivity()?.resources?.getColor(R.color.color_46A0F0)!!)
        }else{
            vDate?.setTextColor(getActivity()?.resources?.getColor(R.color.color_black)!!)
        }
    }

    override fun onItemClick(entity: Int?, position: Int) {
        (getAdapter() as DateAdapter).onDateViewClick(position)
    }


}