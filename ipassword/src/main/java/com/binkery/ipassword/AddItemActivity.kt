package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Utils
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity, itemId: Int) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivity(intent)
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
            setTitle("编辑")
        } else {
            setTitle("新增")
        }

        vSave.setOnClickListener(View.OnClickListener {

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
                finish()
            } else {
                val itemEntity = DBHelper.instance.itemDao().queryById(mItemId)
                itemEntity.name = itemName
                itemEntity.username = username
                itemEntity.password = password
                itemEntity.comments = comments
                DBHelper.instance.itemDao().update(itemEntity)
                finish()
            }


        })


    }

}