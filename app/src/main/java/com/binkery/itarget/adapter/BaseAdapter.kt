package com.binkery.itarget.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 *
 *
 */
abstract class BaseAdapter<Entity>(activity: Activity) : RecyclerView.Adapter<BaseAdapter.ViewHolder<Entity>>() {

    open val mActivityRef: WeakReference<Activity>

    private var mList: MutableList<Entity>? = null

    init {
        mActivityRef = WeakReference(activity)
    }

    final override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<Entity> {
        val viewCard: BaseViewCard<Entity> = onCreateViewCard(parent, viewType)
        val layoutId = viewCard.getLayoutId()
        val view = LayoutInflater.from(parent?.context).inflate(layoutId, parent, false)
        viewCard.onInitCard(view, mActivityRef.get()!!, this)
        return ViewHolder(view, viewCard)
    }

    final override fun onBindViewHolder(holder: ViewHolder<Entity>?, position: Int) {
        holder?.viewCard?.onBindView(getItem(position)!!, holder.itemView)
        holder?.itemView?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                holder.viewCard.onItemClick(getItem(position), position)
            }
        })
    }

    fun setData(list: MutableList<Entity>?) {
        mList = list
        notifyDataSetChanged()
    }

    fun getData(): MutableList<Entity>? {
        return mList
    }

    fun getItem(position: Int): Entity? {
        return mList?.get(position)
    }

    final override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    final override fun getItemViewType(position: Int): Int {
        return getItemViewCardType(position)
    }

    abstract fun getItemViewCardType(position: Int):Int

    abstract fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<Entity>

    class ViewHolder<E>(itemView: View, val viewCard: BaseViewCard<E>) : RecyclerView.ViewHolder(itemView) {

    }
}