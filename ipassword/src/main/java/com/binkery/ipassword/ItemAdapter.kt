package com.binkery.ipassword

import android.widget.TextView
import com.binkery.base.adapter.BaseAdapter
import com.binkery.ipassword.sqlite.ItemEntity

class ItemAdapter(
        private val items: List<ItemEntity>,
        private val listener: OnItemClickListener)
    : BaseAdapter<ItemEntity>() {

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = 0

    override fun getItemLayoutId(viewType: Int): Int = R.layout.layout_item_list

    override fun getItem(position: Int): ItemEntity = items[position]

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        (holder.v0 as TextView).text = item.name
        (holder.v1 as TextView).text = item.username
        holder.itemView.setOnClickListener {
            listener.onItemClick(items[position])
        }
    }


    interface OnItemClickListener {
        fun onItemClick(item: ItemEntity)
    }
}
