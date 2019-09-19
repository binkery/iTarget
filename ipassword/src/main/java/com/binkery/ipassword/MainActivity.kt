package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.base.adapter.BaseAdapter
import com.binkery.base.utils.Dialogs
import com.binkery.base.widgets.RecycleViewDivider
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import com.binkery.ipassword.utils.RequestCode
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BasePasswordActivity() {


    companion object {

        private const val RQC_NEW_ITEM = 100
        private const val RQC_VIEW_ITEM = 101

        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle("iPassword 密码箱")

        DBHelper.instance.init(this)


        vAddItem.setOnClickListener {
            AddItemActivity.start(this@MainActivity, -1, RQC_NEW_ITEM)
        }

        vAppbar.setRightItem("设置", -1, View.OnClickListener {
            SettingActivity.start(this)
        })

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 3, resources?.getColor(R.color.ipw_divider_color)!!))
        updateUI()
    }

    private fun updateUI() {
        val list = DBHelper.instance.itemDao().queryAll()
        val adapter = ItemAdapter(list)
        vRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == RQC_VIEW_ITEM && resultCode == Activity.RESULT_OK -> {
                updateUI()
            }
            requestCode == RQC_NEW_ITEM && resultCode == Activity.RESULT_OK -> {
                updateUI()
            }
        }
    }

    fun onItemClick(item: ItemEntity) {
        ItemViewActivity.start(this, item.id, RQC_VIEW_ITEM)
    }

    override fun shouldCheckPassword(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        val password = SharedUtils.getPassword(this)
        if (password == "") {
            PasswordSettingActivity.start(this, "设置隐私密码", true)
        } else {
            if (SharedUtils.isTokenTimeout(this)) {
                PasswordCheckingActivity.start(this, true)
            } else {
                SharedUtils.updateToken(this)
            }
        }

    }

    inner class ItemAdapter(
            private val items: List<ItemEntity>)
        : BaseAdapter<ItemEntity>() {

        override fun getItemCount(): Int = items.size

        override fun getItemViewType(position: Int): Int = 0

        override fun getItemLayoutId(viewType: Int): Int = R.layout.layout_item_list

        override fun getItem(position: Int): ItemEntity = items[position]

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            (holder.v0 as TextView).text = item.name
            (holder.v1 as TextView).text = "帐号：" + item.username
            (holder.v2 as TextView).text = String(CharArray(item.password.length) { '*' })
            holder.itemView.setOnClickListener {
                onItemClick(items[position])
            }
        }
    }
}
