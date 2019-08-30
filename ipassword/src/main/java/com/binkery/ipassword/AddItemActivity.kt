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
    private var mItemEntity: ItemEntity = ItemEntity()

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {

        setTitle("新增")

        mItemId = intent.getIntExtra("item_id", -1)
        if (mItemId != -1) {
            mItemEntity = DBHelper.instance.itemDao().queryById(mItemId)

            vItemName.setText(mItemEntity.name)
            vUserName.setText(mItemEntity.username)
            vPassword.setText(mItemEntity.password)
            vComments.setText(mItemEntity.comments)
            setTitle("编辑")
        }

        vSave.setOnClickListener(View.OnClickListener {

            val itemName = vItemName.text.toString().trim()
            val username = vUserName.text.toString().trim()
            val password = vPassword.text.toString().trim()
            val comments = vComments.text.toString().trim()

            if (itemName == "") {
                Utils.toast(this@AddItemActivity, "empty item name")
                return@OnClickListener
            }
            if (username == "") {
                Utils.toast(this@AddItemActivity, "empty user name")
                return@OnClickListener
            }
            if (password == "") {
                Utils.toast(this@AddItemActivity, "empty password")
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
                mItemEntity.name = itemName
                mItemEntity.username = username
                mItemEntity.password = password
                mItemEntity.comments = comments
                DBHelper.instance.itemDao().update(mItemEntity)
                finish()
            }


        })


    }

}