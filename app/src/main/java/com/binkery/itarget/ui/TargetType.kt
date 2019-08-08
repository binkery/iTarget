package com.binkery.itarget.ui


/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
interface TargetType {


    companion object {
        val COUNTER: Int = 0
        val DURATION: Int = 1
        fun title(type: Int): String {
            return when (type) {
                COUNTER -> "计次"
                DURATION -> "计时"
                else -> "undefined"
            }
        }

        fun titles(): Array<String> {
            return arrayOf("计次", "计时")
        }
    }

}