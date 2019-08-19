package com.binkery.itarget.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.binkery.itarget.R
import java.lang.ref.WeakReference

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
abstract class BaseAdapter<Entity>(activity: Activity) : RecyclerView.Adapter<BaseAdapter.ViewHolder<Entity>>() {

    open val mActivityRef: WeakReference<Activity>

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
        holder?.viewCard?.onBindView(getItem(position), holder.itemView)
        holder?.itemView?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                holder.viewCard.onItemClick(getItem(position), position)
            }
        })
    }

    abstract fun getItem(position: Int): Entity?

    abstract override fun getItemCount(): Int

    final override fun getItemViewType(position: Int): Int {
        return getItemViewCardType(position)
    }

    abstract fun getItemViewCardType(position: Int): Int

    abstract fun onCreateViewCard(parent: ViewGroup?, viewType: Int): BaseViewCard<Entity>

    override fun onViewAttachedToWindow(holder: ViewHolder<Entity>?) {
        super.onViewAttachedToWindow(holder)
        holder?.itemView?.animation = AnimationUtils.loadAnimation(mActivityRef.get(), R.anim.item_animation_fall_down)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder<Entity>?) {
        super.onViewDetachedFromWindow(holder)
        holder?.itemView?.clearAnimation()
    }

    class ViewHolder<E>(itemView: View, val viewCard: BaseViewCard<E>) : RecyclerView.ViewHolder(itemView) {
    }
}