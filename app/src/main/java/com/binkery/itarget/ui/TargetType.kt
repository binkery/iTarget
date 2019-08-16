package com.binkery.itarget.ui


/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
enum class TargetType(val value: Int, val title: String, val description: String) {


    MANY_COUNT(0, "计次打卡", "记录每次打卡的时间，以完成规定打卡次数为目标"),
    MANY_TIME(1, "计时打卡", "记录每次打卡的时间，累计打卡时间总和达到设定时间为目标");

    companion object {

        fun find(type: Int): TargetType {
            for (targetType in TargetType.values()) {
                if (targetType.value == type) {
                    return targetType
                }
            }
            return MANY_COUNT
        }

        fun title(type: Int): String = find(type).title

        fun titles(): Array<String> {
            val titles = ArrayList<String>()
            for (type in TargetType.values()) {
                titles.add(type.title)
            }
            return titles.toTypedArray()
        }

    }

}