package com.binkery.base.widgets

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.binkery.base.R

class Appbar {

    private lateinit var vTitle: TextView
    private lateinit var vIcon: ImageView
    private lateinit var vRightItem: TextView
    private lateinit var vContainer: RelativeLayout

    fun show() {

    }

    fun hide() {
        vContainer.visibility = View.GONE
    }


    fun init(relativeLayout: RelativeLayout) {
        vContainer = relativeLayout
        vTitle = relativeLayout.findViewById(R.id.vAppbarTitle)
        vIcon = relativeLayout.findViewById(R.id.vAppbarLeft)
        vRightItem = relativeLayout.findViewById(R.id.vAppbarRight)
    }

    fun setTitle(title: String) {
        vTitle.text = title
    }

    fun setTitle(resId: Int) {
        vTitle.setText(resId)
    }

    fun setTitleBackground(color: Int) {
        vContainer.setBackgroundColor(color)
    }

    fun setTitleTextColor(color: Int) {
        vTitle.setTextColor(color)
    }

    fun setLeftIcon(resId: Int) {
        vIcon.setImageResource(resId)
    }

    fun setLeftClickListener(listener: View.OnClickListener) {
        vIcon.setOnClickListener(listener)
    }

    fun setCustomView(view: View) {

    }

    fun setRightItem(title: String, icon: Int, listener: View.OnClickListener) {
        vRightItem.visibility = View.VISIBLE
        vRightItem.text = title
        vRightItem.setOnClickListener(listener)
    }

    fun setRightItem(title: Int, icon: Int, listener: View.OnClickListener) {
        vRightItem.visibility = View.VISIBLE
        vRightItem.setText(title)
        vRightItem.setOnClickListener(listener)
    }

//    fun setLeftItem(title: String, icon: Int, listener: View.OnClickListener) {
//
//    }

}