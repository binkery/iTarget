package com.binkery.itarget.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import com.binkery.itarget.R
import kotlinx.android.synthetic.main.base_activity.*

/**
 *
 *
 */
abstract class BaseActivity : AppCompatActivity() {


    override final fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.base_activity)

        val layoutId = getContentLayoutId()
        LayoutInflater.from(this).inflate(layoutId, vContainer, true)
        onContentCreate(savedInstanceState)
    }

    abstract fun getContentLayoutId():Int
    abstract fun onContentCreate(savedInstanceState: Bundle?)

}
