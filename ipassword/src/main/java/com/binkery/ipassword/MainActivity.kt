package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binkery.base.activity.BaseActivity
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ItemAdapter.OnItemClickListener {


    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle("iPassword")

        DBHelper.instance.init(this)
        vRecyclerView.layoutManager = LinearLayoutManager(this)

        vAddItem.setOnClickListener {
            AddItemActivity.start(this@MainActivity, -1)
        }

    }

    override fun onResume() {
        super.onResume()
        val list = DBHelper.instance.itemDao().queryAll()
        val adapter = ItemAdapter(list, this)
        vRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(item: ItemEntity) {
        ItemViewActivity.start(this, item.id)
    }

}
