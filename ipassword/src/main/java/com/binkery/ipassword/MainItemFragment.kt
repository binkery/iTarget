package com.binkery.ipassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.sqlite.ItemEntity


class MainItemFragment : Fragment(), ItemAdapter.OnItemClickListener {


    private lateinit var vRecyclerView: RecyclerView

    override fun onItemClick(item: ItemEntity) {
        ItemViewActivity.start(activity!!, item.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vRecyclerView = view.findViewById(R.id.vRecyclerView)
        vRecyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()
        val list = DBHelper.instance.itemDao().queryAll()
        val adapter = ItemAdapter(list, this)
        vRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}