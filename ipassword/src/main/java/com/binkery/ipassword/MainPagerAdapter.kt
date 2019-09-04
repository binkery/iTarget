package com.binkery.ipassword

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fm: FragmentManager,private val listener:OnSetPrimaryItemListener) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainItemFragment()
            1 -> MainSettingFragment()
            else -> MainItemFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        Log.i("bky", "setPrimaryItem " + position + ", obj = " + obj)
        listener.onSetPrimaryItem(position)
        super.setPrimaryItem(container, position, obj)
    }

    interface OnSetPrimaryItemListener {
        fun onSetPrimaryItem(position: Int)
    }
}