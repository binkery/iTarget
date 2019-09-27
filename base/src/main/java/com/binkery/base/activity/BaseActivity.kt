package com.binkery.base.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.binkery.base.R
import com.binkery.base.utils.Utils
import com.binkery.base.widgets.Appbar
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity : AppCompatActivity(), ViewContainer {

    protected val vAppbar = Appbar()

    override fun showToast(message: String) {
        Utils.toast(this, message)
    }

    inline fun <reified T : BaseViewModel> getViewModel(): T {
        val viewModel = ViewModelProviders.of(this).get(T::class.java)
        viewModel.init(this)
        return viewModel
    }

     override fun onCreate(savedInstanceState: Bundle?) {
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
        finishActivity()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    fun finishActivity(resultCode: Int = -1, data: Intent? = null) {
        setResult(resultCode, data)
        finish()
    }

}