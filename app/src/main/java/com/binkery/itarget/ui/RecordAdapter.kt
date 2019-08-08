package com.binkery.itarget.ui

import android.view.ViewGroup
import com.binkery.itarget.adapter.BaseAdapter
import com.binkery.itarget.adapter.BaseViewCard
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.ItemEntity

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class RecordAdapter(activity: BaseActivity, private val targetType: Int) : BaseAdapter<ItemEntity>(activity) {

    private var mItemList: MutableList<ItemEntity>? = null

    override fun getItem(position: Int): ItemEntity? = mItemList?.get(position)

    override fun getItemCount(): Int = mItemList?.size ?: 0

    fun update(list:MutableList<ItemEntity>) {
        mItemList = list //
        notifyDataSetChanged()
    }


    override fun getItemViewCardType(position: Int): Int = targetType

    override fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<ItemEntity> {
        return RecordViewCard(targetType)
    }

}