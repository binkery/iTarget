package com.binkery.itarget.ui

import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.TextFormater

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class RecordViewCard(private val targetType: Int) : BaseViewCard<ItemEntity>() {

    private var vStartTime: TextView? = null
    private var vEndTime: TextView? = null
    private var vValue: TextView? = null

    override fun onCreateView(view: View) {
        vStartTime = view.findViewById(R.id.vStartTime)
        vEndTime = view.findViewById(R.id.vEndTime)
        vValue = view.findViewById(R.id.vValue)
    }

    override fun getLayoutId(): Int = R.layout.layout_record_card_count

    override fun onBindView(entity: ItemEntity, view: View) {
        when (targetType) {
            TargetType.COUNTER -> {
                vStartTime?.text = "打卡时间："
                vEndTime?.text = TextFormater.dataTimeWithoutSecond(entity.startTime)
            }
            TargetType.DURATION -> {
                vStartTime?.text = TextFormater.dataTimeWithoutSecond(entity.startTime)
                if (entity.endTime > 0) {
                    vEndTime?.text = TextFormater.dataTimeWithoutSecond(entity.endTime)
                    vValue?.text = TextFormater.durationMins(entity.endTime - entity.startTime)
                }
            }
        }
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        Router.startAddItemActivity(getActivity()!!, entity!!.id)
    }

}