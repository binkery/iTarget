package com.binkery.ipassword.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.binkery.ipassword.R

class PasswordInput : LinearLayout {

    private val vValue1: TextView
    private val vValue2: TextView
    private val vValue3: TextView
    private val vValue4: TextView

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.layout_password_input, this, true)
        vValue1 = findViewById(R.id.vValue1)
        vValue2 = findViewById(R.id.vValue2)
        vValue3 = findViewById(R.id.vValue3)
        vValue4 = findViewById(R.id.vValue4)
    }


    fun setValue(value: String) {
//        if(value.isEmpty()){
//            vValue1.text = ""
//            vValue2.text = ""
//            vValue3.text = ""
//            vValue4.text = ""
//            return
//        }
        vValue1.text = ""
        vValue2.text = ""
        vValue3.text = ""
        vValue4.text = ""
        if(value.length > 0){
            vValue1.text = value.substring(0, 1)
        }
        if(value.length > 1){
            vValue2.text = value.substring(1, 2)
        }
        if(value.length > 2){
            vValue3.text = value.substring(2, 3)
        }
        if(value.length > 3){
            vValue4.text = value.subSequence(3, 4)
        }
    }

}