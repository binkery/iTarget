package com.binkery.base.activity

import androidx.lifecycle.ViewModel

/**
 * Create by binkery@gmail.com
 * on 2019 09 26
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
abstract class BaseViewModel : ViewModel() ,ViewContainer{


    private var mViewContainer: ViewContainer? = null

    fun init(viewContainer: ViewContainer) {
        mViewContainer = viewContainer
    }

    override fun showToast(message: String) {
        mViewContainer?.showToast(message)
    }

}