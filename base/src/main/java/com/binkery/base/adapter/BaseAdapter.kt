package com.binkery.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binkery.base.R

abstract class BaseAdapter<E> : RecyclerView.Adapter<BaseAdapter.VH>() {


    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutId = getItemLayoutId(viewType)
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val vh = VH(view)
        return vh
    }

    abstract override fun getItemCount(): Int
    abstract override fun getItemViewType(position: Int): Int
    abstract fun getItemLayoutId(viewType: Int): Int
    abstract fun getItem(position: Int): E
    abstract override fun onBindViewHolder(holder: VH, position: Int)

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v0: View?
        val v1: View?
        val v2: View?
        val v3: View?
        val v4: View?
        val v5: View?
        val v6: View?
        val v7: View?
        val v8: View?
        val v9: View?
        val v10: View?

        init {
            v0 = itemView.findViewById(R.id.v0)
            v1 = itemView.findViewById(R.id.v1)
            v2 = itemView.findViewById(R.id.v2)
            v3 = itemView.findViewById(R.id.v3)
            v4 = itemView.findViewById(R.id.v4)
            v5 = itemView.findViewById(R.id.v5)
            v6 = itemView.findViewById(R.id.v6)
            v7 = itemView.findViewById(R.id.v7)
            v8 = itemView.findViewById(R.id.v8)
            v9 = itemView.findViewById(R.id.v9)
            v10 = itemView.findViewById(R.id.v10)
        }
    }
}