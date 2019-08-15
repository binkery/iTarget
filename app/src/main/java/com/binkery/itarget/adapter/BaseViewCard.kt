package com.binkery.itarget.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import java.lang.ref.WeakReference

/**
 * Create by binkery@gmail.com
 * on 2019 08 08
 * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
 */
abstract class BaseViewCard<E> {

    private var mActivityRef: WeakReference<Activity>? = null
    private var mAdapter: BaseAdapter<E>? = null

    fun onInitCard(view: View, activity: Activity, adapter: BaseAdapter<E>) {
        mActivityRef = WeakReference(activity)
        mAdapter = adapter
        onCreateView(view)
    }

    open fun getActivity(): Activity? {
        return mActivityRef?.get()
    }

    open fun getContext(): Context? {
        return mActivityRef?.get()?.applicationContext
    }

    open fun getAdapter(): BaseAdapter<E>? {
        return mAdapter
    }

    abstract fun onCreateView(view: View)

    abstract fun getLayoutId(): Int

    abstract fun onBindView(entity: @UnsafeVariance E?, view: View)

    abstract fun onItemClick(entity: @UnsafeVariance E?, position: Int)

}