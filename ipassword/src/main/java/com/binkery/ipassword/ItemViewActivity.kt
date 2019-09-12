package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.binkery.base.activity.BaseActivity
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import kotlinx.android.synthetic.main.activity_item_view.*

class ItemViewActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity, itemId: Int) {
            val intent = Intent(activity, ItemViewActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivity(intent)
        }
    }

    private var mItemId: Int = -1

    override fun getContentLayoutId(): Int = R.layout.activity_item_view

    override fun onContentCreate(savedInstanceState: Bundle?) {
        mItemId = intent.getIntExtra("item_id", -1)
        if (mItemId == -1) {
            finish()
            return
        }
        vAppbar.setRightItem(R.string.edit, -1, View.OnClickListener {
            AddItemActivity.start(this, mItemId)
        })

    }

    override fun onResume() {
        super.onResume()
        val entity = DBHelper.instance.itemDao().queryById(mItemId)

        vAppbar.setTitle(R.string.title_account_detail)

        entity.apply {
            vItemName.text = name
            vUserName.text = username
            vPassword.text = password
            vComments.text = comments
        }

        vDelete.setOnClickListener {
            val message = "是否删除 " + entity.name + " 上，账号为 " + entity.username + " 的信息吗？"

            Dialogs.showDeleteDialog(this, message, object : Dialogs.OnConfromListener {
                override fun onConform() {
                    DBHelper.instance.itemDao().delete(entity)
                    finish()
                }
            })
        }

    }

}