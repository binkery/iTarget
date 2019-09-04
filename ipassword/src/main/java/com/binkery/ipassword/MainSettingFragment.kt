package com.binkery.ipassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.binkery.base.widgets.SettingItemView

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
            PasswordSettingActivity.start(activity, "修改密码", false)
        }

        view.findViewById<View>(R.id.vExportData).setOnClickListener {
            //            Utils.toast(activity!!, "export data")
//            activity?.apply {
////                SelectedExportDataActivity.start(this)
//            }
//            PasswordSettingActivity.start(this,"设置导出文件加密密码",2003)
        }

        view.findViewById<View>(R.id.vImportData).setOnClickListener {

        }
    }


}