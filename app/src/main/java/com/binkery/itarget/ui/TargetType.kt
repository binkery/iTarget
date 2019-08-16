package com.binkery.itarget.ui


/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
enum class TargetType(val value: Int, val title: String) {

    // 一天打卡多次，记录每次打卡时间。比如记录每天喝水次数
    // 一天打卡多次，记录每次打卡时长。比如记录每天看书的时间
    // 一天打一次卡，不记录打卡时间。比如每天一篇文章编写
    // 一天打一次卡，记录打卡时间。比如每天起床时间，睡觉时间
    //

    MANY_COUNT(0, "每日多次打卡"), MANY_TIME(1, "每日多次计时"), ONE_COUNT(2, "每日单次打卡"), ONE_TIME(3, "每日定时打卡");

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