package com.binkery.itarget.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binkery.itarget.sqlite.DBHelper
import com.binkery.itarget.sqlite.ItemEntity
import com.binkery.itarget.sqlite.TargetEntity
import com.binkery.itarget.utils.Const
import java.util.*

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
class TargetViewViewModel : ViewModel() {

    val dataItemList: MutableLiveData<MutableList<ItemEntity>> = MutableLiveData()
    val dataAddItemText:MutableLiveData<String> = MutableLiveData()
    val dataToast:MutableLiveData<String> = MutableLiveData()
    val dataTargetEntity:MutableLiveData<TargetEntity> = MutableLiveData()
    val dataRecordList:MutableLiveData<MutableList<ItemEntity>> = MutableLiveData()

    fun queryTargetEntity(id: Int) {
        val entity = DBHelper.getInstance().targetDao().queryTargetById(id)
        dataTargetEntity.postValue(entity)


        val list = DBHelper.getInstance().itemDao().queryItemByTargetId(id)
        dataItemList.postValue(list)
        val text = when {
            entity.type == TargetType.COUNTER -> "打卡"
            list.last().endTime == 0L -> "结束"
            else -> "开始"
        }
        dataAddItemText.postValue(text)
    }

    fun addItem(type:Int,targetId:Int) {
        when (type) {
            TargetType.COUNTER -> {
                val item = ItemEntity()
                item.uuid = UUID.randomUUID().toString()
                item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                item.targetId = targetId
                DBHelper.getInstance().itemDao().insertItem(item)
                dataToast.postValue("打卡成功")
            }
            TargetType.DURATION -> {
                var item = DBHelper.getInstance().itemDao().queryItemEndTimeNull(targetId)
                if (item == null) {
                    item = ItemEntity()
                    item.uuid = UUID.randomUUID().toString()
                    item.startTime = System.currentTimeMillis() / 60_000 * 60_000
                    item.targetId = targetId
                    DBHelper.getInstance().itemDao().insertItem(item)
                    dataAddItemText.postValue("结束")
                } else {
                    item.endTime = System.currentTimeMillis() / 60_000 * 60_000
                    DBHelper.getInstance().itemDao().updateItem(item)
                    dataAddItemText.postValue("开始")
                }
            }
            else -> {
                dataToast.postValue("未支持")
            }

        }
        queryTargetEntity(targetId)
    }

    fun queryRecordList(targetId: Int, time: Long) {
        val list = DBHelper.getInstance().itemDao().queryOneDayItemsByTargetId(targetId, time, time + Const.ONE_DAY)
        dataRecordList.postValue(list)
    }

}