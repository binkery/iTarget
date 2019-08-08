package com.binkery.itarget.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.utils.Utils
import kotlinx.android.synthetic.main.activity_target_detail.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetViewActivity : BaseActivity() {

    private var mTargetId: Int = -1
    private var mTargetType: Int = TargetType.COUNTER
    private val mDateAdapter: DateAdapter = DateAdapter()
    private val mRecordAdapter: RecordAdapter = RecordAdapter(this)

    override fun getContentLayoutId(): Int = R.layout.activity_target_detail

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        if (mTargetId == -1) {
            finish()
            return
        }

        vTargetSetting.setOnClickListener({
            Router.startSettingActivity(this, mTargetId)
        })

        vm().dataAddItemText.observe(this, Observer<String> { text ->
            vAddTargetItem.text = text
        })


        vAddTargetItem.setOnClickListener({
            vm().addItem(mTargetType, mTargetId)
        })

        vm().dataToast.observe(this, Observer<String> { message ->
            Utils.toast(applicationContext, message)
        })

        vRecyclerView.layoutManager = GridLayoutManager(this, 7)
        vRecyclerView.adapter = mDateAdapter
        vRecordRecyclerView.layoutManager = LinearLayoutManager(this)

        mDateAdapter.setDateViewClickListener(object : DateViewClickListener {
            override fun onClick(starTime: Long) {
                vm().queryRecordList(mTargetId,starTime)
            }
        })
        vRecordRecyclerView.adapter = mRecordAdapter

        vm().dataRecordList.observe(this, Observer<MutableList<ItemEntity>> { list ->
            mRecordAdapter.update(mTargetType, list)
        })

        vm().dataTargetEntity.observe(this, object : Observer<TargetEntity> {
            override fun onChanged(targetEntity: TargetEntity?) {
                if (targetEntity == null) {
                    return
                }
                mTargetType = targetEntity.type
                vTargetType.text = TargetType.title(mTargetType)
                setTitle(targetEntity.name)

            }
        })
        vm().dataItemList.observe(this, object : Observer<MutableList<ItemEntity>> {
            override fun onChanged(itemList: MutableList<ItemEntity>?) {
                if (itemList == null) {
                    return
                }
                vTargetCountSum.text = when (mTargetType) {
                    TargetType.COUNTER -> {
                        "已完成打卡 " + itemList.size + " 次"
                    }
                    TargetType.DURATION -> {
                        var sum = 0L
                        itemList.forEach({
                            if (it.endTime > 0) sum += it.endTime - it.startTime
                        })
                        "累计时间" + TextFormater.durationSum(sum)
                    }
                    else -> ""
                }

                mDateAdapter.updateDate(mTargetType, itemList)
                vRecyclerView.scrollToPosition(Int.MAX_VALUE / 2 - (14))
            }
        })
    }

    private fun vm(): TargetViewViewModel {
        return ViewModelProviders.of(this).get(TargetViewViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        vm().queryTargetEntity(mTargetId)
    }

}