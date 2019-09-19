package com.binkery.ipassword

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.binkery.ipassword.utils.SharedUtils
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_setting

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle("设置")

        vPassword.setOnClickListener {
            PasswordSettingActivity.start(this, "修改密码", false)
        }

        vExportData.setOnClickListener {
            ExportDataActivity.start(this)
        }

        vImportData.setOnClickListener {
            ImportDataActivity.start(this)
        }

        vAbout.setOnClickListener {
            AboutActivity.start(this)
        }

        vVersion.setValue(BuildConfig.VERSION_NAME)

        vAutoBackup.setOnClickListener { AutoBackupActivity.start(this) }


    }

    override fun onResume() {
        super.onResume()
        if (SharedUtils.isOpenAutoBackup(this)) {
            vAutoBackup.setValue("已开启")
        } else {
            vAutoBackup.setValue("已关闭")
        }
    }
}