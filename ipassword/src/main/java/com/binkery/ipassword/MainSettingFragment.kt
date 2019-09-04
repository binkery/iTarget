package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.binkery.base.utils.Utils
import com.binkery.base.widgets.SettingItemView
import com.binkery.ipassword.utils.SharedUtils

class MainSettingFragment : Fragment() {

    private var vPassword: SettingItemView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting_page, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vPassword = view.findViewById(R.id.vPassword)
        vPassword?.setOnClickListener {
            PasswordCheckingActivity.start(this, 2001)
        }

        view.findViewById<View>(R.id.vExportData).setOnClickListener {
            //            Utils.toast(activity!!, "export data")
//            activity?.apply {
////                SelectedExportDataActivity.start(this)
//            }
            PasswordSettingActivity.start(this,"设置导出文件加密密码",2003)
        }

        view.findViewById<View>(R.id.vImportData).setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 2001 && resultCode == Activity.RESULT_OK -> {
                val password = data?.getStringExtra("password") ?: ""
                if (SharedUtils.checkPassword(activity!!, password)) {
                    PasswordSettingActivity.start(this, "修改密码", 2002)
                } else {
                    Utils.toast(activity!!, "密码错误，请重新输入")
                    PasswordCheckingActivity.start(this, 2001)
                }
            }
            requestCode == 2002 && resultCode == Activity.RESULT_OK -> {
                val password = data?.getStringExtra("password") ?: ""
                SharedUtils.setPassword(activity!!, password)
                SharedUtils.updateToken(activity!!)
                Utils.toast(context!!,"密码已修改")
            }
            requestCode == 2003 && resultCode == Activity.RESULT_OK ->{
                val password = data?.getStringExtra("password") ?: ""
                ExportPathActivity.start(activity!!,password)
            }
        }

    }


}