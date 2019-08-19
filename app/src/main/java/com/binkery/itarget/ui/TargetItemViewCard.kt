package com.binkery.itarget.ui

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.binkery.itarget.R
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.utils.Const
import com.binkery.itarget.utils.TextFormater

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetItemViewCard : BaseViewCard<TargetEntity>() {

    private var vTargetName: TextView? = null
    private var vTargetStatus: TextView? = null
    private var vTargetMatch: TextView? = null
    private var vTargetType: TextView? = null
    private var vTargetCount: TextView? = null

    override fun onCreateView(view: View) {
        vTargetName = view.findViewById(R.id.vTargetName)
        vTargetStatus = view.findViewById(R.id.vTargetStatus)
        vTargetMatch = view.findViewById(R.id.vTargetMatch)
        vTargetType = view.findViewById(R.id.vTargetType)
        vTargetCount = view.findViewById(R.id.vTargetCount)
    }

    override fun getLayoutId(): Int = R.layout.layout_home_card_common

    override fun onBindView(entity: TargetEntity?, view: View) {
        vTargetName?.text = entity?.name
        vTargetType?.text = TargetType.title(entity?.type!!)
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(entity.id)
        val match = entity.data1.toInt()

        val result: Int = when (TargetType.find(entity.type)) {
            TargetType.MANY_TIME -> {
                val result = list.filter { it.startTime > TextFormater.getTodayMs() && it.startTime < TextFormater.getTodayMs() + Const.ONE_DAY }
                var sum = 0L
                result.forEach({
                    if (it.endTime > 0) sum += (it.endTime - it.startTime)
                })
                (sum / Const.ONE_MINUTE).toInt()
            }
            TargetType.MANY_COUNT -> {
                val result = list.filter { it.startTime > TextFormater.getTodayMs() && it.startTime < TextFormater.getTodayMs() + Const.ONE_DAY }
                result.size
            }
        }

        vTargetMatch?.text = when {
            result < match -> "未完成"
            else -> "已完成"
        }
        vTargetStatus?.text = when (TargetType.find(entity.type)) {
            TargetType.MANY_COUNT -> "(" + result + "次/" + match + "次)"
            else -> "(" + result + "分钟/" + match + "分钟)"
        }

        when (TargetType.find(entity.type)) {
            TargetType.MANY_COUNT -> {
                vTargetCount?.text = list.size.toString() + "次"
            }
            TargetType.MANY_TIME -> {
                var sum = 0L
                list.forEach({
                    if (it.endTime > 0) sum += (it.endTime - it.startTime)
                })
                vTargetCount?.text = TextFormater.durationMins(sum) + "/" + list.size + "次"
            }
        }

    }

    override fun onItemClick(entity: TargetEntity?, position: Int) {
        val intent = Intent(getActivity(), CheckInActivity::class.java)
        intent.putExtra("target_id", entity?.id)
        getActivity()?.startActivity(intent)

    }

}