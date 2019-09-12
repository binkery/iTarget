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

    private var mPath = ""

    override fun getContentLayoutId(): Int = R.layout.activity_export_path

    override fun onContentCreate(savedInstanceState: Bundle?) {
        vAppbar.setTitle("导出数据")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        vPasswordInput.setText("")

        vAppbar.setRightItem("开始导出", -1, View.OnClickListener {

            val password = vPasswordInput.text.toString()
            if (password.length < 4) {
                Utils.toast(this, "请设置四位数密码")
                return@OnClickListener
            }
            val datas = DBHelper.instance.itemDao().queryAll()
            val json = Gson().toJson(datas)

            CodeEntity.encode2File(mPath, json, password)
            Dialogs.alert(this, "导出成功", "成功导出数据到 " + mPath, "朕知道了", View.OnClickListener {
                finish()
            })

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPath = getFilePath(this, "ipassword")
            vPath.text = mPath
        }
    }

    private fun getFilePath(context: Context, dir: String): String {
        val directoryPath: String =
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir
                } else {//没外部存储就使用内部存储
                    context.getFilesDir().toString() + File.separator + dir
                }
        val file = File(directoryPath)
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs()
        }
        val fileName = Utils.datetimeFormat("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()) + ".pwd"
        val exportPath = File(file, fileName)
        return exportPath.absolutePath
    }
}