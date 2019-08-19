package com.binkery.itarget.ui

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.TextFormater

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class RecordAdapter(activity: BaseActivity) : BaseAdapter<ItemEntity>(activity) {

    private val mDataList = mutableListOf<ItemEntity>()

    private var mTargetType: TargetType = TargetType.MANY_COUNT

    override fun getItem(position: Int): ItemEntity? = mDataList[position]

    override fun getItemCount(): Int = mDataList.size

    fun update(type: TargetType, list: MutableList<ItemEntity>) {
        mTargetType = type
        list.forEach {
            val date = TextFormater.yyyymmdd(it.startTime)
            if (mDataList.find { it.content == date } == null) {
                val item = ItemEntity()
                item.startTime = 0L
                item.content = date
                mDataList.add(item)
            }
            mDataList.add(it)
        }
        notifyDataSetChanged()
    }


    override fun getItemViewCardType(position: Int): Int {
        val item = getItem(position)
        if (item?.startTime == 0L) {
            return 1
        }
        return 0
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<ItemEntity> {
        return when (viewType) {
            0 -> RecordViewCard(mTargetType)
            else -> DateTimeViewCard()
        }
    }

}

private class DateTimeViewCard : BaseViewCard<ItemEntity>() {

    private var vDateTime: TextView? = null

    override fun onCreateView(view: View) {
        vDateTime = view.findViewById(R.id.vDateTime)
    }

    override fun getLayoutId(): Int = R.layout.layout_record_date_card

    override fun onBindView(entity: ItemEntity?, view: View) {
        vDateTime?.text = entity?.content
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
    }

}