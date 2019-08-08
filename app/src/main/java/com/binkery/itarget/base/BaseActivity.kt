package com.binkery.itarget.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.binkery.itarget.R
import kotlinx.android.synthetic.main.base_activity.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
abstract class BaseActivity : AppCompatActivity() {


    override final fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.base_activity)

        val layoutId = getContentLayoutId()
        LayoutInflater.from(this).inflate(layoutId, vContainer, true)
        onContentCreate(savedInstanceState)

        vActionBarBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    abstract fun getContentLayoutId(): Int
    abstract fun onContentCreate(savedInstanceState: Bundle?)

    fun setTitle(title: String) {
        vActionBarTitle.text = title
    }

}
