package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.util.SparseIntArray
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.binkery.base.adapter.BaseAdapter
import com.binkery.base.utils.Utils
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity
import kotlinx.android.synthetic.main.activity_selected_export_data.*

class SelectedExportDataActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SelectedExportDataActivity::class.java)
            activity.startActivity(intent)
        }
    }


    override fun getContentLayoutId(): Int = R.layout.activity_selected_export_data

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle("选择导出内容")
        vRecyclerView.layoutManager = LinearLayoutManager(this)
        val list = DBHelper.instance.itemDao().queryAll()
        val mSelectedIds = Array<Boolean>(list.size) { true }
        val adapter = ItemAdapter(list, object : OnItemSelectedListener {
            override fun onItemSelected(position: Int, selected: Boolean) {
                mSelectedIds[position] = selected
            }
        }, mSelectedIds)
        vRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        vNextStep.setOnClickListener {
            for (i in 0 until mSelectedIds.size) {
                Utils.log(i.toString() + "," + mSelectedIds[i])
            }
//            ExportPathActivity.start(this,"")
        }
    }

    private class ItemAdapter(
            private val items: List<ItemEntity>,
            private val listener: OnItemSelectedListener,
            private val checkeds: Array<Boolean>) : BaseAdapter<ItemEntity>() {

        override fun getItemCount(): Int = items.size

        override fun getItemViewType(position: Int): Int = 0

        override fun getItemLayoutId(viewType: Int): Int = R.layout.layout_item_selector

        override fun getItem(position: Int): ItemEntity = items[position]

        override fun onBindViewHolder(holder: VH, position: Int) {
            (holder.v0 as TextView).text = items[position].name
            (holder.v1 as TextView).text = items[position].username
            holder.itemView.setOnClickListener({
                (holder.v2 as CheckBox).isChecked = !(holder.v2 as CheckBox).isChecked
                Utils.log("item click")
            })
            (holder.v2 as CheckBox).setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    listener.onItemSelected(position, isChecked)
                    Utils.log("item selcted change")
                }
            })
            (holder.v2 as CheckBox).isChecked = checkeds[position]
//
//            holder.itemView.setOnClickListener {
//                (holder.v2 as CheckBox).isSelected = !(holder.v2 as CheckBox).isSelected
//                listener.onItemSelected(position, (holder.v2 as CheckBox).isSelected)
//            }
        }
    }

    private interface OnItemSelectedListener {
        fun onItemSelected(position: Int, selected: Boolean)
    }
}