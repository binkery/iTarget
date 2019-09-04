package com.binkery.base.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.binkery.base.R
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity : AppCompatActivity() {


    override final fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.base_activity)

        val layoutId = getContentLayoutId()
        LayoutInflater.from(this).inflate(layoutId, vContainer, true)
        onContentCreate(savedInstanceState)

        vActionBarBack.setOnClickListener(View.OnClickListener {
            onBackClick()
        })
    }

    fun setActionBarEnable(enable: Boolean) {
        if (!enable) {
            vActionBar.visibility = View.GONE
        }
    }

    abstract fun getContentLayoutId(): Int
    abstract fun onContentCreate(savedInstanceState: Bundle?)

    open fun onBackClick(){
        finish()
    }

    fun setTitle(title: String) {
        vActionBarTitle.text = title
    }

    override fun setTitle(resId: Int) {
        vActionBarTitle.setText(resId)
    }

//    fun setActionBarBackClickListener(listener:View.OnClickListener){
//        vActionBarBack.setOnClickListener(listener)
//    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackClick()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

}