package com.binkery.itarget.ui.durationtarget

import android.view.View
import android.widget.Toast
import com.binkery.itarget.R
import com.binkery.itarget.datatimepicker.DateTimePicker
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.BaseAddItemFragment
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.fragment_item_add_duration.*
import java.util.*

/**
 *
 *
 */
class AddItemDurationFragment : BaseAddItemFragment() {


    var mItemEntity: ItemEntity? = null
    var mStartTime: Long? = null
    var mEndTime: Long? = null

    var mTargetEntity: TargetEntity? = null

    val ONE_MINUTE = 60 * 1000L

    override fun getLayoutId(): Int {
        return R.layout.fragment_item_add_duration
    }

    override fun onFragmentCreate(view: View?, targetEntity: TargetEntity, itemEntity: ItemEntity?) {
        mTargetEntity = targetEntity

        if (itemEntity == null) {
            mItemEntity = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetEntity.id)
        } else {
            mItemEntity = itemEntity
        }

        if (mItemEntity == null) {
            vEndTime.visibility = View.GONE
            vEndTimeTip.visibility = View.GONE
            mStartTime = (System.currentTimeMillis() / ONE_MINUTE) * ONE_MINUTE
        } else {
            vEndTime.visibility = View.VISIBLE
            vEndTimeTip.visibility = View.VISIBLE
            mStartTime = mItemEntity?.startTime
            if (mItemEntity?.endTime == 0L) {
                mEndTime = (System.currentTimeMillis() / ONE_MINUTE) * ONE_MINUTE
            } else {
                mEndTime = mItemEntity?.endTime
            }
        }

        vStartTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var dateTimePicker = DateTimePicker(context, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        mStartTime = (timestamp / ONE_MINUTE) * ONE_MINUTE
                        vStartTime.text = TextFormater.dataTimeWithoutSecond(timestamp)
                    }
                }, mStartTime!! - (1000L * 60 * 60 * 24 * 365), mStartTime!! + (1000L * 60 * 60 * 24 * 365))
                dateTimePicker.show(mStartTime!!)
            }
        })

        vEndTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dataTimePicker = DateTimePicker(context, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        mEndTime = (timestamp / ONE_MINUTE) * ONE_MINUTE
                        vEndTime.text = TextFormater.dataTimeWithoutSecond(timestamp)
                    }
                }, mEndTime!! - (1000L * 60 * 60 * 24 * 365), mEndTime!! + (1000L * 60 * 60 * 24 * 365))
                dataTimePicker.show(mEndTime!!)
            }
        })

        vStartTime.text = TextFormater.dataTimeWithoutSecond(mStartTime!!)
        if (mEndTime != null) {
            vEndTime.text = TextFormater.dataTimeWithoutSecond(mEndTime!!)
        }

        vAddItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (mItemEntity != null) {
                    if (mStartTime!! > mEndTime!!) {
                        Toast.makeText(context, "开始时间不能晚于结束时间！", Toast.LENGTH_LONG).show()
                        return
                    }
                    mItemEntity?.startTime = mStartTime!!
                    mItemEntity?.endTime = mEndTime!!
                    DBHelper.getInstance().itemDao().updateItem(mItemEntity)
                } else {
                    val item = ItemEntity()
                    item.uuid = UUID.randomUUID().toString()
                    item.targetId = mTargetEntity?.id!!
                    item.startTime = mStartTime!!
                    DBHelper.getInstance().itemDao().insertItem(item)

                }
                activity.finish()
            }
        })
    }


}