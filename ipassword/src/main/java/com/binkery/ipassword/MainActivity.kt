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
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle(R.string.app_name)

        DBHelper.instance.init(this)


        vAddItem.setOnClickListener {
            AddItemActivity.start(this@MainActivity, -1)
        }

        vAppbar.setRightItem("设置", -1, View.OnClickListener {
            SettingActivity.start(this)
        })

        vRecyclerView.layoutManager = LinearLayoutManager(this)
        vRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 3, resources?.getColor(R.color.ipw_divider_color)!!))

    }

    fun onItemClick(item: ItemEntity) {
        ItemViewActivity.start(this, item.id)
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
                val list = DBHelper.instance.itemDao().queryAll()
                val adapter = ItemAdapter(list)
                vRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
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
            (holder.v2 as TextView).text = item.password.replace(Regex("[\\w]"), "*")
            holder.itemView.setOnClickListener {
                onItemClick(items[position])
            }
        }
    }
}
