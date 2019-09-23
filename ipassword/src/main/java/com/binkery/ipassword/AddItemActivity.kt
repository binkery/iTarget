package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.sqlite.DBHelper
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : BasePasswordActivity() {

    companion object {
        private const val RQC_PASSWORD_GENERATOR = 1001

        fun start(activity: Activity, itemId: Int, requestCode: Int) {
            val intent = Intent(activity, AddItemActivity::class.java)
            intent.putExtra("item_id", itemId)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    private var mItemId: Int = -1

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {

        val viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        viewModel.getItemEntity().observe(this, Observer {
            if (it == null) {
                vAppbar.setTitle("新建")
                vDelete.visibility = View.GONE
            } else {
                vItemName.setText(it.name)
                vUserName.setText(it.username)
                vPassword.setText(it.password)
                vComments.setText(it.comments)
                vAppbar.setTitle("编辑")
                vDelete.visibility = View.VISIBLE
                vDelete.setOnClickListener {
                    val entity = DBHelper.instance.itemDao().queryById(mItemId)
                    val message = "是否删除 " + entity.name + " 上，账号为 " + entity.username + " 的信息吗？"

                    Dialogs.comfirm(this, "删除确认", message, "再想想", "确认删除", null, View.OnClickListener {
                        viewModel.delete(this)
                    })
                }
            }
        })

        mItemId = intent.getIntExtra("item_id", -1)
        viewModel.loadItemEntity(mItemId)

        viewModel.getToast().observe(this, Observer {
            Utils.toast(this, it)
        })

        vAppbar.setRightItem("保存", -1, View.OnClickListener {
            val itemName = vItemName.text.toString().trim()
            val username = vUserName.text.toString().trim()
            val password = vPassword.text.toString().trim()
            val comments = vComments.text.toString().trim()
            viewModel.save(this, itemName, username, password, comments)
        })

        vPasswordGenerator.setOnClickListener {
            PasswordGeneratorActivity.start(this, RQC_PASSWORD_GENERATOR)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode == RQC_PASSWORD_GENERATOR && resultCode == Activity.RESULT_OK->{
                val password = data?.getStringExtra("password")
                vPassword.setText(password)
            }

        }
    }

}