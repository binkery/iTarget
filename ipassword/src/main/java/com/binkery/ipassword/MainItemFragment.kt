package com.binkery.ipassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binkery.base.adapter.BaseAdapter
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity


class MainItemFragment : Fragment() {

    private lateinit var vRecyclerView: RecyclerView

    fun onItemClick(item: ItemEntity) {
        ItemViewActivity.start(activity!!, item.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vRecyclerView = view.findViewById(R.id.vRecyclerView)
        vRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()
        val list = DBHelper.instance.itemDao().queryAll()
        val adapter = ItemAdapter(list)
        vRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
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
            (holder.v1 as TextView).text = item.username
            holder.itemView.setOnClickListener {
                onItemClick(items[position])
            }
        }
    }
}