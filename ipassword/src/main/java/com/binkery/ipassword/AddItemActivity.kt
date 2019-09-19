package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.code.CodeEntity
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.ExportData
import com.binkery.ipassword.utils.SharedUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity, itemId: Int, requestCode: Int) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var mItemId: Int = -1

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {

        mItemId = intent.getIntExtra("item_id", -1)
        if (mItemId != -1) {
            val itemEntity = DBHelper.instance.itemDao().queryById(mItemId)
            vItemName.setText(itemEntity.name)
            vUserName.setText(itemEntity.username)
            vPassword.setText(itemEntity.password)
            vComments.setText(itemEntity.comments)
            vAppbar.setTitle("编辑")
        } else {
            vAppbar.setTitle("新建")
        }

        vAppbar.setRightItem("保存", -1, View.OnClickListener {
            val itemName = vItemName.text.toString().trim()
            val username = vUserName.text.toString().trim()
            val password = vPassword.text.toString().trim()
            val comments = vComments.text.toString().trim()

            if (itemName == "") {
                Utils.toast(this@AddItemActivity, R.string.empty_item_name)
                return@OnClickListener
            }
            if (username == "") {
                Utils.toast(this@AddItemActivity, R.string.empty_user_name)
                return@OnClickListener
            }
            if (password == "") {
                Utils.toast(this@AddItemActivity, R.string.empty_password)
                return@OnClickListener
            }

            if (mItemId == -1) {
                val item = ItemEntity()
                item.name = itemName
                item.username = username
                item.password = password
                item.comments = comments
                DBHelper.instance.itemDao().insert(item)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                val itemEntity = DBHelper.instance.itemDao().queryById(mItemId)
                itemEntity.name = itemName
                itemEntity.username = username
                itemEntity.password = password
                itemEntity.comments = comments
                DBHelper.instance.itemDao().update(itemEntity)
                setResult(Activity.RESULT_OK)
                finish()
            }

            if (SharedUtils.isOpenAutoBackup(this)) {
                val path = ExportData.export(this, SharedUtils.getExportKey(this))
                Utils.toast(this, "自动备份到 $path")
            }
        })

        if (mItemId == -1) {
            vDelete.visibility = View.GONE
        } else {
            vDelete.visibility = View.VISIBLE
            vDelete.setOnClickListener {
                val entity = DBHelper.instance.itemDao().queryById(mItemId)
                val message = "是否删除 " + entity.name + " 上，账号为 " + entity.username + " 的信息吗？"

                Dialogs.comfirm(this, "删除确认", message, "再想想", "确认删除", null, View.OnClickListener {
                    DBHelper.instance.itemDao().delete(entity)
                    if (SharedUtils.isOpenAutoBackup(this@AddItemActivity)) {
                        val password = SharedUtils.getExportKey(this@AddItemActivity)
                        val path = ExportData.export(this@AddItemActivity, password)
                        Utils.toast(this@AddItemActivity, "自动备份数据到 $path")
                    }
                    setResult(Activity.RESULT_FIRST_USER)
                    finish()
                })
            }
        }


    }

}