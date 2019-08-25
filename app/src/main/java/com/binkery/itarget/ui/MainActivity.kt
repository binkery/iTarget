package com.binkery.itarget.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.base.activity.BaseActivity
import com.binkery.itarget.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class MainActivity : BaseActivity() {

    private var mAdapter: TargetListAdapter? = null

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAddTarget.setOnClickListener({
            val intent = Intent(this@MainActivity, AddTargetTypeActivity::class.java)
            startActivity(intent)
        })

        vActionBarTitle.text = "iTarget"
        vActionBarBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        mAdapter = TargetListAdapter(this)
        targetRecyclerView.layoutManager = LinearLayoutManager(this)
        targetRecyclerView.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        mAdapter?.update()
    }
}
