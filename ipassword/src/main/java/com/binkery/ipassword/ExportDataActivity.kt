package com.binkery.ipassword

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import com.binkery.base.utils.Dialogs
import com.binkery.base.utils.Utils
import com.binkery.ipassword.code.CodeEntity
import com.binkery.ipassword.sqlite.DBHelper
import com.binkery.ipassword.utils.ExportData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_export_path.*
import java.io.File

class ExportDataActivity : BasePasswordActivity() {

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, ExportDataActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int = R.layout.activity_export_path

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle("导出数据")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        vPasswordInput.setText("")

        vAppbar.setRightItem("开始导出", -1, View.OnClickListener {

            val password = vPasswordInput.text.toString()
            if (password.length < 6) {
                Utils.toast(this, "密钥长度大于等于 6")
                return@OnClickListener
            }
            val path = ExportData.export(this, password)
            Dialogs.alert(this, "导出成功", "成功导出数据到 $path", "朕知道了", View.OnClickListener {
                finish()
            })

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            vPath.text = ExportData.getRootPath(this)
        }
    }
}