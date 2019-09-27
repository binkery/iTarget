package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.binkery.base.utils.Dialogs
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

    override fun getContentLayoutId(): Int = R.layout.activity_add_item

    override fun onContentCreate(savedInstanceState: Bundle?) {

        val viewModel = getViewModel<EditViewModel>()
        viewModel.getItemEntity().observe(this, Observer { entity->
            if (entity == null) {
                vAppbar.setTitle("新建")
                vDelete.visibility = View.GONE
            } else {
                vItemName.setText(entity.name)
                vUserName.setText(entity.username)
                vPassword.setText(entity.password)
                vComments.setText(entity.comments)
                vAppbar.setTitle("编辑")
                vDelete.visibility = View.VISIBLE
                vDelete.setOnClickListener {
                    val message = "是否删除 " + entity.name + " 上，账号为 " + entity.username + " 的信息吗？"
                    Dialogs.comfirm(this, "删除确认", message, "再想想", "确认删除", null, View.OnClickListener {
                        viewModel.delete(this)
                    })
                }
            }
        })

        val itemId = intent.getIntExtra("item_id", -1)
        viewModel.loadItemEntity(itemId)

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