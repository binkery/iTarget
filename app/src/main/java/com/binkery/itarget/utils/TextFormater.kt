package com.binkery.itarget.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TextFormater {


    companion object {

        fun dateTime(dateTime: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return format.format(dateTime)
        }

        fun hhmm(ms: Long): String {
            val format = SimpleDateFormat("HH:mm")
            return format.format(ms)
        }

        fun yyyymmdd(ms: Long): String {
            val format = SimpleDateFormat("yyyy.MM.dd")
            return format.format(ms)
        }

        fun dataTimeWithoutSecond(dateTime: Long): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return format.format(dateTime)
        }

        fun duration(duration: Long): String {
            var hours = duration / (1000 * 60 * 60)
            var minutes = (duration / (1000 * 60)) % 60
            var seconds = (duration / 1000) % 60
            return hours.toString() + ":" + minutes.toString() + ":" + seconds.toString()
        }

        fun getTodayMs(): Long {
            var tms = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            tms.set(Calendar.HOUR_OF_DAY, 0)
            tms.set(Calendar.MINUTE, 0)
            tms.set(Calendar.SECOND, 0)
            tms.set(Calendar.MILLISECOND, 0)
            return tms.timeInMillis
        }

        fun durationSum(duration: Long): String {
            val mins = duration / 60_000
            return (mins / 60).toString() + "小时" + (mins % 60) + "分钟"
        }

        fun durationSumChar(duration: Long): String {
            val mins = duration / 60_000
            return (mins / 60).toString() + ":" + (mins % 60)
        }

        fun durationMins(duration: Long): String {
            return (duration / 60_000).toString() + "分钟"
        }

    }
}