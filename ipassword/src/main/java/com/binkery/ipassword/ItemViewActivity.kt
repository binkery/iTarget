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
import com.binkery.ipassword.utils.ExportData
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_item_view.*

class ItemViewActivity : BasePasswordActivity() {

    companion object {

        private const val RQC_EDIT = 101

        fun start(activity: Activity, itemId: Int, requestCode: Int) {
            val intent = Intent(activity, ItemViewActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivityForResult(intent, requestCode)
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
            AddItemActivity.start(this, mItemId, RQC_EDIT)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == RQC_EDIT && resultCode == Activity.RESULT_OK -> {

            }
            requestCode == RQC_EDIT && resultCode == Activity.RESULT_FIRST_USER -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val entity = DBHelper.instance.itemDao().queryById(mItemId)

        vAppbar.setTitle("账号信息")

        entity.apply {
            vItemName.text = name
            vUserName.text = username
            vPassword.text = password
            vComments.text = comments
        }


    }

}