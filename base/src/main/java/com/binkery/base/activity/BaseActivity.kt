package com.binkery.base.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.binkery.base.R
import com.binkery.base.widgets.Appbar
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity : AppCompatActivity() {

    protected val vAppbar = Appbar()


    override final fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.navigationBarColor = resources.getColor(R.color.base_nav_bar_background)
////            vContainer.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//        }
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.base_activity)
        vAppbar.init(vActionBar)
        vAppbar.setLeftClickListener(View.OnClickListener {
            onBackClick()
        })
        val layoutId = getContentLayoutId()
        LayoutInflater.from(this).inflate(layoutId, vContainer, true)
        onContentCreate(savedInstanceState)


    }

    protected fun getContainer(): ViewGroup {
        return vContainer
    }

    abstract fun getContentLayoutId(): Int
    abstract fun onContentCreate(savedInstanceState: Bundle?)

    open fun onBackClick() {
        finish()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

}