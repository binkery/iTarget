package com.binkery.ipassword.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import com.binkery.ipassword.R

class KeyBoardView : GridLayout, View.OnClickListener {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_keyborad, this, true)
        findViewById<TextView>(R.id.vKey0).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey1).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey2).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey3).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey4).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey5).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey6).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey7).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey8).setOnClickListener(this)
        findViewById<TextView>(R.id.vKey9).setOnClickListener(this)
        findViewById<TextView>(R.id.vKeyDelete).setOnClickListener(this)
        findViewById<TextView>(R.id.vKeyReturn).setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.vKey0 -> mListener?.onValueChanged("0")
            R.id.vKey1 -> mListener?.onValueChanged("1")
            R.id.vKey2 -> mListener?.onValueChanged("2")
            R.id.vKey3 -> mListener?.onValueChanged("3")
            R.id.vKey4 -> mListener?.onValueChanged("4")
            R.id.vKey5 -> mListener?.onValueChanged("5")
            R.id.vKey6 -> mListener?.onValueChanged("6")
            R.id.vKey7 -> mListener?.onValueChanged("7")
            R.id.vKey8 -> mListener?.onValueChanged("8")
            R.id.vKey9 -> mListener?.onValueChanged("9")
            R.id.vKeyDelete -> mListener?.onValueDeleted()
            R.id.vKeyReturn -> mListener?.onReturn()
        }
    }

    private var mListener: OnValueChangedListener? = null

    fun setOnValueChangedListener(listener: OnValueChangedListener) {
        mListener = listener
    }

    interface OnValueChangedListener {
        fun onValueChanged(value: String)
        fun onValueDeleted()
        fun onReturn()
    }

}