package com.binkery.itarget.ui

import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.utils.TextFormater

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class RecordViewCard(private val targetType: TargetType) : BaseViewCard<ItemEntity>() {

    private var vStartTime: TextView? = null
    private var vEndTime: TextView? = null
    private var vTitle: TextView? = null
    private var vContent: TextView? = null

    override fun onCreateView(view: View) {
        vStartTime = view.findViewById(R.id.vStartTime)
        vEndTime = view.findViewById(R.id.vEndTime)
        vTitle = view.findViewById(R.id.vTitle)
        vContent = view.findViewById(R.id.vContent)
    }

    override fun getLayoutId(): Int = R.layout.layout_record_card_count

    override fun onBindView(entity: ItemEntity?, view: View) {
        vStartTime?.text = TextFormater.hhmm(entity?.startTime!!)

        when {
            targetType == TargetType.MANY_COUNT -> {
                vEndTime?.text = ""
                vTitle?.text = "增加了一条打卡"
                vContent?.text = if (entity.content == "") "没什么都没有写" else entity.content
            }
            targetType == TargetType.MANY_TIME && entity.endTime == 0L -> {
                vEndTime?.text = ""
                vTitle?.text = "打卡进行中..."
                vContent?.text = ""

            }
            targetType == TargetType.MANY_TIME -> {
                vEndTime?.text = TextFormater.hhmm(entity.endTime)
                vTitle?.text = "增加了一条打卡，共积累 " + (TextFormater.durationMins(entity.endTime - entity.startTime))
                vContent?.text = if (entity.content == "") "没什么都没有写" else entity.content
            }
        }
    }

    override fun onItemClick(entity: ItemEntity?, position: Int) {
        when {
            targetType == TargetType.MANY_TIME && entity?.endTime == 0L -> {

            }
            else -> {
                AddItemActivity.startResult(getActivity()!!, entity?.targetId!!, entity.id, 101)
            }
        }
    }

}