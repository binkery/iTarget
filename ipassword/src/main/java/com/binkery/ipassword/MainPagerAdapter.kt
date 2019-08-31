package com.binkery.ipassword

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainItemFragment()
            1 -> MainSettingFragment()
            else -> MainItemFragment()
        }
    }

    override fun getCount(): Int = 2
}