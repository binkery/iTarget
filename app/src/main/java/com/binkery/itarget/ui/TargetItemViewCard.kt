package com.binkery.itarget.ui

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.utils.TextFormater

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetItemViewCard : BaseViewCard<TargetEntity>() {

    private var vTargetName: TextView? = null
    private var vTargetStatus: TextView? = null
    private var vTargetCount: TextView? = null
    private var vTargetType: TextView? = null

    override fun onCreateView(view: View) {
        vTargetName = view.findViewById(R.id.vTargetName)
        vTargetStatus = view.findViewById(R.id.vTargetStatus)
        vTargetCount = view.findViewById(R.id.vTargetCount)
        vTargetType = view.findViewById(R.id.vTargetType)
    }

    override fun getLayoutId(): Int = R.layout.layout_home_card_common

    override fun onBindView(entity: TargetEntity?, view: View) {
        vTargetName?.text = entity?.name
        vTargetType?.text = TargetType.title(entity?.type!!)
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(entity.id)

        when (TargetType.find(entity.type)) {
            TargetType.MANY_COUNT -> {
                vTargetStatus?.text = when {
                    list.size == 0 -> "暂无打卡"
                    list[0].startTime > TextFormater.getTodayMs() -> "今日已打卡"
                    else -> "最后打卡时间：" + TextFormater.dateTime(list[0].startTime)
                }
                vTargetCount?.text = "已完成打卡 " + list.size + " 次"
            }
            TargetType.MANY_TIME -> {
                vTargetStatus?.text = when {
                    list.size == 0 -> "暂无打卡"
                    list[0].endTime == 0L -> "有进行中的打卡"
                    list[0].startTime > TextFormater.getTodayMs() -> "今日已打卡"
                    else -> "最后打卡时间：" + TextFormater.dateTime(list[0].startTime)
                }
                var sum = 0L
                for (item in list) {
                    if (item.endTime > 0) {
                        sum += (item.endTime - item.startTime)
                    }
                }
                vTargetCount?.text = "累计时间" + TextFormater.durationSum(sum)
            }
        }
    }

    override fun onItemClick(entity: TargetEntity?, position: Int) {

        val intent = Intent(getActivity(), CheckInActivity::class.java)
        intent.putExtra("target_id", entity?.id)
        getActivity()?.startActivity(intent)

    }

}