package com.binkery.itarget.ui.target

import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.binkery.itarget.R
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.TargetViewActivity
import com.binkery.itarget.utils.TextFormater
import com.binkery.itarget.widgets.SettingColumn
import kotlinx.android.synthetic.main.activity_target_detail.*
import java.util.*

/**
 *
 *
 */
class FactoryManager {

    private val countFactory = CounterFactory()
    private val durationFactory = DurationFactory()

    companion object {

        fun getFactory(type: Int): AbsFactory {
            return when (type) {
                TargetType.COUNTER.type -> Inner.holder.countFactory
                TargetType.DURATION.type -> Inner.holder.durationFactory
                else -> Inner.holder.countFactory
            }
        }
    }

    private object Inner {
        val holder = FactoryManager()
    }


}

abstract class AbsFactory {

    open fun getTargetStatus(list: MutableList<ItemEntity>): String = "undefined"

    open fun getTargetSummary(list: MutableList<ItemEntity>): String = "undefined"

    abstract fun updateDateView(list: MutableList<ItemEntity>): String

    abstract fun updateRecordView(vStartTime: TextView, vEndTime: TextView, vValue: TextView, entity: ItemEntity)

    abstract fun updateAddItem(vStartTime: SettingColumn, vEndTime: SettingColumn, vValue: SettingColumn, target: TargetEntity, item: ItemEntity)

    abstract fun updateTargetViewActivity(activity: TargetViewActivity, targetEntity: TargetEntity)
}

class CounterFactory : AbsFactory() {

    override fun updateTargetViewActivity(activity: TargetViewActivity, targetEntity: TargetEntity) {
        activity.vAddTargetItem.text = "打卡"
        activity.vAddTargetItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val item = ItemEntity()
                item.uuid = UUID.randomUUID().toString()
                item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                item.targetId = targetEntity.id
                DBHelper.getInstance().itemDao().insertItem(item)
                Toast.makeText(activity, "打卡成功", Toast.LENGTH_LONG).show()
                activity.update()
            }
        })
    }

    override fun updateAddItem(vStartTime: SettingColumn, vEndTime: SettingColumn, vValue: SettingColumn, target: TargetEntity, item: ItemEntity) {
        vStartTime.setKey("打卡时间")
        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(item.startTime))
        vEndTime.visibility = View.GONE
        vValue.visibility = View.GONE

    }

    override fun updateRecordView(vStartTime: TextView, vEndTime: TextView, vValue: TextView, entity: ItemEntity) {
        vStartTime.text = "打卡时间："
        vEndTime.text = TextFormater.dataTimeWithoutSecond(entity.startTime)
    }

    override fun updateDateView(list: MutableList<ItemEntity>): String {
        return when {
            list.size > 2 -> "打卡" + list.size + "次"
            else -> TextFormater.hhmm(list[0].startTime)
        }
    }

    override fun getTargetStatus(list: MutableList<ItemEntity>): String {
        return when {
            list.size == 0 -> "暂无打卡"
            list[0].startTime > TextFormater.getTodayMs() -> "今日已打卡"
            else -> "最后打卡时间：" + TextFormater.dateTime(list[0].startTime)
        }
    }

    override fun getTargetSummary(list: MutableList<ItemEntity>): String {
        return "已完成打卡 " + list.size + " 次"
    }

}

class DurationFactory : AbsFactory() {
    override fun updateTargetViewActivity(activity: TargetViewActivity, targetEntity: TargetEntity) {
        var item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetEntity.id)
        if (item == null) {
            activity.vAddTargetItem.text = "开始"
        } else {
            activity.vAddTargetItem.text = "结束"
        }
        activity.vAddTargetItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetEntity.id)
                if (item == null) {
                    item = ItemEntity()
                    item.uuid = UUID.randomUUID().toString()
                    item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                    activity.vAddTargetItem.text = "结束"
                    item.targetId = targetEntity.id
                    DBHelper.getInstance().itemDao().insertItem(item)
                    Toast.makeText(activity, "打卡开始", Toast.LENGTH_LONG).show()
                } else {
                    item.endTime = System.currentTimeMillis() / 60_000 * 60_000
                    DBHelper.getInstance().itemDao().updateItem(item)
                    activity.vAddTargetItem.text = "开始"
                    activity.update()
                    Toast.makeText(activity, "打卡成功", Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    override fun updateAddItem(vStartTime: SettingColumn, vEndTime: SettingColumn, vValue: SettingColumn, target: TargetEntity, item: ItemEntity) {

        vStartTime.setKey("")
        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(item.startTime))

        vEndTime.setKey("")
        vEndTime.setValue(TextFormater.dataTimeWithoutSecond(item.endTime))

        vValue.visibility = View.GONE
    }


    override fun updateRecordView(vStartTime: TextView, vEndTime: TextView, vValue: TextView, entity: ItemEntity) {
        vStartTime.text = TextFormater.dataTimeWithoutSecond(entity.startTime)
        if (entity.endTime > 0) {
            vEndTime.text = TextFormater.dataTimeWithoutSecond(entity.endTime)
            vValue.text = TextFormater.durationMins(entity.endTime - entity.startTime)
        }
    }


    override fun updateDateView(list: MutableList<ItemEntity>): String {
        var sum = 0L
        list.forEach { sum += (it.endTime - it.startTime) }
        return TextFormater.durationSumChar(sum)
    }

    override fun getTargetStatus(list: MutableList<ItemEntity>): String {
        return when {
            list.size == 0 -> "暂无打卡"
            list[0].endTime == 0L -> "有进行中的打卡"
            list[0].startTime > TextFormater.getTodayMs() -> "今日已打卡"
            else -> "最后打卡时间：" + TextFormater.dateTime(list[0].startTime)
        }
    }

    override fun getTargetSummary(list: MutableList<ItemEntity>): String {
        var sum = 0L
        for (item in list) {
            if (item.endTime > 0) {
                sum += (item.endTime - item.startTime)
            }
        }
        return "累计时间" + TextFormater.durationSum(sum)
    }
}