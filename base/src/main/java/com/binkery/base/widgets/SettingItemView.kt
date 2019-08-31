package com.binkery.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.binkery.base.R

class SettingItemView : RelativeLayout {

    private val vKey: TextView
    private val vValue: TextView

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_setting_item_view, this, true)
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SettingItemView, defStyleAttr, 0)
        vKey = findViewById(R.id.vKey)
        vValue = findViewById(R.id.vValue)
        vKey.text = typedArray.getText(R.styleable.SettingItemView_key)
        vValue.text = typedArray.getText(R.styleable.SettingItemView_value)
        typedArray.recycle()
    }

    fun setKey(text: String) {
        vKey.text = text
    }

    fun setValue(text: String) {
        vValue.text = text
    }

    fun getValue(): String {
        return vValue.text.toString()
    }
}