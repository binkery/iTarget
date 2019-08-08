package com.binkery.itarget.ui

import android.view.ViewGroup
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetListAdapter(activity: MainActivity) : BaseAdapter<TargetEntity>(activity) {


    override fun getItem(position: Int): TargetEntity? = mList?.get(position)

    override fun getItemCount(): Int = mList?.size ?: 0

    private var mList: MutableList<TargetEntity>? = null

    fun update() {
        mList = DBHelper.getInstance().targetDao().queryAllTarget()
        notifyDataSetChanged()
    }

    override fun getItemViewCardType(position: Int): Int {
        return 0
    }

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<TargetEntity> {
        return TargetItemViewCard()
    }

}