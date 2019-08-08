package com.binkery.itarget.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.router.Router
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.activity_target_detail.*
import kotlinx.android.synthetic.main.base_activity.*
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetViewActivity : BaseActivity() {

    private var mTargetId: Int = -1

    override fun getContentLayoutId(): Int = R.layout.activity_target_detail

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        if (mTargetId == -1) {
            finish()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    fun update() {
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId)
        val itemList = DBHelper.getInstance().itemDao().queryItemByTargetId(targetEntity.id)
        // title
        vActionBarTitle.text = targetEntity.name
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        // target info
        vTargetType.text = TargetType.title(targetEntity.type)

        vTargetCountSum.text = when (targetEntity.type) {
            TargetType.COUNTER -> {
                "已完成打卡 " + itemList.size + " 次"
            }
            TargetType.DURATION -> {
                var sum = 0L
                for (item in itemList) {
                    if (item.endTime > 0) {
                        sum += (item.endTime - item.startTime)
                    }
                }
                "累计时间" + TextFormater.durationSum(sum)
            }
            else -> ""
        }
        vTargetSetting.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Router.startSettingActivity(this@TargetViewActivity, targetEntity?.id!!)
            }
        })

        // recyclerview
        vRecyclerView.layoutManager = GridLayoutManager(this, 7)
        val dateAdapter = DateAdapter(targetEntity)
        vRecyclerView.adapter = dateAdapter



        vRecordRecyclerView.layoutManager = LinearLayoutManager(this)
        val recordAdapter = RecordAdapter(this, targetEntity.type)

        dateAdapter.setDateViewClickListener(object : DateViewClickListener {
            override fun onClick(targetEntity: TargetEntity, starTime: Long) {
                val list = DBHelper.getInstance().itemDao().queryOneDayItemsByTargetId(targetEntity.id, starTime, starTime + (1000 * 60 * 60 * 24))
                recordAdapter.update(list)
            }
        })
        vRecordRecyclerView.adapter = recordAdapter
        dateAdapter.updateDate(itemList)
        vRecyclerView.scrollToPosition(Int.MAX_VALUE / 2 - (14))

        // 打卡按钮
        vAddTargetItem.text = when (targetEntity.type) {
            TargetType.COUNTER -> "打卡"
            TargetType.DURATION -> {
                val item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetEntity.id)
                if (item == null) "开始" else "结束"
            }
            else -> "undefined"
        }
        vAddTargetItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (targetEntity.type) {
                    TargetType.COUNTER -> {
                        val item = ItemEntity()
                        item.uuid = UUID.randomUUID().toString()
                        item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                        item.targetId = targetEntity?.id!!
                        DBHelper.getInstance().itemDao().insertItem(item)
                        Toast.makeText(this@TargetViewActivity, "打卡成功", Toast.LENGTH_LONG).show()
                        update()
                    }
                    TargetType.DURATION -> {
                        var item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetEntity?.id!!)
                        if (item == null) {
                            item = ItemEntity()
                            item.uuid = UUID.randomUUID().toString()
                            item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                            vAddTargetItem.text = "结束"
                            item.targetId = targetEntity?.id!!
                            DBHelper.getInstance().itemDao().insertItem(item)
                            Toast.makeText(this@TargetViewActivity, "打卡开始", Toast.LENGTH_LONG).show()
                        } else {
                            item.endTime = System.currentTimeMillis() / 60_000 * 60_000
                            DBHelper.getInstance().itemDao().updateItem(item)
                            vAddTargetItem.text = "开始"
                            update()
                            Toast.makeText(this@TargetViewActivity, "打卡成功", Toast.LENGTH_LONG).show()
                        }
                    }
                    else -> {
                        Toast.makeText(applicationContext, "未支持", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })


    }

}