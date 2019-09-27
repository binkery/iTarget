package com.binkery.ipassword

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binkery.base.activity.BaseViewModel
import com.binkery.base.ext.longToast
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.ExportData
import com.binkery.ipassword.utils.SharedUtils

class EditViewModel : BaseViewModel() {

    private val mItemEntity: MutableLiveData<ItemEntity?> = MutableLiveData()

    fun getItemEntity(): LiveData<ItemEntity?> {
        return mItemEntity
    }

    fun loadItemEntity(id: Int) {
        val entity = DBHelper.instance.itemDao().queryById(id)
        mItemEntity.value = entity
    }

    private fun autoBackup(context: Context) {
        if (SharedUtils.isOpenAutoBackup(context)) {
            val password = SharedUtils.getExportKey(context)
            val path = ExportData.export(context, password)
            "自动备份数据到 $path".longToast(context)

        } else {
            "已保存，您未开启自动备份功能".longToast(context)
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
            "网站名字为空".longToast(activity)
            return
        }
        if (user == "") {
            "用户名为空".longToast(activity)
            return
        }
        if (password == "") {
            "密码为空".longToast(activity)
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