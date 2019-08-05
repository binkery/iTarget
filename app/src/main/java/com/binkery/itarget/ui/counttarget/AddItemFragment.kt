package com.binkery.itarget.ui.counttarget

import android.view.View
import android.widget.Toast
import com.binkery.itarget.R
import com.binkery.itarget.datatimepicker.DateTimePicker
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.ui.target.BaseAddItemFragment
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.fragment_item_add_count.*
import java.util.*

/**
 *
 *
 */
class AddItemFragment : BaseAddItemFragment() {


    var mSelectDateTime: Long? = null
    var mItemEntity: ItemEntity?= null
    var mTargetEntity: TargetEntity?= null

    override fun onFragmentCreate(view: View?, targetEntity: TargetEntity, itemEntity: ItemEntity?) {
        mItemEntity = itemEntity
        mTargetEntity = targetEntity
        if(mItemEntity == null){
            mSelectDateTime = System.currentTimeMillis()
        }else{
            mSelectDateTime = mItemEntity?.startTime
        }

        vDateTime.text = TextFormater.dateTime(mSelectDateTime!!)


        vAddItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mItemEntity == null) {
                    val item = ItemEntity()
                    item.uuid = UUID.randomUUID().toString()
                    item.startTime = mSelectDateTime!!
                    item.targetId = mTargetEntity?.id!!
                    DBHelper.getInstance().itemDao().insertItem(item)
                } else {
                    mItemEntity?.startTime = mSelectDateTime!!
                    DBHelper.getInstance().itemDao().updateItem(itemEntity)
                }

                activity.finish()
            }
        })

        vDateTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val dateTimePicker = DateTimePicker(context, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        Toast.makeText(context, "time " + timestamp.toString(), Toast.LENGTH_LONG).show()
                        mSelectDateTime = timestamp
                        vDateTime.text = TextFormater.dateTime(mSelectDateTime!!)
                    }
                }, mSelectDateTime!! - (1000L * 60 * 60 * 24 * 365), mSelectDateTime!! + (1000L * 60 * 60 * 24 * 365))
                dateTimePicker.show(mSelectDateTime!!)
            }
        })

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_item_add_count
    }


}