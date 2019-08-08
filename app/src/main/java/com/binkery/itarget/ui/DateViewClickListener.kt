package com.binkery.itarget.ui

import com.binkery.itarget.sqlite.TargetEntity

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
interface DateViewClickListener {

    fun onClick(targetEntity: TargetEntity, starTime: Long)

}