package com.binkery.ipassword

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.ExportData
import com.binkery.ipassword.utils.SharedUtils

class EditViewModel : ViewModel() {

    private val mItemEntity: MutableLiveData<ItemEntity?> = MutableLiveData()
    private val mToast: MutableLiveData<String> = MutableLiveData()

    fun getItemEntity(): LiveData<ItemEntity?> {
        return mItemEntity
    }

    fun getToast(): LiveData<String> {
        return mToast
    }

    fun loadItemEntity(id: Int) {
        val entity = DBHelper.instance.itemDao().queryById(id)
        mItemEntity.value = entity
    }

    private fun autoBackup(context: Context) {
        if (SharedUtils.isOpenAutoBackup(context)) {
            val password = SharedUtils.getExportKey(context)
            val path = ExportData.export(context, password)
            mToast.value = "自动备份数据到 $path"
        } else {
            mToast.value = "已保存，您未开启自动备份功能"
        }
    }

    fun delete(activity: AddItemActivity) {
        mItemEntity.value?.apply {
            DBHelper.instance.itemDao().delete(this)
            autoBackup(activity)
            activity.setResult(Activity.RESULT_FIRST_USER)
            activity.finish()
        }
    }

    fun save(activity: AddItemActivity, name: String, user: String, password: String, comment: String) {
        if (name == "") {
            mToast.value = "网站名字为空"
            return
        }
        if (user == "") {
            mToast.value = "用户名为空"
            return
        }
        if (password == "") {
            mToast.value = "密码为空"
            return
        }
        if (mItemEntity.value == null) {
            val item = ItemEntity()
            item.name = name
            item.username = user
            item.password = password
            item.comments = comment
            DBHelper.instance.itemDao().insert(item)
        } else {
            mItemEntity.value?.apply {
                this.name = name
                this.username = user
                this.password = password
                this.comments = comment
                DBHelper.instance.itemDao().update(this)
            }
        }
        autoBackup(activity)
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }


}