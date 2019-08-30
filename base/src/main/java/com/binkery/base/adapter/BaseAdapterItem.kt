package com.binkery.base.adapter

import android.view.View

abstract class BaseAdapterItem<E> {

    abstract fun getLayoutId(): Int

    abstract fun onBindView(view: View)

    abstract fun onBindData(e: E)
}