//package com.binkery.itarget.base
//
//import android.app.Activity
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.binkery.itarget.utils.Utils
//
///**
// * Create by binkery@gmail.com
// * on 2019 08 13
// * Copyright (c) 2019 iTarget.binkery.com. All rights reserved.
// */
//abstract class BaseFragment : Fragment() {
//
//    private val TAG = "BaseFragment"
//
//
//    final override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        val layoutId = getContentLayoutId()
//        val view = LayoutInflater.from(context).inflate(layoutId, container, false)
//        return view
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        Utils.log(TAG, "onActivityCreated")
//        super.onActivityCreated(savedInstanceState)
//    }
//
//    override fun onAttach(context: Context?) {
//        Utils.log(TAG, "onAttach" + context)
//        super.onAttach(context)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Utils.log(TAG, "onCreate")
//        super.onCreate(savedInstanceState)
//    }
//
//    abstract fun getContentLayoutId(): Int
//}