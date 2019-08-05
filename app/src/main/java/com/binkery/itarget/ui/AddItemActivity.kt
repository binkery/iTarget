package com.binkery.itarget.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.binkery.itarget.R
import com.binkery.itarget.datatimepicker.DateTimePicker
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.ui.target.FactoryManager
import com.binkery.itarget.utils.TextFormater
import kotlinx.android.synthetic.main.activity_add_item.*

/**
 *
 *
 */
class AddItemActivity : AppCompatActivity() {

    private val ONE_MINUTE = 60L * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        supportActionBar?.title = "更新打卡"

        val itemId = intent.getIntExtra("item_id", -1)
        val itemEntity = DBHelper.getInstance().itemDao().queryItemById(itemId)
        val targetEntity = DBHelper.getInstance().targetDao().queryTargetById(itemEntity.targetId)

        FactoryManager.getFactory(targetEntity.type).updateAddItem(vStartTime, vEndTime, vValue, targetEntity, itemEntity)

        vStartTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var dateTimePicker = DateTimePicker(this@AddItemActivity, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        itemEntity.startTime = (timestamp / ONE_MINUTE) * ONE_MINUTE
                        vStartTime.setValue(TextFormater.dataTimeWithoutSecond(timestamp))
                    }
                }, itemEntity.startTime!! - (1000L * 60 * 60 * 24 * 365), itemEntity.startTime!! + (1000L * 60 * 60 * 24 * 365))
                dateTimePicker.show(itemEntity.startTime!!)
            }
        })

        vEndTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dataTimePicker = DateTimePicker(this@AddItemActivity, object : DateTimePicker.Callback {
                    override fun onTimeSelected(timestamp: Long) {
                        itemEntity.endTime = (timestamp / ONE_MINUTE) * ONE_MINUTE
                        vEndTime.setValue(TextFormater.dataTimeWithoutSecond(timestamp))
                    }
                }, itemEntity.endTime!! - (1000L * 60 * 60 * 24 * 365), itemEntity.endTime!! + (1000L * 60 * 60 * 24 * 365))
                dataTimePicker.show(itemEntity.endTime!!)
            }
        })

        vAddItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (itemEntity.endTime != 0L && itemEntity.endTime < itemEntity.startTime) {
                    Toast.makeText(this@AddItemActivity, "结束时间早于开始时间", Toast.LENGTH_LONG).show()
                    return
                }
                DBHelper.getInstance().itemDao().updateItem(itemEntity)
                finish()
            }
        })

    }

}