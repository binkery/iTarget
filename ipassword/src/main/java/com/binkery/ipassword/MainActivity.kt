package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.base.activity.BaseActivity
import com.binkery.ipassword.sqlite.DBHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity()  {


    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun onContentCreate(savedInstanceState: Bundle?) {
        setTitle(R.string.app_name)

        DBHelper.instance.init(this)


        vViewPager.adapter = MainPagerAdapter(supportFragmentManager)

//

        vAddItem.setOnClickListener {
            AddItemActivity.start(this@MainActivity, -1)
        }

    }

    override fun onResume() {
        super.onResume()

    }


}
