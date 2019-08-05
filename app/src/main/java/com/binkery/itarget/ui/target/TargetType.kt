package com.binkery.itarget.ui.target

import com.binkery.itarget.ui.counttarget.CountTargetFactory
import com.binkery.itarget.ui.durationtarget.DurationTargetFactory

//import com.binkery.itarget.ui.durationtarget.DurationTarget

//import com.binkery.itarget.ui.valuetarget.ValueTarget

/**
 *
 *
 */
enum class TargetType(val type: Int, val title: String, val description: String, val factory: TargetViewFactory) {

    COUNTER(0, "计次", "一天打卡多次", CountTargetFactory()),
    DURATION(1,"计时","" ,DurationTargetFactory());
//    VALUES(2, ValueTarget());


    companion object {

        fun titles(): Array<String> {
            val titles = arrayListOf<String>()
            for (type in TargetType.values()) {
                titles.add(type.title)
            }
            return titles.toTypedArray()
        }

        fun find(type: Int): TargetType {
            for (item in TargetType.values()) {
                if (item.type == type) {
                    return item
                }
            }
            return COUNTER
        }

        fun find(title: String): TargetType {
            for (item in TargetType.values()) {
                if (item.title == title) {
                    return item
                }
            }
            return COUNTER
        }
    }

}