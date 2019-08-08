package com.binkery.itarget.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.itarget.R
import com.binkery.itarget.base.BaseActivity
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.TargetEntity
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class RecordActivity : BaseActivity() {

    var mAdapter: RecordAdapter = RecordAdapter(this)
    var targetEntity: TargetEntity? = null
    var mTargetId: Int? = null


    override fun getContentLayoutId(): Int = R.layout.activity_record

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mTargetId = intent.getIntExtra("target_id", -1)
        if (mTargetId == -1) {
            finish()
            return
        }
        targetEntity = DBHelper.getInstance().targetDao().queryTargetById(mTargetId!!)
        if (targetEntity == null) {
            finish()
            return
        }

        vActionBarTitle.text = "打卡记录"
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

//        mAdapter = RecordAdapter(this, targetEntity?.type!!)
        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.adapter = mAdapter
    }


    override fun onResume() {
        super.onResume()
        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(targetEntity?.id!!)
        mAdapter.update(targetEntity?.type!!,list)
    }

}

